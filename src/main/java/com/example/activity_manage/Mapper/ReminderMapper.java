package com.example.activity_manage.Mapper;

import com.example.activity_manage.Entity.Reminder;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface ReminderMapper {
    void createNewReminder(Reminder reminder);
    void deleteReminderById(long id);
    Page<Reminder> getReminderList(long uid);

    List<Reminder> getUpcomingReminders(); //获取即将到来的提醒

}
