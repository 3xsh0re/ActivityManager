package com.example.activity_manage.Service;

import com.example.activity_manage.Entity.DTO.NoticePageQueryDTO;
import com.example.activity_manage.Entity.DTO.NoticeToManagerPageQueryDTO;
import com.example.activity_manage.Entity.Notice;
import com.example.activity_manage.Result.PageResult;

public interface NoticeService {
    void createNotice(Notice n);
    Boolean updateIfRead(long nid);
    PageResult getNoticeToUser(NoticePageQueryDTO pageQueryDTO);
    PageResult getNoticeToManager(NoticeToManagerPageQueryDTO pageQueryDTO);
}
