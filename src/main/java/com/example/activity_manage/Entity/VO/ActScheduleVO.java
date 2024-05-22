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
public class ActScheduleVO implements Serializable {
    private long aid; //活动id
    private String actName;//活动名
    private Date beginTime;
    private Date endTime;
    private String role; //在活动中的角色
}
