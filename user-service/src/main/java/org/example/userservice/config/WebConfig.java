package org.example.userservice.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web 配置类
 * 注册拦截器
 * 
 * @author user-service
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private AuthInterceptor authInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/userservice/user/login",  // 登录接口
                        "/userservice/user/register", // 注册接口（如果需要）
                        "/error",  // 错误页面
                        "/swagger-ui.html",  // Swagger UI
                        "/swagger-resources/**",  // Swagger 资源
                        "/v2/api-docs",  // Swagger API 文档
                        "/v2/api-docs/**",  // Swagger API 文档
                        "/webjars/**",  // Swagger 静态资源
                        "/doc.html",  // Knife4j 文档
                        "/uploads/**" // 上传文件静态访问
                );
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 将本地 uploads 目录映射为 /uploads/** 静态资源
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:uploads/");
    }
}

