package com.example.activity_manage.Service;

import ch.qos.logback.core.joran.sanity.Pair;
import com.example.activity_manage.Entity.Activity;
import com.example.activity_manage.Entity.DTO.*;
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
    void deleteActivity(long uid,long aid);
    void setBudget(long uid,long aid,int budget);
    Integer getBudget(long aid, long uid);
    void joinAct(long uid,long aid, String reason);
    boolean checkApplication(long uid,long aid, long unCheckedId,boolean result);
    boolean setRankForAct(long uid,long aid,double rank);
    PageResult pageQueryBaseActInfoVO(BasePageQueryDTO basePageQueryDTO);
    PageResult pageQueryUnCheckedUser(ActivityPageQueryDTO pageQueryDTO);
    Boolean setParticipantRole(ActivitySetParticipantRoleDTO activitySetParticipantRoleDTO); // 设置活动参与者分组, 输入为: 管理者, 活动, 参与者
    Boolean serParticipantGroup(ActivitySetParticipantGroupDTO activitySetParticipantGroupDTO); // 设置分组
    Boolean participantInteractiveSend(ActivityParticipantInteractiveSendDTO activityParticipantInteractiveSendDTO); // 发送消息
}
