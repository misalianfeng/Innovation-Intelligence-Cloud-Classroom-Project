package org.cdjc.classroompojo.entity;


import lombok.Data;
import java.time.LocalDateTime;

/**
 * 用户实体，对应表 user。
 * 说明：
 *  - 表结构请以真实数据库为准
 *  - 如果你在表中新增了 password / email 等字段，请同步在这里增加属性
 */
@Data
public class User {

    /**
     * PK, 用户唯一ID
     */
    private Integer userId;

    /**
     * 用户名（唯一）
     */
    private String username;

    /**
     * 手机号（唯一）
     */
    private String phone;

    /**
     * 微信绑定手机号
     */
    private String wechatPhone;

    /**
     * 头像 URL
     */
    private String avatar;

    /**
     * 状态：0-禁用, 1-启用
     */
    private Integer status;

    /**
     * 角色：0-管理员, 1-普通用户
     */
    private Integer roleId;

    /**
     * 最后登录时间
     */
    private LocalDateTime lastLoginTime;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    // ===== 登录相关字段（需要你在数据库中自行增加） =====

    /**
     * 登录密码（建议加密存储：例如 BCrypt）
     * 注意：当前你的建表 SQL 中没有 password 字段，
     * 如需在后端使用密码，请在数据库 user 表中新增：password varchar(255)。
     */
    private String password;

    /**
     * 邮箱（管理员申请注册时使用）
     * 同样需要你在数据库 user 表中新增：email varchar(255)。
     */
    private String email;
}


