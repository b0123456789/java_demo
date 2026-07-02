package com.example.demo.config;

import com.example.demo.dao.UserMapper;
import com.example.demo.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collections;

/**
 * 自定义认证提供者
 * 接管 DaoAuthenticationProvider 的职责，自行完成用户加载 + 密码校验
 */
@Component
public class SaltAuthenticationProvider implements AuthenticationProvider {

    private static final Logger log = LoggerFactory.getLogger(SaltAuthenticationProvider.class);

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public SaltAuthenticationProvider(UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String rawPassword = authentication.getCredentials().toString();

        log.info("开始认证用户: {}", username);

        // 1. 从数据库加载用户
        User user = userMapper.findByUsername(username);
        if (user == null) {
            log.warn("用户不存在: {}", username);
            throw new UsernameNotFoundException("用户不存在: " + username);
        }

        // 2. 密码校验
        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            log.warn("密码错误: {}", username);
            throw new BadCredentialsException("用户名或密码错误");
        }

        log.info("用户认证成功: {}", username);

        // 3. 返回已认证的 token
        UsernamePasswordAuthenticationToken result = new UsernamePasswordAuthenticationToken(
                user.getUsername(),        // principal: 用户名，保证 auth.getName() 能正确返回
                null,                      // credentials: 清空密码
                Collections.emptyList()    // authorities: 暂无角色，按需扩展
        );
        result.setDetails(user);           // details 中存放完整 User 实体，方便后续获取
        return result;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
