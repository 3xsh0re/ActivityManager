package com.example.activity_manage.Mapper;

import ch.qos.logback.core.joran.sanity.Pair;
import org.apache.ibatis.annotations.Mapper;

import java.sql.Date;

@Mapper
public interface ActivityMapper {
    public Pair<Date,Date> activityDateGet(long AID);
}
