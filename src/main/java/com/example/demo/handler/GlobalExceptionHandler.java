package com.example.demo.handler;

import com.example.demo.common.Result;
import org.jspecify.annotations.NonNull;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(NoHandlerFoundException.class)
    public Result handle404(@NonNull NoHandlerFoundException e) {
        return Result.error("接口不存在: " + e.getRequestURL());
    }

    @ExceptionHandler(Exception.class)
    public Result handle500(Exception e) {
        System.out.println("error=>"+e.getMessage());
        return Result.error("服务器内部错误");
    }
}