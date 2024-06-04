package com.example.activity_manage.Entity.VO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActInfoToManagerVO implements Serializable {
    private long id;
    private long uid; //组织者id
    private String username; //组织者姓名
    private String actName;
    private String actDescription;
    private int status;
    private Date beginTime;
    private Date endTime;
    private int totalBudget;//预算
    private String place;
    private double rank; //活动评分
}
