package com.example.activity_manage.Mapper;

import ch.qos.logback.core.joran.sanity.Pair;
import com.example.activity_manage.Entity.Activity;
import com.example.activity_manage.Entity.DTO.ActivityCreateDTO;
import org.apache.ibatis.annotations.Mapper;

import java.sql.Date;

@Mapper
public interface ActivityMapper {
    public Pair<Date,Date> activityDateGet(long AID);
    public boolean activityCreate(ActivityCreateDTO activity);
}
