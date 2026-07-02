package com.example.demo.entity;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;
    private String username;
    private String password;
    private String salt;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
}
