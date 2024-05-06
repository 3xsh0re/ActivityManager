package com.example.activity_manage.Controller.User;

import com.example.activity_manage.Entity.DTO.UserLoginDTO;
import com.example.activity_manage.Entity.User;
import com.example.activity_manage.Entity.VO.UserLoginVO;
import com.example.activity_manage.Result.Result;
import com.example.activity_manage.Service.UserService;
import com.example.activity_manage.Utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {
    @GetMapping("/test")
    public String test()
    {
        return "hello";
    }
    @Autowired
    UserService userService;
    @PostMapping("/login")
    public Result<UserLoginVO> Login(@RequestBody UserLoginDTO userLoginDTO)
    {
        User user = userService.Login(userLoginDTO);
        //登录成功后，生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", user.getId());
        claims.put("username", user.getUsername());
        String token = JwtUtil.createJWT(
                "123456",
                7 * 60 * 60 * 24,
                claims);
        UserLoginVO userLoginVO = UserLoginVO.builder()
                .id(user.getId())
                .token(token)
                .build();

        return Result.success(userLoginVO);
    }

    @PostMapping("/register")
    public Result<Boolean> Register(@RequestBody UserLoginDTO userLoginDTO){
        return Result.success(userService.Register(userLoginDTO));
    }
}
