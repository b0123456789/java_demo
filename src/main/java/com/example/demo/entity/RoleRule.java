package com.example.demo.entity;

import lombok.Data;

import java.io.Serializable;
 
//角色权限 一个角色对应多个权限
@Data
public class RoleRule implements Serializable {
    //主键自增
    private Integer id;
    //角色id
    private Integer RoleId;
    //权限id
    private Integer RuleId;
}
