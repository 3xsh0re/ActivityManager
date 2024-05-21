package com.example.activity_manage.Service;

import com.example.activity_manage.Entity.DTO.ResourceAdditionDTO;
import com.example.activity_manage.Entity.DTO.ResourceReservationDTO;
import com.example.activity_manage.Entity.Resource;

import java.util.List;

public interface ResourceService {
    Boolean resourceAddition(ResourceAdditionDTO resourceAdditionDTO); // 资源添加接口申明
    Boolean resourceReservation(ResourceReservationDTO resourceReservationDTO); // 资源申请接口申明
    List<Resource> getAllResource();
}
