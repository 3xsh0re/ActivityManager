package com.example.activity_manage.Controller.Reminder;

import com.example.activity_manage.Entity.Reminder;
import com.example.activity_manage.Result.Result;
import com.example.activity_manage.Service.ReminderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
