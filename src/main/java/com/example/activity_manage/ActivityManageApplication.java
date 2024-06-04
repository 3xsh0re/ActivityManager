package com.example.activity_manage;

import jakarta.annotation.PostConstruct;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ScopeMetadata;

import java.util.TimeZone;

@SpringBootApplication
@MapperScan("com.example.activity_manage.Mapper")
public class ActivityManageApplication {

    public static void main(String[] args) {
        SpringApplication.run(ActivityManageApplication.class, args);
    }

    @PostConstruct
    public void init() {
        // 设置默认时区为UTC
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }

}
