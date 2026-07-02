package com.example.demo.entity;

import lombok.Data;

import java.io.Serializable;

//用户角色 一个用户对应多个角色
@Data
public class UserRole implements Serializable {
    //主键自增
    private Integer id;
    //用户id
    private Integer UserId;
    //角色id
    private Integer RoleId;
}
