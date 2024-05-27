package com.example.activity_manage.Entity.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResourceReservationDTO {
    private long uid; // 申请者UID
    private long aid; // 申请活动的AID
    private String resource; // 资源名称
    private int quantity; //资源数量
}
