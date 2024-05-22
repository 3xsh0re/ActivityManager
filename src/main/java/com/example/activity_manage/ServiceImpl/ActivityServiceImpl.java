package com.example.activity_manage.ServiceImpl;

import ch.qos.logback.core.joran.sanity.Pair;
import com.example.activity_manage.Constant.MessageConstant;
import com.example.activity_manage.Entity.Activity;
import com.example.activity_manage.Entity.DTO.ActivityCreateDTO;
import com.example.activity_manage.Entity.VO.ActInfoToAllVO;
import com.example.activity_manage.Exception.NotOrganizerForActivityException;
import com.example.activity_manage.Exception.PageNotFoundException;
import com.example.activity_manage.Exception.SystemBusyException;
import com.example.activity_manage.Mapper.ActivityMapper;
import com.example.activity_manage.Mapper.UserMapper;
import com.example.activity_manage.Service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
            throw new SystemBusyException(MessageConstant.SYSTEM_BUSY);
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
            throw new NotOrganizerForActivityException(MessageConstant.NOT_ORGANIZER_FOR_ACTIVITY);
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
}
