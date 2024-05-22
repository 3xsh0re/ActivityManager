package com.example.activity_manage.ServiceImpl;

import ch.qos.logback.core.joran.sanity.Pair;
import com.example.activity_manage.Entity.Activity;
import com.example.activity_manage.Entity.DTO.ActivityCreateDTO;
import com.example.activity_manage.Mapper.ActivityMapper;
import com.example.activity_manage.Service.ActivityService;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class ActivityServiceImpl implements ActivityService {
    @Autowired
    ActivityMapper activityMapper;
    @Override
    public Boolean ActivityCreate(ActivityCreateDTO activityCreateDTO)
    {
        long userid = activityCreateDTO.getUserid();
        String activityName = activityCreateDTO.getActivityName();
        String activityDescription = activityCreateDTO.getActivityDescription();
        Date beginTime = activityCreateDTO.getBeginTime();
        Date endTime = activityCreateDTO.getEndTime();
        Activity activity = new Activity();

        return null;
    }

    @Override
    public Pair<java.sql.Date, java.sql.Date> ActivityDateGet(long AID) {
        return activityMapper.activityDateGet(AID);
    }
}
