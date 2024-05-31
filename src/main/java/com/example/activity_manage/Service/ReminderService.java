package com.example.activity_manage.Service;

import com.example.activity_manage.Entity.DTO.ReminderPageQueryDTO;
import com.example.activity_manage.Entity.Reminder;
import com.example.activity_manage.Result.PageResult;

import java.util.List;

public interface ReminderService {
    void createNewReminder(Reminder reminder,long uid);
    void deleteReminderById(long id,long uid,long uidInRedis);
    PageResult pageQueryReminder(ReminderPageQueryDTO pageQueryDTO,long uid);
    List<Reminder> getUpcomingReminders();
}
