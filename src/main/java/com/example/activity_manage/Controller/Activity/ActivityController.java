package com.example.activity_manage.Controller.Activity;

import ch.qos.logback.core.joran.sanity.Pair;
import com.example.activity_manage.Entity.Activity;
import com.example.activity_manage.Entity.DTO.ActivityCreateDTO;
import com.example.activity_manage.Entity.DTO.ActivitySetParticipantRoleDTO;
import com.example.activity_manage.Entity.DTO.ActivityPageQueryDTO;
import com.example.activity_manage.Entity.DTO.BasePageQueryDTO;
import com.example.activity_manage.Entity.VO.ActInfoToAllVO;
import com.example.activity_manage.Entity.VO.ActScheduleVO;
import com.example.activity_manage.Result.PageResult;
import com.example.activity_manage.Result.Result;
import com.example.activity_manage.Service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
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
    public Result<Pair<Timestamp, Timestamp>> activityDateGet(@RequestParam("aid") long aid){
        return Result.success(activityService.ActivityDateGet(aid));
    }

    // 面向组织者应该提供活动全部信息
    @GetMapping("/getActInfoToOrganizer")
    public Result<Activity> getActInfoToOrganizer(@RequestParam("uid") long uid, @RequestParam("aid") long aid){
        return Result.success(activityService.getActInfoToOrganizer(uid,aid));
    }
    // 面向用户提供活动的部分信息
    @GetMapping("/getActInfoToAll")
    public Result<ActInfoToAllVO> getActInfoToAll(@RequestParam("aid") long aid){
        return Result.success(activityService.getActInfoToAll(aid));
    }
    // 返回用户的所有活动信息,用于日程视图
    @GetMapping("/getActSchedule")
    public Result<List<ActScheduleVO>> getActSchedule(@RequestParam("uid") long uid){
        return Result.success(activityService.getActSchedule(uid));
    }
    // 分页返回活动页的所有活动
    @PostMapping("/getAllActivity")
    public Result<PageResult> getAllActivity(@RequestBody BasePageQueryDTO basePageQueryDTO){
        return Result.success(activityService.pageQueryBaseActInfoVO(basePageQueryDTO));
    }

    // 删除活动,只有活动组织者或者管理员具有权限
    @GetMapping("/deleteActivity")
    public Result<String> deleteActivity(@RequestParam("uid") long uid,@RequestParam("aid") long aid)
    {
        activityService.deleteActivity(uid,aid);
        return Result.success("删除成功!");
    }
    // 活动预算设定
    @GetMapping("/setBudget")
    public Result<Boolean> setBudget(@RequestParam("uid") long uid,@RequestParam("aid") long aid,@RequestParam("budget") int budget){
        activityService.setBudget(uid,aid,budget);
        return Result.success();
    }

    // 用户参与活动
    @GetMapping("/JoinActivity")
    public Result<Boolean> joinAct(@RequestParam("uid") long uid,@RequestParam("aid") long aid,@RequestParam("reason") String reason){
        activityService.joinAct(uid, aid, reason);
        return Result.success();
    }

}
