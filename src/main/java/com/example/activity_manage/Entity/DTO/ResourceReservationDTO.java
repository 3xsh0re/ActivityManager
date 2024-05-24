package com.example.activity_manage.Entity.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResourceReservationDTO {
    private long uid; // 申请者UID
    private String resource; // 资源名称
    private int quantity; //资源数量
    private Date beginTime;
    private Date endTime;
}
