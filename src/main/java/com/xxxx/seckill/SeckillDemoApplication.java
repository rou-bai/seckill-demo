package com.xxxx.seckill;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.util.ResourceUtils;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@SpringBootApplication
@MapperScan("com.xxxx.seckill.mapper")
public class SeckillDemoApplication{

    public static void main(String[] args) {
        SpringApplication.run(SeckillDemoApplication.class, args);
    }
}
