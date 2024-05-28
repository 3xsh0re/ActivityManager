package com.example.activity_manage.Entity.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExpenseToManagerVO {
    private long eid;
    private String postUsername;
    private double cost;
    private String content;
    private int status;
}
