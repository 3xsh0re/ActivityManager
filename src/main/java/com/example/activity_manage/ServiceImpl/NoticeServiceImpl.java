package com.example.activity_manage.ServiceImpl;

import com.example.activity_manage.Entity.Notice;
import com.example.activity_manage.Mapper.NoticeMapper;
import com.example.activity_manage.Service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NoticeServiceImpl implements NoticeService {
    @Autowired
    NoticeMapper noticeMapper;
    @Override
    public void createNotice(Notice n) {
        noticeMapper.createNotice(n);
    }

    @Override
    public void getActivityNotice(long aid) {

    }

    @Override
    public void getNoticeToUser(long uid) {

    }
}
