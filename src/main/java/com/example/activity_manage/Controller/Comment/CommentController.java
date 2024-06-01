package com.example.activity_manage.Controller.Comment;

import com.example.activity_manage.Entity.Comments;
import com.example.activity_manage.Entity.DTO.ActivityPageQueryDTO;
import com.example.activity_manage.Result.PageResult;
import com.example.activity_manage.Result.Result;
import com.example.activity_manage.Service.CommentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comment")
public class CommentController {
    @Autowired
    CommentsService commentsService;

    @PostMapping("/addComment")
    public Result<Boolean> addComment(@RequestBody Comments comments){
        commentsService.addNewComment(comments);
        return Result.success();
    }
    @PostMapping("/getCommentList")
    public Result<PageResult> getCommentList(@RequestBody ActivityPageQueryDTO pageQueryDTO)
    {
        return Result.success(commentsService.getCommentsToAct(pageQueryDTO));
    }
    @GetMapping("/addLikes")
    public Result<Boolean> addLikes(@RequestParam("cid") long cid,@RequestParam("uid") long uid){
        commentsService.addLikes(cid,uid);
        return Result.success();
    }

    @GetMapping("/undoLikes")
    public Result<Boolean> undoLikes(@RequestParam("cid") long cid,@RequestParam("uid") long uid){
        commentsService.undoLikes(cid,uid);
        return Result.success();
    }
    @GetMapping("/deleteComment")
    public Result<Boolean> deleteComment(@RequestParam("cid") long cid,@RequestParam("uid") long uid){
        commentsService.deleteComment(cid,uid);
        return Result.success();
    }


}
