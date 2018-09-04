package com.mall.demo.model.blog;

import com.mall.demo.model.base.BaseTO;
import com.mall.demo.model.privilege.User;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Entity
public class Article extends BaseTO {

    //置顶
    public static final short Article_TOP = 1;

    //不置顶
    public static final short Article_Common = 2;

    //文章公开
    public static final short ARTICLE_PUBLIC = 1;

    //文章不公开
    public static final short ARTICLE_PPRIVATE = 2;

    //普通 返回文章+图片URL List
    public static final short ARTICLE_TYPE_COMMON = 1;

    //视频 返回视频url
    public static final short ARTICLE_TYPE_VEDIO = 2;

    //外部分享
    public static final short ARTICLE_TYPE_IMAGE_ARTICLE = 3;

    //置顶
    @ApiModelProperty(value="置顶 0:否 1:是",name="weight")
    private short weight = Article_Common;

    //文章权限
    @ApiModelProperty(value="权限 1:公开 2: 个人",name="privilege")
    private short privilege = ARTICLE_PUBLIC;

    /**
     * 1. 普通 返回文章+图片URL List
     * 2. 图文 返回html
     * 3. 视频 返回视频url
     */
    @ApiModelProperty(value="文章类型 [1:内容+图片][2. 标题+视频地址][3: 外部转载]",name="articleType")
    private short articleType = ARTICLE_TYPE_COMMON;

    @ApiModelProperty(value="标题",name="title")
    @NotBlank
    @Column(name = "title", length = 40)
    private String title;


    @ApiModelProperty(value="文章内容",name="body")
    @OneToOne(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "articleBoyId")
    private ArticleBody articleBody;

    @ApiModelProperty(value="作者(用户)",name="author")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_user_id")
    private User author;

    @ApiModelProperty(value="类别",name="category")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @ApiModelProperty(value="位置信息",name="location")
    //如果orphanRemoval = true 删除对象，false只删除关系
    @OneToOne(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY, orphanRemoval = true)
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
    @OneToOne(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "viewCount_id")
    private ArticleViewCount articleViewCount;

    //赞数表
    @OneToOne(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "thumbsUpCount_id")
    private ArticleThumbsUpCount articleThumbsUpCount;

    //踩数表
    @OneToOne(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "thumbsDownCount_id")
    private ArticleThumbsDownCount articleThumbsDownCount;

    //评论数表
    @OneToOne(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY, orphanRemoval = true)
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArticleBody getArticleBody() {
        return articleBody;
    }

    public void setArticleBody(ArticleBody articleBody) {
        this.articleBody = articleBody;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
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


}
