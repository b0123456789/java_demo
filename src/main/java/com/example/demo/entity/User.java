package com.example.demo.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class User {
    private Integer id;
    private String username;
    private String password;
    private String salt;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
}
