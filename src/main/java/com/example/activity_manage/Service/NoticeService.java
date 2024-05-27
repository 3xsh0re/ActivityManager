package com.example.activity_manage.Service;

import com.example.activity_manage.Entity.Notice;

public interface NoticeService {
    void createNotice(Notice n);
    void getActivityNotice(long aid);
    void getNoticeToUser(long uid);
}
