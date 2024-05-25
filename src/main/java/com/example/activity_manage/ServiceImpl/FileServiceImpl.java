package com.example.activity_manage.ServiceImpl;

import com.example.activity_manage.Constant.MessageConstant;
import com.example.activity_manage.Controller.File.FileController;
import com.example.activity_manage.Entity.DTO.BasePageQueryDTO;
import com.example.activity_manage.Entity.UploadFile;
import com.example.activity_manage.Exception.FileUploadException;
import com.example.activity_manage.Mapper.FileMapper;
import com.example.activity_manage.Result.PageResult;
import com.example.activity_manage.Service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class FileServiceImpl implements FileService {
    @Autowired
    FileMapper fileMapper;

    @Override
    public String uploadFile(MultipartFile file, long aid) throws IOException {
        //判断上传文件是否为空，若为空则返回错误信息
        if(file.isEmpty()){
            throw new FileUploadException(MessageConstant.FILE_UPLOAD_ERROR);
        }
        else
        {
            // 创建文件夹
            ApplicationHome applicationHome = new ApplicationHome(FileController.class);
            File parentDirectory = applicationHome.getSource().getParentFile(); // 获取当前jar包的运行路径
            String newDirectoryName = DigestUtils.md5DigestAsHex(Long.toString(aid).getBytes());//生成MD5文件包名,防止文件夹冲突
//            try {
                File newDirectory = new File(parentDirectory, newDirectoryName);
                boolean dirflag = newDirectory.mkdir();
                if (!dirflag){
                    throw new FileUploadException(MessageConstant.FILE_UPLOAD_ERROR);
                }
                String folderPath = newDirectory.getAbsolutePath();

                //获取文件原名
                String originalFilename  = file.getOriginalFilename();
                if (originalFilename == null)
                {
                    throw new FileUploadException(MessageConstant.FILE_UPLOAD_ERROR);
                }

                // 获取一个当前时间的时间戳
                String timestamp = Long.toString(System.currentTimeMillis());
                // 将文件前缀hash防止冲突
                String newFilePrefix = DigestUtils.md5DigestAsHex(timestamp.getBytes());
                //获取源文件后缀
                String fileNameSuffix =  originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase();
                // 白名单判断一下后缀是否合法
                if (fileNameSuffix.equals(".pdf") || fileNameSuffix.equals(".doc") || fileNameSuffix.equals(".docx") ||
                        fileNameSuffix.equals(".zip") || fileNameSuffix.equals(".xlsx")||
                        fileNameSuffix.equals(".jpg") || fileNameSuffix.equals(".png"))
                {
                    //创建一个新的File对象用于存放上传的文件
                    File fileNew = new File(folderPath+File.separator+newFilePrefix+fileNameSuffix);
                    file.transferTo(fileNew);

                    // 文件信息同步到数据库
                    UploadFile uploadFile = new UploadFile();
                    uploadFile.setFileName(originalFilename);
                    uploadFile.setAid(aid);
                    uploadFile.setTiming(timestamp);
                    uploadFile.setHashName(newFilePrefix);
                    fileMapper.uploadNewFile(uploadFile);
                }
                else {
                    throw new FileUploadException(MessageConstant.ERROR_FILE_SUFFIX);
                }
//            } catch (Exception e) {
//                System.out.println(e.toString());
//                throw new FileUploadException(MessageConstant.FILE_UPLOAD_ERROR);
//            }
        }
        return "文件上传成功!";
    }

    @Override
    public boolean downloadFile() {
        return false;
    }

    @Override
    public boolean deleteFile() {
        return false;
    }

    @Override
    public PageResult pageQueryFileByAid(BasePageQueryDTO basePageQueryDTO) {
        return null;
    }
}
