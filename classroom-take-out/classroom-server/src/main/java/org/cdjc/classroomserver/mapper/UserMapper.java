package org.cdjc.classroomserver.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.cdjc.classroompojo.entity.User;

/**
 * 用户表 Mapper（admin 登录/查询等使用）
 */
@Mapper
public interface UserMapper {

    /**
     * 根据用户名查询用户
     */
    User selectByUsername(@Param("username") String username);

    /**
     * 根据邮箱查询用户（管理员注册申请时使用）
     */
    User selectByEmail(@Param("email") String email);

    /**
     * 插入用户（管理员注册申请）
     */
    int insert(User user);

    /**
     * 更新最后登录时间
     */
    int updateLastLoginTime(@Param("userId") Integer userId);
}


