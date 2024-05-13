package com.example.activity_manage.Entity.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResourceAdditionDTO {
    private String resource; //资源名称
    private int quantity; // 资源数量
    private int type; // 资源类型
}
