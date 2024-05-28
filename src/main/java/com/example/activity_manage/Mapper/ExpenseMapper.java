package com.example.activity_manage.Mapper;

import com.example.activity_manage.Entity.Expense;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ExpenseMapper {
    void addNewExpense(Expense expense);
    Expense getExpenseByEid(long eid);
    Page<Expense> getExpenseListToManger(long aid); // 返回给组织者或者具有权限的用户,对于某个活动来说
    Page<Expense> getExpenseListToUser(long uid);   // 对于用户,可以查看自己提交的所有报销单
    void checkExpense(long eid, int status);
}
