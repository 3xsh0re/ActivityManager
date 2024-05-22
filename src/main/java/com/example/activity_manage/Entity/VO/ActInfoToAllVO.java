package com.example.activity_manage.Entity.VO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.minidev.json.JSONObject;

import java.io.Serializable;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActInfoToAllVO implements Serializable {//用于生成活动详情页，面向所有用户
    private long orgId; //组织者id
    private String username; //组织者姓名
    private String actName;
    private String actDescription;
    private int status;
    private Date beginTime;
    private Date endTime;
    private int totalBudget;//预算
    private String place;
    private double rank; //活动评分
    private JSONObject userGroup;//参与者分组
    private JSONObject userList; //参与者列表<id,role>
    private JSONObject actStatus; //活动内部细节流程<StatusName,StatusDescription>
}
