package com.onlineexam.config;

import com.onlineexam.interceptor.LoginInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {
    
    @Value("${video.upload.path:./videos}")
    private String uploadPath;

    /**
     * 配置拦截器
     * @param registry 相当于拦截器的注册中心
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor())
                .addPathPatterns()
                .excludePathPatterns("/login", "/ai/**", "/exam/video/**", "/videos/**", "/page/switch/record/**");
    }

    /**
     * 配置静态资源访问路径
     * @param registry 资源处理器注册表
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 配置视频文件的访问路径
        registry.addResourceHandler("/videos/**")
                .addResourceLocations("file:" + uploadPath + "/");
    }


    @Bean
    public CorsFilter corsFilter() {
        // 1.创建 CORS 配置对象
        CorsConfiguration config = new CorsConfiguration();
        // 支持所有域
        config.addAllowedOrigin("*");
        // 不携带 Cookie（与通配域兼容，避免浏览器拒绝）
        config.setAllowCredentials(false);
        // 支持请求方式
        config.addAllowedMethod("*");
        // 允许的原始请求头部信息
        config.addAllowedHeader("*");
        // 2.添加地址映射
        UrlBasedCorsConfigurationSource corsConfigurationSource = new UrlBasedCorsConfigurationSource();
        corsConfigurationSource.registerCorsConfiguration("/**", config);
        // 3.返回 CorsFilter 对象
        return new CorsFilter(corsConfigurationSource);
    }
}
