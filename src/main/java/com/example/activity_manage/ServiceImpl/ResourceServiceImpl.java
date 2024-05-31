package com.example.activity_manage.ServiceImpl;

import com.example.activity_manage.Constant.MessageConstant;
import com.example.activity_manage.Entity.Activity;
import com.example.activity_manage.Entity.DTO.BasePageQueryDTO;
import com.example.activity_manage.Entity.DTO.ResourceAdditionDTO;
import com.example.activity_manage.Entity.DTO.ResourceReservationDTO;
import com.example.activity_manage.Entity.Resource;
import com.example.activity_manage.Exception.ActivityException;
import com.example.activity_manage.Exception.SystemException;
import com.example.activity_manage.Mapper.ActivityMapper;
import com.example.activity_manage.Mapper.ResourceMapper;
import com.example.activity_manage.Result.PageResult;
import com.example.activity_manage.Service.ResourceService;
import com.example.activity_manage.Utils.JwtUtil;
import com.example.activity_manage.Utils.RedisUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class ResourceServiceImpl implements ResourceService {
    @Autowired
    ResourceMapper resourceMapper;
    @Autowired
    ActivityMapper activityMapper;
    @Autowired
    RedisUtil redisUtil;

    protected static boolean isTimeConflict(Date activityBegin, Date activityEnd, Date specifiedBegin, Date specifiedEnd) {
        return !((activityBegin.after(specifiedEnd)) || activityEnd.before(specifiedBegin));
    }
    protected static void mergeResources(JSONObject total, JSONObject toAdd) {
        if (toAdd != null)
        {
            for (String key : toAdd.keySet()) {
                if (total.containsKey(key)) {
                    int existingValue = (int) total.get(key);
                    int additionalValue = (int) toAdd.get(key);
                    total.put(key, existingValue + additionalValue);
                } else {
                    total.put(key, toAdd.get(key));
                }
            }
        }
    }
    protected static JSONObject checkActivityConflicts(List<Activity> activities, Date beginTime, Date endTime) {
        JSONObject totalResourceUsage = new JSONObject();

        for (Activity activity : activities) {
            if (isTimeConflict(activity.getBeginTime(), activity.getEndTime(), beginTime, endTime)) {
                JSONObject resource = activity.getResource();
                mergeResources(totalResourceUsage, resource);
            }
        }

        return totalResourceUsage;
    }
    @Transactional
    public Boolean resourceReservation(ResourceReservationDTO resourceReservationDTO) {
        int quantityNeed = resourceReservationDTO.getQuantity();
        if (quantityNeed < 0){
            throw new ActivityException(MessageConstant.NOT_ILLEGAL_INPUT);
        }
        long UID = resourceReservationDTO.getUid(); // 获取申请者UID, 鉴权, 只有活动创建者与活动管理员可以申请资源
        long AID = resourceReservationDTO.getAid();
        String resourceName = resourceReservationDTO.getResource();
        // redis锁：控制并发场景下资源预约
        String lockKey = "R_LOCK_" + resourceMapper.getRidByResourceName(resourceName);
        String lockValue = JwtUtil.getNowTimeHash(); // 当前系统时间的hash作为value
        try{
            // 尝试获取锁
            boolean lockAcquired = redisUtil.tryLock(lockKey, lockValue, 1, TimeUnit.MINUTES);
            if (!lockAcquired) {
                throw new SystemException(MessageConstant.SYSTEM_BUSY);
            }
            // 预约资源逻辑
            Activity nowActivity = activityMapper.getActInfoToOrganizer(AID);
            Date beginTime = nowActivity.getBeginTime();
            Date endTime   = nowActivity.getEndTime();
            List<Activity> actList = activityMapper.getAllAct();
            // 计算在指定时间段内已经被占用的资源
            JSONObject totalResourceUsage = checkActivityConflicts(actList, beginTime, endTime);
            // 获取操作者的角色
            String userRole = activityMapper.getUserRole(AID, UID);
            // 设置哪些角色有权限进行资源申请
            Set<String> validRoles = new HashSet<>(Arrays.asList("组织者", "管理员"));
            if (validRoles.contains(userRole) && resourceMapper.checkResourceByName(resourceName)) {// 检查资源是否存在
                int quantityCurr = resourceMapper.selectResourceByName(resourceName);
                // 从totalResourceUsage中获取已经被占用的数量
                int quantityUsed = (int) totalResourceUsage.getOrDefault(resourceName, 0);
                // 可用资源数量
                int availableQuantity = quantityCurr - quantityUsed;
                // 判断当前申请的资源数量是否能被满足
                if(availableQuantity >= quantityNeed)
                {
                    activityMapper.updateActivityResource(AID, resourceName, quantityNeed);
                    return true;
                }
            }
            return false;
        }finally {
            // 操作完成释放锁
            redisUtil.unlock(lockKey,lockValue);
        }
    }

    @Override
    public Boolean resourceAddition(ResourceAdditionDTO resourceAdditionDTO) {
        String resourceName = resourceAdditionDTO.getResource();
        int quantity = resourceAdditionDTO.getQuantity();
        int type = resourceAdditionDTO.getType();
        if(resourceMapper.checkResourceByName(resourceName))
        {
            int quantityCurr = resourceMapper.selectResourceByName(resourceName);
            int quantityNew = quantity + quantityCurr;
            resourceMapper.updateResourceQuantityByName(resourceName, quantityNew);
        }
        else
        {
            resourceMapper.insertResource(resourceName, quantity, type);
        }
        return true;
    }

    public PageResult pageQueryAllResource(BasePageQueryDTO basePageQueryDTO){
        try {
            //开始分页查询
            PageHelper.startPage(basePageQueryDTO.getPage(), basePageQueryDTO.getPageSize());
            Page<Resource> page = resourceMapper.pageQueryAllResource();
            long total = page.getTotal();
            List<Resource> records = page.getResult();
            return new PageResult(total, records);
        }
        catch (Exception e){
            throw new SystemException(MessageConstant.SYSTEM_BUSY);
        }
    }
}
