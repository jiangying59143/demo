package com.mall.demo.rest.controller;

import com.alibaba.fastjson.support.spring.annotation.FastJsonFilter;
import com.alibaba.fastjson.support.spring.annotation.FastJsonView;
import com.mall.demo.common.annotation.LogAnnotation;
import com.mall.demo.common.constants.ResultCode;
import com.mall.demo.common.result.Result;
import com.mall.demo.model.blog.Comment;
import com.mall.demo.model.privilege.User;
import com.mall.demo.service.CommentService;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

/**
 * 评论api
 *
 * @author shimh
 * <p>
 * 2018年1月25日
 */
@RestController
@RequestMapping(value = "/comments")
public class CommentController {


    @Autowired
    private CommentService commentService;

    @GetMapping
    @LogAnnotation(module = "评论", operation = "获取所有评论")
    public Result listComments() {
        List<Comment> comments = commentService.findAll();

        return Result.success(comments);
    }

    @ApiIgnore
    @GetMapping("/{id}")
    @LogAnnotation(module = "评论", operation = "根据id获取评论")
    public Result getCommentById(@PathVariable("id") Long id) {

        Result r = new Result();

        if (null == id) {
            r.setResultCode(ResultCode.PARAM_IS_BLANK);
            return r;
        }

        Comment comment = commentService.getCommentById(id);

        r.setResultCode(ResultCode.SUCCESS);
        r.setData(comment);
        return r;
    }

    @ApiIgnore
    @GetMapping("/article/{id}")
    @FastJsonView(
            exclude = {
                    @FastJsonFilter(clazz = Comment.class, props = {"article", "parent"})},
            include = {@FastJsonFilter(clazz = User.class, props = {"id", "nick", "avatar"})})
    @LogAnnotation(module = "评论", operation = "根据文章获取评论")
    public Result listCommentsByArticle(@PathVariable("id") Long id) {

        Result r = new Result();

        if (null == id) {
            r.setResultCode(ResultCode.PARAM_IS_BLANK);
            return r;
        }

        List<Comment> comments = commentService.listCommentsByArticle(id);

        r.setResultCode(ResultCode.SUCCESS);
        r.setData(comments);
        return r;
    }


    @PostMapping("/create")
    @RequiresAuthentication
    @LogAnnotation(module = "评论", operation = "添加评论")
    public Result saveComment(@Validated @RequestBody Comment comment) {

        Long commentId = commentService.saveComment(comment);

        Result r = Result.success();
        r.simple().put("commentId", commentId);
        return r;
    }


    @GetMapping("/delete/{id}")
    @RequiresAuthentication
    @LogAnnotation(module = "评论", operation = "删除评论")
    public Result deleteCommentById(@PathVariable("id") Long id) {
        Result r = new Result();

        if (null == id) {
            r.setResultCode(ResultCode.PARAM_IS_BLANK);
            return r;
        }

        commentService.deleteCommentById(id);

        r.setResultCode(ResultCode.SUCCESS);
        return r;
    }

    @ApiIgnore
    @PostMapping("/create/change")
    @FastJsonView(
            exclude = {
                    @FastJsonFilter(clazz = Comment.class, props = {"article"})},
            include = {@FastJsonFilter(clazz = User.class, props = {"id", "nickname", "avatar"})})
    @RequiresAuthentication
    @LogAnnotation(module = "评论", operation = "添加评论，增加评论数")
    public Result saveCommentAndChangeCounts(@RequestBody Comment comment) {

        Comment savedComment = commentService.saveCommentAndChangeCounts(comment);

        Result r = Result.success(savedComment);
        return r;
    }


    @GetMapping("/delete/change/{id}")
    @RequiresAuthentication
    @LogAnnotation(module = "评论", operation = "删除评论，减少评论数")
    public Result deleteCommentByIdAndChangeCounts(@PathVariable("id") Long id) {
        Result r = new Result();

        if (null == id) {
            r.setResultCode(ResultCode.PARAM_IS_BLANK);
            return r;
        }

        commentService.deleteCommentByIdAndChangeCounts(id);

        r.setResultCode(ResultCode.SUCCESS);
        return r;
    }

}