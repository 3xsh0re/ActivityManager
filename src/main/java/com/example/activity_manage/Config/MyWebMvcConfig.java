package com.example.activity_manage.Config;

import com.example.activity_manage.Interceptor.TokenInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MyWebMvcConfig implements WebMvcConfigurer {
    @Autowired
    private TokenInterceptor tokenInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //测试接口暂时注释
        registry.addInterceptor(tokenInterceptor)
                .addPathPatterns("/activity/*","/user/*","/reminder/*","/file/*","/resource/*","/comment/*","/expense/*","/admin/*")
                .excludePathPatterns("/user/login", "/user/register","/user/getCaptchaByPhone","/user/resetPasswd","/user/getCaptchaByEmail"); // 排除不需要拦截的路径
    }
}
