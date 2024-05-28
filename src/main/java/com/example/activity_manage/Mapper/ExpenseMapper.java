package com.example.activity_manage.Mapper;

import com.example.activity_manage.Entity.Expense;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ExpenseMapper {
    void addNewExpense(Expense expense);
    Page<Expense> getExpense(long aid);
    void checkExpense(long eid, int status);
}
