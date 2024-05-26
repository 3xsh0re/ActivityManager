package com.example.activity_manage.Mapper;

import ch.qos.logback.core.joran.sanity.Pair;
import com.example.activity_manage.Entity.Activity;
import com.example.activity_manage.Entity.DTO.ActivityCreateDTO;
import com.example.activity_manage.Entity.DTO.BasePageQueryDTO;
import com.example.activity_manage.Entity.VO.ActInfoToAdminVO;
import com.example.activity_manage.Entity.VO.ActInfoToAllVO;
import com.example.activity_manage.Entity.VO.BaseActInfoVO;
import com.github.pagehelper.Page;
import net.minidev.json.JSONObject;
import org.apache.ibatis.annotations.Mapper;

import java.sql.Timestamp;
import java.util.List;

@Mapper
public interface ActivityMapper {
    Pair<Timestamp,Timestamp> activityDateGet(long aid);
    void activityCreate(ActivityCreateDTO activity);
    long getUidByAid(long aid);
    Activity getActInfoToOrganizer(long aid);
    List<Activity> getAllAct();
    ActInfoToAllVO getActInfoToAll(long aid);
//    List<Activity> getActByUid(long uid);
    JSONObject getUserList(long aid); //获取参与此活动的全部用户
    Boolean checkActContent(long aid, int status, String checkResult);//更新审核活动结果
    int getStatusById(long aid);
    long getMaxId();
    void deleteActivity(long aid);
    void setBudget(long aid,int budget);
    Page<BaseActInfoVO>  pageQueryBaseActInfoVO(BasePageQueryDTO basePageQueryDTO);
    Page<ActInfoToAdminVO> pageQueryActInfoToAdmin(BasePageQueryDTO basePageQueryDTO);//分页返回给管理员的所有活动信息
}
