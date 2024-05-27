package com.example.activity_manage.Mapper;

import com.example.activity_manage.Entity.DTO.NoticePageQueryDTO;
import com.example.activity_manage.Entity.DTO.NoticeToManagerPageQueryDTO;
import com.example.activity_manage.Entity.Notice;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface NoticeMapper {
    void createNotice(Notice notice);//发送通知
    void updateIfRead(long nid); // 用户已读
    int  getReadCount(long groupId); // 统计同一groupId已读的消息
    int  getSendCount(long groupId); // 统计同一groupId消息
    Page<Notice> getActivityNotice(NoticeToManagerPageQueryDTO pageQueryDTO);//组织者或者管理员获取通知
    Page<Notice> getNoticeToUser(NoticePageQueryDTO pageQueryDTO);//用户获取通知
}
