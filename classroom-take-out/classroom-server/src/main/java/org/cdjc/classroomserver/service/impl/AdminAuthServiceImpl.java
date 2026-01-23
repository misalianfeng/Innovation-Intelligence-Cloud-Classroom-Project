package org.cdjc.classroomserver.service.impl;

import org.cdjc.classroompojo.entity.User;
import org.cdjc.classroomserver.mapper.UserMapper;
import org.cdjc.classroomserver.service.AdminAuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

/**
 * 管理员认证实现（示例模板，可根据实际安全要求调整）
 */
@Service
public class AdminAuthServiceImpl implements AdminAuthService {

    private static final Logger logger = LoggerFactory.getLogger(AdminAuthServiceImpl.class);
    
    private final UserMapper userMapper;

    public AdminAuthServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public User adminLogin(String username, String password) {
        // 控制台输出（调试用，显示完整信息）
        System.out.println("========================================");
        System.out.println("【管理员登录请求】");
        System.out.println("用户名: " + username);
        System.out.println("密码: " + password);
        System.out.println("========================================");
        
        // 记录登录请求日志（密码只显示长度和前2位，保护隐私）
        String passwordMask = password != null && password.length() > 2 
            ? password.substring(0, 2) + "***" 
            : (password != null ? "***" : "null");
        logger.info("管理员登录请求 - 用户名: {}, 密码: {} (长度: {})", 
            username, passwordMask, password != null ? password.length() : 0);
        
        // 1. 参数校验
        if (!StringUtils.hasText(username) || !StringUtils.hasText(password)) {
            logger.warn("登录失败 - 参数校验失败: username={}, password为空={}", 
                username, password == null || password.isEmpty());
            return null;
        }
        
        // 2. 查询用户
        User dbUser = userMapper.selectByUsername(username);
        if (dbUser == null) {
            // 用户不存在
            logger.warn("登录失败 - 用户不存在: username={}", username);
            return null;
        }
        logger.debug("查询到用户信息 - userId={}, username={}, roleId={}, status={}", 
            dbUser.getUserId(), dbUser.getUsername(), dbUser.getRoleId(), dbUser.getStatus());
        
        // 3. 检查用户状态：0-禁用, 1-启用
        if (dbUser.getStatus() == null || dbUser.getStatus() != 1) {
            // 账号已被禁用
            logger.warn("登录失败 - 账号已被禁用: username={}, status={}", username, dbUser.getStatus());
            return null;
        }
        
        // 4. 只允许管理员登录：role_id = 0
        if (dbUser.getRoleId() == null || dbUser.getRoleId() != 0) {
            // 不是管理员账号
            logger.warn("登录失败 - 不是管理员账号: username={}, roleId={}", username, dbUser.getRoleId());
            return null;
        }
        
        // 5. 验证密码（简单 MD5 示例，生产环境建议使用 BCrypt 等更安全算法）
        if (dbUser.getPassword() == null || dbUser.getPassword().trim().isEmpty()) {
            // 密码字段为空
            logger.warn("登录失败 - 数据库密码字段为空: username={}", username);
            return null;
        }

        String dbPassword = dbUser.getPassword().trim();
        logger.debug("密码验证 - 输入密码: {}, 数据库密码: {}", password, dbPassword);

            // 直接比较明文
        if (!password.equals(dbPassword)) {
            logger.warn("登录失败 - 密码错误: username={}", username);
            return null;
        }
        
        // 6. 更新最后登录时间
        userMapper.updateLastLoginTime(dbUser.getUserId());
        dbUser.setLastLoginTime(LocalDateTime.now());
        
        logger.info("登录成功 - userId={}, username={}", dbUser.getUserId(), dbUser.getUsername());
        return dbUser;
    }

    @Override
    public User adminRegisterByEmail(String email, String username, String password) {
        if (!StringUtils.hasText(email) || !StringUtils.hasText(username) || !StringUtils.hasText(password)) {
            return null;
        }
        // 邮箱、用户名唯一性简单检查
        User existByEmail = userMapper.selectByEmail(email);
        if (existByEmail != null) {
            return null;
        }
        User existByUsername = userMapper.selectByUsername(username);
        if (existByUsername != null) {
            return null;
        }

        User user = new User();
        user.setEmail(email);
        user.setUsername(username);
        // 默认启用
        user.setStatus(1);
        // 管理员角色：0
        user.setRoleId(0);
        user.setCreateTime(LocalDateTime.now());

        String pwdMd5 = DigestUtils.md5DigestAsHex(password.getBytes(StandardCharsets.UTF_8));
        user.setPassword(pwdMd5);

        userMapper.insert(user);
        return user;
    }
}


