package com.example.activity_manage.ServiceImpl;

import com.example.activity_manage.Constant.MessageConstant;
import com.example.activity_manage.Entity.DTO.BasePageQueryDTO;
import com.example.activity_manage.Entity.DTO.ResourceAdditionDTO;
import com.example.activity_manage.Entity.DTO.ResourceReservationDTO;
import com.example.activity_manage.Entity.Resource;
import com.example.activity_manage.Exception.SystemException;
import com.example.activity_manage.Mapper.ResourceMapper;
import com.example.activity_manage.Result.PageResult;
import com.example.activity_manage.Service.ResourceService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResourceServiceImpl implements ResourceService {
    @Autowired
    ResourceMapper resourceMapper;

    public Boolean resourceReservation(ResourceReservationDTO resourceReservationDTO)
    {
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

    public PageResult pageQueryAllResource(BasePageQueryDTO basePageQueryDTO){
        try {
            //开始分页查询
            PageHelper.startPage(basePageQueryDTO.getPage(), basePageQueryDTO.getPageSize());
            Page<Resource> page = resourceMapper.pageQueryAllResource();
            long total = page.getTotal();
            List<Resource> records = page.getResult();
            return new PageResult(total, records);
        }
        catch (Exception e){
            throw new SystemException(MessageConstant.SYSTEM_BUSY);
        }
    }
}
