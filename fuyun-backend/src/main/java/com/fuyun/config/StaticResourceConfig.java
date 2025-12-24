package com.fuyun.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.ResourceHandlerRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;

/**
 * 静态资源配置
 * 配置Spring Boot服务前端静态资源
 */
@Configuration
public class StaticResourceConfig implements WebFluxConfigurer {
    
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 配置静态资源处理器
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/")
                .setCachePeriod(3600); // 缓存1小时
        
        // 配置API路径，避免被静态资源拦截
        registry.addResourceHandler("/api/**")
                .addResourceLocations("classpath:/static/api/");
    }
}