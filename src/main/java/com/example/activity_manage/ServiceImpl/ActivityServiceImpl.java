package com.example.activity_manage.ServiceImpl;

import ch.qos.logback.core.joran.sanity.Pair;
import com.example.activity_manage.Constant.MessageConstant;
import com.example.activity_manage.Entity.Activity;
import com.example.activity_manage.Entity.DTO.ActivityCreateDTO;
import com.example.activity_manage.Entity.DTO.ActivitySetParticipantRoleDTO;
import com.example.activity_manage.Entity.DTO.ActivityPageQueryDTO;
import com.example.activity_manage.Entity.DTO.BasePageQueryDTO;
import com.example.activity_manage.Entity.VO.ActInfoToAllVO;
import com.example.activity_manage.Entity.VO.ActScheduleVO;
import com.example.activity_manage.Entity.VO.BaseActInfoVO;
import com.example.activity_manage.Entity.VO.UnCheckedUserVO;
import com.example.activity_manage.Exception.ActivityException;
import com.example.activity_manage.Exception.LoginRegisterException;
import com.example.activity_manage.Exception.PageNotFoundException;
import com.example.activity_manage.Mapper.ActivityMapper;
import com.example.activity_manage.Mapper.UserMapper;
import com.example.activity_manage.Result.PageResult;
import com.example.activity_manage.Service.ActivityService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;

@Service
public class ActivityServiceImpl implements ActivityService {
    @Autowired
    ActivityMapper activityMapper;
    @Autowired
    UserMapper userMapper;
    @Override
    public Boolean ActivityCreate(ActivityCreateDTO activityCreateDTO)
    {
        JSONObject jsonObject = userMapper.getActList(activityCreateDTO.getUid());//获取当前用户参与的活动表
        if (jsonObject != null)
        {
            JSONObject actList = (JSONObject) jsonObject.get("actList");
            // 获取JSONObject的键的集合
            Set<String> keys = actList.keySet();
            // 获取新创建的活动时间
            java.util.Date beginTime = activityCreateDTO.getBeginTime();
            java.util.Date endTime   = activityCreateDTO.getEndTime();
            // 遍历所有的键,即活动id
            for (String aid : keys) {
                // 获取时间
                Pair<Timestamp, Timestamp> tmp = activityMapper.activityDateGet(Long.parseLong(aid));
                java.util.Date extActBeginTime = new java.util.Date(tmp.first.getTime());
                java.util.Date extActEndTime = new java.util.Date(tmp.second.getTime());
                // 检测时间冲突
                if (extActBeginTime.before(beginTime) && extActEndTime.after(beginTime) ||
                        extActBeginTime.before(endTime) && extActEndTime.after(endTime) ||
                        extActBeginTime.equals(beginTime) || extActBeginTime.equals(endTime)||
                        extActEndTime.equals(beginTime) || extActEndTime.equals(endTime)
                ){
                    throw new ActivityException(MessageConstant.ACTIVITY_TIME_CONFLICT);
                }
            }
        }
        // 设定活动的第一个用户：组织者
        JSONObject userList = new JSONObject();
        userList.put(Long.toString(activityCreateDTO.getUid()), "组织者");
        activityCreateDTO.setUserList(userList);
        // 创建活动,直接将activityCreateDTO传入即可
        activityMapper.activityCreate(activityCreateDTO);
        // 更新User表中字段
        if (jsonObject == null){
            // 将创建的活动修改到User的ActList中
            JSONObject new_actList = new JSONObject();
            new_actList.put(Long.toString(activityMapper.getMaxId()),activityCreateDTO.getActName());
            userMapper.updateActList(activityCreateDTO.getUid(),new_actList);
        }
        else {
            JSONObject actList = (JSONObject) jsonObject.get("actList");
            actList.put(Long.toString(activityMapper.getMaxId()),activityCreateDTO.getActName());
            userMapper.updateActList(activityCreateDTO.getUid(),actList);
        }
        return true;
    }

    @Override
    public Pair<Timestamp, Timestamp> ActivityDateGet(long AID) {
        return activityMapper.activityDateGet(AID);
    }

    @Override
    public Activity getActInfoToOrganizer(long uid, long aid) {
        long uid_t = activityMapper.getUidByAid(aid);
        if (uid == 0)
        {
            throw new PageNotFoundException(MessageConstant.PAGE_NOT_FOUND);//返回页面不存在
        }
        //非组织者用户非法查看
        if (uid != uid_t)
        {
            throw new ActivityException(MessageConstant.NOT_ORGANIZER_FOR_ACTIVITY);
        }
        return activityMapper.getActInfoToOrganizer(aid);
    }

