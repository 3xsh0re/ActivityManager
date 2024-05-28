package com.example.activity_manage.Controller.Expense;

import com.example.activity_manage.Entity.DTO.CheckExpenseDTO;
import com.example.activity_manage.Entity.DTO.ExpensePageToManagerQueryDTO;
import com.example.activity_manage.Entity.DTO.ExpensePageToUserQueryDTO;
import com.example.activity_manage.Entity.Expense;
import com.example.activity_manage.Result.PageResult;
import com.example.activity_manage.Result.Result;
import com.example.activity_manage.Service.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/expense")
public class ExpenseController {
    @Autowired
    ExpenseService expenseService;
    // 用户提交对应活动的报销单
    @PostMapping("/commitExpense")
    Result<Boolean> commitExpense(@RequestBody Expense expense){
        return Result.success(expenseService.commitExpense(expense));
    }
    // 组织者或者有审核权限的用户可以审核某个报销单
    @PostMapping("/checkExpense")
    Result<Boolean> checkExpense(@RequestBody CheckExpenseDTO checkExpenseDTO)
    {
        return Result.success(expenseService.checkCommitExpense(checkExpenseDTO));
    }
    // 审核员获取报销单的列表
    @PostMapping("/getExpenseListToManager")
    Result<PageResult> getExpenseListToManager(@RequestBody ExpensePageToManagerQueryDTO queryDTO){
        return Result.success(expenseService.getExpenseListToManager(queryDTO));
    }

    // 所有用户获取自己提交的报销单列表
    @PostMapping("/getExpenseListToUser")
    Result<PageResult> getExpenseListToUser(@RequestBody ExpensePageToUserQueryDTO pageToUserQueryDTO){
        return Result.success(expenseService.getExpenseListToUser(pageToUserQueryDTO));
    }
}
