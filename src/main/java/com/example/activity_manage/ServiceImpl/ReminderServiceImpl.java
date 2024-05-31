package com.example.activity_manage.ServiceImpl;

import com.example.activity_manage.Constant.MessageConstant;
import com.example.activity_manage.Entity.DTO.ReminderPageQueryDTO;
import com.example.activity_manage.Entity.Reminder;
import com.example.activity_manage.Exception.ActivityException;
import com.example.activity_manage.Mapper.ReminderMapper;
import com.example.activity_manage.Service.ReminderService;
import com.example.activity_manage.Utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.time.LocalDateTime.*;

@Service
public class ReminderServiceImpl implements ReminderService {
    @Autowired
    ReminderMapper reminderMapper;
    @Autowired
    RedisUtil redisUtil;

    @Override
    public void createNewReminder(Reminder reminder) {
        String key = "TOKEN_" + reminder.getUid();
        Map<Object, Object> userMap  = redisUtil.hmget(key);
        if (userMap.isEmpty())
        {
            throw new ActivityException(MessageConstant.NOT_HAVE_THIS_PERMISSION);
        }
        int id = (int)userMap.get("id");
        // 从redis中取出id判断是否为当前用户操作
        if (id != reminder.getUid())
        {
            throw new ActivityException(MessageConstant.NOT_HAVE_THIS_PERMISSION);
        }
        if (reminder.getReminderTime().before(new Date())) {
            throw new ActivityException("提醒时间必须晚于当前时间！");
        }
        // 更新Reminder数据库
        reminderMapper.createNewReminder(reminder);
    }

    @Override
    public void deleteReminderById(long id) {
        reminderMapper.deleteReminderById(id);
    }

    @Override
    public void pageQueryReminder(ReminderPageQueryDTO pageQueryDTO) {

    }

    @Override
    public List<Reminder> getUpcomingReminders() {
        return reminderMapper.getUpcomingReminders();
    }
}
