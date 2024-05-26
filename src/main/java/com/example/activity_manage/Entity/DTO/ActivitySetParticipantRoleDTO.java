package com.example.activity_manage.Entity.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActivitySetParticipantRoleDTO { // 对应userList
    private long managerUid; // 操作者 用户ID
    private long aid; // 活动ID
    private long participantUid; // 参与者uid
    private String Role; // 参与者角色
}
