package com.example.activity_manage.ServiceImpl;

import com.example.activity_manage.Constant.MessageConstant;
import com.example.activity_manage.Entity.Activity;
import com.example.activity_manage.Entity.DTO.BasePageQueryDTO;
import com.example.activity_manage.Entity.DTO.ResourceAdditionDTO;
import com.example.activity_manage.Entity.DTO.ResourceReservationDTO;
import com.example.activity_manage.Entity.Resource;
import com.example.activity_manage.Exception.SystemException;
import com.example.activity_manage.Mapper.ResourceMapper;
import com.example.activity_manage.Result.PageResult;
import com.example.activity_manage.Service.ActivityService;
import com.example.activity_manage.Service.ResourceService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;

@Service
public class ResourceServiceImpl implements ResourceService {
    @Autowired
    ResourceMapper resourceMapper;
    ActivityService activityService;
    public static boolean isTimeConflict(Date activityBegin, Date activityEnd, Date specifiedBegin, Date specifiedEnd) {
        return (activityBegin.before(specifiedEnd) && activityEnd.after(specifiedBegin));
    }
    public static void mergeResources(JSONObject total, JSONObject toAdd) {
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
    public static JSONObject checkActivityConflicts(List<Activity> activities, Date beginTime, Date endTime) {
        JSONObject totalResourceUsage = new JSONObject();

        for (Activity activity : activities) {
            if (isTimeConflict((Date) activity.getBeginTime(), (Date) activity.getEndTime(), beginTime, endTime)) {
                JSONObject resource = activity.getResource();
                mergeResources(totalResourceUsage, resource);
            }
        }

        return totalResourceUsage;
    }
    public Boolean resourceReservation(ResourceReservationDTO resourceReservationDTO) {
//        Long UID = resourceReservationDTO.getUid(); // 获取申请者UID, 目前没什么用
        String resourceName = resourceReservationDTO.getResource();
        int quantityNeed = resourceReservationDTO.getQuantity();
        Date beginTime = resourceReservationDTO.getBeginTime();
        Date endTime = resourceReservationDTO.getEndTime();
        List<Activity> actList = activityService.getAllActivity();
        // 计算在指定时间段内已经被占用的资源
        JSONObject totalResourceUsage = checkActivityConflicts(actList, beginTime, endTime);
        // 打印总资源使用情况（调试用）
        // System.out.println("总资源使用情况: " + totalResourceUsage.toString());
        if (resourceMapper.checkResourceByName(resourceName)) {// 检查资源是否存在
            int quantityCurr = resourceMapper.selectResourceByName(resourceName);
            // 从totalResourceUsage中获取已经被占用的数量
            int quantityUsed = (int) totalResourceUsage.getOrDefault(resourceName, 0);
            // 可用资源数量
            int availableQuantity = quantityCurr - quantityUsed;
            // 判断当前申请的资源数量是否能被满足
            return availableQuantity >= quantityNeed;
        }
        return false;
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
