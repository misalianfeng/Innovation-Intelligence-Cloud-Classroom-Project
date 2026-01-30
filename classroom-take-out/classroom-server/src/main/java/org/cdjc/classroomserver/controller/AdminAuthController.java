package org.cdjc.classroomserver.controller;

import org.cdjc.classroompojo.entity.User;
import org.cdjc.classroomserver.service.AdminAuthService;
import org.cdjc.classroomserver.util.JwtUtil;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 管理员认证 Controller（登录 / 注册申请）
 * 提示：
 * - 为方便你快速接入，这里使用了非常简单的返回结构 Map
 * - 你后续可以替换为自己在 common 模块中的统一返回结果类
 */
@RestController
@RequestMapping("/api/admin/auth")
public class AdminAuthController {

    private final AdminAuthService adminAuthService;


    public AdminAuthController(AdminAuthService adminAuthService) {
        this.adminAuthService = adminAuthService;
    }

    /**
     * 管理员登录
     * 示例请求：
     * POST /api/admin/auth/login
     * {
     *   "username": "admin",
     *   "password": "123456"
     * }
     */
    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, String> body) {
        Map<String, Object> resp = new HashMap<>();
        
        // 1. 参数校验
        String username = body.get("username");
        String password = body.get("password");
        if (username == null || username.trim().isEmpty() || 
            password == null || password.trim().isEmpty()) {
            resp.put("success", false);
            resp.put("message", "用户名和密码不能为空");
            return resp;
        }
        
        // 2. 调用登录服务
        User user = adminAuthService.adminLogin(username.trim(), password);
        if (user == null) {
            resp.put("success", false);
            resp.put("message", "登录失败：用户名或密码错误，或不是管理员账号（roleId必须为0），或账号已被禁用（status必须为1）");
            return resp;
        }
        
        // 3. 生成 JWT，载荷中放入 userId / username / roleId
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getUserId());
        claims.put("username", user.getUsername());
        claims.put("roleId", user.getRoleId());
        String token = JwtUtil.generateToken(String.valueOf(user.getUserId()), claims);


        // 4. 返回成功响应
        resp.put("success", true);
        resp.put("message", "登录成功");
        Map<String, Object> data = new HashMap<>();
        data.put("token", token);
        data.put("userId", user.getUserId());
        data.put("username", user.getUsername());
        data.put("avatar", user.getAvatar());
        data.put("roleId", user.getRoleId());
        resp.put("data", data);
        return resp;
    }

    /**
     * 管理员邮箱注册申请
     * 示例请求：
     * POST /api/admin/auth/register
     * {
     *   "email": "admin@xxx.com",
     *   "username": "admin",
     *   "password": "123456"
     * }
     * 此处未接入真实邮箱发送流程，只是简单插入一条管理员记录，方便你后续自行扩展：
     * - 接入邮件服务（阿里云邮件、QQ 邮箱等）
     * - 增加审批流程 / 激活链接等
     */
    @PostMapping("/register")
    public Map<String, Object> register(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String username = body.get("username");
        String password = body.get("password");

        User user = adminAuthService.adminRegisterByEmail(email, username, password);
        Map<String, Object> resp = new HashMap<>();
        if (user == null) {
            resp.put("success", false);
            resp.put("message", "注册失败，可能是邮箱或用户名已存在");
            return resp;
        }
        resp.put("success", true);
        resp.put("message", "管理员注册申请成功");
        Map<String, Object> data = new HashMap<>();
        data.put("userId", user.getUserId());
        data.put("username", user.getUsername());
        data.put("email", user.getEmail());
        resp.put("data", data);
        return resp;
    }
}


