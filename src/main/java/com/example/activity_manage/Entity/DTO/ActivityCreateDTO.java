package com.example.activity_manage.Entity.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActivityCreateDTO {
    private long userid; // 用户ID
    private String activityName; // 活动的名称
    private String activityDescription; // 活动描述
    private Date beginTime; // 开始时间
    private Date endTime; // 活动的结束时间
}
