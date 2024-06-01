package com.example.activity_manage.Controller.Comment;

import com.example.activity_manage.Entity.Comment;
import com.example.activity_manage.Entity.DTO.ActivityPageQueryDTO;
import com.example.activity_manage.Result.PageResult;
import com.example.activity_manage.Result.Result;
import com.example.activity_manage.Service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comment")
public class CommentController {
    @Autowired
    CommentService commentService;

    @PostMapping("/addComment")
    public Result<Boolean> addComment(@RequestBody Comment comment){
        commentService.addNewComment(comment);
        return Result.success();
    }
    @PostMapping("/getCommentList")
    public Result<PageResult> getCommentList(@RequestBody ActivityPageQueryDTO pageQueryDTO)
    {
        return Result.success(commentService.getCommentsToAct(pageQueryDTO));
    }
    @GetMapping("/addLikes")
    public Result<Boolean> addLikes(@RequestParam("cid") long cid,@RequestParam("uid") long uid){
        commentService.addLikes(cid,uid);
        return Result.success();
    }

    @GetMapping("/undoLikes")
    public Result<Boolean> undoLikes(@RequestParam("cid") long cid,@RequestParam("uid") long uid){
        commentService.undoLikes(cid,uid);
        return Result.success();
    }


}
