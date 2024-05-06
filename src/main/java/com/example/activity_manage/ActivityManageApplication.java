package com.example.activity_manage;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ScopeMetadata;

@SpringBootApplication
@MapperScan("com.example.activity_manage.Mapper")
public class ActivityManageApplication {

    public static void main(String[] args) {
        SpringApplication.run(ActivityManageApplication.class, args);
    }

}
