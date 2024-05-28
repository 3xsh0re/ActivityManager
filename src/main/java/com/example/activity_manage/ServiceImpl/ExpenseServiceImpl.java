package com.example.activity_manage.ServiceImpl;

import com.example.activity_manage.Constant.MessageConstant;
import com.example.activity_manage.Entity.Activity;
import com.example.activity_manage.Entity.Expense;
import com.example.activity_manage.Exception.ActivityException;
import com.example.activity_manage.Mapper.ActivityMapper;
import com.example.activity_manage.Mapper.ExpenseMapper;
import com.example.activity_manage.Service.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExpenseServiceImpl implements ExpenseService {
    @Autowired
    ExpenseMapper expenseMapper;
    @Autowired
    ActivityMapper activityMapper;

    @Override
    public boolean commitExpense(Expense expense) {
        expenseMapper.addNewExpense(expense);
        return true;
    }

    @Override
    public boolean checkCommitExpense(long aid,long uid,long eid, int status, String comment) {
        // 判断活动是否存在
        Activity activity = activityMapper.getActInfoToOrganizer(aid);
        if (activity == null)
            throw new ActivityException(MessageConstant.ACTIVITY_NOT_EXIST);

        String role = activityMapper.getUserRole(aid,uid);
        // 校验该用户是否有权限审核
        if (role == null || !(role.equals("组织者") || role.equals("报销员") || role.equals("财务员"))){
            throw new ActivityException(MessageConstant.NOT_HAVE_THIS_PERMISSION);
        }
        // 检测输入参数合法
        if (status != 1 && status != -1)
            throw new ActivityException(MessageConstant.NOT_ILLEGAL_CHECK_STATUS);
        // 更新报销单表
        expenseMapper.checkExpense(eid,status);
        // 向用户发送报销审核结果


        return true;
    }
}
