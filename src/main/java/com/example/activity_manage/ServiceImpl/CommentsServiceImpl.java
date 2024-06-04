package com.example.activity_manage.ServiceImpl;

import com.example.activity_manage.Constant.MessageConstant;
import com.example.activity_manage.Entity.Comments;
import com.example.activity_manage.Entity.DTO.ActivityPageQueryDTO;
import com.example.activity_manage.Entity.VO.CommentVO;
import com.example.activity_manage.Exception.ActivityException;
import com.example.activity_manage.Mapper.ActivityMapper;
import com.example.activity_manage.Mapper.CommentsMapper;
import com.example.activity_manage.Mapper.UserMapper;
import com.example.activity_manage.Result.PageResult;
import com.example.activity_manage.Service.CommentsService;
import com.example.activity_manage.Utils.XSSFilterUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommentsServiceImpl implements CommentsService {
    @Autowired
    CommentsMapper commentsMapper;
    @Autowired
    ActivityMapper activityMapper;
    @Autowired
    UserMapper userMapper;

    @Override
    public void addNewComment(Comments comments) {
        if (comments.getContent().equals("")){
            throw new ActivityException("评论不能为空!");
        }
        long uid = comments.getUid();
        long aid = comments.getAid();
        String role = activityMapper.getUserRole(aid,uid);
        if (role == null )
        {
            throw new ActivityException("尚未参与此活动,不能参加评论!");
        }
        // 过滤评论内容中XSS攻击
        String filteredComment = XSSFilterUtil.filter(comments.getContent());
        comments.setContent(filteredComment);
        commentsMapper.addNewComment(comments);
    }

    @Override
    public PageResult getCommentsToAct(ActivityPageQueryDTO pageQueryDTO) {
        //开始分页查询
        PageHelper.startPage(pageQueryDTO.getPage(), pageQueryDTO.getPageSize());
        Page<Comments> page = commentsMapper.getCommentsToAct(pageQueryDTO.getAid());
        long total = page.getTotal();
        List<Comments> commentsList = page.getResult();
        List<CommentVO> records = new ArrayList<>();
        for (Comments comments : commentsList){
            CommentVO commentVO = new CommentVO();
            commentVO.setUsername(userMapper.getUsernameById(comments.getUid()));
            commentVO.setContent(comments.getContent());
            commentVO.setCommentTime(comments.getCommentTime());
            commentVO.setId(comments.getId());
            commentVO.setLikes(comments.getLikes());
            records.add(commentVO);
        }
        return new PageResult(total, records);
    }

    @Override
    public void addLikes(long cid, long uid) {
        JSONObject jsonObject = commentsMapper.getLikeUserList(cid,uid);
        if (jsonObject == null)
        {
            commentsMapper.addLikes(cid,uid);
            return;
        }
        JSONObject likeUserList = (JSONObject) jsonObject.get("likeUserList");
        if (likeUserList.containsKey(Long.toString(uid)))
        {
            throw new ActivityException("你已经点过赞!");
        }
        commentsMapper.addLikes(cid,uid);
    }

    @Override
    public void undoLikes(long cid, long uid) {
        JSONObject jsonObject = commentsMapper.getLikeUserList(cid,uid);
        if (jsonObject == null)
        {
            commentsMapper.addLikes(cid,uid);
            return;
        }
        JSONObject likeUserList = (JSONObject) jsonObject.get("likeUserList");
        if (!likeUserList.containsKey(Long.toString(uid)))
        {
            throw new ActivityException("你还没有点赞!");
        }
        commentsMapper.undoLikes(cid,uid);
    }

    @Override
    public void deleteComment(long cid, long uid) {
        Comments comments = commentsMapper.getCommentByCid(cid);
        if (comments == null) {
            throw new ActivityException("此评论已被删除!");
        }
        if (comments.getUid() != uid){
            throw new ActivityException(MessageConstant.NOT_HAVE_THIS_PERMISSION);
        }
        commentsMapper.deleteComment(cid);
    }

}
