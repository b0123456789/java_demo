package com.example.demo.entity;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "enterprise")
public class ConfigurationEnterprice {
    private String name;
    private Integer age;
    private String tel;
    private String[] subject;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }

    public String getTel() { return tel; }
    public void setTel(String tel) { this.tel = tel; }

    public String[] getSubject() { return subject; }
    public void setSubject(String[] subject) { this.subject = subject; }
}