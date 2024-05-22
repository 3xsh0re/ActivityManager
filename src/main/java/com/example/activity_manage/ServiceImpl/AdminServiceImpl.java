package com.example.activity_manage.ServiceImpl;

import com.example.activity_manage.Constant.MessageConstant;
import com.example.activity_manage.Entity.Activity;
import com.example.activity_manage.Entity.DTO.ResourceAdditionDTO;
import com.example.activity_manage.Entity.DTO.ResourceReservationDTO;
import com.example.activity_manage.Entity.VO.ActInfoToAdminVO;
import com.example.activity_manage.Entity.VO.GetUserVO;
import com.example.activity_manage.Exception.SystemBusyException;
import com.example.activity_manage.Mapper.ActivityMapper;
import com.example.activity_manage.Mapper.ResourceMapper;
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
            throw new SystemBusyException(MessageConstant.SYSTEM_BUSY);
        }
    }
    public List<String> getAllPhone() {
        try {
            return userMapper.selectAllPhone();
        }
        catch (Exception e)
        {
            throw new SystemBusyException(MessageConstant.SYSTEM_BUSY);
        }
    }

    @Override
    public List<ActInfoToAdminVO> getAllActivity() {
        try {
            return activityMapper.getAllActInfoToAdmin();
        }
        catch (Exception e)
        {
            throw new SystemBusyException(MessageConstant.SYSTEM_BUSY);
        }
    }

}
