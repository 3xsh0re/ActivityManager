package com.example.activity_manage.ServiceImpl;

import ch.qos.logback.core.joran.sanity.Pair;
import com.example.activity_manage.Constant.MessageConstant;
import com.example.activity_manage.Entity.Activity;
import com.example.activity_manage.Entity.Comments;
import com.example.activity_manage.Entity.DTO.*;
import com.example.activity_manage.Entity.Notice;
import com.example.activity_manage.Entity.VO.*;
import com.example.activity_manage.Exception.ActivityException;
import com.example.activity_manage.Exception.LoginRegisterException;
import com.example.activity_manage.Exception.PageNotFoundException;
import com.example.activity_manage.Mapper.ActivityMapper;
import com.example.activity_manage.Mapper.CommentsMapper;
import com.example.activity_manage.Mapper.NoticeMapper;
import com.example.activity_manage.Mapper.UserMapper;
import com.example.activity_manage.Result.PageResult;
import com.example.activity_manage.Service.ActivityService;
import com.example.activity_manage.Utils.JwtUtil;
import com.example.activity_manage.Utils.RedisUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;

@Service
public class ActivityServiceImpl implements ActivityService {
    @Autowired
    ActivityMapper activityMapper;
    @Autowired
    UserMapper userMapper;
    @Autowired
    NoticeMapper noticeMapper;
    @Autowired
    CommentsMapper commentsMapper;
    @Autowired
    RedisUtil redisUtil;

    @Override
    public Boolean ActivityCreate(ActivityCreateDTO activityCreateDTO)
    {
        UserInfoVO user = userMapper.getUserInfo(activityCreateDTO.getUid());
        // 从redis中判断用户是否登陆
        if (redisUtil.hasKey("TOKEN" + activityCreateDTO.getUid())){
            throw new ActivityException(MessageConstant.ACCOUNT_NOT_LOGIN);
        }
        // 判断用户是否存在
        if (user == null){
            throw new ActivityException(MessageConstant.ACCOUNT_NOT_FOUND);
        }
        JSONObject jsonObject = userMapper.getActList(activityCreateDTO.getUid());//获取当前用户参与的活动表
        if (jsonObject != null)
        {
            JSONObject actList = (JSONObject) jsonObject.get("actList");
            // 获取JSONObject的键的集合
            Set<String> keys = actList.keySet();
            // 获取新创建的活动时间
            java.util.Date beginTime = activityCreateDTO.getBeginTime();
            java.util.Date endTime   = activityCreateDTO.getEndTime();
            // 遍历所有的键,即活动id
            for (String aid : keys) {
                // 获取时间
                Pair<Timestamp, Timestamp> tmp = activityMapper.activityDateGet(Long.parseLong(aid));
                java.util.Date extActBeginTime = new java.util.Date(tmp.first.getTime());
                java.util.Date extActEndTime = new java.util.Date(tmp.second.getTime());
                // 检测时间冲突
                if (extActBeginTime.before(beginTime) && extActEndTime.after(beginTime) ||
                        extActBeginTime.before(endTime) && extActEndTime.after(endTime) ||
                        extActBeginTime.equals(beginTime) || extActBeginTime.equals(endTime)||
                        extActEndTime.equals(beginTime) || extActEndTime.equals(endTime)
                ){
                    throw new ActivityException(MessageConstant.ACTIVITY_TIME_CONFLICT);
                }
            }
        }
        // 设定活动的组织者
        JSONObject userList = new JSONObject();
        userList.put(Long.toString(activityCreateDTO.getUid()), "组织者");
        activityCreateDTO.setUserList(userList);

        // 创建活动,直接将activityCreateDTO传入即可
        activityMapper.activityCreate(activityCreateDTO);
        // 更新User表中字段
        if (jsonObject == null){
            // 将创建的活动修改到User的ActList中
            JSONObject new_actList = new JSONObject();
            new_actList.put(Long.toString(activityMapper.getMaxId()),activityCreateDTO.getActName());
            userMapper.updateActList(activityCreateDTO.getUid(),new_actList);
        }
        else {
            JSONObject actList = (JSONObject) jsonObject.get("actList");
            actList.put(Long.toString(activityMapper.getMaxId()),activityCreateDTO.getActName());
            userMapper.updateActList(activityCreateDTO.getUid(),actList);
        }
        return true;
    }

    @Override
    public Pair<Timestamp, Timestamp> ActivityDateGet(long AID) {
        return activityMapper.activityDateGet(AID);
    }

