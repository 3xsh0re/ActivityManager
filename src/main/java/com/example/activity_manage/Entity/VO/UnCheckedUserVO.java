package com.example.activity_manage.Entity.VO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UnCheckedUserVO implements Serializable {
    long uid;
    String username;
    String phoneNumber;
    String reason;
}
