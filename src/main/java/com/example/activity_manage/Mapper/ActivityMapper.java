package com.example.activity_manage.Mapper;

import ch.qos.logback.core.joran.sanity.Pair;
import com.example.activity_manage.Entity.Activity;
import com.example.activity_manage.Entity.DTO.ActivityCreateDTO;
import com.example.activity_manage.Entity.VO.ActInfoToAdminVO;
import com.example.activity_manage.Entity.VO.ActInfoToAllVO;
import org.apache.ibatis.annotations.Mapper;

import java.sql.Date;
import java.util.List;

@Mapper
public interface ActivityMapper {
    Pair<Date,Date> activityDateGet(long AID);
    boolean activityCreate(ActivityCreateDTO activity);
    long getUidByAid(long aid);
    Activity getActInfoToOrganizer(long aid);
    List<Activity> getAllAct();
    List<ActInfoToAdminVO> getAllActInfoToAdmin();//返回给管理员的所有活动信息
    ActInfoToAllVO getActInfoToAll(long aid);
    List<Activity> getActByUid(long uid);
    Boolean checkActContent(long aid, int status, String checkResult);//更新审核活动结果
    int getStatusById(long aid);
}
