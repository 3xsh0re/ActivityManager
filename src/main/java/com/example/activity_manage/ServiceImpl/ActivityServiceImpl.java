package com.example.activity_manage.ServiceImpl;

import ch.qos.logback.core.joran.sanity.Pair;
import com.example.activity_manage.Constant.MessageConstant;
import com.example.activity_manage.Entity.DTO.ActivityCreateDTO;
import com.example.activity_manage.Exception.SystemBusyException;
import com.example.activity_manage.Mapper.ActivityMapper;
import com.example.activity_manage.Service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ActivityServiceImpl implements ActivityService {
    @Autowired
    ActivityMapper activityMapper;
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
}
