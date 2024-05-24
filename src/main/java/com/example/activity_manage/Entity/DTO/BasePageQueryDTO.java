package com.example.activity_manage.Entity.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BasePageQueryDTO implements Serializable {
    //页码
    private int page;
    //每页显示记录数
    private int pageSize;
}
