package com.example.activity_manage.ServiceImpl;

import com.example.activity_manage.Constant.MessageConstant;
import com.example.activity_manage.Entity.Activity;
import com.example.activity_manage.Entity.DTO.BasePageQueryDTO;
import com.example.activity_manage.Entity.DTO.ResourceAdditionDTO;
import com.example.activity_manage.Entity.DTO.ResourceReservationDTO;
import com.example.activity_manage.Entity.DTO.ResourceToActDTO;
import com.example.activity_manage.Entity.Notice;
import com.example.activity_manage.Entity.Resource;
import com.example.activity_manage.Exception.ActivityException;
import com.example.activity_manage.Exception.SystemException;
import com.example.activity_manage.Mapper.ActivityMapper;
import com.example.activity_manage.Mapper.NoticeMapper;
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
    NoticeMapper noticeMapper;
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
            if (MessageConstant.All_Permission_Role.contains(userRole) && resourceMapper.checkResourceByName(resourceName)) {// 检查资源是否存在
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
                else
                {
                    throw new ActivityException("资源剩余量不足");
                }
            }
            else {
                throw new ActivityException(MessageConstant.NOT_HAVE_THIS_PERMISSION);
            }
        }finally {
            // 操作完成释放锁
            redisUtil.unlock(lockKey,lockValue);
        }
    }

    @Override
    public Boolean resourceAddition(ResourceAdditionDTO resourceAdditionDTO) {
        String resourceName = resourceAdditionDTO.getResource();
        if (resourceName == null || resourceName.equals("") || resourceName.contains(" "))
        {
            throw new ActivityException(MessageConstant.NOT_ILLEGAL_INPUT);
        }
        int quantity = resourceAdditionDTO.getQuantity();
        int type = resourceAdditionDTO.getType();
        if ( type != 1 && type !=0){
            throw new ActivityException(MessageConstant.NOT_ILLEGAL_INPUT);
        }
        // 资源数量不能小于0
        if (quantity < 0){
            throw new ActivityException(MessageConstant.NOT_ILLEGAL_INPUT);
        }
        // 对于地点类资源,quantity只能为0或1
        if (type == 1 && (quantity > 1)){
            throw new ActivityException(MessageConstant.NOT_ILLEGAL_INPUT);
        }
        // 对于资源,在调整时,需要对已经分配对应资源的活动进行判断,如果资源调整后小于了需要的资源,需要发出通知告知该活动的组织者
        List<Activity> actList = activityMapper.getAllAct();
        for (Activity act: actList) {
            // 获取当前时间的hash,用于发送消息
            String timeHash = JwtUtil.getNowTimeHash();
            // 初始化消息类对象
            Notice notice = new Notice();
            notice.setSendUid(1);
            notice.setContent("活动资源{" + resourceName + "}由于管理员对其数量进行调整现已经被设置为0,请重新预约该资源!");
            notice.setType(0);
            notice.setGroupId(timeHash);
            notice.setIfRead(false);
            // 取出每个活动中该资源的使用数量
            long aid = act.getId();
            int usedCount = activityMapper.getReQuantityByReName(aid,resourceName);
            // 设定后的该资源比使用量小,需要修改对应活动的资源使用量为0,并通知用户重新预约资源
            if (usedCount > quantity){
                activityMapper.updateActivityResource(aid,resourceName,0);
                // 对于需要的活动,发送消息
                notice.setAid(aid);
                notice.setReceiveUid(act.getUid());
                noticeMapper.createNotice(notice);
            }
        }
        // 判断是修改资源还是添加资源
        if(resourceMapper.checkResourceByName(resourceName))
        {
            resourceMapper.updateResourceQuantityByName(resourceName, quantity);
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

    @Override
    public PageResult pageQueryResourceToAct(ResourceToActDTO resourceToActDTO) {
//        try {
            List<Activity> actList = activityMapper.getAllAct();
            // 计算在指定时间段内已经被占用的资源
            JSONObject totalResourceUsage = checkActivityConflicts(actList, resourceToActDTO.getBeginTime(), resourceToActDTO.getEndTime());
            //开始分页查询
            PageHelper.startPage(resourceToActDTO.getPage(), resourceToActDTO.getPageSize());
            Page<Resource> page = resourceMapper.pageQueryAllResource();
            long total = page.getTotal();
            List<Resource> records = page.getResult();
            for (Resource re: records) {
                String resourceName = re.getResourceName();
                int quantityCurr = resourceMapper.selectResourceByName(resourceName);
                // 从totalResourceUsage中获取已经被占用的数量
                int quantityUsed = (int) totalResourceUsage.getOrDefault(resourceName, 0);
                // 可用资源数量
                int availableQuantity = quantityCurr - quantityUsed;
                re.setQuantity(availableQuantity);
            }
            return new PageResult(total, records);
//        }
//        catch (Exception e){
//            throw new SystemException(MessageConstant.SYSTEM_BUSY);
//        }
    }
}
