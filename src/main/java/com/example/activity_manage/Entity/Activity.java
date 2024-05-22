package com.example.activity_manage.Entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Map;

@Data
@NoArgsConstructor
public class Activity {
    private long id;
    private String actName;
    private Map<String, String> userList; //参与者列表<id,role>
    private String actDescription;
    private int status;
    private Date beginTime;
    private Date endTime;
    private double totalBudget;//预算
    private Map<String, Double> userCost;//用户开销
    private Map<String, Integer> userGroup;//参与者分组
    private String place;
    private boolean ifFileStore;//是否开启文件上传功能
    private double rank; //活动评分
    private Map<String ,Integer> resource; //其他资源及其使用量
    private Map<String,String> roleList; //用户角色列表
    private Map<String,Double> rankList; //用户评分列表
    private Map<String,String> actStatus; //活动内部细节流程<StatusName,StatusDescription>
}
