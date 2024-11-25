package com.neko.blog_app.config;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
  // @Bean
  // public WebMvcConfigurer corsConfigurer() {
  //   return new WebMvcConfigurer() {
  //     @Override
  //     public void addCorsMappings(CorsRegistry registry) {
  //       registry.addMapping("/**") // Áp dụng cho tất cả các endpoint
  //           .allowedOrigins("http://localhost:4200") // Chỉ định origin của Angular
  //           .allowedMethods("*")
  //           .allowedHeaders("*")
  //           .allowCredentials(true);
  //     }
  //   };
  // }

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOrigins(Arrays.asList("http://localhost:4200")); // Cho phép origin của Angular
    configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
    configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));
    configuration.setAllowCredentials(true); // Nếu bạn gửi cookies/session thông qua requests
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }
}
