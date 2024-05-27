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
}
