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
public class BaseActInfoVO implements Serializable {
    private long id; //活动id
    private String actName;//活动名
    private Date beginTime;
    private Date endTime;
    private String actDescription; //活动描述
    private String status;
    private String place;
}
