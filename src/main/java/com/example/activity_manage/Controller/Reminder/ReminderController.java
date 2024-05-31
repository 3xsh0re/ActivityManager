package com.example.activity_manage.Controller.Reminder;

import com.example.activity_manage.Entity.DTO.ReminderPageQueryDTO;
import com.example.activity_manage.Entity.Reminder;
import com.example.activity_manage.Result.PageResult;
import com.example.activity_manage.Result.Result;
import com.example.activity_manage.Service.ReminderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reminder")
public class ReminderController {
    @Autowired
    ReminderService reminderService;

    @PostMapping("/setReminder")
    public Result<Boolean> setReminder(@RequestBody Reminder reminder){
        reminderService.createNewReminder(reminder);
        return Result.success(true);
    }
    @GetMapping("/deleteReminder")
    public Result<Boolean> deleteReminder(@RequestParam("reminderId") long reminderId){
        reminderService.deleteReminderById(reminderId);
        return Result.success(true);
    }
    @PostMapping("/getReminderList")
    public Result<PageResult> getReminderList(@RequestBody ReminderPageQueryDTO pageQueryDTO)
    {
        return Result.success(reminderService.pageQueryReminder(pageQueryDTO));
    }

}
