package com.example.activity_manage.Entity.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResetPwdDTO implements Serializable {
    private String phoneNumber;//手机号
    private String captcha; //验证码
    private String passwd; //新密码
}
