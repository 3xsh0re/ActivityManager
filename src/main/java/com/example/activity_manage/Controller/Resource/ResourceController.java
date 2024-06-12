package com.example.activity_manage.Controller.Resource;

import com.example.activity_manage.Entity.DTO.BasePageQueryDTO;
import com.example.activity_manage.Entity.DTO.ResourceAdditionDTO;
import com.example.activity_manage.Entity.DTO.ResourceReservationDTO;
import com.example.activity_manage.Entity.DTO.ResourceToActDTO;
import com.example.activity_manage.Result.PageResult;
import com.example.activity_manage.Result.Result;
import com.example.activity_manage.Service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/resource")
public class ResourceController {
    @Autowired
    ResourceService resourceService;
    @PostMapping("/getAllResource") //获取所有资源详情
    public Result<PageResult> pageQueryAllResource(@RequestBody BasePageQueryDTO basePageQueryDTO){
        return Result.success(resourceService.pageQueryAllResource(basePageQueryDTO));
    }
    @PostMapping("/resourceAddition")// 资源添加接口
    public Result<Boolean> resourceAddition(@RequestBody ResourceAdditionDTO resourceAdditionDTO) {
        return Result.success(resourceService.resourceAddition(resourceAdditionDTO));
    }
    @PostMapping("/resourceReservation")// 资源申请接口
    public Result<Boolean> resourceReservation(@RequestBody ResourceReservationDTO resourceReservationDTO) {
        return Result.success(resourceService.resourceReservation(resourceReservationDTO));
    }
    @PostMapping("/getAllResourceToAct")
    public Result<PageResult> getAllResourceToAct(@RequestBody ResourceToActDTO resource)
    {
        return Result.success(resourceService.pageQueryResourceToAct(resource));
    }
}
