package com.example.activity_manage.Mapper;

import com.example.activity_manage.Entity.Resource;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ResourceMapper {
    public boolean checkResourceByName(String ResourceName);
    public int selectResourceByName(String ResourceName);
    public void updateResourceQuantityByName(String ResourceName, int quantity);
    public void insertResource(String ResourceName, int quantity, int type);
    public List<Resource> getAllResource();
}
