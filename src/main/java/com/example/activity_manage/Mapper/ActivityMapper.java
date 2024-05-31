package com.example.activity_manage.Mapper;

import ch.qos.logback.core.joran.sanity.Pair;
import com.example.activity_manage.Entity.Activity;
import com.example.activity_manage.Entity.DTO.ActivityCreateDTO;
import com.example.activity_manage.Entity.DTO.BasePageQueryDTO;
import com.example.activity_manage.Entity.VO.ActInfoToManagerVO;
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
    String getActNameByAid(long aid);
    long getUidByAid(long aid);
    Activity getActInfoToOrganizer(long aid);
    List<Activity> getAllAct();
    ActInfoToAllVO getActInfoToAll(long aid);
    JSONObject getUserList(long aid); //获取参与此活动的全部用户
    Boolean checkUserExist(long aid, long uid); // 判断指定用户是否存在于指定活动中
    Boolean checkActContent(long aid, int status, String checkResult);//更新审核活动结果
    int getStatusById(long aid);
    long getMaxId();
    void deleteActivity(long aid);
    void setBudget(long aid,int budget);
    int getBudget(long aid);
    int getReQuantityByReName(long aid,String resourceName); //通过资源名获取资源数量
    void updateActivityResource(long aid, String resourceName, int quantity); // 更新活动的资源
    void setRankForAct(long aid, JSONObject rankList, double score);// 为活动评分
    JSONObject getRankList(long aid);
    void updateUnCheckedUserList(long aid,JSONObject unCheckedUserList);
    void deleteUserInUnCheckedList(long aid,String uid);
    void updateUserList(long aid,JSONObject userList);
    void deleteUserInGroup(long aid, String uid);
    JSONObject getUnCheckedUserList(long aid);
    Page<BaseActInfoVO>  pageQueryBaseActInfoVO(BasePageQueryDTO basePageQueryDTO);
    Page<ActInfoToManagerVO> pageQueryActInfoToAdmin(BasePageQueryDTO basePageQueryDTO);//分页返回给管理员的所有活动信息
    String getUserRole(long aid, long  uid); // 获取活动里指定用户的角色
    void updateUserRole(long aid, long uid, String role); // 更新用户角色
    int getRoleNum(long aid, String role); // 获取role的数量
    void updateRoleList(long aid, String role, int quantity); // 更新roleList对应的数量
    void updateUserGroup(long aid, long uid, String group); // 更新用户分组
    void insertMessage(long aid, String timestamp, String message); // 发送消息
    Boolean checkUserInActivity(long aid, long uid); // 检查用户UID是否存在于指定活动中
    List<JSONObject> getAllMessage(long aid); // 获取活动全部消息记录
}
