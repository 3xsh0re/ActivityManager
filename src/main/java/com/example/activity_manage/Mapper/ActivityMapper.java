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
    Boolean checkUserExist(long aid, long uid); // 判断指定用户是否存在于指定活动中
    Boolean checkActContent(long aid, int status, String checkResult);//更新审核活动结果
    int getStatusById(long aid);
    long getMaxId();
    void deleteActivity(long aid);
    void setBudget(long aid,int budget);
    int getBudget(long aid);
    void updateUnCheckedUserList(long aid,JSONObject unCheckedUserList);
    JSONObject getUnCheckedUserList(long aid);
    Page<BaseActInfoVO>  pageQueryBaseActInfoVO(BasePageQueryDTO basePageQueryDTO);
    Page<ActInfoToAdminVO> pageQueryActInfoToAdmin(BasePageQueryDTO basePageQueryDTO);//分页返回给管理员的所有活动信息
    void updateActivityResource(long aid, String resourceName, int quantity); // 更新活动的资源
    String getUserRole(long aid, long  uid); // 获取活动里指定用户的角色
    void updateUserRole(long aid, long uid, String role); // 更新用户角色
    int getRoleNum(long aid, String role); // 获取role的数量
    void updateRoleList(long aid, String role, int quantity); // 更新roleList对应的数量
    void updateUserGroup(long aid, long uid, String group); // 更新用户分组
}
