package com.example.activity_manage.Controller.User;

import com.example.activity_manage.Entity.DTO.BasePageQueryDTO;
import com.example.activity_manage.Result.PageResult;
import com.example.activity_manage.Result.Result;
import com.example.activity_manage.Service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    AdminService adminService;

    @PostMapping("/getAllUser")
    public Result<PageResult> getAllUser(@RequestBody BasePageQueryDTO basePageQueryDTO)
    {
        return Result.success(adminService.getAllUser(basePageQueryDTO));
    }
    @PostMapping("/getAllPhone")
    public Result<PageResult> getAllPhone(@RequestBody BasePageQueryDTO basePageQueryDTO)
    {
        return Result.success(adminService.pageQueryAllPhone(basePageQueryDTO));
    }
    //用于管理员获取所有活动基本信息
    @PostMapping("/getAllActivity")
    public Result<PageResult> getAllActivity(@RequestBody BasePageQueryDTO basePageQueryDTO){return Result.success(adminService.pageQueryActInfoToAdmin(basePageQueryDTO));}

    //审核
    @GetMapping("/checkActContent")
    public Result<Boolean> checkActContent(@RequestParam("aid")long aid ,@RequestParam("status") int status, @RequestParam("result") String result){
        return Result.success(adminService.checkActContent(aid,status,result));
    }
    //删除用户
    @GetMapping("/deleteUser")
    public Result<Boolean> deleteUser(@RequestParam("deleteId") long deleteId){
        return Result.success(adminService.deleteUser(deleteId));
    }

}
