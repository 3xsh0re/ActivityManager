package com.example.activity_manage.Controller.User;


import com.example.activity_manage.Constant.MessageConstant;
import com.example.activity_manage.Entity.DTO.ActivityCreateDTO;
import com.example.activity_manage.Entity.DTO.ResetPwdDTO;
import com.example.activity_manage.Entity.DTO.ResourceReservationDTO;
import com.example.activity_manage.Entity.DTO.UserLoginDTO;
import com.example.activity_manage.Entity.User;
import com.example.activity_manage.Entity.VO.UserLoginVO;
import com.example.activity_manage.Exception.DuplicateCaptchaExecption;
import com.example.activity_manage.Exception.EmptyPhoneNumberException;
import com.example.activity_manage.Result.Result;
import com.example.activity_manage.Utils.CaptchaUtil;
import com.example.activity_manage.Service.UserService;
import com.example.activity_manage.Utils.JwtUtil;
import com.example.activity_manage.Utils.RedisUtil;
import com.example.activity_manage.Utils.SendUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;


import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {
    @GetMapping("/test")
    public String test()
    {
        return "hello";
    }
    @Autowired
    UserService userService;
    @Autowired
    CaptchaUtil captchaUtil;
    @Autowired
    RedisUtil redisUtil;
    @Autowired
    SendUtil sendUtil;

    @PostMapping("/login")
    public Result<UserLoginVO> Login(@RequestBody UserLoginDTO userLoginDTO)
    {
        User user = userService.Login(userLoginDTO);
        // 登录成功后，生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", user.getId());
        claims.put("username", user.getUsername());
        String token = JwtUtil.createJWT(
                "123456",
                60 * 60 * 24 * 3,
                claims);
        UserLoginVO userLoginVO = UserLoginVO.builder()
                .id(user.getId())
                .token(token)
                .build();
        // token存入redis
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("username", user.getUsername());
        userMap.put("phoneNumber", user.getPhoneNumber());
        userMap.put("email", user.getEmail());
        userMap.put("actList", user.getActList());
        redisUtil.hmset("TOKEN_" + token, userMap ,60*60*24*5);

        return Result.success(userLoginVO);
    }

    @PostMapping("/register")
    public Result<Boolean> Register(@RequestBody UserLoginDTO userLoginDTO){
        return Result.success(userService.Register(userLoginDTO));
    }
    @Autowired
    private JavaMailSender sender;
    @GetMapping("/getCaptchaByEmail/{email}")
    public Result<Boolean> getCaptchaByEmail(@PathVariable("email") String email){

        SimpleMailMessage message = new SimpleMailMessage();
        //设置邮件标题
        message.setSubject("ActivityManager");
        //设置邮件内容
        String captcha = captchaUtil.generateCaptcha(email);
        message.setText("[ActivityManager]您正在进行邮箱验证，验证码为：" + captcha + "，请在5分钟内按页面提示提交验证码，切勿将验证码泄露于他人");
        message.setTo(email);
        //邮件发送者，这里要与配置文件中的保持一致
        message.setFrom("xieyuheng_ustb@163.com");
        //发送
        sender.send(message);
        return Result.success(Boolean.TRUE);
    }

    @GetMapping("/getCaptchaByPhone")
    public Result<Boolean> getCaptchaByPhone(@RequestParam("phoneNumber") String phoneNumber){
        if (phoneNumber.equals(""))
        {
            throw new EmptyPhoneNumberException(MessageConstant.EMPTY_PHONE_NUMBER);
        }
        // 判断redis中是否还有验证码未过期
        if (redisUtil.get("UMS_" + phoneNumber) != null)
        {
            throw new DuplicateCaptchaExecption(MessageConstant.CAPTCHA_DUPLICATE);
        }
        String phoneCaptcha = captchaUtil.generateCaptcha(phoneNumber);

        return Result.success(sendUtil.SendMessage(phoneNumber,phoneCaptcha));
    }
    @PostMapping("/resetPasswd")
    public Result<Boolean> resetPasswd(@RequestBody ResetPwdDTO resetPwdDTO)
    {
        return Result.success(userService.ResetPwd(resetPwdDTO));
    }

}
