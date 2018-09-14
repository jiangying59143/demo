package com.mall.demo.rest.controller;

import com.alibaba.fastjson.support.spring.annotation.FastJsonFilter;
import com.alibaba.fastjson.support.spring.annotation.FastJsonView;
import com.mall.demo.common.annotation.LogAnnotation;
import com.mall.demo.common.constants.Base;
import com.mall.demo.common.constants.ResultCode;
import com.mall.demo.common.result.Result;
import com.mall.demo.model.blog.Article;
import com.mall.demo.model.blog.Comment;
import com.mall.demo.model.privilege.User;
import com.mall.demo.service.CommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Arrays;
import java.util.List;

@Api(value = "评论", description = "评论")
@RestController
@RequestMapping(value = "/comments")
public class CommentController {


    @Autowired
    private CommentService commentService;

    @ApiOperation(value="获取所有评论", notes="获取所有评论")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Oauth-Token", value = "令牌", required = true, dataType = "String", paramType = "header")
    })
    @FastJsonView(
            include = {@FastJsonFilter(clazz = Comment.class, props = {"content", "level", "author", "article"}),
                    @FastJsonFilter(clazz = User.class, props = {"id", "account", "nickname", "avatar", "avatarPath"}),
                    @FastJsonFilter(clazz = Article.class, props = {"id", "title"})})
    @GetMapping
    @RequiresRoles(Base.ROLE_ADMIN)
    @LogAnnotation(module = "评论", operation = "获取所有评论")
    public Result listComments() {
        List<Comment> comments = commentService.findAll();
        return Result.success(comments);
    }

    @ApiOperation(value="根据主键获取评论", notes="根据主键获取评论")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Oauth-Token", value = "令牌", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "id", value = "评论主键", required = true, dataType = "Long", paramType = "path")
    })
    @FastJsonView(
            include = {@FastJsonFilter(clazz = Comment.class, props = {"content", "level", "author", "article", "children", "parent_id"}),
                    @FastJsonFilter(clazz = User.class, props = {"id", "account", "nickname", "avatar", "avatarPath"}),
                    @FastJsonFilter(clazz = Article.class, props = {"id", "title"})})
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

    @ApiOperation(value="根据文章id获取评论", notes="根据文章id获取评论")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Oauth-Token", value = "令牌", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "id", value = "文章主键", required = true, dataType = "Long", paramType = "path")
    })
    @GetMapping("/article/{id}")
    @FastJsonView(
            exclude = {
                    @FastJsonFilter(clazz = Comment.class, props = {"article", "parent"})},
            include = {@FastJsonFilter(clazz = User.class, props = {"id", "account", "nickname", "avatar", "avatarPath"})})
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

    @ApiOperation(value="添加评论", notes="content必填\n" +
            "articleId parentId toUid有三种情况\n" +
            "   articleId 不为null, 另两个是null 表示文章评论\n" +
            "   articleId 为null, parentId 不为null, toUid 为null 表示评论的评论\n" +
            "   articleId 为null, 其他两个不为null， 表示评论的回复" +
            "toUid 是被评论的用户ID")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Oauth-Token", value = "令牌", required = true, dataType = "String", paramType = "header")
    })
    @PostMapping("/create")
    @RequiresAuthentication
    @LogAnnotation(module = "评论", operation = "添加评论")
    public Result saveComment(@Validated @RequestBody Comment comment) {
        if(!BooleanUtils.or(new boolean[]{comment.getArticleId()!=null && comment.getParentId()==null && comment.getToUid() == null,
                comment.getArticleId()==null && comment.getParentId()!=null && comment.getToUid() == null,
                comment.getArticleId()==null && comment.getParentId()!=null && comment.getToUid() != null})){
            return Result.error(ResultCode.PARAM_IS_BLANK);
        }
        Long commentId = commentService.saveComment(comment);
        Result r = Result.success();
        r.simple().put("commentId", commentId);
        return r;
    }

    @ApiOperation(value="删除评论", notes="删除评论")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Oauth-Token", value = "令牌", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "id", value = "文章主键", required = true, dataType = "Long", paramType = "path")
    })
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

    @ApiIgnore
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
