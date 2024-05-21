package com.example.activity_manage.ServiceImpl;

import com.example.activity_manage.Constant.MessageConstant;
import com.example.activity_manage.Entity.DTO.ResourceAdditionDTO;
import com.example.activity_manage.Entity.DTO.ResourceReservationDTO;
import com.example.activity_manage.Entity.VO.GetUserVO;
import com.example.activity_manage.Exception.SystemBusyException;
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
    @Override
    public List<GetUserVO> getAllUser() {
        try {
            return userMapper.selectAllUser();
        }
        catch (Exception e)
        {
            throw new SystemBusyException(MessageConstant.EMPTY_PHONE_NUMBER);
        }
    }
    public List<String> getAllPhone() {
        try {
            return userMapper.selectAllPhone();
        }
        catch (Exception e)
        {
            throw new SystemBusyException(MessageConstant.EMPTY_PHONE_NUMBER);
        }
    }
}
