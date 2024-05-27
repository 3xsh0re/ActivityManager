package com.example.activity_manage.Mapper;

import com.example.activity_manage.Entity.Notice;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface NoticeMapper {
    void createNotice(Notice notice);//发送通知
    void getActivityNotice();//组织者或者管理员获取通知
    void getNoticeToUser();//用户获取通知
}
