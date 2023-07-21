package com.sapred.ordermanagerred.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
//import org.springframework.web.servlet.config.annotation.CorsRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI api() {
        return new OpenAPI().info(new Info().title("Order Manager Red").version("1.0"));
    }

//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/user/singUp")
//                .allowedOrigins("http://localhost:3000") // replace with your desired origin
//                .allowedMethods("GET", "POST") // specify the allowed HTTP methods
////                .allowedHeaders("Content-Type") // specify the allowed headers
//                .allowCredentials(true); // enable cookies
//    }

}

