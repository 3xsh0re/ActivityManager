package com.example.activity_manage.Mapper;

import com.example.activity_manage.Entity.Comment;
import com.github.pagehelper.Page;
import net.minidev.json.JSONObject;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CommentMapper {
    void addNewComment(Comment comment);
    Page<Comment> getCommentsToAct(long aid);
    Comment getCommentByCid(long cid);
    void addLikes(long cid,long uid);
    void undoLikes(long cid,long uid);
    JSONObject getLikeUserList(long cid);
    void deleteComment(long cid);
    void deleteAllCommentToAct(long aid);
}
