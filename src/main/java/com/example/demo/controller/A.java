package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.common.Result;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/a")
public class A {
    @GetMapping("/b")
    public Result getById() {
        Map<String, String> rs = new HashMap<>();
        rs.put("id", "oK");
        return Result.success(rs);
    }
}

