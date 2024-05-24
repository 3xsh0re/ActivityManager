package com.example.activity_manage.Service;

import com.example.activity_manage.Entity.Activity;
import com.example.activity_manage.Entity.DTO.ResourceAdditionDTO;
import com.example.activity_manage.Entity.DTO.ResourceReservationDTO;
import com.example.activity_manage.Entity.VO.ActInfoToAdminVO;
import com.example.activity_manage.Entity.VO.GetUserVO;

import java.util.List;

public interface AdminService {
    List<GetUserVO> getAllUser();
    List<String> getAllPhone();
    List<ActInfoToAdminVO> getAllActivity();
    Boolean checkActContent(long aid,int status,String result);//活动审核
}
