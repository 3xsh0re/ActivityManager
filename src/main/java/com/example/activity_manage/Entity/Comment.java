package com.example.activity_manage.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
    private long id;
    private long uid;
    private long aid;
    private String content;
    private int likes;
    private Date commentTime;
}
