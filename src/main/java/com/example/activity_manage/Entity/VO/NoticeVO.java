package com.example.activity_manage.Entity.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NoticeVO {
    private long nid;
    private long type;
    private String sendUser;
    private String content;
    private Boolean ifRead;
}
