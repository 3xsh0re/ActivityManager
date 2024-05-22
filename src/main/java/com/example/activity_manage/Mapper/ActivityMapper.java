package com.example.activity_manage.Mapper;

import ch.qos.logback.core.joran.sanity.Pair;
import com.example.activity_manage.Entity.Activity;
import com.example.activity_manage.Entity.DTO.ActivityCreateDTO;
import com.example.activity_manage.Entity.VO.ActInfoToAllVO;
import org.apache.ibatis.annotations.Mapper;

import java.sql.Date;

@Mapper
public interface ActivityMapper {
    public Pair<Date,Date> activityDateGet(long AID);
    public boolean activityCreate(ActivityCreateDTO activity);
    public long getUidByAid(long aid);
    public Activity getActInfoToOrganizer(long aid);
    public ActInfoToAllVO getActInfoToAll(long aid);
}
