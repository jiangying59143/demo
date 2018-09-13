package com.mall.demo.model.blog;

import com.mall.demo.common.utils.DateUtils;
import com.mall.demo.common.utils.UserUtils;
import com.mall.demo.model.base.BaseTO;
import com.mall.demo.model.privilege.User;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.time.DateFormatUtils;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.List;

@Entity
public class Article extends BaseTO {

    public Article() {
    }

    public Article(Long id) {
        this.id = id;
    }

    //置顶
    public static final short Article_TOP = 1;

    //不置顶
    public static final short Article_Common = 2;

    //文章公开
    public static final short ARTICLE_PUBLIC = 1;

    //文章不公开
    public static final short ARTICLE_PPRIVATE = 2;

    //内容+图片
    public static final short ARTICLE_TYPE_IMAGE_ARTICLE = 1;

    //视频 返回视频url
    public static final short ARTICLE_TYPE_VEDIO = 2;

    //普通 返回文章+图片URL List
    public static final short ARTICLE_TYPE_HTML= 3;

    public static final short ARTICLE_TYPE_IMAG_CONTENT_LIST= 4;

    //置顶
    @ApiModelProperty(value="置顶 1:否 2:是",name="weight")
    private short weight = Article_Common;

    //文章权限
    @ApiModelProperty(value="权限 1:公开 2: 个人",name="privilege")
    private short privilege = ARTICLE_PUBLIC;

    /**
     * 1. 普通 返回文章+图片URL List
     * 2. 图文 返回html
     * 3. 视频 返回视频url
     */
    @ApiModelProperty(value="文章类型 [1:内容+图片][2. 标题+视频地址][3: 图文html][4: 图+内容 list]",name="articleType")
    private short articleType = ARTICLE_TYPE_IMAGE_ARTICLE;

    @ApiModelProperty(value="标题",name="title")
    @NotBlank
    @Column(name = "title", length = 2000)
    private String title;


    @ApiModelProperty(value="[1:内容+图片]",name="articleBody1")
    @OneToOne(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "article_Body1_Id")
    private ArticleBody1 articleBody1;

    @ApiModelProperty(value="[2. 标题+视频地址]",name="articleVideoBody")
    @OneToOne(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "article_video_body_Id")
    private ArticleVideoBody articleVideoBody;

    @ApiModelProperty(value="[3: 图文html]",name="contentBody")
    @OneToOne(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "article_Body3_Id")
    private ArticleBody3 articleBody3;

