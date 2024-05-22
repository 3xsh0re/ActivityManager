package com.example.activity_manage.Controller.File;

import com.example.activity_manage.Constant.MessageConstant;
import com.example.activity_manage.Exception.FileUploadErrorException;
import com.example.activity_manage.Result.Result;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Date;

@RestController
@RequestMapping("/file")
public class FileController {
    @PostMapping (value = "/upload")
    public Result<String> upload(@RequestParam("file") MultipartFile file,@RequestParam("aid") long aid){
        //判断上传文件是否为空，若为空则返回错误信息
        if(file.isEmpty()){
            throw new FileUploadErrorException(MessageConstant.FILE_UPLOAD_ERROR);
        }
        else
        {
            // 创建文件夹
            ApplicationHome applicationHome = new ApplicationHome(FileController.class);
            File parentDirectory = applicationHome.getSource().getParentFile(); // 获取当前jar包的运行路径
            String newDirectoryName = DigestUtils.md5DigestAsHex(Long.toString(aid).getBytes());//生成MD5文件包名,防止文件夹冲突
            try {
                File newDirectory = new File(parentDirectory, newDirectoryName);
                String folderPath = newDirectory.getAbsolutePath();

                //获取文件原名
                String originalFilename  = file.getOriginalFilename();

                //获取源文件前缀
                String fileNamePrefix =  originalFilename.substring(0,originalFilename.lastIndexOf("."));
                //获取源文件后缀
                String fileNameSuffix =  originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase();
                if (fileNameSuffix.equals(".pdf") || fileNameSuffix.equals(".doc") || fileNameSuffix.equals(".docx") ||
                        fileNameSuffix.equals(".zip") || fileNameSuffix.equals(".exe")) {
                    //将源文件前缀之后加上时间戳避免重名
                    String newFileNamePrefix = fileNamePrefix + new Date().getTime();
                    //得到上传后新文件的文件名
                    String newFileName =  newFileNamePrefix+fileNameSuffix;
                    //创建一个新的File对象用于存放上传的文件
                    File  fileNew = new File(folderPath+File.separator+newFileName);
                    file.transferTo(fileNew);
                }
                else {
                    throw new FileUploadErrorException(MessageConstant.ERROR_FILE_SUFFIX);
                }
            } catch (Exception e) {
                throw new FileUploadErrorException(MessageConstant.FILE_UPLOAD_ERROR);
            }
        }
        return Result.success("上传成功");
    }
}
