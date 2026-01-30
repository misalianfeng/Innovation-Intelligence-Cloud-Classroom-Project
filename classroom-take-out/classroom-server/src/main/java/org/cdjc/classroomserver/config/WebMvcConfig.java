package org.cdjc.classroomserver.config;

import org.cdjc.classroomserver.intercepotor.AdminJwtInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web MVC 配置：注册 JWT 拦截器
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @NonNull
    private final AdminJwtInterceptor adminJwtInterceptor;

    @Autowired
    public WebMvcConfig(@NonNull AdminJwtInterceptor adminJwtInterceptor) {
        this.adminJwtInterceptor = adminJwtInterceptor;
    }

    @Override
public void addInterceptors(@NonNull InterceptorRegistry registry) {
        registry.addInterceptor( adminJwtInterceptor)
                .addPathPatterns("/api/admin/**")
                .excludePathPatterns(
                        "/api/admin/auth/login",
                        "/api/admin/auth/register",
                        "/admin-login.html",
                        "/admin-dashboard.html",
                        "/css/**",
                        "/js/**",
                        "/images/**"
                );
    }
}


