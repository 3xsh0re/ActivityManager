package com.example.activity_manage.Service;

import com.example.activity_manage.Entity.DTO.ResourceAdditionDTO;
import com.example.activity_manage.Entity.DTO.ResourceReservationDTO;

public interface AdminService {
    Boolean resourceReservation(ResourceReservationDTO resourceReservationDTO); // 资源申请接口申明
    Boolean resourceAddition(ResourceAdditionDTO resourceAdditionDTO); // 资源添加接口申明
}
