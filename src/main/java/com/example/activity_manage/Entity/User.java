package com.example.activity_manage.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.minidev.json.JSONObject;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private long    id;
    private String  username; //用户名
    private String  passwd;   //用户密码
    private String phoneNumber;//手机号
    private String email;  //邮箱
    private int role;
    private JSONObject actList;//用户活动日程
    private JSONObject wantJoinActList; // 想要参与的活动申请<aid,reason>
}
