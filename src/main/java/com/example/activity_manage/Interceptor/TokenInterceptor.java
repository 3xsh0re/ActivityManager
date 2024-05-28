package com.example.activity_manage.Interceptor;

import com.example.activity_manage.Constant.MessageConstant;
import com.example.activity_manage.Utils.JwtUtil;
import com.example.activity_manage.Utils.RedisUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Map;

@Component
public class TokenInterceptor implements HandlerInterceptor {
    @Autowired
    private RedisUtil redisUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler){
        String token = request.getHeader("Authorization");

        if (token == null || token.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        // 获取token的解析
        Claims claims;
        try {
            claims = JwtUtil.parseJWT(MessageConstant.JWT_SECRET_KET,token);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        // 验证redis中是否含有token
        String userId = claims.get("id").toString();
        String redisKey = "TOKEN_" + userId;

        if (!redisUtil.hasKey(redisKey)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        Map<Object, Object> userInfo = redisUtil.hmget(redisKey);
        request.setAttribute("userInfo", userInfo);

        return true;
    }
}
