package com.example.activity_manage.Controller.User;


import com.example.activity_manage.Constant.MessageConstant;
import com.example.activity_manage.Entity.DTO.NoticePageQueryDTO;
import com.example.activity_manage.Entity.DTO.NoticeToManagerPageQueryDTO;
import com.example.activity_manage.Entity.DTO.ResetPwdDTO;
import com.example.activity_manage.Entity.DTO.UserLoginDTO;
import com.example.activity_manage.Entity.User;
import com.example.activity_manage.Entity.VO.UserInfoVO;
import com.example.activity_manage.Entity.VO.UserLoginVO;
import com.example.activity_manage.Exception.LoginRegisterException;
import com.example.activity_manage.Result.PageResult;
import com.example.activity_manage.Result.Result;
import com.example.activity_manage.Service.NoticeService;
import com.example.activity_manage.Utils.CaptchaUtil;
import com.example.activity_manage.Service.UserService;
import com.example.activity_manage.Utils.JwtUtil;
import com.example.activity_manage.Utils.RedisUtil;
import com.example.activity_manage.Utils.SendUtil;
import org.springframework.beans.factory.annotation.Autowired;
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
    NoticeService noticeService;
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
        claims.put("role", user.getRole());

        String token = JwtUtil.createJWT(
                MessageConstant.JWT_SECRET_KET,
                60 * 60 * 24 * 2 * 1000,
                claims);
        UserLoginVO userLoginVO = UserLoginVO.builder()
                .id(user.getId())
                .token(token)
                .build();
        // token存入redis
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("id", user.getId());
        userMap.put("role", user.getRole());
        redisUtil.hmset("TOKEN_" + token, userMap ,60*60*24*2);

        return Result.success(userLoginVO);
    }

    @PostMapping("/register")
    public Result<Boolean> Register(@RequestBody UserLoginDTO userLoginDTO){
        return Result.success(userService.Register(userLoginDTO));
    }
    @GetMapping("/getCaptchaByEmail")
    public Result<Boolean> getCaptchaByEmail(@RequestParam("email") String email){
        //设置邮件内容
        String captcha = captchaUtil.generateCaptcha(email);
        boolean t = sendUtil.SendMessageByEmail(email,"[ActivityManager]您正在进行邮箱验证，验证码为：" + captcha + "，请在5分钟内按页面提示提交验证码，切勿将验证码泄露于他人");
        return Result.success(t);
    }

    @GetMapping("/getCaptchaByPhone")
    public Result<Boolean> getCaptchaByPhone(@RequestParam("phoneNumber") String phoneNumber){
        if (phoneNumber.equals(""))
        {
            throw new LoginRegisterException(MessageConstant.EMPTY_PHONE_NUMBER);
        }
        // 判断redis中是否还有验证码未过期
        if (redisUtil.get("UMS_" + phoneNumber) != null)
        {
            throw new LoginRegisterException(MessageConstant.CAPTCHA_DUPLICATE);
        }
        String phoneCaptcha = captchaUtil.generateCaptcha(phoneNumber);

        return Result.success(sendUtil.SendMessageByPhone(phoneNumber,phoneCaptcha));
    }
    @PostMapping("/resetPasswd")
    public Result<Boolean> resetPasswd(@RequestBody ResetPwdDTO resetPwdDTO)
    {
        return Result.success(userService.ResetPwd(resetPwdDTO));
    }

    @PostMapping("/getNoticeToUser")
    public Result<PageResult> getNoticeToUser(@RequestBody NoticePageQueryDTO pageQueryDTO)
    {
        return Result.success(noticeService.getNoticeToUser(pageQueryDTO));
    }

    @GetMapping("/openNotice")
    public Result<Boolean> openNotice(@RequestParam("nid") long nid){
        return Result.success(noticeService.updateIfRead(nid));
    }

    @PostMapping("/getNoticeToManager")
    public Result<PageResult> getNoticeToManager(@RequestBody NoticeToManagerPageQueryDTO pageQueryDTO){
        return Result.success(noticeService.getNoticeToManager(pageQueryDTO));
    }

    @GetMapping("/getUserInfoByUid")
    public Result<UserInfoVO> getUserInfo(@RequestParam("uid") long uid){
        return Result.success(userService.getUserInfo(uid));
    }

}
