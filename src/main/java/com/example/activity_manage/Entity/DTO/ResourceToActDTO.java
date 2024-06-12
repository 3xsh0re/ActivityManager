package com.example.activity_manage.Entity.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResourceToActDTO {
    private Date beginTime;
    private Date endTime;
    //页码
    private int page;
    //每页显示记录数
    private int pageSize;
}
