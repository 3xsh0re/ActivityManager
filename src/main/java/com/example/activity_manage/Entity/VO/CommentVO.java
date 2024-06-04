package com.example.activity_manage.Entity.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentVO {
    private long id;
    private String username;
    private String content;
    private int likes;
    private Date commentTime;
}
