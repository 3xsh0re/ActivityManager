package com.example.activity_manage.Service;

import com.example.activity_manage.Entity.DTO.ResourceAdditionDTO;
import com.example.activity_manage.Entity.DTO.ResourceReservationDTO;

public interface AdminService {
    Boolean resourceAddition(ResourceAdditionDTO resourceAdditionDTO); // 资源添加接口申明
}
