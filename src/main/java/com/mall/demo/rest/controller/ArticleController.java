package com.mall.demo.rest.controller;

import com.alibaba.fastjson.support.spring.annotation.FastJsonFilter;
import com.alibaba.fastjson.support.spring.annotation.FastJsonView;
import com.google.common.collect.Lists;
import com.mall.demo.common.annotation.LogAnnotation;
import com.mall.demo.common.constants.Base;
import com.mall.demo.common.constants.ResultCode;
import com.mall.demo.common.result.Result;
import com.mall.demo.common.utils.UserUtils;
import com.mall.demo.model.blog.Article;
import com.mall.demo.model.blog.ArticleBody;
import com.mall.demo.model.blog.UserSearchHistory;
import com.mall.demo.model.privilege.User;
import com.mall.demo.service.ArticleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

@Api(value = "文章", description = "文章")
@RestController
@RequestMapping(value = "/article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @ApiOperation(value="添加搜索历史", notes="添加搜索历史")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "令牌", required = true, dataType = "String", paramType = "header")
    })
    @GetMapping("/addHistory")
    @LogAnnotation(module = "添加搜索历史", operation = "添加搜索历史")
    public Result addSearchHistory(@RequestParam String content){
        User currentUser = UserUtils.getCurrentUser();
        articleService.addSearchHistory(currentUser, content);
        return Result.success();
    }

    @ApiOperation(value="搜索历史", notes="搜索历史")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "令牌", required = true, dataType = "String", paramType = "header")
    })
    @FastJsonView(
            include = {
                    @FastJsonFilter(clazz = UserSearchHistory.class, props = {"content"})})
    @GetMapping("/searchHistory")
    @LogAnnotation(module = "搜索历史", operation = "搜索历史")
    public Result getSearchHistory(){
        User currentUser = UserUtils.getCurrentUser();
        Page<UserSearchHistory> pageInfoList = articleService.listSearchHistory(currentUser.getId(), 0, 4);
        List<UserSearchHistory> list = Lists.newArrayList(pageInfoList.iterator());
        return Result.success(list);
    }

    @ApiOperation(value="猜你想搜的", notes="猜你想搜的")
    @FastJsonView(
            include = {
                    @FastJsonFilter(clazz = UserSearchHistory.class, props = {"content"})})
    @GetMapping("/searchGuess")
    @LogAnnotation(module = "猜你想搜的", operation = "猜你想搜的")
    public Result getSearchGuess(){
        List<UserSearchHistory> list = articleService.listSearchGuess(0, 20);
        return Result.success(list);
    }

    @ApiIgnore
    @ApiOperation(value="获取最热文章", notes="根据id获取文章")
    @GetMapping("/hot")
    @FastJsonView(include = {@FastJsonFilter(clazz = Article.class, props = {"id", "title"})})
    @LogAnnotation(module = "文章", operation = "获取最热文章")
    public Result listHotArticles() {
        int limit = 6;
        List<Article> articles = articleService.listHotArticles(limit);
        return Result.success(articles);
    }

    @ApiIgnore
    @GetMapping("/new")
    @FastJsonView(include = {@FastJsonFilter(clazz = Article.class, props = {"id", "title"})})
    @LogAnnotation(module = "文章", operation = "获取最新文章")
    public Result listNewArticles() {
        int limit = 6;
        List<Article> articles = articleService.listNewArticles(limit);

        return Result.success(articles);
    }

    @ApiIgnore
    @ApiOperation(value="获取一篇文章", notes="根据id获取文章")
    @GetMapping("/{id}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "主键", required = true, dataType = "Long", paramType = "path"),
            @ApiImplicitParam(name = "Authorization", value = "令牌", required = true, dataType = "String", paramType = "header")
    })
    @FastJsonView(
            exclude = {
                    @FastJsonFilter(clazz = Article.class, props = {"comments","location","tags","author"})
            })
    @LogAnnotation(module = "文章", operation = "根据id获取文章")
    public Result getArticleById(@PathVariable("id") Long id) {

        Result r = new Result();

        if (null == id) {
            r.setResultCode(ResultCode.PARAM_IS_BLANK);
            return r;
        }

        Article article = articleService.getArticleById(id);
        r.setResultCode(ResultCode.SUCCESS);
        r.setData(article);
        return r;
    }

    @ApiIgnore
    @GetMapping("/view/{id}")
    @FastJsonView(
            exclude = {
                    @FastJsonFilter(clazz = Article.class, props = {"comments"}),
                    @FastJsonFilter(clazz = ArticleBody.class, props = {"contentHtml"})},
            include = {@FastJsonFilter(clazz = User.class, props = {"id", "name", "avatar"})})
    @LogAnnotation(module = "文章", operation = "根据id获取文章，添加阅读数")
    public Result getArticleAndAddViews(@PathVariable("id") Long id) {
        Result r = new Result();
        if (null == id) {
            r.setResultCode(ResultCode.PARAM_IS_BLANK);
            return r;
        }
        Article article = articleService.getArticleAndAddViews(id);
        r.setResultCode(ResultCode.SUCCESS);
        r.setData(article);
        return r;
    }

    @ApiIgnore
    @GetMapping("/category/{id}")
    @FastJsonView(
            exclude = {
                    @FastJsonFilter(clazz = Article.class, props = {"body", "category", "comments"})},
            include = {@FastJsonFilter(clazz = User.class, props = {"nickname"})})
    @LogAnnotation(module = "文章", operation = "根据分类获取文章")
    public Result listArticlesByCategory(@PathVariable Long id) {
        List<Article> articles = articleService.listArticlesByCategory(id);
        return Result.success(articles);
    }

    @ApiIgnore
    @PostMapping("/publish")
    @RequiresAuthentication
    @LogAnnotation(module = "文章", operation = "发布文章")
    public Result saveArticle(@Validated @RequestBody Article article) {
        Long articleId = articleService.publishArticle(article);
        Result r = Result.success();
        r.simple().put("articleId", articleId);
        return r;
    }

    @ApiIgnore
    @PostMapping("/update")
    @RequiresRoles(Base.ROLE_ADMIN)
    @LogAnnotation(module = "文章", operation = "修改文章")
    public Result updateArticle(@RequestBody Article article) {
        Result r = new Result();
        if (null == article.getId()) {
            r.setResultCode(ResultCode.USER_NOT_EXIST);
            return r;
        }
        Long articleId = articleService.updateArticle(article);

        r.setResultCode(ResultCode.SUCCESS);
        r.simple().put("articleId", articleId);
        return r;
    }

    @ApiIgnore
    @GetMapping("/delete/{id}")
    @RequiresRoles(Base.ROLE_ADMIN)
    @LogAnnotation(module = "文章", operation = "删除文章")
    public Result deleteArticleById(@PathVariable("id") Long id) {
        Result r = new Result();
        if (null == id) {
            r.setResultCode(ResultCode.PARAM_IS_BLANK);
            return r;
        }
        articleService.deleteArticleById(id);

        r.setResultCode(ResultCode.SUCCESS);
        return r;
    }

}