    @Override
    public Activity getActInfoToOrganizer(long uid, long aid) {
        long uid_t = activityMapper.getUidByAid(aid);
        if (uid == 0)
        {
            throw new PageNotFoundException(MessageConstant.PAGE_NOT_FOUND);//返回页面不存在
        }
        //非组织者用户非法查看
        if (uid != uid_t)
        {
            throw new ActivityException(MessageConstant.NOT_ORGANIZER_FOR_ACTIVITY);
        }
        return activityMapper.getActInfoToOrganizer(aid);
    }

    @Override
    public ActInfoToAllVO getActInfoToAll(long aid) {
        ActInfoToAllVO actInfoToAllVO = activityMapper.getActInfoToAll(aid);
        // 活动不存在
        if (actInfoToAllVO == null)
        {
            throw new PageNotFoundException(MessageConstant.PAGE_NOT_FOUND);//返回页面不存在
        }
        actInfoToAllVO.setUsername(userMapper.getUsernameById(actInfoToAllVO.getOrgId()));
        return actInfoToAllVO;
    }

    @Override
    public List<ActScheduleVO> getActSchedule(long uid) {
        List<Activity> l_act = activityMapper.getAllAct();
        List<ActScheduleVO> l_actSchedule = new ArrayList<>();
        // 将Activity转换到VO
        for (Activity act : l_act){
            String role = act.getUserList().getAsString(Long.toString(uid));
            if (role != null)
            {
                ActScheduleVO actScheduleVO = new ActScheduleVO();
                actScheduleVO.setAid(act.getId());
                actScheduleVO.setActName(act.getActName());
                actScheduleVO.setBeginTime(act.getBeginTime());
                actScheduleVO.setEndTime(act.getEndTime());
                actScheduleVO.setRole(act.getUserList().getAsString(Long.toString(uid)));
                l_actSchedule.add(actScheduleVO);
            }
        }
        return l_actSchedule;
    }

    @Override
    public ActReportVO getActReport(long aid, long uid) {
        String role = activityMapper.getUserRole(aid,uid);
        if (role==null)
        {
            throw new ActivityException(MessageConstant.NOT_HAVE_THIS_PERMISSION);
        }
        if (!role.equals("组织者") && !role.equals("管理员"))
        {
            throw new ActivityException(MessageConstant.NOT_HAVE_THIS_PERMISSION);
        }
        Activity activity = activityMapper.getActInfoToOrganizer(aid);
        ActReportVO reportVO = new ActReportVO();
        reportVO.setAid(aid);
        reportVO.setActName(activity.getActName());
        reportVO.setOrgName(userMapper.getUsernameById(uid));
        reportVO.setRank(activity.getRank());
        JSONObject jsonObject = activityMapper.getUserList(aid);
        // 获取活动参与人数
        JSONObject userList   = (JSONObject) jsonObject.get("userList");
        reportVO.setParticipantNum(userList.size());
        // 获取聊天室消息数
        List<JSONObject> messageList = activityMapper.getAllMessage(aid);
        reportVO.setMessageNum(messageList.size());
        // 获取活动内部流程
        JSONObject jsonActProcess = activityMapper.getActProcess(aid);
        JSONObject actStatus   = (JSONObject) jsonActProcess.get("actStatus");
        List<String> statusList = JwtUtil.sortJSONObjectByValue(actStatus);
        reportVO.setActStatus(statusList);
        // 获取前五个高赞评论
        List<Comments> commentsList = commentsMapper.getAllCommentToAct(aid);
        commentsList.sort(Comparator.comparingInt(Comments::getLikes).reversed());
        List<String> highLikesList = new ArrayList<>();
        int index = 0;
        for (Comments c: commentsList) {
            if (index == 5)
                break;
            highLikesList.add(c.getContent());
            index++;
        }
        reportVO.setHighLikesCommentList(highLikesList);
        return reportVO;
    }


    @Override
    public void deleteActivity(long uid,long aid) {
        if (activityMapper.getActInfoToOrganizer(aid) == null)
        {
            throw new ActivityException(MessageConstant.ACTIVITY_NOT_EXIST);
        }
        if (uid == activityMapper.getUidByAid(aid) || uid == 1)//非组织者或者管理员不能删除活动
        {
            // 删除活动的评论
            commentsMapper.deleteAllCommentToAct(aid);
            // 删除活动表中的数据
            activityMapper.deleteActivity(aid);
        }
        else {
            throw new ActivityException(MessageConstant.NOT_HAVE_THIS_PERMISSION);
        }
    }

