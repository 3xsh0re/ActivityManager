package com.example.activity_manage.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Resource {
    private long id;
    private String  resourceName;
    private Integer type; // 资源类型（区分物品/地点）
    private Integer quantity;// 资源数
}
