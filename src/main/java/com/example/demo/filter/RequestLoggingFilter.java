package com.example.demo.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collections;

@Component
public class RequestLoggingFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(RequestLoggingFilter.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;

        long start = System.currentTimeMillis();
        chain.doFilter(request, response);
        long elapsed = System.currentTimeMillis() - start;

        StringBuilder sb = new StringBuilder();
        sb.append("[").append(req.getMethod()).append("] ");
        sb.append(req.getRequestURI());
        if (req.getQueryString() != null) {
            sb.append("?").append(req.getQueryString());
        }

        // 请求参数
        Collections.list(req.getParameterNames())
                .forEach(name -> sb.append("\n    param ").append(name).append("=").append(req.getParameter(name)));

        sb.append("\n    elapsed=").append(elapsed).append("ms");
        log.info(sb.toString());
    }
}
