package com.example.activity_manage.Controller.Expense;

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
    @GetMapping("/checkExpense")
    Result<Boolean> checkExpense(@RequestParam("aid") long aid,@RequestParam("uid") long uid,@RequestParam("eid") long eid,@RequestParam("status") int status,@RequestParam("comment") String comment)
    {
        return Result.success(expenseService.checkCommitExpense(aid, uid, eid, status,comment));
    }
    // 审核员获取报销单的列表
    Result<PageResult> getExpenseList(@RequestParam("aid") long aid,@RequestParam("uid") long uid){
        return Result.success();
    }

}