    @Override
    public void setBudget(long uid, long aid, int budget) {
        if (activityMapper.getActInfoToAll(aid) == null)
        {
            throw new ActivityException(MessageConstant.ACTIVITY_NOT_EXIST);
        }
        if (uid != activityMapper.getUidByAid(aid))
        {
            throw new ActivityException(MessageConstant.NOT_HAVE_THIS_PERMISSION);
        }
        activityMapper.setBudget(aid,budget);
    }
    @Override
    public Integer getBudget(long aid, long uid)
    {
        if(activityMapper.checkUserExist(aid, uid)) // 判断用户是否存在于活动中, 仅有活动参与者可以查询预算
        {
            return activityMapper.getBudget(aid);
        }
        else {
            throw new ActivityException(MessageConstant.ACCOUNT_NOT_JOIN);
        }
    }

    @Override
    public void updateActProcess(UpdateActProcessDTO updateActProcessDTO) {
        long uid = updateActProcessDTO.getUid();
        long aid = updateActProcessDTO.getAid();
        Activity activity = activityMapper.getActInfoToOrganizer(aid);
        if (activity == null)
        {
            throw new ActivityException(MessageConstant.ACTIVITY_NOT_EXIST);
        }
        long orgId = activityMapper.getUidByAid(aid);
        if (uid != orgId) // 只有组织者有更新活动流程的权限
        {
            throw new ActivityException(MessageConstant.NOT_HAVE_THIS_PERMISSION);
        }
        JSONObject actProcess =  updateActProcessDTO.getActStatus();
        activityMapper.updateActProcess(aid,actProcess);
    }

    @Override
    public void joinAct(long uid, long aid, String reason) {
        ActInfoToAllVO activity = activityMapper.getActInfoToAll(aid);
        if (activity == null)
        {
            throw new ActivityException(MessageConstant.ACTIVITY_NOT_EXIST);
        }
        Date begin = activity.getBeginTime();
        Date end   = activity.getEndTime();
        String  username = userMapper.getUsernameById(uid);
        // 判断当前参加活动是否和用户已经参加的活动冲突
        JSONObject jsonObject = userMapper.getActList(uid);
        JSONObject actList  = (JSONObject) jsonObject.get("actList");
        Set<String> keys = actList.keySet();
        for (String key:keys) {
            Activity activity_Joined = activityMapper.getActInfoToOrganizer(Long.parseLong(key));
            Date beginTime = activity_Joined.getBeginTime();
            Date endTime   = activity_Joined.getEndTime();
            // 判断时间是否冲突
            if ( !(begin.after(endTime) || end.before(beginTime))){
                throw new ActivityException(MessageConstant.ACTIVITY_TIME_CONFLICT + "\n发生冲突的活动为：" + activity_Joined.getActName());
            }
        }
        if (username == null){
            throw new LoginRegisterException(MessageConstant.ACCOUNT_NOT_FOUND);
        }
        // 更新User的待审核活动
        JSONObject objectUser = userMapper.getWantJoinActList(uid);
        if (objectUser == null)//User没有待申请的活动
        {
            objectUser = new JSONObject();
            objectUser.put( Long.toString(aid), reason);
            userMapper.updateWantJoinActList(uid,objectUser);
        }
        else{
            JSONObject wJActList = (JSONObject) objectUser.get("wantJoinActList");
            wJActList.put( Long.toString(aid), reason);
            userMapper.updateWantJoinActList(uid, wJActList);
        }
        // 更新Activity的待审核用户
        JSONObject objectActivity = activityMapper.getUnCheckedUserList(aid);
        if (objectActivity == null){
            objectActivity = new JSONObject();
            objectActivity.put(Long.toString(uid),reason);
            activityMapper.updateUnCheckedUserList(aid,objectActivity);
        }
        else {
            JSONObject uCActList = (JSONObject) objectActivity.get("unCheckedUserList");
            uCActList.put(Long.toString(uid),reason);
            activityMapper.updateUnCheckedUserList(aid,uCActList);
        }
    }

