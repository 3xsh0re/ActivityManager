package com.example.activity_manage.ServiceImpl;

import com.example.activity_manage.Constant.MessageConstant;
import com.example.activity_manage.Entity.Activity;
import com.example.activity_manage.Entity.DTO.CheckExpenseDTO;
import com.example.activity_manage.Entity.DTO.ExpensePageToManagerQueryDTO;
import com.example.activity_manage.Entity.DTO.ExpensePageToUserQueryDTO;
import com.example.activity_manage.Entity.Expense;
import com.example.activity_manage.Entity.Notice;
import com.example.activity_manage.Entity.VO.ExpenseToManagerVO;
import com.example.activity_manage.Entity.VO.ExpenseToUserVO;
import com.example.activity_manage.Exception.ActivityException;
import com.example.activity_manage.Mapper.ActivityMapper;
import com.example.activity_manage.Mapper.ExpenseMapper;
import com.example.activity_manage.Mapper.NoticeMapper;
import com.example.activity_manage.Mapper.UserMapper;
import com.example.activity_manage.Result.PageResult;
import com.example.activity_manage.Service.ExpenseService;
import com.example.activity_manage.Utils.JwtUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ExpenseServiceImpl implements ExpenseService {
    @Autowired
    ExpenseMapper expenseMapper;
    @Autowired
    ActivityMapper activityMapper;
    @Autowired
    NoticeMapper noticeMapper;
    @Autowired
    UserMapper userMapper;
    @Override
    public boolean commitExpense(Expense expense) {
        expenseMapper.addNewExpense(expense);
        return true;
    }

    @Override
    public boolean checkCommitExpense(CheckExpenseDTO checkExpenseDTO) {
        long aid = checkExpenseDTO.getAid();
        long uid = checkExpenseDTO.getUid();
        long eid = checkExpenseDTO.getEid();
        int  status = checkExpenseDTO.getStatus();

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
        // 获取当前报销单
        Expense expense = expenseMapper.getExpenseByEid(eid);
        // 向用户发送报销审核结果
        Notice notice = new Notice();
        notice.setType(0);
        notice.setAid(aid);
        notice.setSendUid(uid);
        if (status == 1)
        {
            notice.setContent("报销单审核通过!");
        }
        else {
            notice.setContent("审核不通过!\n原因：" + checkExpenseDTO.getComment());
        }
        notice.setReceiveUid(expense.getUid());
        // 获取当前时间hash
        String groupId = JwtUtil.getNowTimeHash();
        notice.setGroupId(groupId);
        noticeMapper.createNotice(notice);
        return true;
    }

    @Override
    public PageResult getExpenseListToManager(ExpensePageToManagerQueryDTO pageQueryDTO) {
        long uid = pageQueryDTO.getUid();
        long aid = pageQueryDTO.getAid();
        String role = activityMapper.getUserRole(aid,uid);
        // 校验该用户是否有权限审核
        if (role == null || !(role.equals("组织者") || role.equals("报销员") || role.equals("财务员"))){
            throw new ActivityException(MessageConstant.NOT_HAVE_THIS_PERMISSION);
        }

        //开始分页查询
        PageHelper.startPage(pageQueryDTO.getPage(), pageQueryDTO.getPageSize());
        Page<Expense> page = expenseMapper.getExpenseListToManger(aid);
        long total = page.getTotal();
        List<Expense> expenseList = page.getResult();
        List<ExpenseToManagerVO> records = new ArrayList<>();
        for (Expense e: expenseList) {
            ExpenseToManagerVO expenseToManagerVO = new ExpenseToManagerVO();
            expenseToManagerVO.setEid(e.getId());
            expenseToManagerVO.setCost(e.getCost());
            expenseToManagerVO.setContent(e.getContent());
            expenseToManagerVO.setStatus(e.getStatus());
            expenseToManagerVO.setPostUsername(userMapper.getUsernameById(e.getUid()));
            records.add(expenseToManagerVO);
        }
        return new PageResult(total, records);
    }

    @Override
    public PageResult getExpenseListToUser(ExpensePageToUserQueryDTO pageToUserQueryDTO) {
        //开始分页查询
        PageHelper.startPage(pageToUserQueryDTO.getPage(), pageToUserQueryDTO.getPageSize());
        Page<Expense> page = expenseMapper.getExpenseListToUser(pageToUserQueryDTO.getUid());
        long total = page.getTotal();
        List<Expense> expenseList = page.getResult();
        List<ExpenseToUserVO> records = new ArrayList<>();
        for (Expense e: expenseList) {
            ExpenseToUserVO expenseToUserVO = new ExpenseToUserVO();
            expenseToUserVO.setEid(e.getId());
            expenseToUserVO.setCost(e.getCost());
            expenseToUserVO.setContent(e.getContent());
            expenseToUserVO.setStatus(e.getStatus());
            expenseToUserVO.setActName(activityMapper.getActNameByAid(e.getAid()));
            records.add(expenseToUserVO);
        }
        return new PageResult(total, records);
    }
}
