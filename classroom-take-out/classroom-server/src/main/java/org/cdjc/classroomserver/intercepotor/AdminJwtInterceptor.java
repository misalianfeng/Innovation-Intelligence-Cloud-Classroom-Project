package org.cdjc.classroomserver.intercepotor;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;

import org.cdjc.classroomserver.util.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 简单的 JWT 拦截器：
 * - 登录接口、静态页面（例如 /admin/login.html）、静态资源
 * - 其他 /api/admin/** 接口都需要携带合法 token
 */
@Component
public class AdminJwtInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) throws Exception {
        String uri = request.getRequestURI();

        // 1. 登录与注册接口
        if (uri.startsWith("/api/admin/auth/login") || uri.startsWith("/api/admin/auth/register")) {
            return true;
        }

        // 2. 放行静态登录页、管理后台页面和静态资源
        if (uri.startsWith("/admin-login.html")
                || uri.startsWith("/admin-dashboard.html")
                || uri.startsWith("/css/")
                || uri.startsWith("/js/")
                || uri.startsWith("/images/")
        ) {
            return true;
        }


        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return unauthorized(response);
        }
        String token = authHeader.substring(7);
        try {
            Claims claims = JwtUtil.parseToken(token);
            // 在请求中保存一下用户基础信息，后续 Controller 可以从 request 中取
            request.setAttribute("adminUserId", claims.get("userId"));
            request.setAttribute("adminUsername", claims.get("username"));
            request.setAttribute("adminRoleId", claims.get("roleId"));
            return true;
        } catch (ExpiredJwtException e) {
            return unauthorized(response, "登录已过期，请重新登录");
        } catch (JwtException e) {
            return unauthorized(response, "无效的令牌，请重新登录");
        }
    }

    private boolean unauthorized(HttpServletResponse response) throws IOException {
        return unauthorized(response, "未登录或令牌无效");
    }

    private boolean unauthorized(HttpServletResponse response, String msg) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write("{\"success\":false,\"code\":401,\"message\":\"" + msg + "\"}");
        return false;
    }
}


