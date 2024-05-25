package com.example.activity_manage.Service;

import com.example.activity_manage.Entity.DTO.BasePageQueryDTO;
import com.example.activity_manage.Entity.DTO.FilePageQueryDTO;
import com.example.activity_manage.Result.PageResult;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileService {
    String uploadFile(MultipartFile file, long aid , long uid) throws IOException;
    String deleteFile(long fid,long aid,long uid);
    PageResult pageQueryFile(FilePageQueryDTO dto);
    ResponseEntity<FileSystemResource> downloadFile(Long fid,Long aid,String fileName);
}
