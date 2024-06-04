package com.example.activity_manage.Service;

import com.example.activity_manage.Entity.DTO.BasePageQueryDTO;
import com.example.activity_manage.Result.PageResult;

public interface AdminService {
    PageResult getAllUser(BasePageQueryDTO basePageQueryDTO);
    PageResult pageQueryAllPhone(BasePageQueryDTO basePageQueryDTO);
    PageResult pageQueryActInfoToAdmin(BasePageQueryDTO basePageQueryDTO);
    Boolean checkActContent(long aid,int status,String result);//活动审核
    Boolean deleteUser(long uid);
}
