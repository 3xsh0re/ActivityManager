package com.example.activity_manage.ServiceImpl;

import com.example.activity_manage.Constant.MessageConstant;
import com.example.activity_manage.Entity.DTO.ResetPwdDTO;
import com.example.activity_manage.Entity.DTO.ResourceReservationDTO;
import com.example.activity_manage.Entity.DTO.UserLoginDTO;
import com.example.activity_manage.Entity.User;
import com.example.activity_manage.Entity.VO.GetUserVO;
import com.example.activity_manage.Exception.*;
import com.example.activity_manage.Mapper.ResourceMapper;
import com.example.activity_manage.Mapper.UserMapper;
import com.example.activity_manage.Service.UserService;
import com.example.activity_manage.Utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.List;


@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserMapper userMapper;
    @Autowired
    ResourceMapper resourceMapper;
    @Autowired
    RedisUtil redisUtil;
    @Override
    public User Login(UserLoginDTO userLoginDTO) {
        String phoneNumber = userLoginDTO.getPhoneNumber();
        String password = userLoginDTO.getPasswd();
        String email = userLoginDTO.getEmail();
        String L_captcha = userLoginDTO.getCaptcha();
        // 比对验证码
        String true_captcha_phone = (String) redisUtil.get("UMS_" + phoneNumber);
        String true_captcha_email = (String) redisUtil.get("UMS_" + email);
        if ( L_captcha.equals("") ) throw new ErrorCaptchaException(MessageConstant.ERROR_CAPTCHA);
        if ( !L_captcha.equals(true_captcha_email) && !L_captcha.equals(true_captcha_phone) ) throw new ErrorCaptchaException(MessageConstant.ERROR_CAPTCHA);

        //1、根据用户名查询数据库中的数据
        User user = userMapper.selectUserByPhone(phoneNumber);
        //2、判断是否为管理员
        if (userLoginDTO.isAdmin())
        {
            if (!phoneNumber.equals(user.getPhoneNumber()))
                throw new NotAdminLoginException(MessageConstant.ACCOUNT_NOT_ADMIN);
        }
        //3、处理各种异常情况（用户名不存在、密码不对、账号被锁定）
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
        String phoneNumber = userLoginDTO.getPhoneNumber();
        String email = userLoginDTO.getEmail();
        String pwd = userLoginDTO.getPasswd();
        String R_captcha = userLoginDTO.getCaptcha();
        // 比对验证码
        String true_captcha_phone = (String) redisUtil.get("UMS_" + phoneNumber);
        String true_captcha_email = (String) redisUtil.get("UMS_" + email);
        if ( R_captcha.equals("") ) throw new ErrorCaptchaException(MessageConstant.ERROR_CAPTCHA);
        if ( !R_captcha.equals(true_captcha_email) && !R_captcha.equals(true_captcha_phone) ) throw new ErrorCaptchaException(MessageConstant.ERROR_CAPTCHA);

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
            new_user.setEmail(email);
            userMapper.insertUser(new_user);
        }
        return true;
    }

    @Override
    public Boolean ResetPwd(ResetPwdDTO resetPwdDTO) {
        String phoneNumber = resetPwdDTO.getPhoneNumber();
        String Reset_Captcha = resetPwdDTO.getCaptcha();
        String passwd = resetPwdDTO.getPasswd();
        // 比对验证码
        String true_captcha_phone = (String) redisUtil.get("UMS_" + phoneNumber);
        if ( Reset_Captcha.equals("") ) throw new ErrorCaptchaException(MessageConstant.ERROR_CAPTCHA);
        if ( !Reset_Captcha.equals(true_captcha_phone) ) throw new ErrorCaptchaException(MessageConstant.ERROR_CAPTCHA);

        User user = userMapper.selectUserByPhone(phoneNumber);
        if (user == null) {
            //账号不存在
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }
        passwd = DigestUtils.md5DigestAsHex(passwd.getBytes());
        userMapper.setPwd(passwd,phoneNumber);
        return Boolean.TRUE;
    }

    @Override
    public void checkPhoneNumberExist(String phoneNumber) {
        User user = userMapper.selectUserByPhone(phoneNumber);
        if ( user == null)
        {
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }
    }

    @Override
    public List<Long> getAIDbyUID(Long UID) {
        return userMapper.selectAIDByUID(UID);
    }
}
