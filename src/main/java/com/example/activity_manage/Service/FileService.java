package com.example.activity_manage.Service;

import com.example.activity_manage.Entity.DTO.BasePageQueryDTO;
import com.example.activity_manage.Result.PageResult;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileService {
    String uploadFile(MultipartFile file, long aid) throws IOException;
    boolean downloadFile();
    boolean deleteFile();
    PageResult pageQueryFileByAid(BasePageQueryDTO basePageQueryDTO);
}
