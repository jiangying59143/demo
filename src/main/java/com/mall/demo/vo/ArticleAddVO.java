package com.mall.demo.vo;

import com.mall.demo.model.blog.*;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ArticleAddVO {
    @ApiModelProperty(value="置顶 1:否 2:是",name="weight")
    private Short weight;

    @ApiModelProperty(value="权限 1:公开 2: 个人",name="privilege")
    private Short privilege;

    @ApiModelProperty(value="文章类型 [1:内容+图片][2. 标题+视频地址][3: 图文html][4: 图+内容 list]",name="articleType")
    private Short articleType;

    @ApiModelProperty(value="标题",name="title")
    private String title;

    @ApiModelProperty(value="内容",name="content")
    private String content;

    @ApiModelProperty(value="内容list",name="content")
    private List<String> contentList;

    @ApiModelProperty(value="主题主键list",name="categoryList")
    private List<Long> categoryList = new ArrayList<>();

    @ApiModelProperty(value="位置信息",name="location")
    private Location location;


    public Short getWeight() {
        return weight;
    }

    public void setWeight(Short weight) {
        this.weight = weight;
    }

    public Short getPrivilege() {
        return privilege;
    }

    public void setPrivilege(Short privilege) {
        this.privilege = privilege;
    }

    public Short getArticleType() {
        return articleType;
    }

    public void setArticleType(Short articleType) {
        this.articleType = articleType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<Long> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<Long> categoryList) {
        this.categoryList = categoryList;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public List<String> getContentList() {
        return contentList;
    }

    public void setContentList(List<String> contentList) {
        this.contentList = contentList;
    }

    public List<Category> getCategoryCollection(){
        if(BooleanUtils.and(new boolean[]{categoryList != null, categoryList.size() > 0})){
            List<Category> list = categoryList.stream().map(c-> {return new Category(c);}).collect(Collectors.toList());
            return list;
        }
        return null;
    }

    public Article getArticle(){
        Article article = new Article();
        article.setWeight(this.weight);
        article.setPrivilege(this.privilege);
        article.setTitle(this.title);
        article.setArticleType(this.articleType);
        article.setCategoryList(this.getCategoryCollection());
        article.setLocation(this.location);
        if(this.articleType == Article.ARTICLE_TYPE_IMAGE_ARTICLE){
            ArticleBody1 articleBody1 = new ArticleBody1(content);
        }else if(this.articleType == Article.ARTICLE_TYPE_HTML){
            ArticleBody3 articleBody3 = new ArticleBody3(content);
        }
        article.setArticleViewCount(new ArticleViewCount(0L));
        article.setArticleThumbsUpCount(new ArticleThumbsUpCount(0L));
        article.setArticleThumbsDownCount(new ArticleThumbsDownCount(0L));
        article.setArticleCommentCount(new CommentCount(0L));
        return article;
    }

    public String checkParamsEmpty(){
        if(weight == null){
            return "weight is null or type is wrong";
        }else if(privilege == null){
            return "privilege is null or type is wrong";
        }else if(articleType == null){
            return "articleType is null or type is wrong";
        }
        return "";
    }
}
