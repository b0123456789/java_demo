package com.example.demo.controller;

import com.example.demo.common.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cnf/enterprice")
public class CnfEnterprice {
    @Autowired
    private com.example.demo.entity.ConfigurationEnterprice confEnterprice;

    @Value("${enterprise.name}")
    private String enterpriseName;

    @GetMapping("/all")
    public Result show() {
        return Result.success(confEnterprice);
    }

    @GetMapping("/name")
    public Result name() {
        return Result.success(enterpriseName);
    }
}
