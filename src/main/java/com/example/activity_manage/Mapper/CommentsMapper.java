package com.example.activity_manage.Mapper;

import com.example.activity_manage.Entity.Comments;
import com.github.pagehelper.Page;
import net.minidev.json.JSONObject;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CommentsMapper {
    void addNewComment(Comments comments);
    Page<Comments> getCommentsToAct(long aid); // 分页返回该活动的所有评论
    Comments getCommentByCid(long cid);
    void addLikes(long cid,long uid);
    void undoLikes(long cid,long uid);
    JSONObject getLikeUserList(long cid,long uid);
    List<Comments> getAllCommentToAct(long aid); // 获取该活动的所有评论
    void deleteComment(long cid);
    void deleteAllCommentToAct(long aid);
}
