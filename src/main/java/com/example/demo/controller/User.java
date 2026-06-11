package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.common.Result;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class User {

    @Autowired
    private Environment envData;

    @GetMapping("/{id}")
    public Result getById(@PathVariable Integer id) {
        System.out.println("id=>" + id);
        Map<String, String> rs = new HashMap<>();
        rs.put("id", "oK");
        return Result.success(rs);
    }

    @GetMapping("/s")
    public Result s() {
        return Result.success();
    }

    @GetMapping("/obj")
    public Result obj() {
        Map<String, Object> rs = new HashMap<>();
        rs.put("id", 1);
        rs.put("name", "oK");
        return Result.success(rs);
    }

    @GetMapping("/e")
    public String e() throws Exception {
        if (true) {
            throw new Exception("exception");
        }
        return "ok";
    }

    @GetMapping("/str")
    public String str() {
        return "ok";
    }

    @GetMapping("/env/username")
    public Result envUsername() {
        Map<String, Object> rs = new HashMap<>();
        rs.put("username", envData.getProperty("spring.datasource.username"));
        return Result.success(rs);
    }
}

