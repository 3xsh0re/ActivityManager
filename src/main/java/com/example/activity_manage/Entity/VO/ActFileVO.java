package com.example.activity_manage.Entity.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActFileVO {
    private long fid;
    private long aid;
    private String fileName;
    private Date uploadDate;
    private int downloadTimes;
}