    @Override
    public void exitAct(long uid, long aid) {
        JSONObject json = userMapper.getWantJoinActList(uid);
        JSONObject wtjList = (JSONObject) json.get("wantJoinActList");
        if (wtjList.containsKey(Long.toString(aid)))
        {
            // 如果用户还未加入
            userMapper.deleteActInWantJoinList(Long.toString(aid),uid);
            activityMapper.deleteUserInUnCheckedList(aid,Long.toString(uid));
        }
        else {
            // 如果用户已经加入,更新用户的参与活动列表已经活动中的用户表
            JSONObject jsonObject = userMapper.getActList(uid);
            JSONObject actList    = (JSONObject) jsonObject.get("actList");
            System.out.println("actList: " + actList);
            JSONObject jsonObject1 = activityMapper.getUserList(aid);
            JSONObject userList   = (JSONObject) jsonObject1.get("userList");
            System.out.println("userList: " + actList);
            Set<String> keys = actList.keySet();
            for (String key:keys) {
                if (Long.toString(aid).equals(key)){
                    // 对于用户表,更新actList即可
                    actList.remove(key);
                    // 对于活动表,需要更新userList,roleList,userGroup
                    userList.remove(Long.toString(uid));
                    System.out.println("被删除的活动: " + key);
                    String role = activityMapper.getUserRole(aid,uid);
                    int num = activityMapper.getRoleNum(aid,role);

                    System.out.println("Deleted actList: " + actList);
                    System.out.println("Deleted userList: " + userList);
                    userMapper.updateActList(uid,actList);
                    activityMapper.updateRoleList(aid,role,num-1);
                    activityMapper.deleteUserInGroup(aid,Long.toString(uid));
                    activityMapper.updateUserList(aid,userList);

                    return;
                }
            }
        }
    }

    // 审核用户申请
    @Override
    public boolean checkApplication(long uid, long aid, long unCheckedId,boolean result) {
        // 审核通过
        if (result){
            // 更新Activity表
            // 将新增的用户设为普通参与者
            ActivitySetParticipantRoleDTO roleDTO = new ActivitySetParticipantRoleDTO(uid,aid,unCheckedId,"普通参与者");
            setParticipantRole(roleDTO);
            // 为用户新增活动
            JSONObject jsonObject3 = userMapper.getActList(unCheckedId);
            JSONObject actList = (JSONObject) jsonObject3.get("actList");
            actList.put(Long.toString(aid),activityMapper.getActNameByAid(aid));
            userMapper.updateActList(unCheckedId,actList);
        }
        JSONObject jsonObject = activityMapper.getUnCheckedUserList(aid);
        JSONObject unCheckedUserList = (JSONObject) jsonObject.get("unCheckedUserList");
        // Activity删除审核过的用户
        unCheckedUserList.remove(Long.toString(unCheckedId));
        activityMapper.updateUnCheckedUserList(aid,unCheckedUserList);
        // User删除审核过的活动
        JSONObject jsonObject2 = userMapper.getWantJoinActList(unCheckedId);
        JSONObject wantJoinActList = (JSONObject) jsonObject2.get("wantJoinActList");
        wantJoinActList.remove(Long.toString(aid));
        userMapper.updateWantJoinActList(unCheckedId,wantJoinActList);
        // 发送通知,返回给用户审核结果
        Notice notice = new Notice();
        notice.setAid(aid);
        notice.setType(0);
        notice.setSendUid(uid);
        notice.setReceiveUid(unCheckedId);
        if (result) {
            notice.setContent("审核通过");
        } else {
            notice.setContent("审核不通过");
        }
        notice.setGroupId(JwtUtil.getNowTimeHash());
        noticeMapper.createNotice(notice);
        return true;
    }

    @Override
    public boolean setRankForAct(long uid, long aid, double rank) {
        String role = activityMapper.getUserRole(aid,uid);
        if (role == null)
        {
            throw new ActivityException(MessageConstant.NOT_HAVE_THIS_PERMISSION);
        }
        JSONObject jsonObject = activityMapper.getRankList(aid);
        if (jsonObject != null)
        {
            JSONObject rankList   = (JSONObject) jsonObject.get("rankList");
            rankList.put(Long.toString(uid) , rank);
            Set<String> keySet = rankList.keySet();
            double score = 0;
            int rankPeopleNum = rankList.size();
            for (String key: keySet) {
                score += (double) rankList.get(key);
            }
            activityMapper.setRankForAct(aid,rankList,score/rankPeopleNum);
        }
        else {
            JSONObject rankList = new JSONObject();
            rankList.put(Long.toString(uid) , rank);
            activityMapper.setRankForAct(aid,rankList,rank);
        }
        return true;
    }

