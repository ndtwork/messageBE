package com.example.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration // Đánh dấu đây là class cấu hình Spring
public class CorsConfig {

//    @Bean // Tạo một bean WebMvcConfigurer
//    public WebMvcConfigurer corsConfigurer() {
//        return new WebMvcConfigurer() {
//            @Override
//            public void addCorsMappings(CorsRegistry registry) {
//                registry.addMapping("/api/**") // Áp dụng CORS cho các endpoint bắt đầu bằng "/api/"
//                        .allowedOrigins("http://localhost:3000"); // **Đây là ORIGIN CỤ THỂ, KHÔNG phải "*"**
////                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Cho phép các HTTP method này
////                        .allowedHeaders("Authorization", "Content-Type") // Cho phép các header này
////                        .allowCredentials(false) // Cân nhắc sử dụng allowCredentials
////                        .maxAge(3600); // Thời gian cache pre-flight request (giây)
//            }
//        };
//    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**")
                        .allowedOrigins("http://localhost:3000") // Không dùng "*"
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .allowCredentials(true) // Cho phép gửi credentials như cookies
                        .maxAge(3600);
            }
        };
    }

}