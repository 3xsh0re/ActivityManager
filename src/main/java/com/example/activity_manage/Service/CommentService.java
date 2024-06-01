package com.example.activity_manage.Service;

import com.example.activity_manage.Entity.Comment;
import com.example.activity_manage.Entity.DTO.ActivityPageQueryDTO;
import com.example.activity_manage.Result.PageResult;

public interface CommentService {
    void addNewComment(Comment comment);
    PageResult getCommentsToAct(ActivityPageQueryDTO pageQueryDTO);
    void addLikes(long cid,long uid);
    void undoLikes(long cid,long uid);
    void deleteComment(long cid,long uid);
}
