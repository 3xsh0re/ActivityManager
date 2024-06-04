package com.example.activity_manage.Service;

import com.example.activity_manage.Entity.DTO.CheckExpenseDTO;
import com.example.activity_manage.Entity.DTO.ExpensePageToManagerQueryDTO;
import com.example.activity_manage.Entity.DTO.ExpensePageToUserQueryDTO;
import com.example.activity_manage.Entity.Expense;
import com.example.activity_manage.Result.PageResult;

public interface ExpenseService {
    boolean commitExpense(Expense expense);
    boolean checkCommitExpense(CheckExpenseDTO checkExpenseDTO);
    PageResult getExpenseListToManager(ExpensePageToManagerQueryDTO pageQueryDTO);
    PageResult getExpenseListToUser(ExpensePageToUserQueryDTO pageToUserQueryDTO);
}
