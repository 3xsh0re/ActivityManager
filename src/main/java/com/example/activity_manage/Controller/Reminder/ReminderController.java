package com.example.activity_manage.Controller.Reminder;

import com.example.activity_manage.Entity.DTO.ReminderPageQueryDTO;
import com.example.activity_manage.Entity.Reminder;
import com.example.activity_manage.Result.PageResult;
import com.example.activity_manage.Result.Result;
import com.example.activity_manage.Service.ReminderService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/reminder")
public class ReminderController {
    @Autowired
    ReminderService reminderService;

    @PostMapping("/setReminder")
    public Result<Boolean> setReminder(@RequestBody Reminder reminder, HttpServletRequest request){
        Map<Object, Object> userInfo = (Map<Object, Object>) request.getAttribute("userInfo");
        long uid = Long.parseLong(userInfo.get("id").toString());
        reminderService.createNewReminder(reminder,uid);
        return Result.success(true);
    }
    @GetMapping("/deleteReminder")
    public Result<Boolean> deleteReminder(@RequestParam("reminderId") long reminderId,@RequestParam("uid") long uid, HttpServletRequest request){
        Map<Object, Object> userInfo = (Map<Object, Object>) request.getAttribute("userInfo");
        long uidInRedis = Long.parseLong(userInfo.get("id").toString());
        reminderService.deleteReminderById(reminderId,uid,uidInRedis);
        return Result.success(true);
    }
    @PostMapping("/getReminderList")
    public Result<PageResult> getReminderList(@RequestBody ReminderPageQueryDTO pageQueryDTO,HttpServletRequest request)
    {
        Map<Object, Object> userInfo = (Map<Object, Object>) request.getAttribute("userInfo");
        long uid = Long.parseLong(userInfo.get("id").toString());
        return Result.success(reminderService.pageQueryReminder(pageQueryDTO,uid));
    }
}
