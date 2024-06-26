package com.example.activity_manage.Entity.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
//接收客户端参数
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginDTO implements Serializable {
    private String  username; //用户名
    private String  passwd;   //用户密码
    private String phoneNumber;//手机号
    private String email;  //邮箱
    private String captcha; //验证码
    private boolean isAdmin;//用户身份,0是普通用户
}
