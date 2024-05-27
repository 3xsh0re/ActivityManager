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
    private long groupId; //记录消息发送的组,如果同时发送的消息为同一组,设为第一个Notice的id,主要是解决通知多发的情况
}