    //分页查询返回活动
    public PageResult pageQueryBaseActInfoVO(BasePageQueryDTO basePageQueryDTO) {
        //开始分页查询
        PageHelper.startPage(basePageQueryDTO.getPage(), basePageQueryDTO.getPageSize());
        Page<BaseActInfoVO> page = activityMapper.pageQueryBaseActInfoVO(basePageQueryDTO);
        long total = page.getTotal();
        List<BaseActInfoVO> records = page.getResult();
        return new PageResult(total, records);
    }
    @Override
    public PageResult pageQueryUnCheckedUser(ActivityPageQueryDTO activityPageQueryDTO) {
        long uid = activityPageQueryDTO.getUid();
        long orgId = activityMapper.getUidByAid(activityPageQueryDTO.getAid());
        if ( uid != orgId)
        {
            throw new ActivityException(MessageConstant.NOT_HAVE_THIS_PERMISSION);
        }
        //开始分页查询
        JSONObject object = (JSONObject) activityMapper.getUnCheckedUserList(activityPageQueryDTO.getAid()).get("unCheckedUserList");
        int page = activityPageQueryDTO.getPage();
        int pageSize = activityPageQueryDTO.getPageSize();
        Iterator<String> keys = object.keySet().iterator();
        List<UnCheckedUserVO> records = new ArrayList<>();
        int num = 0;
        while (keys.hasNext()){
            String key = keys.next(); //key为申请者id
            if ( (page - 1) * pageSize <= num && num < page * pageSize ){
                UnCheckedUserVO unCheckedUserVO = new UnCheckedUserVO();
                unCheckedUserVO.setUid(Long.parseLong(key));
                unCheckedUserVO.setUsername(userMapper.getUsernameById(Long.parseLong(key)));
                unCheckedUserVO.setPhoneNumber(userMapper.getPhoneByUid(Long.parseLong(key)));
                unCheckedUserVO.setReason((String) object.get(key));
                records.add(unCheckedUserVO);
            }
            num++;
        }

        long total = records.size();
        return new PageResult(total, records);
    }
    public Boolean setParticipantRole(ActivitySetParticipantRoleDTO activitySetParticipantRoleDTO)
    {
        long managerUid = activitySetParticipantRoleDTO.getManagerUid();
        long aid = activitySetParticipantRoleDTO.getAid();
        long participantUid = activitySetParticipantRoleDTO.getParticipantUid();
        String newRole = activitySetParticipantRoleDTO.getRole();
        String oriRole = activityMapper.getUserRole(aid, participantUid);
        int oriRoleNum = activityMapper.getRoleNum(aid, oriRole);
        // 获取操作者身份, 鉴权
        Set<String> validRoles = new HashSet<>(Arrays.asList("组织者", "管理员"));
        String managerRole = activityMapper.getUserRole(aid, managerUid);
        if(validRoles.contains(managerRole))
        {
            // 如果用户原本有角色, 将roleList对应角色的数量减一
            if(oriRole != null && oriRoleNum > 0)
            {
                activityMapper.updateRoleList(aid, oriRole, oriRoleNum - 1);
            }
            // 更新新角色的roleList对应quantity
            int newRoleNum = activityMapper.getRoleNum(aid, newRole);
            activityMapper.updateRoleList(aid, newRole, newRoleNum + 1);
            // 更新userList的角色
            activityMapper.updateUserRole(aid, participantUid, newRole);
            return true;
        }
        return false;
    }
    public Boolean serParticipantGroup(ActivitySetParticipantGroupDTO activitySetParticipantGroupDTO)
    {
        long managerUid = activitySetParticipantGroupDTO.getManagerUid();
        long aid = activitySetParticipantGroupDTO.getAid();
        long participantUid = activitySetParticipantGroupDTO.getParticipantUid();
        String group = activitySetParticipantGroupDTO.getGroup();
        // 获取操作者身份, 鉴权
        Set<String> validRoles = new HashSet<>(Arrays.asList("组织者", "管理员"));
        String managerRole = activityMapper.getUserRole(aid, managerUid);
        if(validRoles.contains(managerRole))
        {
            // 更新userList的角色
            activityMapper.updateUserGroup(aid, participantUid, group);
            return true;
        }
        return false;
    }
    public Boolean participantInteractiveSend(ActivityParticipantInteractiveSendDTO activityParticipantInteractiveSendDTO) {
        long aid = activityParticipantInteractiveSendDTO.getAid();
        long uid = activityParticipantInteractiveSendDTO.getUid();
        if(activityMapper.checkUserInActivity(aid, uid)) // 只有该活动成员能够发送消息
        {
            String message = activityParticipantInteractiveSendDTO.getMessage();
            message = uid + ":" + message;
            String timestamp = Long.toString(System.currentTimeMillis());
            activityMapper.insertMessage(aid, timestamp, message);
            return true;
        }
        return false;
    }
    public List<JSONObject> participantInteractiveReceive(ActivityParticipantInteractiveReceiveDTO activityParticipantInteractiveReceiveDTO)
    {
        long aid = activityParticipantInteractiveReceiveDTO.getAid();
        long uid = activityParticipantInteractiveReceiveDTO.getUid();
        if(activityMapper.checkUserInActivity(aid, uid)) {
            return activityMapper.getAllMessage(aid);
        }
        return null;
    }

}
