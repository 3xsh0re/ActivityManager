package com.example.activity_manage.Entity.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NoticeToManagerPageQueryDTO extends BasePageQueryDTO{ // 针对某个活动的通知查看
    private long aid;
    private long uid;
}
