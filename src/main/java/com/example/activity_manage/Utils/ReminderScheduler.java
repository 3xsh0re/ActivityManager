package com.example.activity_manage.Utils;

import com.example.activity_manage.Entity.Activity;
import com.example.activity_manage.Entity.Notice;
import com.example.activity_manage.Entity.Reminder;
import com.example.activity_manage.Entity.VO.UserInfoVO;
import com.example.activity_manage.Mapper.ActivityMapper;
import com.example.activity_manage.Service.NoticeService;
import com.example.activity_manage.Service.ReminderService;
import com.example.activity_manage.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

// 用于触发定时任务
@Component
public class ReminderScheduler {
    @Autowired
    ReminderService reminderService;
    @Autowired
    UserService userService;
    @Autowired
    NoticeService noticeService;

    @Autowired
    ActivityMapper activityMapper;

    @Autowired
    SendUtil sendUtil;
    @Scheduled(fixedRate = 30000) // 每分钟执行一次
    public void checkReminders() {
        List<Reminder> reminders = reminderService.getUpcomingReminders();
        Date currentDate = new Date();
        // 创建Calendar实例，并设置为当前时间，同时指定时区为中国标准时间
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Shanghai"));
        calendar.setTime(currentDate);
        // 将时间增加8小时
        calendar.add(Calendar.HOUR_OF_DAY, 8);
        calendar.add(Calendar.MINUTE,1);
        // 获取加上8小时后的时间
        Date nowTime = calendar.getTime();

        for (Reminder reminder : reminders) {
            if (reminder.getReminderTime().before(nowTime)) {
                UserInfoVO userInfoVO = userService.getUserInfo(reminder.getUid());
                // 邮箱提醒
                String email = userInfoVO.getEmail();
                sendUtil.SendMessageByEmail(email,reminder.getContent());
                // 通知提醒
                Notice notice = new Notice();
                notice.setIfRead(false);
                notice.setType(0);
                notice.setContent(reminder.getContent());
                notice.setSendUid(1);
                notice.setReceiveUid(userInfoVO.getId());
                String nowTimeHash = JwtUtil.getNowTimeHash();
                notice.setGroupId(nowTimeHash);
                noticeService.createNotice(notice);
            }
        }
    }
    @Scheduled(fixedRate = 30000) // 每分钟执行一次
    public void checkActivityEnd() {
        List<Activity> actList = activityMapper.getAllAct();
        Date currentDate = new Date();
        // 创建Calendar实例，并设置为当前时间，同时指定时区为中国标准时间
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Shanghai"));
        calendar.setTime(currentDate);
        // 将时间增加8小时
        calendar.add(Calendar.HOUR_OF_DAY, 8);
        calendar.add(Calendar.MINUTE,1);
        // 获取加上8小时后的时间
        Date nowTime = calendar.getTime();
        for (Activity act: actList) {
            if (act.getBeginTime().before(nowTime))
            {
                activityMapper.setActStatus(2,act.getId());
            }
            // 活动结束
            if (act.getEndTime().before(nowTime)) {
                activityMapper.setActStatus(3,act.getId());
            }
        }
    }
}
