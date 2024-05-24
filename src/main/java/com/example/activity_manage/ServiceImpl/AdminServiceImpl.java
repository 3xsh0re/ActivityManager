package com.example.activity_manage.ServiceImpl;

import com.example.activity_manage.Constant.MessageConstant;
import com.example.activity_manage.Entity.DTO.BasePageQueryDTO;
import com.example.activity_manage.Entity.VO.ActInfoToAdminVO;
import com.example.activity_manage.Entity.VO.BaseActInfoVO;
import com.example.activity_manage.Entity.VO.GetUserVO;
import com.example.activity_manage.Exception.AdminException;
import com.example.activity_manage.Exception.SystemException;
import com.example.activity_manage.Mapper.ActivityMapper;
import com.example.activity_manage.Mapper.UserMapper;
import com.example.activity_manage.Result.PageResult;
import com.example.activity_manage.Service.AdminService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
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
    public PageResult getAllUser(BasePageQueryDTO basePageQueryDTO) {
        try {
            //开始分页查询
            PageHelper.startPage(basePageQueryDTO.getPage(), basePageQueryDTO.getPageSize());
            Page<GetUserVO> page = userMapper.pageQueryAllUser();
            long total = page.getTotal();
            List<GetUserVO> records = page.getResult();
            return new PageResult(total, records);
        }
        catch (Exception e)
        {
            throw new SystemException(MessageConstant.SYSTEM_BUSY);
        }
    }
    public PageResult pageQueryAllPhone(BasePageQueryDTO basePageQueryDTO) {
        try {
            //开始分页查询
            PageHelper.startPage(basePageQueryDTO.getPage(), basePageQueryDTO.getPageSize());
            Page<String> page = userMapper.pageQueryAllPhone();
            long total = page.getTotal();
            List<String> records = page.getResult();
            return new PageResult(total, records);
        }
        catch (Exception e)
        {
            throw new SystemException(MessageConstant.SYSTEM_BUSY);
        }
    }

    @Override
    public PageResult pageQueryActInfoToAdmin(BasePageQueryDTO basePageQueryDTO) {
        try {
            //开始分页查询
            PageHelper.startPage(basePageQueryDTO.getPage(), basePageQueryDTO.getPageSize());
            Page<ActInfoToAdminVO> page = activityMapper.pageQueryActInfoToAdmin(basePageQueryDTO);
            long total = page.size();
            List<ActInfoToAdminVO> records = page.getResult();
            for (ActInfoToAdminVO act : records){
                act.setUsername(userMapper.getUsernameById(act.getUid()));
            }
            return new PageResult(total, records);
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
