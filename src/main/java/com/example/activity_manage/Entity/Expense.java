package com.example.activity_manage.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Expense { // 报销单类
    private long id;
    private long uid;
    private long aid;
    private double cost;
    private String content;
}
