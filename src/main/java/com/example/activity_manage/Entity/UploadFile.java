package com.example.activity_manage.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UploadFile {
    private long fid;
    private long aid;
    private String fileName;
    private String hashName;
    private String timing;
    private int downloadTimes;
}
