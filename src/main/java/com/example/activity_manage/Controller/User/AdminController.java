package com.example.activity_manage.Controller.User;

import com.example.activity_manage.Entity.DTO.ResourceAdditionDTO;
import com.example.activity_manage.Entity.DTO.ResourceReservationDTO;
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
    UserService userService;
    @Autowired
    AdminService adminService;

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
    @PostMapping("/resourceReservation")// 资源申请接口
    public Result<Boolean> resourceReservation(@RequestBody ResourceReservationDTO resourceReservationDTO) {
        return Result.success(adminService.resourceReservation(resourceReservationDTO));
    }
    @PostMapping("/resourceAddition")// 资源添加接口
    public Result<Boolean> resourceAddition(@RequestBody ResourceAdditionDTO resourceAdditionDTO) {
        return Result.success(adminService.resourceAddition(resourceAdditionDTO));
    }
}
