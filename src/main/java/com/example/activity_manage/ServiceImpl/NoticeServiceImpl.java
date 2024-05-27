package com.example.activity_manage.ServiceImpl;

import com.example.activity_manage.Entity.DTO.NoticePageQueryDTO;
import com.example.activity_manage.Entity.Notice;
import com.example.activity_manage.Entity.VO.NoticeVO;
import com.example.activity_manage.Mapper.NoticeMapper;
import com.example.activity_manage.Mapper.UserMapper;
import com.example.activity_manage.Result.PageResult;
import com.example.activity_manage.Service.NoticeService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class NoticeServiceImpl implements NoticeService {
    @Autowired
    NoticeMapper noticeMapper;
    @Autowired
    UserMapper userMapper;
    @Override
    public void createNotice(Notice n) {
        noticeMapper.createNotice(n);
    }

    @Override
    public void getActivityNotice(long aid) {

    }

    @Override
    public Boolean updateIfRead(long nid) {
        noticeMapper.updateIfRead(nid);
        return true;
    }

    @Override
    public PageResult getNoticeToUser(NoticePageQueryDTO pageQueryDTO) {
        //开始分页查询
        PageHelper.startPage(pageQueryDTO.getPage(), pageQueryDTO.getPageSize());
        Page<Notice> page = noticeMapper.getNoticeToUser(pageQueryDTO);
        long total = page.getTotal();
        List<NoticeVO> records = new ArrayList<>();
        for (Notice n: page) {
            NoticeVO noticeVO = new NoticeVO();
            noticeVO.setNid(n.getId());
            noticeVO.setType(n.getType());
            noticeVO.setSendUser(userMapper.getUsernameById(n.getSendUid()));
            noticeVO.setIfRead(n.isIfRead());
            noticeVO.setContent(n.getContent());
            records.add(noticeVO);
        }

        return new PageResult(total, records);
    }
}
