package com.example.activity_manage.Entity.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.minidev.json.JSONObject;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateActProcessDTO {
    private long uid;
    private long aid;
    private JSONObject actStatus; // 活动流程
}
