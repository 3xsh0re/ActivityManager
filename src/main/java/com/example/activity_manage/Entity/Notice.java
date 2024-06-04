package com.example.activity_manage.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Notice {
    private long id;
    private long aid;
    int type;
    private long sendUid;
    private long receiveUid;
    private String content;
    private boolean ifRead;
    private String groupId; //记录消息发送的组,设置为当前时间hash(调用JwtUtil.getNowTimeHash()),主要是解决通知多发的情况
}
