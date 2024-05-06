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
public class GetUserVO implements Serializable{
    private Long id;
    private String username;
    private String phoneNumber;
    private String email;
}
