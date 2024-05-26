package com.example.activity_manage.Service;

import ch.qos.logback.core.joran.sanity.Pair;
import com.example.activity_manage.Entity.Activity;
import com.example.activity_manage.Entity.DTO.ActivityCreateDTO;
import com.example.activity_manage.Entity.DTO.BasePageQueryDTO;
import com.example.activity_manage.Entity.VO.ActInfoToAllVO;
import com.example.activity_manage.Entity.VO.ActScheduleVO;
import com.example.activity_manage.Result.PageResult;

import java.sql.Timestamp;
import java.util.List;

public interface ActivityService {
    Boolean ActivityCreate(ActivityCreateDTO activityCreateDTO);
    Pair<Timestamp, Timestamp> ActivityDateGet(long AID);
    Activity getActInfoToOrganizer(long uid,long aid); //面向组织者
    ActInfoToAllVO getActInfoToAll(long aid); //面向所有用户
    List<ActScheduleVO> getActSchedule(long uid); //获取某个用户的所有活动日程
    List<Activity> getAllActivity(); // 获取全部活动数据
    void deleteActivity(long uid,long aid);
    void setBudget(long uid,long aid,int budget);
    PageResult pageQueryBaseActInfoVO(BasePageQueryDTO basePageQueryDTO);
}
