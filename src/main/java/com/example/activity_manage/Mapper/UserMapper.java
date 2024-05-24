package com.example.activity_manage.Mapper;

import com.example.activity_manage.Entity.User;
import com.example.activity_manage.Entity.VO.GetUserVO;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {
    // 添加用户
    void insertUser(User user);
    // 查找用户
    User selectUserByPhone(String phoneNumber);
    // 返回所有用户
    Page<GetUserVO> pageQueryAllUser();
    Page<String> pageQueryAllPhone();
    void setPwd(String passwd,String phoneNumber);
    Long selectIdByPhone(String phoneNumber);
    String getUsernameById(long uid);
}
