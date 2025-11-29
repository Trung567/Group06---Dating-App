package com.testing.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Lấy đường dẫn tuyệt đối đến thư mục 'uploads'
        String uploadDir = Paths.get("uploads").toAbsolutePath().normalize().toString();

        // Cấu hình để phục vụ file từ thư mục 'uploads'
        // Khi request /uploads/**, Spring sẽ tìm file trong thư mục uploads/
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + uploadDir + "/");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Áp dụng cho tất cả API
                .allowedOrigins("http://localhost:3000", "https://app.yourdomain.com") // Chỉ cho phép Frontend gọi
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Các method cho phép
                .allowedHeaders("*")
                .allowCredentials(true) // Cho phép gửi cookie/auth header
                .maxAge(3600);
    }
}