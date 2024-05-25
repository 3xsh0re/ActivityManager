package com.example.activity_manage.Controller.File;

import com.example.activity_manage.Constant.MessageConstant;
import com.example.activity_manage.Exception.FileUploadException;
import com.example.activity_manage.Result.Result;
import com.example.activity_manage.Service.FileService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
@RestController
@RequestMapping("/file")
public class FileController {
    @Autowired
    FileService fileService;
    @PostMapping (value = "/upload")
    public Result<String> upload(@RequestParam("file") MultipartFile file,@RequestParam("aid") long aid) throws IOException {
        return Result.success(fileService.uploadFile(file,aid));
    }

    @GetMapping(value = "/download")
    public Result<String> download(HttpServletResponse response,@RequestParam("aid") Long aid,@RequestParam("filename") String fileName) throws IOException {
        //通过response输出流将文件传递到浏览器
        //1、获取文件路径
        ApplicationHome applicationHome = new ApplicationHome(FileController.class);
        File parentDirectory = applicationHome.getSource().getParentFile(); // 获取当前jar包的运行路径
        String newDirectoryName = DigestUtils.md5DigestAsHex(Long.toString(aid).getBytes());//生成MD5文件包名,防止文件夹冲突
        String parentPath = parentDirectory + File.separator + newDirectoryName;
        //2.构建一个文件通过Paths工具类获取一个Path对象
        Path path = Paths.get(parentPath, fileName);
        //判断文件是否存在
        if (Files.exists(path)) {
            //存在则下载
            //通过response设定他的响应类型
            //4.获取文件的后缀名
            String fileSuffix = fileName.substring(fileName.lastIndexOf(".") + 1);
            //5.设置contentType ,只有指定contentType才能下载
            response.setContentType("application/" + fileSuffix);
            //6.添加http头信息,因为fileName的编码格式是UTF-8 但是http头信息只识别 ISO8859-1 的编码格式,因此要对 fileName 重新编码
            response.addHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes(StandardCharsets.UTF_8), "ISO8859-1"));
            //7.使用 Path 和 response 输出流将文件输出到浏览器
            Files.copy(path, response.getOutputStream());
        }
        else {
            throw new FileUploadException(MessageConstant.FILE_NOT_FOUND);
        }
        return Result.success("文件下载成功!");
    }
}
