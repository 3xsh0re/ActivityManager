package com.example.activity_manage.Entity.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
import java.util.Map;

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
    private Map<String,Integer> roleList; // 身份列表<role,quantity>
    private boolean ifFileStore;// 活动是否开启文件上传功能
    private Map<String,String> actStatus;//活动内部细节流程<StatusName,StatusDescription>
}
