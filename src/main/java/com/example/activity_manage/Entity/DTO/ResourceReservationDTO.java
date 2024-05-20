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
    private String username; // 申请者用户名
    private String resource; // 资源名称
    private int quantity; //资源数量
}
