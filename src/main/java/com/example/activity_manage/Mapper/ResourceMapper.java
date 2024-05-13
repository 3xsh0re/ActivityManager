package com.example.activity_manage.Mapper;

public interface ResourceMapper {
    public boolean checkResourceByName(String ResourceName);
    public int selectResourceByName(String ResourceName);
    public void updateResourceQuantityByName(String ResourceName, int quantity);
    public void insertResource(String ResourceName, int quantity, int type);
}
