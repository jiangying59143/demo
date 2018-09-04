package com.mall.demo.rest.controller;

import com.alibaba.fastjson.support.spring.annotation.FastJsonFilter;
import com.alibaba.fastjson.support.spring.annotation.FastJsonView;
import com.google.common.collect.Lists;
import com.mall.demo.common.annotation.LogAnnotation;
import com.mall.demo.common.constants.Base;
import com.mall.demo.common.constants.ResultCode;
import com.mall.demo.common.result.Result;
import com.mall.demo.common.utils.FileUtils;
import com.mall.demo.common.utils.StringUtils;
import com.mall.demo.common.utils.UserUtils;
import com.mall.demo.model.blog.*;
import com.mall.demo.model.privilege.User;
import com.mall.demo.service.ArticleService;
import com.mall.demo.vo.ArticleAddVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

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

    @ApiOperation(value="添加文章", notes="添加文章")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "令牌", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "files", value = "图片文件list", required = true, dataType = "file", paramType = "form")
    })
    @PostMapping("/addContentAndImages")
    @LogAnnotation(module = "添加内容和图片", operation = "添加内容和图片")
    public Result addContentAndImages(@RequestBody ArticleAddVO articleAddVO, @RequestParam("files") MultipartFile[] files){
        Result r = new Result();
        String paramError = articleAddVO.checkParamsEmpty();
        if (BooleanUtils.isFalse(StringUtils.isEmpty(paramError))) {
            r.setResultCode(ResultCode.PARAM_IS_INVALID);
            return r;
        }
        User currentUser = UserUtils.getCurrentUser();
        Article article = articleAddVO.getArticle();
        List<MultipartFile> fileList = Arrays.asList(files);
        if(!CollectionUtils.isEmpty(fileList)) {
            List<ArticleImage> articleImages = null;
            List<ArticleBody4> articleBody4s = null;
            for (int i=0; i< fileList.size(); i++) {
                MultipartFile file = fileList.get(i);
                if(file != null) {
                    String fileName = UUID.randomUUID().toString();
                    boolean flag = FileUtils.singleFileUpload(file, currentUser.getId(), fileName);
                    if (!flag) {
                        FileUtils.deleteArticleTempFolder(currentUser.getId());
                        r.setMsg(file.getOriginalFilename());
                        r.setResultCode(ResultCode.UPLOAD_ERROR);
                        return r;
                    }
                    if(article.getArticleType() == Article.ARTICLE_TYPE_IMAGE_ARTICLE || article.getArticleType() == Article.ARTICLE_TYPE_HTML) {
                        if(articleImages==null){articleImages = new ArrayList<>();}
                        ArticleImage articleImage = new ArticleImage();
                        articleImage.setArticle(article);
                        articleImage.setOrderCount(i);
                        articleImage.setUrl(fileName);
                        articleImages.add(articleImage);
                    }else if(article.getArticleType() == Article.ARTICLE_TYPE_VEDIO){
                        ArticleVideoBody articleVideoBody = new ArticleVideoBody(fileName);
                        article.setArticleVideoBody(articleVideoBody);
                    }else if (article.getArticleType() == Article.ARTICLE_TYPE_IMAG_CONTENT_LIST){
                        if(BooleanUtils.and(new boolean[]{articleAddVO.getContentList() != null && articleAddVO.getContentList().size() <= files.length })) {
                            ArticleBody4 articleBody4 = new ArticleBody4();
                            ArticleImage articleImage = new ArticleImage();
                            articleImage.setArticle(article);
                            articleImage.setOrderCount(i);
                            articleImage.setUrl(fileName);
                            articleBody4.setArticleImage(articleImage);
                            articleBody4.setContent(StringUtils.isEmpty(articleAddVO.getContentList().get(i)) ? "" : articleAddVO.getContentList().get(i));
                            articleBody4.setArticle(article);
                            articleBody4s.add(articleBody4);
                        }else{
                            r.setResultCode(ResultCode.PARAM_IS_INVALID);
                            return r;
                        }
                    }
                }
            }
            article.setArticleImages(articleImages);
            article.setArticleBody4(articleBody4s);
        }
        article.setAuthor(currentUser);
        Long articleId = articleService.saveArticle(article);

        if(!FileUtils.renameArticleTempFolder(currentUser.getId(), articleId)){
            articleService.deleteArticleById(articleId);
            r.setResultCode(ResultCode.UPLOAD_ERROR);
            return r;
        }
        return Result.success();
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
                    @FastJsonFilter(clazz = ArticleBody4.class, props = {"contentHtml"})},
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
