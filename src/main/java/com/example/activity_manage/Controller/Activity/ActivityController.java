package com.example.activity_manage.Controller.Activity;

import ch.qos.logback.core.joran.sanity.Pair;
import com.example.activity_manage.Entity.DTO.ActivityCreateDTO;
import com.example.activity_manage.Result.Result;
import com.example.activity_manage.Service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;

@RestController
@RequestMapping("/activity")
public class ActivityController {
    @Autowired
    ActivityService activityService;
    @PostMapping("/create")
    public Result<Boolean> activityCreate(@RequestBody ActivityCreateDTO activityCreateDTO)
    {
        return  Result.success(activityService.ActivityCreate(activityCreateDTO));
    }
    @GetMapping("/getDate")
    public Result<Pair<Date, Date>> activityDateGet(@RequestParam("AID") long AID){
        return Result.success(activityService.ActivityDateGet(AID));
    }
}
