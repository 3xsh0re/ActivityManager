package com.example.activity_manage.Entity.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NoticeToManagerVO {
    private long nid;
    private int type;
    private String sendUser;
    private String content;
    private int totalSend;
    private int totalRead;
}
