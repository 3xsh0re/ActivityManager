package com.example.activity_manage.Mapper;

import com.example.activity_manage.Entity.UploadFile;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FileMapper {
    void uploadNewFile(UploadFile file);
}
