package com.example.demo.controller;

import com.example.demo.common.Result;
import com.example.demo.dao.UserMapper;
import com.example.demo.dto.AuthRequest;
import com.example.demo.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public AuthController(AuthenticationManager authenticationManager,
                          UserMapper userMapper,
                          PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public Result register(AuthRequest req) {
        String username = req.getUsername();
        String password = req.getPassword();

        // 检查用户名是否已存在
        User existing = userMapper.findByUsername(username);
        if (existing != null) {
            return Result.error("用户名已存在");
        }

        // 密码加密（SHA-256 + 随机盐，格式: 密文:盐值）
        String encodedPassword = passwordEncoder.encode(password);

        // 提取盐值单独存储（方便后续扩展使用）
        String salt = encodedPassword.substring(encodedPassword.indexOf(":") + 1);

        User user = new User();
        user.setUsername(username);
        user.setPassword(encodedPassword);  // 密文:盐值 存入 password
        user.setSalt(salt);                 // 盐值单独存入 salt
        user.setCreateAt(LocalDateTime.now());
        user.setUpdateAt(LocalDateTime.now());

        userMapper.insert(user);

        return Result.success(Map.of("username", username, "message", "注册成功"));
    }

    @PostMapping("/login")
    public Result login(AuthRequest req, HttpServletRequest request) {
        String username = req.getUsername();
        String password = req.getPassword();

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);
        Authentication authentication = authenticationManager.authenticate(token);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        request.getSession().setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());

        return Result.success(Map.of("username", username, "message", "login success"));
    }

    @GetMapping("/logout")
    public Result logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        SecurityContextHolder.clearContext();
        return Result.success("logout success");
    }

    @GetMapping("/me")
    public Result me() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()) {
            return Result.success(Map.of("username", auth.getName(),
            "detail",auth.getDetails(),
            "getCredentials",auth.getCredentials(),
            "getAuthorities",auth.getAuthorities()
        ));
        }
        return Result.error("not login");
    }
}
