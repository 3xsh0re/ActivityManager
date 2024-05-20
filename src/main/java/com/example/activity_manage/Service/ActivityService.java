package com.example.activity_manage.Service;

import ch.qos.logback.core.joran.sanity.Pair;
import com.example.activity_manage.Entity.DTO.ActivityCreateDTO;

import java.sql.Date;

public interface ActivityService {
    Boolean ActivityCreate(ActivityCreateDTO activityCreateDTO);
    Pair<Date, Date> ActivityDateGet(long AID);
}
