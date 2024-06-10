package com.example.activity_manage.Controller.Activity;

import ch.qos.logback.core.joran.sanity.Pair;
import com.example.activity_manage.Entity.Activity;
import com.example.activity_manage.Entity.DTO.*;
import com.example.activity_manage.Entity.VO.ActInfoToAllVO;
import com.example.activity_manage.Entity.VO.ActReportVO;
import com.example.activity_manage.Entity.VO.ActScheduleVO;
import com.example.activity_manage.Result.PageResult;
import com.example.activity_manage.Result.Result;
import com.example.activity_manage.Service.ActivityService;
import net.minidev.json.JSONObject;
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
        System.out.println(activityCreateDTO);
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
    @GetMapping("/getBudget")
    public Result<Integer> getBudget(@RequestParam("aid") long aid,@RequestParam("uid") long uid){
        return Result.success(activityService.getBudget(aid, uid));
    }

    // 活动流程更新
    @PostMapping("/updateActProcess")
    public Result<Boolean> updateActProcess(@RequestBody UpdateActProcessDTO updateActProcessDTO){
        activityService.updateActProcess(updateActProcessDTO);
        return Result.success();
    }


    // 管理活动参与者角色
    @PostMapping("/setParticipantRole")
    public Result<Boolean> setParticipantRole(@RequestBody ActivitySetParticipantRoleDTO activitySetParticipantRoleDTO)
    {
        return Result.success(activityService.setParticipantRole(activitySetParticipantRoleDTO));
    }
    // 用户参与活动
    @GetMapping("/JoinActivity")
    public Result<Boolean> joinAct(@RequestParam("uid") long uid,@RequestParam("aid") long aid,@RequestParam("reason") String reason){
        activityService.joinAct(uid, aid, reason);
        return Result.success();
    }
    // 用户退出活动
    @GetMapping("/exitActivity")
    public Result<Boolean> exitAct(@RequestParam("uid") long uid,@RequestParam("aid") long aid){
        activityService.exitAct(uid,aid);
        return Result.success();
    }

    // 分页返回用户申请活动请求
    @PostMapping("/GetUnCheckedUserList")
    public Result<PageResult> getUnCheckedUserList(@RequestBody ActivityPageQueryDTO pageQueryDTO){
        return Result.success(activityService.pageQueryUnCheckedUser(pageQueryDTO));
    }
    @PostMapping("/setParticipantGroup")
    public Result<Boolean> serParticipantGroup(@RequestBody ActivitySetParticipantGroupDTO activitySetParticipantGroupDTO)
    {
        return Result.success(activityService.serParticipantGroup(activitySetParticipantGroupDTO));
    }

    // 审核活动参与申请
    @GetMapping("/CheckApplication")
    public Result<Boolean> checkApplication(@RequestParam("uid") long uid,@RequestParam("aid") long aid, @RequestParam("unCheckedId") long unCheckedId,@RequestParam("result") boolean result){
        return Result.success(activityService.checkApplication(uid, aid, unCheckedId, result));
    }

    // 用户为活动评分
    @GetMapping("/setRankForAct")
    public Result<Boolean> setRankForAct(@RequestParam("uid") long uid,@RequestParam long aid,@RequestParam double rank){
        return Result.success(activityService.setRankForAct(uid,aid,rank));
    }

    @PostMapping("/participantInteractiveSend")
    public Result<Boolean> participantInteractiveSend(@RequestBody ActivityParticipantInteractiveSendDTO activityParticipantInteractiveSendDTO)
    {
        return Result.success(activityService.participantInteractiveSend(activityParticipantInteractiveSendDTO));
    }

    @PostMapping("/participantInteractiveReceive")
    public Result<List<JSONObject>> participantInteractiveReceive(@RequestBody ActivityParticipantInteractiveReceiveDTO activityParticipantInteractiveReceiveDTO)
    {
        return Result.success(activityService.participantInteractiveReceive(activityParticipantInteractiveReceiveDTO));
    }

    // 生成活动报告
    @GetMapping("/getActReport")
    public Result<ActReportVO> getActReport(@RequestParam("aid") long aid, @RequestParam("uid") long uid){
        return Result.success(activityService.getActReport(aid,uid));
    }
}
