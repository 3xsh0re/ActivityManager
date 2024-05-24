package com.example.activity_manage.Service;

import com.example.activity_manage.Entity.DTO.ResourceAdditionDTO;
import com.example.activity_manage.Entity.DTO.ResourceReservationDTO;
import com.example.activity_manage.Entity.Resource;

import java.util.List;

public interface ResourceService {
    Boolean resourceAddition(ResourceAdditionDTO resourceAdditionDTO); // 资源添加接口
    Boolean resourceReservation(ResourceReservationDTO resourceReservationDTO); // 资源申请接口
    List<Resource> getAllResource(); // 获取全部资源
}
