package com.example.activity_manage.ServiceImpl;

import com.example.activity_manage.Constant.MessageConstant;
import com.example.activity_manage.Controller.File.FileController;
import com.example.activity_manage.Entity.DTO.FileDownloadDTO;
import com.example.activity_manage.Entity.DTO.FilePageQueryDTO;
import com.example.activity_manage.Entity.UploadFile;
import com.example.activity_manage.Entity.VO.ActFileVO;
import com.example.activity_manage.Exception.FileUploadException;
import com.example.activity_manage.Mapper.ActivityMapper;
import com.example.activity_manage.Mapper.FileMapper;
import com.example.activity_manage.Result.PageResult;
import com.example.activity_manage.Service.FileService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;

@Service
public class FileServiceImpl implements FileService {
    @Autowired
    FileMapper fileMapper;
    @Autowired
    ActivityMapper activityMapper;

    @Override
    public String uploadFile(MultipartFile file, long aid ,long uid){
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
            try {
                File newDirectory = new File(parentDirectory, newDirectoryName);
                boolean dirFlag = newDirectory.mkdir();

                String folderPath = newDirectory.getAbsolutePath();

                //获取文件原名
                String originalFilename  = file.getOriginalFilename();
                if (originalFilename == null)
                {
                    throw new FileUploadException(MessageConstant.FILE_UPLOAD_ERROR);
                }

                // 获取一个当前时间的时间戳
                String timestamp = Long.toString(System.currentTimeMillis());
                Date date = new Date();
                // 将文件前缀hash防止冲突
                String newFilePrefix = DigestUtils.md5DigestAsHex(timestamp.getBytes());
                //获取源文件后缀
                String fileNameSuffix =  originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase();
                // 白名单判断一下后缀是否合法
                if (fileNameSuffix.equals(".pdf") || fileNameSuffix.equals(".doc") || fileNameSuffix.equals(".docx") ||
                        fileNameSuffix.equals(".zip") || fileNameSuffix.equals(".xlsx")||
                        fileNameSuffix.equals(".jpg") || fileNameSuffix.equals(".png") ||
                        fileNameSuffix.equals(".txt") || fileNameSuffix.equals(".rar"))
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
                    uploadFile.setUploadDate(date);
                    uploadFile.setUid(uid);
                    fileMapper.uploadNewFile(uploadFile);
                }
                else {
                    throw new FileUploadException(MessageConstant.ERROR_FILE_SUFFIX);
                }
            } catch (Exception e) {
                throw new FileUploadException(MessageConstant.FILE_UPLOAD_ERROR);
            }
        }
        return "文件上传成功!";
    }

    @Override
    public ResponseEntity<FileSystemResource> downloadFile(FileDownloadDTO fileDownloadDTO) {
        long fid = fileDownloadDTO.getFid();
        long aid = fileDownloadDTO.getAid();
        String fileName = fileDownloadDTO.getFileName();
        if (fileMapper.getFileByFid(fid) == null)
        {
            return ResponseEntity.notFound().build();
        }
        // 获取文件路径
        ApplicationHome applicationHome = new ApplicationHome(FileController.class);
        File parentDirectory = applicationHome.getSource().getParentFile(); // 获取当前jar包的运行路径
        String newDirectoryName = DigestUtils.md5DigestAsHex(Long.toString(aid).getBytes());//生成MD5文件包名
        String parentPath = parentDirectory + File.separator + newDirectoryName;


        // 文件前缀hash
        String FilePrefix = DigestUtils.md5DigestAsHex(fileMapper.getTimingByFid(fid).getBytes());
        // 获取文件后缀
        String fileSuffix = fileName.substring(fileName.lastIndexOf(".")).toLowerCase();

        // 构建一个文件通过Paths工具类获取一个Path对象
        Path path = Paths.get(parentPath, FilePrefix+fileSuffix);

        // 检查文件是否存在
        if (!Files.exists(path) || !fileMapper.getFileByFid(fid).getFileName().equals(fileName)) {
            return ResponseEntity.notFound().build();
        }

        // 设置响应头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", fileName);

        // 创建文件系统资源
        FileSystemResource resource = new FileSystemResource(path.toFile());

        return new ResponseEntity<>(resource, headers, HttpStatus.OK);
    }

    @Override
    public String deleteFile(long fid,long aid,long uid) {
        UploadFile file = fileMapper.getFileByFid(fid);
        // 判断文件存在
        if (file == null)
        {
            throw new FileUploadException(MessageConstant.FILE_NOT_FOUND);
        }
        // 判断删除者是否为组织者或者为文件上传者本人
        if ( uid != file.getUid() && uid != activityMapper.getActInfoToAll(file.getAid()).getOrgId()){
            throw new FileUploadException(MessageConstant.NOT_HAVE_THIS_PERMISSION);
        }
        // 获取文件路径
        ApplicationHome applicationHome = new ApplicationHome(FileController.class);
        File parentDirectory = applicationHome.getSource().getParentFile(); // 获取当前jar包的运行路径
        String newDirectoryName = DigestUtils.md5DigestAsHex(Long.toString(aid).getBytes());//生成MD5文件包名
        String parentPath = parentDirectory + File.separator + newDirectoryName;
        // 获取文件名
        String suffix = file.getFileName().substring(file.getFileName().lastIndexOf('.'));
        String fileName = file.getHashName() + suffix;
        // 完整文件路径
        String completeFilePath = parentPath + File.separator + fileName;
        File deleteFile = new File(completeFilePath);

        // 检查文件是否存在
        if (deleteFile.exists()) {
            // 删除文件
            if (deleteFile.delete()) {
                //从数据库删除数据
                fileMapper.deleteFile(fid);
                return "文件删除成功";
            } else {
                throw new FileUploadException(MessageConstant.ERROR_FILE_DELETE);
            }
        } else {
            throw new FileUploadException(MessageConstant.FILE_NOT_FOUND);
        }
    }

    @Override
    public PageResult pageQueryFile(FilePageQueryDTO pageQueryDTO) {
        // 判断该用户是否参与这个活动
        long uid = pageQueryDTO.getUid();
        long aid = pageQueryDTO.getAid();
        JSONObject jsonObject = activityMapper.getUserList(aid);
        if (jsonObject != null)
        {
            JSONObject userList = (JSONObject) jsonObject.get("userList");
            if (!userList.containsKey(Long.toString(uid))){
                // 此活动没有此用户参与
                throw new FileUploadException(MessageConstant.NOT_HAVE_THIS_PERMISSION);
            }
            // 开始分页查询
            PageHelper.startPage(pageQueryDTO.getPage(), pageQueryDTO.getPageSize());
            Page<ActFileVO> page = fileMapper.pageQueryFileByAid(aid);
            long total = page.getTotal();
            List<ActFileVO> records = page.getResult();
            return new PageResult(total, records);
        }
        else
        {
            return null;
        }
    }
}
