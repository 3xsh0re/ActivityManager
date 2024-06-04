package com.example.activity_manage.Entity.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExpenseToUserVO {
    private long eid;
    private String actName;
    private double cost;
    private String content;
    private int status;
}
