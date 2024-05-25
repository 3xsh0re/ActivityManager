package com.example.activity_manage.Mapper;

import com.example.activity_manage.Entity.Resource;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ResourceMapper {
    boolean checkResourceByName(String ResourceName);
    int selectResourceByName(String ResourceName);
    void updateResourceQuantityByName(String ResourceName, int quantity);
    void insertResource(String ResourceName, int quantity, int type);
//    List<Resource> getAllResource();
    Page<Resource> pageQueryAllResource();
}