    @ApiModelProperty(value="[4: 图+内容 list]",name="articleBody4")
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "article", orphanRemoval = true)
    private List<ArticleBody4> articleBody4;

    @ApiModelProperty(value="文章图片",name="articleImages")
    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER, mappedBy = "article", orphanRemoval = true)
    private List<ArticleImage> articleImages;

    @ApiModelProperty(value="作者(用户)",name="author")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_user_id")
    private User author;

    @ApiModelProperty(value="类别",name="categoryList")
    @ManyToMany(fetch= FetchType.LAZY)
    @JoinTable(name = "articleCategory", joinColumns = { @JoinColumn(name = "articleId") }, inverseJoinColumns ={@JoinColumn(name = "categoryId") })
    private List<Category> categoryList;

    @ApiModelProperty(value="位置信息",name="location")
    //如果orphanRemoval = true 删除对象，false只删除关系
    @OneToOne(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "location_id")
    private Location location;

    @ApiModelProperty(value="评论",name="comments")
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "article", orphanRemoval = true)
    private List<Comment> comments;

    //赞
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "article", orphanRemoval = true)
    private List<ArticleThumbsUp> articleThumbsUps;

    //踩
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "article", orphanRemoval = true)
    private List<ArticleThumbsDown> articleThumbsDowns;

    //阅读量表
    @OneToOne(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "viewCount_id")
    private ArticleViewCount articleViewCount;

    //赞数表
    @OneToOne(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "thumbsUpCount_id")
    private ArticleThumbsUpCount articleThumbsUpCount;

    //踩数表
    @OneToOne(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "thumbsDownCount_id")
    private ArticleThumbsDownCount articleThumbsDownCount;

    //评论数表
    @OneToOne(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "commentsCount_id")
    private CommentCount articleCommentCount;

    @ApiModelProperty(value="阅读量",name="viewCount")
    @Transient
    private long viewCount;

    @ApiModelProperty(value="评论数",name="commentCount")
    @Transient
    private long commentCount;

    @ApiModelProperty(value="赞数",name="ThumbsUpCount")
    @Transient
    private long thumbsUpCount;

    @ApiModelProperty(value="踩数",name="ThumbsDownCount")
    @Transient
    private long thumbsDownCount;

    @ApiModelProperty(value="黑名单",name="comments")
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "article", orphanRemoval = true)
    private List<ArticleBlackList> articleBlackLists;

    @Transient
    private String time;

    public ArticleBody1 getArticleBody1() {
        return articleBody1;
    }

    public void setArticleBody1(ArticleBody1 articleBody1) {
        this.articleBody1 = articleBody1;
    }

    public ArticleVideoBody getArticleVideoBody() {
        return articleVideoBody;
    }

    public void setArticleVideoBody(ArticleVideoBody articleVideoBody) {
        this.articleVideoBody = articleVideoBody;
    }

    public ArticleBody3 getArticleBody3() {
        return articleBody3;
    }

    public void setArticleBody3(ArticleBody3 articleBody3) {
        this.articleBody3 = articleBody3;
    }

    public List<ArticleBody4> getArticleBody4() {
        return articleBody4;
    }

    public void setArticleBody4(List<ArticleBody4> articleBody4) {
        this.articleBody4 = articleBody4;
    }

    public List<ArticleImage> getArticleImages() {
        return articleImages;
    }

    public void setArticleImages(List<ArticleImage> articleImages) {
        this.articleImages = articleImages;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Category> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<Category> categoryList) {
        this.categoryList = categoryList;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public short getWeight() {
        return weight;
    }

    public void setWeight(short weight) {
        this.weight = weight;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public short getPrivilege() {
        return privilege;
    }

    public void setPrivilege(short privilege) {
        this.privilege = privilege;
    }

    public short getArticleType() {
        return articleType;
    }

    public void setArticleType(short articleType) {
        this.articleType = articleType;
    }

    public ArticleViewCount getArticleViewCount() {
        return articleViewCount;
    }

    public void setArticleViewCount(ArticleViewCount articleViewCount) {
        this.articleViewCount = articleViewCount;
    }

    public ArticleThumbsUpCount getArticleThumbsUpCount() {
        return articleThumbsUpCount;
    }

    public void setArticleThumbsUpCount(ArticleThumbsUpCount articleThumbsUpCount) {
        this.articleThumbsUpCount = articleThumbsUpCount;
    }

    public ArticleThumbsDownCount getArticleThumbsDownCount() {
        return articleThumbsDownCount;
    }

    public void setArticleThumbsDownCount(ArticleThumbsDownCount articleThumbsDownCount) {
        this.articleThumbsDownCount = articleThumbsDownCount;
    }

    public List<ArticleThumbsUp> getArticleThumbsUps() {
        return articleThumbsUps;
    }

    public void setArticleThumbsUps(List<ArticleThumbsUp> articleThumbsUps) {
        this.articleThumbsUps = articleThumbsUps;
    }

    public List<ArticleThumbsDown> getArticleThumbsDowns() {
        return articleThumbsDowns;
    }

    public void setArticleThumbsDowns(List<ArticleThumbsDown> articleThumbsDowns) {
        this.articleThumbsDowns = articleThumbsDowns;
    }

    public CommentCount getArticleCommentCount() {
        return articleCommentCount;
    }

    public void setArticleCommentCount(CommentCount articleCommentCount) {
        this.articleCommentCount = articleCommentCount;
    }

    public long getViewCount() {
        if(articleViewCount != null){
            viewCount = articleViewCount.getViewCount();
        }
        return viewCount;
    }

    public long getCommentCount() {
        if(articleCommentCount != null){
            commentCount = articleCommentCount.getCommentCount();
        }
        return commentCount;
    }

    public long getThumbsUpCount() {
        if(articleThumbsUpCount != null){
            thumbsUpCount = articleThumbsUpCount.getThumbsUpCount();
        }
        return thumbsUpCount;
    }

    public long getThumbsDownCount() {
        if(articleThumbsDownCount != null){
            thumbsDownCount = articleThumbsDownCount.getThumbsDownCount();
        }
        return thumbsDownCount;
    }

    public List<ArticleBlackList> getArticleBlackLists() {
        return articleBlackLists;
    }

    public void setArticleBlackLists(List<ArticleBlackList> articleBlackLists) {
        this.articleBlackLists = articleBlackLists;
    }

    public String getTime() {
        time = DateFormatUtils.format(this.createDate, DateUtils.DATE_TIME_TO_MINUTE);
        return time;
    }
}
