package com.example.activity_manage.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.minidev.json.JSONObject;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Activity {
    private long id;
    private long uid; //组织者id
    private String actName;
    private String actDescription;
    private int status;
    private Date beginTime;
    private Date endTime;
    private int totalBudget;//预算
    private String place;
    private boolean ifFileStore;//是否开启文件上传功能
    private double rank; //活动评分
    private JSONObject userCost;//用户开销
    private JSONObject userGroup;//参与者分组
    private JSONObject resource; //其他资源及其使用量
    private JSONObject userList; //参与者列表<id,role>
    private JSONObject roleList; //用户角色列表
    private JSONObject rankList; //用户评分列表
    private JSONObject actStatus; //活动内部细节流程<StatusName,StatusDescription>
    private JSONObject unCheckedUserList; //待审核的用户列表<uid,reason>
    private JSONObject message;
}
