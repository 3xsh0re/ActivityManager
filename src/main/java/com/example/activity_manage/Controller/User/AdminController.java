package com.example.activity_manage.Controller.User;

import com.example.activity_manage.Entity.User;
import com.example.activity_manage.Entity.VO.GetUserVO;
import com.example.activity_manage.Result.Result;
import com.example.activity_manage.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    UserService userService;
    @GetMapping("/getAllUser")
    public Result<List<GetUserVO>> getAllUser()
    {
        return Result.success(userService.getAllUser());
    }
    @GetMapping("/getAllPhone")
    public Result<List<String>> getAllPhone()
    {
        return Result.success(userService.getAllPhone());
    }
}
