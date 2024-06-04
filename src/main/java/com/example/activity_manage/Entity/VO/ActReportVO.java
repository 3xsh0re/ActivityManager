package com.example.activity_manage.Entity.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActReportVO {
    private long aid;// 归属的活动
    private String actName; // 活动名
    private String orgName; // 组织者用户名
    private double rank;//活动最终评分
    private int participantNum; // 参与人数
    private int messageNum; // 聊天室消息数
    private List<String> actStatus; // 本活动所经历的流程
    private List<String> highLikesCommentList; // 高赞评论
}
