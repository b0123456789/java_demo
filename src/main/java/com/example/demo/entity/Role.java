package com.example.demo.entity;

import lombok.Data;
import java.io.Serializable;

//角色
@Data
public class Role implements Serializable {
    //主键自增
    private Integer id;
    //角色名称
    private String name;
}
