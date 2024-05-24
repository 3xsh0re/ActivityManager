package com.example.activity_manage.Mapper;

import com.example.activity_manage.Entity.User;
import com.example.activity_manage.Entity.VO.GetUserVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {
    // 添加用户
    public void insertUser(User user);
    // 查找用户
    public User selectUserByPhone(String phoneNumber);
    // 返回所有用户
    public List<GetUserVO> selectAllUser();
    public List<String> selectAllPhone();
    public void setPwd(String passwd,String phoneNumber);
    public Long selectIdByPhone(String phoneNumber);
    public List<Long> selectAIDByUID(Long UID);
    String getUsernameById(long uid);
}
