package com.example.activity_manage.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.minidev.json.JSONObject;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comments {
    private long id;
    private long uid;
    private long aid;
    private String content;
    private int likes;
    private Date commentTime;
    private JSONObject likeUserList;
}
