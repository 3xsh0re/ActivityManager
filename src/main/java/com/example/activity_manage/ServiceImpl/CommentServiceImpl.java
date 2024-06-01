package com.example.activity_manage.ServiceImpl;

import com.example.activity_manage.Constant.MessageConstant;
import com.example.activity_manage.Entity.Comment;
import com.example.activity_manage.Entity.DTO.ActivityPageQueryDTO;
import com.example.activity_manage.Entity.VO.ActInfoToManagerVO;
import com.example.activity_manage.Entity.VO.CommentVO;
import com.example.activity_manage.Exception.ActivityException;
import com.example.activity_manage.Mapper.ActivityMapper;
import com.example.activity_manage.Mapper.CommentMapper;
import com.example.activity_manage.Mapper.UserMapper;
import com.example.activity_manage.Result.PageResult;
import com.example.activity_manage.Service.CommentService;
import com.example.activity_manage.Utils.XSSFilterUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    CommentMapper commentMapper;
    @Autowired
    ActivityMapper activityMapper;
    @Autowired
    UserMapper userMapper;

    @Override
    public void addNewComment(Comment comment) {
        if (comment.getContent() == null){
            throw new ActivityException("评论不能为空!");
        }
        // 过滤评论内容中XSS攻击
        String filteredComment = XSSFilterUtil.filter(comment.getContent());
        comment.setContent(filteredComment);
        commentMapper.addNewComment(comment);
    }

    @Override
    public PageResult getCommentsToAct(ActivityPageQueryDTO pageQueryDTO) {
        //开始分页查询
        PageHelper.startPage(pageQueryDTO.getPage(), pageQueryDTO.getPageSize());
        Page<Comment> page = commentMapper.getCommentsToAct(pageQueryDTO.getAid());
        long total = page.getTotal();
        List<Comment> commentList = page.getResult();
        List<CommentVO> records = new ArrayList<>();
        for (Comment comment : commentList){
            CommentVO commentVO = new CommentVO();
            commentVO.setUsername(userMapper.getUsernameById(comment.getUid()));
            commentVO.setContent(comment.getContent());
            commentVO.setCommentTime(comment.getCommentTime());
            commentVO.setId(comment.getId());
            commentVO.setLikes(comment.getLikes());
            records.add(commentVO);
        }
        return new PageResult(total, records);
    }

    @Override
    public void addLikes(long cid, long uid) {
        JSONObject jsonObject = commentMapper.getLikeUserList(cid);
        JSONObject likeUserList = (JSONObject) jsonObject.get("likeUserList");
        if (likeUserList.containsKey(Long.toString(uid)))
        {
            throw new ActivityException("你已经点过赞!");
        }
        commentMapper.addLikes(cid,uid);
    }

    @Override
    public void undoLikes(long cid, long uid) {
        JSONObject jsonObject = commentMapper.getLikeUserList(cid);
        JSONObject likeUserList = (JSONObject) jsonObject.get("likeUserList");
        if (!likeUserList.containsKey(Long.toString(uid)))
        {
            throw new ActivityException("你还没有点赞!");
        }
        commentMapper.undoLikes(cid,uid);
    }

    @Override
    public void deleteComment(long cid, long uid) {
        Comment comment = commentMapper.getCommentByCid(cid);
        if (comment.getUid() != uid){
            throw new ActivityException(MessageConstant.NOT_HAVE_THIS_PERMISSION);
        }
        commentMapper.deleteComment(cid);
    }

}
