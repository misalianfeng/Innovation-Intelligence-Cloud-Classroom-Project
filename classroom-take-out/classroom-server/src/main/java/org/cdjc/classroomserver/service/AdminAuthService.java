package org.cdjc.classroomserver.service;

import org.cdjc.classroompojo.entity.User;

/**
 * 管理员认证相关业务接口
 */
public interface AdminAuthService {

    /**
     * 管理员登录
     *
     * @param username 用户名
     * @param password 明文密码（建议后续改为前端加密 + 服务端加盐存储）
     * @return 登录成功返回 User 信息（只允许 roleId = 0 的用户通过）
     */
    User adminLogin(String username, String password);

    /**
     * 管理员邮箱注册申请
     *
     * @param email    管理员邮箱
     * @param username 申请用户名
     * @param password 登录密码
     * @return 新建的用户
     */
    User adminRegisterByEmail(String email, String username, String password);
}


