package com.example.activity_manage.Controller.User;

import com.example.activity_manage.Entity.Activity;
import com.example.activity_manage.Entity.DTO.ResourceAdditionDTO;
import com.example.activity_manage.Entity.DTO.ResourceReservationDTO;
import com.example.activity_manage.Entity.VO.ActInfoToAdminVO;
import com.example.activity_manage.Entity.VO.ActInfoToAllVO;
import com.example.activity_manage.Entity.VO.GetUserVO;
import com.example.activity_manage.Result.Result;
import com.example.activity_manage.Service.AdminService;
import com.example.activity_manage.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    AdminService adminService;

    @GetMapping("/getAllUser")
    public Result<List<GetUserVO>> getAllUser()
    {
        return Result.success(adminService.getAllUser());
    }
    @GetMapping("/getAllPhone")
    public Result<List<String>> getAllPhone()
    {
        return Result.success(adminService.getAllPhone());
    }
    //用于管理员获取所有活动基本信息
    @GetMapping("/getAllActivity")
    public Result<List<ActInfoToAdminVO>> getAllActivity(){return Result.success(adminService.getAllActivity());}

    @GetMapping("/checkActContent")
    public Result<Boolean> checkActContent(@RequestParam("aid")long aid ,@RequestParam("status") int status, @RequestParam("result") String result){
        return Result.success(adminService.checkActContent(aid,status,result));
    }


}
