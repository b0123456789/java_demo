package com.example.demo.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface RuleMapper {

    /**
     * 查询用户拥有的所有权限路径
     * User → UserRole → RoleRule → Rule.path
     */
    @Select("SELECT DISTINCT r.path FROM user_role ur " +
            "JOIN role_rule rr ON ur.role_id = rr.role_id " +
            "JOIN rule r ON rr.rule_id = r.id " +
            "WHERE ur.user_id = #{userId}")
    List<String> findPathsByUserId(@Param("userId") Integer userId);
}
