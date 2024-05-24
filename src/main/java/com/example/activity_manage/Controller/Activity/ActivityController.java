package com.example.activity_manage.Controller.Activity;

import ch.qos.logback.core.joran.sanity.Pair;
import com.example.activity_manage.Entity.Activity;
import com.example.activity_manage.Entity.DTO.ActivityCreateDTO;
import com.example.activity_manage.Entity.VO.ActInfoToAllVO;
import com.example.activity_manage.Entity.VO.ActScheduleVO;
import com.example.activity_manage.Result.Result;
import com.example.activity_manage.Service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.List;

@RestController
@RequestMapping("/activity")
public class ActivityController {
    @Autowired
    ActivityService activityService;
    // 创建活动
    @PostMapping("/createActivity")
    public Result<Boolean> activityCreate(@RequestBody ActivityCreateDTO activityCreateDTO)
    {
        return Result.success(activityService.ActivityCreate(activityCreateDTO));
    }
    @GetMapping("/getDate")
    public Result<Pair<Date, Date>> activityDateGet(@RequestParam("aid") long aid){
        return Result.success(activityService.ActivityDateGet(aid));
    }

    //面向组织者应该提供活动全部信息。
    @GetMapping("/getActInfoToOrganizer")
    public Result<Activity> getActInfoToOrganizer(@RequestParam("uid") long uid, @RequestParam("aid") long aid){
        return Result.success(activityService.getActInfoToOrganizer(uid,aid));
    }
    //面向用户提供活动的部分信息
    @GetMapping("/getActInfoToAll")
    public Result<ActInfoToAllVO> getActInfoToAll(@RequestParam("aid") long aid){
        return Result.success(activityService.getActInfoToAll(aid));
    }
    //返回用户的所有活动信息,用于日程视图
    @GetMapping("/getActSchedule")
    public Result<List<ActScheduleVO>> getActSchedule(@RequestParam("uid") long uid){
        return Result.success(activityService.getActSchedule(uid));
    }
}
