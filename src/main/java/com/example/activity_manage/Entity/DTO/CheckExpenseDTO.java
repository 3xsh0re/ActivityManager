package com.example.activity_manage.Entity.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CheckExpenseDTO {
    private long aid;
    private long uid;
    private long eid;
    private int status;
    private String comment;
}
