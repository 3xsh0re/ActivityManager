package com.example.activity_manage.ServiceImpl;

import com.example.activity_manage.Entity.DTO.ResourceAdditionDTO;
import com.example.activity_manage.Entity.DTO.ResourceReservationDTO;
import com.example.activity_manage.Mapper.ResourceMapper;
import com.example.activity_manage.Service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ResourceServiceImpl implements ResourceService {
    @Autowired
    ResourceMapper resourceMapper;

    public Boolean resourceReservation(ResourceReservationDTO resourceReservationDTO)
    {
        String userName = resourceReservationDTO.getUsername();
        String resourceName = resourceReservationDTO.getResource();
        int quantityNeed = resourceReservationDTO.getQuantity();
        if(resourceMapper.checkResourceByName(resourceName))
        {
            int quantityCurr = resourceMapper.selectResourceByName(resourceName);
            if(quantityCurr >= quantityNeed)
            {
                int quantityNew = quantityCurr - quantityNeed;
                resourceMapper.updateResourceQuantityByName(resourceName, quantityNew);
                return true;
            }
        }
        return false;
    }
    @Override
    public Boolean resourceAddition(ResourceAdditionDTO resourceAdditionDTO) {
        String resourceName = resourceAdditionDTO.getResource();
        int quantity = resourceAdditionDTO.getQuantity();
        int type = resourceAdditionDTO.getType();
        if(resourceMapper.checkResourceByName(resourceName))
        {
            int quantityCurr = resourceMapper.selectResourceByName(resourceName);
            int quantityNew = quantity + quantityCurr;
            resourceMapper.updateResourceQuantityByName(resourceName, quantityNew);
        }
        else
        {
            resourceMapper.insertResource(resourceName, quantity, type);
        }
        return true;
    }
}
