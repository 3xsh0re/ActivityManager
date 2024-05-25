package com.example.activity_manage.Entity.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FilePageQueryDTO extends BasePageQueryDTO{
    private long uid; // 用于检查该用户是否参加此活动
    private long aid;
}
