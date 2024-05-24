package com.example.activity_manage.ServiceImpl;

import ch.qos.logback.core.joran.sanity.Pair;
import com.example.activity_manage.Constant.MessageConstant;
import com.example.activity_manage.Entity.Activity;
import com.example.activity_manage.Entity.DTO.ActivityCreateDTO;
import com.example.activity_manage.Entity.DTO.BasePageQueryDTO;
import com.example.activity_manage.Entity.VO.ActInfoToAllVO;
import com.example.activity_manage.Entity.VO.ActScheduleVO;
import com.example.activity_manage.Entity.VO.BaseActInfoVO;
import com.example.activity_manage.Exception.ActivityException;
import com.example.activity_manage.Exception.PageNotFoundException;
import com.example.activity_manage.Exception.SystemException;
import com.example.activity_manage.Mapper.ActivityMapper;
import com.example.activity_manage.Mapper.UserMapper;
import com.example.activity_manage.Result.PageResult;
import com.example.activity_manage.Service.ActivityService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        try {
            // 创建活动,直接将activityCreateDTO传入即可
            return activityMapper.activityCreate(activityCreateDTO);
        }catch (Exception e){
            throw new SystemException(MessageConstant.SYSTEM_BUSY);
        }
    }

    @Override
    public Pair<java.sql.Date, java.sql.Date> ActivityDateGet(long AID) {
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
    public List<Activity> getAllActivity()
    {
        return activityMapper.getAllAct();
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
        activityMapper.setBudget(aid,budget);
    }
    //分页查询返回活动
    public PageResult pageQuery(BasePageQueryDTO basePageQueryDTO) {
        //开始分页查询
        PageHelper.startPage(basePageQueryDTO.getPage(), basePageQueryDTO.getPageSize());
        Page<BaseActInfoVO> page = activityMapper.pageQuery(basePageQueryDTO);
        long total = page.getTotal();
        List<BaseActInfoVO> records = page.getResult();
        return new PageResult(total, records);
    }
}
