package com.example.activity_manage.Service;

import com.example.activity_manage.Entity.Expense;

public interface ExpenseService {
    boolean commitExpense(Expense expense);
    boolean checkCommitExpense(long aid,long uid,long eid,int status, String comment);

}
