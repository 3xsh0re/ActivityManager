package com.example.activity_manage.Controller.Resource;

import com.example.activity_manage.Entity.DTO.ResourceAdditionDTO;
import com.example.activity_manage.Entity.DTO.ResourceReservationDTO;
import com.example.activity_manage.Entity.Resource;
import com.example.activity_manage.Result.Result;
import com.example.activity_manage.Service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/resource")
public class ResourceController {
    @Autowired
    ResourceService resourceService;
    @GetMapping("/getAllResource") //获取所有资源详情
    public Result<List<Resource>> getAllResource(){
        return Result.success(resourceService.getAllResource());
    }
    @PostMapping("/resourceAddition")// 资源添加接口
    public Result<Boolean> resourceAddition(@RequestBody ResourceAdditionDTO resourceAdditionDTO) {
        return Result.success(resourceService.resourceAddition(resourceAdditionDTO));
    }
    @PostMapping("/resourceReservation")// 资源申请接口
    public Result<Boolean> resourceReservation(@RequestBody ResourceReservationDTO resourceReservationDTO) {
        return Result.success(resourceService.resourceReservation(resourceReservationDTO));
    }
}
