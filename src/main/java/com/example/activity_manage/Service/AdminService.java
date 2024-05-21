package com.example.activity_manage.Service;

import com.example.activity_manage.Entity.DTO.ResourceAdditionDTO;
import com.example.activity_manage.Entity.DTO.ResourceReservationDTO;
import com.example.activity_manage.Entity.VO.GetUserVO;

import java.util.List;

public interface AdminService {
    List<GetUserVO> getAllUser();
    List<String> getAllPhone();
}
