package com.example.activity_manage.Service;

import com.example.activity_manage.Entity.Activity;
import com.example.activity_manage.Entity.DTO.BasePageQueryDTO;
import com.example.activity_manage.Entity.DTO.ResourceAdditionDTO;
import com.example.activity_manage.Entity.DTO.ResourceReservationDTO;
import com.example.activity_manage.Entity.VO.ActInfoToAdminVO;
import com.example.activity_manage.Entity.VO.GetUserVO;
import com.example.activity_manage.Result.PageResult;

import java.util.List;

public interface AdminService {
    PageResult getAllUser(BasePageQueryDTO basePageQueryDTO);
    PageResult pageQueryAllPhone(BasePageQueryDTO basePageQueryDTO);
    PageResult pageQueryActInfoToAdmin(BasePageQueryDTO basePageQueryDTO);
    Boolean checkActContent(long aid,int status,String result);//活动审核
}
