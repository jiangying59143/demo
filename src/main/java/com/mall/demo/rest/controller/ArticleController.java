package com.mall.demo.rest.controller;

import com.alibaba.fastjson.support.spring.annotation.FastJsonFilter;
import com.alibaba.fastjson.support.spring.annotation.FastJsonView;
import com.google.common.collect.Lists;
import com.mall.demo.common.annotation.LogAnnotation;
import com.mall.demo.common.constants.Base;
import com.mall.demo.common.constants.ResultCode;
import com.mall.demo.common.result.Result;
import com.mall.demo.common.utils.FileUtils;
import com.mall.demo.common.utils.HttpUtils;
import com.mall.demo.common.utils.StringUtils;
import com.mall.demo.common.utils.UserUtils;
import com.mall.demo.model.blog.*;
import com.mall.demo.model.privilege.User;
import com.mall.demo.service.ArticleBlackService;
import com.mall.demo.service.ArticleBody4Service;
import com.mall.demo.service.ArticleService;
import com.mall.demo.vo.ArticleAddTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
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

    @Autowired
    private ArticleBody4Service articleBody4Service;

    @Autowired
    private ArticleBlackService articleBlackService;

    @Autowired
    private HttpServletRequest request;

    @Value("${me.upload.path}")
    private String baseFolderPath;

    @ApiOperation(value="添加搜索历史", notes="添加搜索历史")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Oauth-Token", value = "令牌", required = true, dataType = "String", paramType = "header")
    })
    @PostMapping("/addHistory")
    @LogAnnotation(module = "添加搜索历史", operation = "添加搜索历史")
    public Result addSearchHistory(@RequestParam String content){
        User currentUser = UserUtils.getCurrentUser();
        articleService.addSearchHistory(currentUser, content);
        return Result.success();
    }

    @ApiOperation(value="搜索历史", notes="搜索历史")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Oauth-Token", value = "令牌", required = true, dataType = "String", paramType = "header")
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
    @ApiOperation(value="添加文章（支持postmanc测试）", notes="添加文章（支持postmanc测试）")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Oauth-Token", value = "令牌", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "files", value = "图片文件list", required = true, dataType = "file", paramType = "form")
    })
    @PostMapping("/create")
    @LogAnnotation(module = "添加内容和图片", operation = "添加内容和图片")
    public Result addContentAndImages(@RequestBody ArticleAddTO articleAddTO, @RequestParam("files") MultipartFile[] files){
        Result r = new Result();
        String paramError = articleAddTO.checkParamsEmpty();
        if (BooleanUtils.isFalse(StringUtils.isEmpty(paramError))) {
            r.setResultCode(ResultCode.PARAM_IS_INVALID);
            return r;
        }
        User currentUser = UserUtils.getCurrentUser();
        Article article = articleAddTO.achieveArticle();
        List<MultipartFile> fileList = Arrays.asList(files);
        if(!CollectionUtils.isEmpty(fileList)) {
            List<ArticleImage> articleImages = null;
            List<ArticleBody4> articleBody4s = null;
            for (int i=0; i< fileList.size(); i++) {
                MultipartFile file = fileList.get(i);
                if(file != null) {
                    String fileName = UUID.randomUUID().toString();
                    boolean flag = FileUtils.singleFileUpload(file, baseFolderPath, FileUtils.UPLOAD_FILE_IMAGE, currentUser.getId(), fileName);
                    if (!flag) {
                        FileUtils.deleteArticleTempFolder(baseFolderPath, FileUtils.UPLOAD_FILE_IMAGE,currentUser.getId(), fileName);
                        r.setMsg(file.getOriginalFilename());
                        r.setResultCode(ResultCode.UPLOAD_ERROR);
                        return r;
                    }
                    if(article.getArticleType() == Article.ARTICLE_TYPE_IMAGE_ARTICLE || article.getArticleType() == Article.ARTICLE_TYPE_HTML) {
                        if(articleImages==null){articleImages = new ArrayList<>();}
                        ArticleImage articleImage = new ArticleImage();
                        articleImage.setArticle(article);
                        articleImage.setOrderCount(i);
                        articleImage.setFileName(fileName);
                        articleImages.add(articleImage);
                    }else if(article.getArticleType() == Article.ARTICLE_TYPE_VEDIO){
                        ArticleVideoBody articleVideoBody = new ArticleVideoBody(fileName);
                        article.setArticleVideoBody(articleVideoBody);
                    }else if (article.getArticleType() == Article.ARTICLE_TYPE_IMAG_CONTENT_LIST){
                        if(BooleanUtils.and(new boolean[]{articleAddTO.getContentList() != null && articleAddTO.getContentList().size() <= files.length })) {
                            ArticleBody4 articleBody4 = new ArticleBody4();
                            ArticleImage articleImage = new ArticleImage();
                            articleImage.setArticle(article);
                            articleImage.setOrderCount(i);
                            articleImage.setFileName(fileName);
                            articleBody4.setArticleImage(articleImage);
                            articleBody4.setContent(StringUtils.isEmpty(articleAddTO.getContentList().get(i)) ? "" : articleAddTO.getContentList().get(i));
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

//        if(!FileUtils.renameArticleTempFolder(currentUser.getId(), articleId)){
//            articleService.deleteArticleById(articleId);
//            r.setResultCode(ResultCode.UPLOAD_ERROR);
//            return r;
//        }
        return Result.success();
    }

    private Article getArticle(User user, Long categoryId, String title, String content, Short contentType){
        Article article = new Article();
        article.setWeight(Article.Article_Common);
        article.setPrivilege(Article.ARTICLE_PUBLIC);
        article.setTitle(title);
        article.setArticleType(contentType);
        article.setCategoryList(Arrays.asList(new Category(categoryId)));
        if(Article.ARTICLE_TYPE_IMAGE_ARTICLE == contentType) {
            article.setArticleBody1(new ArticleBody1(content));
        }else if(Article.ARTICLE_TYPE_VEDIO== contentType){
            article.setArticleVideoBody(new ArticleVideoBody(content));
        }else if(Article.ARTICLE_TYPE_HTML==contentType){
            article.setArticleBody3(new ArticleBody3(content));
        }
        article.setArticleViewCount(new ArticleViewCount(0L));
        article.setArticleThumbsUpCount(new ArticleThumbsUpCount(0L));
        article.setArticleThumbsDownCount(new ArticleThumbsDownCount(0L));
        article.setArticleCommentCount(new CommentCount(0L));
        //@TODO 位置
        article.setAuthor(user);
        return article;
    }

    @ApiOperation(value="添加图片文章", notes="添加图片文章")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Oauth-Token", value = "令牌", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "categoryId", value = "主题ID", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "title", value = "文章标题", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "files", value = "图片文件", dataType = "file[]", paramType = "form")
    })
    @PostMapping("/createPicture")
    @LogAnnotation(module = "添加图片文章", operation = "添加图片文章")
    public Result addVideo(Long categoryId, String title, String content, MultipartFile[] files){
        Result r = new Result();
        if (BooleanUtils.isTrue(StringUtils.isEmpty(title))) {
            r.setResultCode(ResultCode.PARAM_IS_INVALID);
            return r;
        }
        User currentUser = UserUtils.getCurrentUser();
        Article article = getArticle(currentUser, categoryId, title, content, Article.ARTICLE_TYPE_IMAGE_ARTICLE);
        List<MultipartFile> fileList = Arrays.asList(files);
        if(!CollectionUtils.isEmpty(fileList)) {
            List<ArticleImage> articleImages = null;
            for (int i=0; i< fileList.size(); i++) {
                MultipartFile file = fileList.get(i);
                if(file != null) {
                    String fileName = String.valueOf(System.currentTimeMillis()) + "." + file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")+1);
                    boolean flag = FileUtils.singleFileUpload(file,baseFolderPath, FileUtils.UPLOAD_FILE_IMAGE, currentUser.getId(), fileName);
                    if (!flag) {
                        r.setResultCode(ResultCode.UPLOAD_ERROR);
                        return r;
                    }
                    if(articleImages==null){articleImages = new ArrayList<>();}
                    ArticleImage articleImage = new ArticleImage();
                    articleImage.setArticle(article);
                    articleImage.setOrderCount(i);
                    articleImage.setFileName(fileName);
                    articleImages.add(articleImage);
                }
            }
            article.setArticleImages(articleImages);
        }
        Long articleId = articleService.saveArticle(article);
        return Result.success(articleId);
    }

    @ApiOperation(value="添加VIDEO文章", notes="添加VIDEO文章")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Oauth-Token", value = "令牌", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "categoryId", value = "主题ID", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "title", value = "文章标题", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "videoFile", value = "视频文件", required = true, dataType = "file", paramType = "form"),
            @ApiImplicitParam(name = "imageFile", value = "视频图片", required = true, dataType = "file", paramType = "form")
    })
    @PostMapping("/createVideo")
    @LogAnnotation(module = "添加VIDEO文章", operation = "添加VIDEO文章")
    public Result addVideo(Long categoryId, String title, MultipartFile videoFile, MultipartFile imageFile){
        Result r = new Result();
        if (BooleanUtils.isTrue(StringUtils.isEmpty(title))) {
            r.setResultCode(ResultCode.PARAM_IS_INVALID);
            return r;
        }
        User currentUser = UserUtils.getCurrentUser();
        String fileName = String.valueOf(System.currentTimeMillis()) + "." + videoFile.getOriginalFilename().substring(videoFile.getOriginalFilename().lastIndexOf(".")+1);
        boolean flag = FileUtils.singleFileUpload(videoFile,baseFolderPath, FileUtils.UPLOAD_FILE_VIDEO, currentUser.getId(), fileName);
        Article article = getArticle(currentUser, categoryId, title, fileName, Article.ARTICLE_TYPE_VEDIO);

        String fileName1 = String.valueOf(System.currentTimeMillis()) + "." + imageFile.getOriginalFilename().substring(imageFile.getOriginalFilename().lastIndexOf(".")+1);
        boolean flag1 = FileUtils.singleFileUpload(imageFile,baseFolderPath, FileUtils.UPLOAD_FILE_IMAGE, currentUser.getId(), fileName);
        if (!flag) {
            r.setResultCode(ResultCode.UPLOAD_ERROR);
            return r;
        }
        ArticleImage articleImage = new ArticleImage();
        articleImage.setArticle(article);
        articleImage.setOrderCount(0);
        articleImage.setFileName(fileName1);
        article.setArticleImages(Arrays.asList(articleImage));
        if(flag) {
            Long articleId = articleService.saveArticle(article);
            return Result.success(articleId);
        }

        return Result.error(ResultCode.ERROR);
    }

    @ApiOperation(value="添加轮播文章", notes="添加轮播文章")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Oauth-Token", value = "令牌", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "categoryId", value = "主题ID", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "title", value = "文章标题", required = true, dataType = "String", paramType = "form"),
    })
    @PostMapping("/createCarousel")
    @LogAnnotation(module = "添加轮播文章", operation = "添加轮播文章")
    public Result addCarousel(Long categoryId, String title){
        Result r = new Result();
        if (BooleanUtils.isTrue(StringUtils.isEmpty(title))) {
            r.setResultCode(ResultCode.PARAM_IS_INVALID);
            return r;
        }
        User currentUser = UserUtils.getCurrentUser();
        Article article = getArticle(currentUser, categoryId, title, null, Article.ARTICLE_TYPE_IMAG_CONTENT_LIST);
        Long articleId = articleService.saveArticle(article);
        return Result.success(articleId);
    }

    @ApiOperation(value="添加轮播文章单片", notes="添加轮播文章单片")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Oauth-Token", value = "令牌", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "articleId", value = "文章ID", required = true, dataType = "Long", paramType = "form"),
            @ApiImplicitParam(name = "content", value = "文章内容", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "file", value = "图片文件", required = true, dataType = "file", paramType = "form")
    })
    @PostMapping("/createCarouselPiece")
    @LogAnnotation(module = "添加轮播文章单片", operation = "添加轮播文章单片")
    public Result addCarouselPiece(Long articleId, String content, MultipartFile file){
        Result r = new Result();
        if(articleId == null || content == null || file==null){
            r.setResultCode(ResultCode.PARAM_IS_INVALID);
            return r;
        }

        if(articleService.getArticleById(articleId) == null){
            r.setResultCode(ResultCode.DATA_NOT_EXIST);
            return r;
        }
        User currentUser = UserUtils.getCurrentUser();
        String fileName = String.valueOf(System.currentTimeMillis()) + "." + file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);
        boolean flag = FileUtils.singleFileUpload(file, baseFolderPath, FileUtils.UPLOAD_FILE_IMAGE, currentUser.getId(), fileName);
        if (!flag) {
            r.setResultCode(ResultCode.UPLOAD_ERROR);
            return r;
        }
        ArticleImage articleImage = new ArticleImage();
        articleImage.setArticle(new Article(articleId));
        Integer orderCount = articleBody4Service.findMaxOrderArticleId(articleId);
        articleImage.setOrderCount(orderCount==null ? 0: orderCount+1);
        articleImage.setFileName(fileName);
        ArticleBody4 articleBody4 = new ArticleBody4();
        articleBody4.setArticle(new Article(articleId));
        articleBody4.setOrderCount(articleImage.getOrderCount());
        articleBody4.setArticleImage(articleImage);
        articleBody4.setContent(content);
        articleBody4 = articleBody4Service.save(articleBody4);
        return Result.success(articleBody4.getId());
    }

    @ApiOperation(value="添加HTML文章", notes="添加HTML文章")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Oauth-Token", value = "令牌", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "categoryId", value = "主题ID", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "title", value = "文章标题", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "content", value = "文章内容", required = true, dataType = "String", paramType = "form")
    })
    @PostMapping("/createHtml")
    @LogAnnotation(module = "添加HTML文章", operation = "添加HTML文章")
    public Result addHTML(Long categoryId, String title, String content){
        Result r = new Result();
        if (BooleanUtils.isTrue(StringUtils.isEmpty(title))) {
            r.setResultCode(ResultCode.PARAM_IS_INVALID);
            return r;
        }
        User currentUser = UserUtils.getCurrentUser();
        Article article = getArticle(currentUser, categoryId, title, content, Article.ARTICLE_TYPE_HTML);
        Long articleId = articleService.saveArticle(article);

        return Result.success(articleId);
    }

    private void operateUrlOfFile(List<Article> articles){
        if(BooleanUtils.and(new boolean[]{articles != null, articles.size() > 0})){
            for (Article article : articles) {
                operateUrlOfFile(article);
            }
        }
    }

    private void operateUrlOfFile(Article article){
        if(article.getArticleVideoBody() != null){
            article.getArticleVideoBody().setPath(HttpUtils.getSystemUrl(request,  FileUtils.UPLOAD_FILE_VIDEO, article.getAuthor(), article.getArticleVideoBody().getFileName()));
        }
        if(BooleanUtils.and(new boolean[]{article.getArticleImages() != null, article.getArticleImages().size() > 0})){
            for (ArticleImage articleImage : article.getArticleImages()) {
                articleImage.setPath(HttpUtils.getSystemUrl(request, FileUtils.UPLOAD_FILE_IMAGE, article.getAuthor(), articleImage.getFileName()));
            }
        }
    }

    @ApiOperation(value="添加文章到黑名单", notes="添加文章到黑名单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Oauth-Token", value = "令牌", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "articleId", value = "文章ID", required = true, dataType = "String", paramType = "form"),
    })
    @PostMapping("/addIntoBlack")
    @LogAnnotation(module = "添加文章到黑名单", operation = "添加文章到黑名单")
    public Result addIntoBlack(Long articleId){
        User currentUser = UserUtils.getCurrentUser();
        ArticleBlackList articleBlackList = new ArticleBlackList();
        articleBlackList.setArticle(new Article(articleId));
        articleBlackList.setUser(currentUser);
        articleBlackList = articleBlackService.save(articleBlackList);
        return Result.success(articleBlackList.getId());
    }

    @ApiOperation(value="获取一篇文章", notes="根据id获取文章-文章类型 [1:内容+图片 articleBody1][2. 标题+视频地址 articleVideoBody][3: 图文html articleBody3][4: 图+内容 list articleBody4]")
    @GetMapping("/item/{id}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "主键", required = true, dataType = "Long", paramType = "path"),
            @ApiImplicitParam(name = "Oauth-Token", value = "令牌", dataType = "String", paramType = "header")
    })
    @FastJsonView(
            include = {
                    @FastJsonFilter(clazz = Article.class, props = {"articleType","title","articleBody1", "articleVideoBody", "articleBody3", "articleBody4", "time",
                            "categoryList", "articleImages", "author","viewCount", "commentCount", "thumbsUpCount", "thumbsDownCount", "comments"}),
                    @FastJsonFilter(clazz = Location.class, props = {"location","latitude","longitude"}),
                    @FastJsonFilter(clazz = Comment.class, props = {"id", "content", "author", "time"}),
                    @FastJsonFilter(clazz = User.class, props = {"id","avatar", "nickname", "attentionFlag"})
            },
            exclude = {
                    @FastJsonFilter(clazz = ArticleImage.class, props = {"article"}),
                    @FastJsonFilter(clazz = ArticleBody4.class, props = {"article"})
            })
    @LogAnnotation(module = "文章", operation = "根据id获取文章")
    public Result getArticleById(@PathVariable("id") Long id) {
        Result r = new Result();
        if (null == id) {
            r.setResultCode(ResultCode.PARAM_IS_BLANK);
            return r;
        }
        Article article = articleService.getArticleById(id);
        if(article == null){
            r.setResultCode(ResultCode.RESULE_DATA_NONE);
            return r;
        }
        operateUrlOfFile(article);
        r.setResultCode(ResultCode.SUCCESS);
        r.setData(article);
        return r;
    }

    @ApiOperation(value="获取特定标题的文章", notes="获取特定标题的文章-文章类型 [1:内容+图片 articleBody1][2. 标题+视频地址 articleVideoBody][3: 图文html articleBody3][4: 图+内容 list articleBody4]")
    @GetMapping("/searchByCategory")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Oauth-Token", value = "令牌", dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "id", value = "主题主键", dataType = "Long", paramType = "query")
    })
    @FastJsonView(
            include = {
                    @FastJsonFilter(clazz = Article.class, props = {"id", "articleType","title", "time",
                            "categoryList", "articleImages", "author","viewCount", "commentCount", "thumbsUpCount", "thumbsDownCount"}),
                    @FastJsonFilter(clazz = Location.class, props = {"location","latitude","longitude"}),
                    @FastJsonFilter(clazz = User.class, props = {"id", "nickname"})
            },
            exclude = {
                    @FastJsonFilter(clazz = ArticleImage.class, props = {"article"}),
                    @FastJsonFilter(clazz = ArticleBody4.class, props = {"article"})
            })
    @LogAnnotation(module = "获取特定标题的文章", operation = "获取特定标题的文章")
    public Result getSpecialTypeArticles(Long id, String title) {
        Result r = new Result();
        List<Article> articles = articleService.listArticlesByCategory(id, title==null ? "":title );
        operateUrlOfFile(articles);
        r.setResultCode(ResultCode.SUCCESS);
        r.setData(articles);
        return r;
    }

    @ApiOperation(value="获取所有文章", notes="获取所有文章-文章类型 [1:内容+图片 articleBody1][2. 标题+视频地址 articleVideoBody][3: 图文html articleBody3][4: 图+内容 list articleBody4]")
    @GetMapping("/all")
    @FastJsonView(
            include = {
                    @FastJsonFilter(clazz = Article.class, props = {"id", "articleType","title", "time",
                            "categoryList", "articleImages", "author","viewCount", "commentCount", "thumbsUpCount", "thumbsDownCount"}),
                    @FastJsonFilter(clazz = Location.class, props = {"location","latitude","longitude"}),
                    @FastJsonFilter(clazz = User.class, props = {"id", "nickname"})
            },
            exclude = {
                    @FastJsonFilter(clazz = ArticleImage.class, props = {"article"}),
                    @FastJsonFilter(clazz = ArticleBody4.class, props = {"article"})
            })
    @LogAnnotation(module = "获取所有文章", operation = "获取所有文章")
    public Result getAllArticles() {
        Result r = new Result();
        List<Article> articles = articleService.findAll();
        operateUrlOfFile(articles);
        r.setResultCode(ResultCode.SUCCESS);
        r.setData(articles);
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
        List<Article> articles = articleService.listArticlesByCategory(id, null);
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
