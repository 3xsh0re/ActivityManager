package com.example.activity_manage.ServiceImpl;

import com.example.activity_manage.Constant.MessageConstant;
import com.example.activity_manage.Entity.DTO.ResourceReservationDTO;
import com.example.activity_manage.Entity.DTO.UserLoginDTO;
import com.example.activity_manage.Entity.User;
import com.example.activity_manage.Entity.VO.GetUserVO;
import com.example.activity_manage.Exception.AccountNotFoundException;
import com.example.activity_manage.Exception.DuplicatePhoneException;
import com.example.activity_manage.Exception.PasswdErrorException;
import com.example.activity_manage.Mapper.ResourceMapper;
import com.example.activity_manage.Mapper.UserMapper;
import com.example.activity_manage.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.List;


@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserMapper userMapper;
    @Override
    public User Login(UserLoginDTO userLoginDTO) {
        String phoneNumber = userLoginDTO.getPhone_number();
        String password = userLoginDTO.getPasswd();

        //1、根据用户名查询数据库中的数据
        User user = userMapper.selectUserByPhone(phoneNumber);

        //2、处理各种异常情况（用户名不存在、密码不对、账号被锁定）
        if (user == null) {
            //账号不存在
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        //密码比对
        //对前端传过来的明文密码进行md5加密处理
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        if (!password.equals(user.getPasswd())) {
            //密码错误
            throw new PasswdErrorException(MessageConstant.PASSWORD_ERROR);
        }

        //3、返回实体对象
        return user;
    }

    @Override
    public Boolean Register(UserLoginDTO userLoginDTO) {
        String username = userLoginDTO.getUsername();
        String phoneNumber = userLoginDTO.getPhone_number();
        String pwd = userLoginDTO.getPasswd();
        User user = userMapper.selectUserByPhone(phoneNumber);
        if (user != null)
        {
            throw new DuplicatePhoneException(MessageConstant.PHONE_DUPLICATE);
        }
        else {
            User new_user = new User();
            new_user.setUsername(username);
            new_user.setRole(0);
            new_user.setPhoneNumber(phoneNumber);
            new_user.setPasswd(DigestUtils.md5DigestAsHex(pwd.getBytes()));
            userMapper.insertUser(new_user);
        }
        return true;
    }

    @Override
    public List<GetUserVO> getAllUser() {
        return userMapper.selectAllUser();
    }
    public List<String> getAllPhone() {
        return userMapper.selectAllPhone();
    }

}
