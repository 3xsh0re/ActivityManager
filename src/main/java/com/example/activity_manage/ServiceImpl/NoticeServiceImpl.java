package com.example.activity_manage.ServiceImpl;

import com.example.activity_manage.Constant.MessageConstant;
import com.example.activity_manage.Entity.DTO.NoticePageQueryDTO;
import com.example.activity_manage.Entity.DTO.NoticeToManagerPageQueryDTO;
import com.example.activity_manage.Entity.Notice;
import com.example.activity_manage.Entity.VO.NoticeToManagerVO;
import com.example.activity_manage.Entity.VO.NoticeVO;
import com.example.activity_manage.Exception.ActivityException;
import com.example.activity_manage.Mapper.ActivityMapper;
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
    @Autowired
    ActivityMapper activityMapper;
    @Override
    public void createNotice(Notice n) {
        noticeMapper.createNotice(n);
    }

    @Override
    public PageResult getNoticeToManager(NoticeToManagerPageQueryDTO pageQueryDTO) {
        long uid = pageQueryDTO.getUid();
        long aid = pageQueryDTO.getAid();
        String role = activityMapper.getUserRole(aid,uid);
        if (role == null || !role.equals("组织者")&&role.equals("管理员")){
            throw new ActivityException(MessageConstant.NOT_HAVE_THIS_PERMISSION);
        }

        //开始分页查询
        PageHelper.startPage(pageQueryDTO.getPage(), pageQueryDTO.getPageSize());
        Page<Notice> page = noticeMapper.getActivityNotice(pageQueryDTO);
        long total = page.getTotal();
        List<NoticeToManagerVO> records = new ArrayList<>();
        for (Notice n: page) {
            NoticeToManagerVO noticeVO = new NoticeToManagerVO();
            noticeVO.setNid(n.getId());
            noticeVO.setType(n.getType());
            noticeVO.setSendUser(userMapper.getUsernameById(n.getSendUid()));
            // 统计已读量
            int readCount = noticeMapper.getReadCount(n.getGroupId());
            int sendCount = noticeMapper.getSendCount(n.getGroupId());
            noticeVO.setTotalRead(readCount);
            noticeVO.setTotalSend(sendCount);
            noticeVO.setContent(n.getContent());
            records.add(noticeVO);
        }
        return new PageResult(total, records);
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
