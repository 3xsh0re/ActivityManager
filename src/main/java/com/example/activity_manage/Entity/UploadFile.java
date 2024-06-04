package com.example.activity_manage.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UploadFile {
    private long fid;
    private long aid;
    private long uid; // 文件上传者
    private String fileName;
    private String hashName;
    private Date uploadDate;
    private String timing;
    private int downloadTimes;
}
