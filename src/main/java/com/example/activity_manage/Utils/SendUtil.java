package com.example.activity_manage.Utils;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.example.activity_manage.Constant.MessageConstant;
import com.example.activity_manage.Exception.SystemBusyException;
import net.minidev.json.JSONObject;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class SendUtil {
    public Boolean SendMessage(String phoneNumber,String phone_captcha){
        // 连接阿里云
        DefaultProfile profile = DefaultProfile.getProfile("cn-Beijing", "LTAI5tFecUJxe4fA1tQRcjKV", "GWGAex7POIN6IL1OLVwBu1xNiK2Zxa");
        IAcsClient client = new DefaultAcsClient(profile);
        // 构建请求
        CommonRequest request = new CommonRequest();
        request.setSysMethod(MethodType.POST);
        //下面这3个不要改动
        request.setSysDomain("dysmsapi.aliyuncs.com");
        request.setSysVersion("2017-05-25");
        request.setSysAction("SendSms");
        //接收短信的手机号码
        request.putQueryParameter("PhoneNumbers",phoneNumber);//此处写电话号码
        //短信签名名称
        request.putQueryParameter("SignName","阿里云短信测试");
        //短信模板ID
        request.putQueryParameter("TemplateCode","SMS_154950909");
        //短信模板变量对应的实际值 ${code} 中的值
        Map<String,String> param = new HashMap<>(2);

        param.put("code", String.valueOf(phone_captcha)); //写入的短信内容,验证码
        request.putQueryParameter("TemplateParam", JSONObject.toJSONString(param));
        try {
            CommonResponse response = client.getCommonResponse(request);
            System.out.println(response.getData());
        } catch (ServerException e) {
            throw new SystemBusyException(MessageConstant.SYSTEM_BUSY);
        } catch (ClientException e) {
            throw new RuntimeException(e);
        }
        return Boolean.TRUE;
    }
}