    @Override
    public ActInfoToAllVO getActInfoToAll(long aid) {
        ActInfoToAllVO actInfoToAllVO = activityMapper.getActInfoToAll(aid);
        // 活动不存在
        if (actInfoToAllVO == null)
        {
            throw new PageNotFoundException(MessageConstant.PAGE_NOT_FOUND);//返回页面不存在
        }
        actInfoToAllVO.setUsername(userMapper.getUsernameById(actInfoToAllVO.getOrgId()));
        return actInfoToAllVO;
    }

    @Override
    public List<ActScheduleVO> getActSchedule(long uid) {
        List<Activity> l_act = activityMapper.getAllAct();
        List<ActScheduleVO> l_actSchedule = new ArrayList<>();
        // 将Activity转换到VO
        for (Activity act : l_act){
            String role = act.getUserList().getAsString(Long.toString(uid));
            if (role != null)
            {
                ActScheduleVO actScheduleVO = new ActScheduleVO();
                actScheduleVO.setAid(act.getId());
                actScheduleVO.setActName(act.getActName());
                actScheduleVO.setBeginTime(act.getBeginTime());
                actScheduleVO.setEndTime(act.getEndTime());
                actScheduleVO.setRole(act.getUserList().getAsString(Long.toString(uid)));
                l_actSchedule.add(actScheduleVO);
            }
        }
        return l_actSchedule;
    }


    @Override
    public void deleteActivity(long uid,long aid) {
        if (activityMapper.getActInfoToOrganizer(aid) == null)
        {
            throw new ActivityException(MessageConstant.ACTIVITY_NOT_EXIST);
        }
        if (uid == activityMapper.getUidByAid(aid) || uid == 1)//非组织者或者管理员不能删除活动
        {
            activityMapper.deleteActivity(aid);
        }
        else {
            throw new ActivityException(MessageConstant.NOT_HAVE_THIS_PERMISSION);
        }
    }

    @Override
    public void setBudget(long uid, long aid, int budget) {
        if (activityMapper.getActInfoToAll(aid) == null)
        {
            throw new ActivityException(MessageConstant.ACTIVITY_NOT_EXIST);
        }
        if (uid != activityMapper.getUidByAid(aid))
        {
            throw new ActivityException(MessageConstant.NOT_HAVE_THIS_PERMISSION);
        }
        activityMapper.setBudget(aid,budget);
    }

    @Override
    public void joinAct(long uid, long aid, String reason) {
        ActInfoToAllVO activity = activityMapper.getActInfoToAll(aid);
        String  username = userMapper.getUsernameById(uid);
        if (activity == null)
        {
            throw new ActivityException(MessageConstant.ACTIVITY_NOT_EXIST);
        }
        if (username == null){
            throw new LoginRegisterException(MessageConstant.ACCOUNT_NOT_FOUND);
        }
        // 更新User的待审核活动
        JSONObject objectUser = userMapper.getWantJoinActList(uid);
        if (objectUser == null)//User没有待申请的活动
        {
            objectUser = new JSONObject();
            objectUser.put( Long.toString(aid), reason);
            userMapper.updateWantJoinActList(aid,objectUser);
        }
        else{
            JSONObject wJActList = (JSONObject) objectUser.get("wantJoinActList");
            wJActList.put( Long.toString(aid), reason);
            userMapper.updateWantJoinActList(aid, wJActList);
        }
        // 更新Activity的待审核用户
        JSONObject objectActivity = activityMapper.getUnCheckedUserList(aid);
        if (objectActivity == null){
            objectActivity = new JSONObject();
            objectActivity.put(Long.toString(uid),reason);
            activityMapper.updateUnCheckedUserList(aid,objectActivity);
        }
        else {
            JSONObject uCActList = (JSONObject) objectActivity.get("unCheckedUserList");
            uCActList.put(Long.toString(uid),reason);
            activityMapper.updateUnCheckedUserList(aid,uCActList);
        }
    }

    //分页查询返回活动
    public PageResult pageQueryBaseActInfoVO(BasePageQueryDTO basePageQueryDTO) {
        //开始分页查询
        PageHelper.startPage(basePageQueryDTO.getPage(), basePageQueryDTO.getPageSize());
        Page<BaseActInfoVO> page = activityMapper.pageQueryBaseActInfoVO(basePageQueryDTO);
        long total = page.getTotal();
        List<BaseActInfoVO> records = page.getResult();
        return new PageResult(total, records);
    }
}
