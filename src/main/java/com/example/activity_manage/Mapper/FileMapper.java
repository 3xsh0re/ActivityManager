package com.example.activity_manage.Mapper;

import com.example.activity_manage.Entity.UploadFile;
import com.example.activity_manage.Entity.VO.ActFileVO;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FileMapper {
    void uploadNewFile(UploadFile file);
    String getTimingByFid(long fid);
    void increaseDownloadTimes(long fid);
    void deleteFile(long fid);
    UploadFile getFileByFid(long fid);
    Page<ActFileVO> pageQueryFileByAid(long aid);
}
