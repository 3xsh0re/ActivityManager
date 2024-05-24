package com.example.activity_manage.ServiceImpl;

import com.example.activity_manage.Constant.MessageConstant;
import com.example.activity_manage.Entity.VO.ActInfoToAdminVO;
import com.example.activity_manage.Entity.VO.GetUserVO;
import com.example.activity_manage.Exception.AdminException;
import com.example.activity_manage.Exception.SystemException;
import com.example.activity_manage.Mapper.ActivityMapper;
import com.example.activity_manage.Mapper.UserMapper;
import com.example.activity_manage.Service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    UserMapper userMapper;
    @Autowired
    ActivityMapper activityMapper;
    @Override
    public List<GetUserVO> getAllUser() {
        try {
            return userMapper.selectAllUser();
        }
        catch (Exception e)
        {
            throw new SystemException(MessageConstant.SYSTEM_BUSY);
        }
    }
    public List<String> getAllPhone() {
        try {
            return userMapper.selectAllPhone();
        }
        catch (Exception e)
        {
            throw new SystemException(MessageConstant.SYSTEM_BUSY);
        }
    }

    @Override
    public List<ActInfoToAdminVO> getAllActivity() {
        try {
            return activityMapper.getAllActInfoToAdmin();
        }
        catch (Exception e)
        {
            throw new SystemException(MessageConstant.SYSTEM_BUSY);
        }
    }

    @Override
    public Boolean checkActContent(long aid, int status, String result) {
        if (activityMapper.getActInfoToOrganizer(aid) == null)
        {
            throw new AdminException(MessageConstant.ACTIVITY_NOT_EXIST);
        }
        if (activityMapper.getStatusById(aid) != 0){
            throw new AdminException(MessageConstant.ACTIVITY_ALREADY_CHECKED);
        }
        if (status != 1 && status != -1)
        {
            throw new AdminException(MessageConstant.NOT_ILLEGAL_CHECK_STATUS);
        }
        return activityMapper.checkActContent(aid,status,result);
    }

}
