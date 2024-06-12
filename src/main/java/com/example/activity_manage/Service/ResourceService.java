package com.example.activity_manage.Service;

import com.example.activity_manage.Entity.DTO.BasePageQueryDTO;
import com.example.activity_manage.Entity.DTO.ResourceAdditionDTO;
import com.example.activity_manage.Entity.DTO.ResourceReservationDTO;
import com.example.activity_manage.Entity.DTO.ResourceToActDTO;
import com.example.activity_manage.Result.PageResult;

public interface ResourceService {
    Boolean resourceAddition(ResourceAdditionDTO resourceAdditionDTO); // 资源添加接口
    Boolean resourceReservation(ResourceReservationDTO resourceReservationDTO); // 资源申请接口
    PageResult pageQueryAllResource(BasePageQueryDTO basePageQueryDTO); // 获取全部资源
    PageResult pageQueryResourceToAct(ResourceToActDTO resourceToActDTO);
}
