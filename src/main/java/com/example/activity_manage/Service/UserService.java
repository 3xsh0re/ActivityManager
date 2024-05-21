package com.example.activity_manage.Service;

import com.example.activity_manage.Entity.DTO.ResetPwdDTO;
import com.example.activity_manage.Entity.DTO.ResourceReservationDTO;
import com.example.activity_manage.Entity.DTO.UserLoginDTO;
import com.example.activity_manage.Entity.User;
import com.example.activity_manage.Entity.VO.GetUserVO;

import java.util.List;

public interface UserService {
    User Login(UserLoginDTO userLoginDTO);
    Boolean Register(UserLoginDTO userLoginDTO);
    Boolean ResetPwd(ResetPwdDTO resetPwdDTO);
    void checkPhoneNumberExist(String phone);
}
