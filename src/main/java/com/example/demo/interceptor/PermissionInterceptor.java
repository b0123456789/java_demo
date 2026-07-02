package com.example.demo.interceptor;

import com.example.demo.entity.User;
import com.example.demo.service.PermissionService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;
import java.util.Set;

/**
 * 权限拦截器
 * 校验用户是否有权访问当前请求路径，无权限时复用 SecurityConfig 的 accessDeniedHandler 返回 403
 */
@Component
public class PermissionInterceptor implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(PermissionInterceptor.class);

    private final PermissionService permissionService;
    private final AccessDeniedHandler accessDeniedHandler;
    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    public PermissionInterceptor(PermissionService permissionService,
                                 AccessDeniedHandler accessDeniedHandler) {
        this.permissionService = permissionService;
        this.accessDeniedHandler = accessDeniedHandler;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) throws Exception {
        String path = request.getRequestURI();

        // 1. 获取当前登录用户
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
            return true; // 未登录，交给 Spring Security 返回 401
        }

        User user = (User) auth.getDetails();
        if (user == null || user.getId() == null) {
            return true;
        }

        // 2. 加载用户权限路径
        Set<String> userPaths = permissionService.getUserPaths(user.getId());

        // 3. 用 AntPathMatcher 匹配
        boolean hasPermission = userPaths.stream()
                .anyMatch(p -> pathMatcher.match(p, path));

        if (hasPermission) {
            return true;
        }

        // 4. 无权限 → 复用 SecurityConfig 的 accessDeniedHandler 返回 403
        log.warn("用户 {} (id={}) 无权限访问: {}", user.getUsername(), user.getId(), path);
        try {
            accessDeniedHandler.handle(request, response,
                    new AccessDeniedException("无权限访问: " + path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return false;
    }
}
