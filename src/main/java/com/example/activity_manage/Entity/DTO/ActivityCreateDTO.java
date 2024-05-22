package com.example.activity_manage.Entity.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.minidev.json.JSONObject;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActivityCreateDTO {
    private long uid; // 用户ID
    private String actName; // 活动的名称
    private String actDescription; // 活动描述
    private Date beginTime; // 开始时间
    private Date endTime; // 活动的结束时间
    private boolean ifFileStore;// 活动是否开启文件上传功能
    private JSONObject roleList; // 身份列表<role,quantity>
    private JSONObject actStatus;//活动内部细节流程<StatusName,StatusDescription>
}
