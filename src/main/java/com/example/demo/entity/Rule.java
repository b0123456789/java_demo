package com.example.demo.entity;

import lombok.Data;
import java.io.Serializable;

//权限
@Data
public class Rule implements Serializable {
    //主键自增
    private Integer id;
    //权限名称
    private String name;
    //权限路径 对应url中的path
    private String path;
}
