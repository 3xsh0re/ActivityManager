package com.example.activity_manage.Entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Map;

@Data
@NoArgsConstructor
public class Activity {
    private long id;
    private String actiName;
    private Map<String, Integer> userList;
    private String actDescription;
    private int status;
    private Date beginTime;
    private Date endTime;
    private double totalBudget;//预算
    private Map<String, Double> userCost;//用户开销
    private Map<String, Integer> userGroup;//参与者分组
    private String place;
    private Map<String ,Integer> resource;//其他资源及其使用量
}
