package com.example.activity_manage.Controller.File;

import com.example.activity_manage.Entity.DTO.FileDownloadDTO;
import com.example.activity_manage.Entity.DTO.FilePageQueryDTO;
import com.example.activity_manage.Result.PageResult;
import com.example.activity_manage.Result.Result;
import com.example.activity_manage.Service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
@RestController
@RequestMapping("/file")
public class FileController {
    @Autowired
    FileService fileService;
    @PostMapping( "/download")
    public ResponseEntity<Object> download(@RequestBody FileDownloadDTO fileDownloadDTO){
        return fileService.downloadFile(fileDownloadDTO);
    }
    @PostMapping ("/upload")
    public Result<String> upload(@RequestParam("file") MultipartFile file,@RequestParam("aid") long aid,@RequestParam("uid") long uid) throws IOException {
        return Result.success(fileService.uploadFile(file,aid,uid));
    }

    @PostMapping("/getAllFile")
    public Result<PageResult> getAllFile(@RequestBody FilePageQueryDTO filePageQueryDTO) {
        return Result.success(fileService.pageQueryFile(filePageQueryDTO));
    }

    @GetMapping("/deleteFile")
    public Result<String> deleteFile(@RequestParam("fid") Long fid, @RequestParam("aid") Long aid, @RequestParam("uid") long uid){
        return Result.success(fileService.deleteFile(fid,aid,uid));
    }
}
