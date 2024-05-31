package com.example.activity_manage.Constant;

/**
 * 信息提示常量类
 */
public class MessageConstant {
    public static final String JWT_SECRET_KET = "123456";

    public static final String PASSWORD_ERROR = "密码错误";
    public static final String ACCOUNT_NOT_FOUND = "账号不存在";
    public static final String ACCOUNT_NOT_LOGIN = "此用户尚未登陆系统";
    public static final String ACCOUNT_NOT_JOIN = "此用户未参与此活动";
    public static final String ACCOUNT_NOT_ADMIN = "此账号非管理员用户";
    public static final String NOT_ILLEGAL_CHECK_STATUS = "不是管理员审核后的合法状态";
    public static final String NOT_HAVE_THIS_PERMISSION = "没有此操作权限";
    public static final String ERROR_CAPTCHA = "验证码错误";
    public static final String EMPTY_PHONE_NUMBER = "手机号为空";
    public static final String CAPTCHA_DUPLICATE = "5min内发送过一次验证码";
    public static final String SYSTEM_BUSY = "系统繁忙,请稍后重试";
    public static final String PAGE_NOT_FOUND = "页面不存在";
    public static final String NOT_ILLEGAL_INPUT = "不合法的输入参数";
    // 活动类
    public static final String ACTIVITY_TIME_CONFLICT = "活动时间冲突";
    public static final String ACTIVITY_NOT_EXIST = "此活动不存在";
    public static final String ACTIVITY_ALREADY_CHECKED = "此活动已经被审核";
    public static final String NOT_ORGANIZER_FOR_ACTIVITY = "此账号不是本活动的组织者";
    //文件类
    public static final String FILE_NOT_FOUND = "文件不存在";
    public static final String FILE_UPLOAD_ERROR = "文件上传失败";
    public static final String ERROR_FILE_SUFFIX = "文件后缀只能为.pdf或.docx或.doc或.exe或.zip";
    public static final String ERROR_FILE_DELETE = "文件不存在，或已经删除";
    public static final String PHONE_DUPLICATE = "手机号已被注册";

}
