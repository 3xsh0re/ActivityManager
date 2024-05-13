package com.example.activity_manage.Utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Random;
@Component
public class CaptchaUtil {
    @Autowired
    private RedisUtil redisUtil;

    public  String generateCaptcha(String sign)
    {
        Random random = new Random();
        int code = random.nextInt(900000) + 100000; // 生成 6 位随机数
        redisUtil.set("UMS_" + sign,String.valueOf(code),60*5);
        return String.valueOf(code);
    }
}
