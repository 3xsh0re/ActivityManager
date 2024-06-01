package com.example.activity_manage.Mapper;

import com.example.activity_manage.Entity.Comments;
import com.github.pagehelper.Page;
import net.minidev.json.JSONObject;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CommentsMapper {
    void addNewComment(Comments comments);
    Page<Comments> getCommentsToAct(long aid);
    Comments getCommentByCid(long cid);
    void addLikes(long cid,long uid);
    void undoLikes(long cid,long uid);
    JSONObject getLikeUserList(long cid);
    void deleteComment(long cid);
    void deleteAllCommentToAct(long aid);
}
