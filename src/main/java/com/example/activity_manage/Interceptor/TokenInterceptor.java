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
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.Map;

@Component
public class TokenInterceptor implements HandlerInterceptor {
    @Autowired
    private RedisUtil redisUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        String token = request.getHeader("Authorization");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        String origin = request.getHeader("Origin");
        response.setHeader("Access-Control-Allow-Origin",origin);

        if (token == null || token.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write(MessageConstant.ACCOUNT_NOT_LOGIN);
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
        String redisKey = "TOKEN_" + token;

        if (!redisUtil.hasKey(redisKey)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write(MessageConstant.ACCOUNT_NOT_LOGIN);
            return false;
        }

        int role = (int) claims.get("role");
        String requestURI = request.getRequestURI();

        // 非管理员用户访问/admin/*路径时拒绝访问
        if ((role == 0) && (requestURI.startsWith("/admin/") || requestURI.startsWith("resource/resourceReservation"))) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write(MessageConstant.NOT_HAVE_THIS_PERMISSION);
            return false;
        }

        Map<Object, Object> userInfo = redisUtil.hmget(redisKey);
        request.setAttribute("userInfo", userInfo);

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView){
        // 获取Token
        String token = request.getHeader("Authorization");

        // 检查Token是否存在
        if (token != null && redisUtil.hasKey("TOKEN_" + token)) {
            // 更新Token的过期时间
            redisUtil.expire("TOKEN_" + token, 60 * 60 * 24 * 2);
        }
    }
}