package com.mall.demo.model.blog;

import com.alibaba.fastjson.annotation.JSONField;
import com.mall.demo.common.utils.DateUtils;
import com.mall.demo.common.utils.UserUtils;
import com.mall.demo.model.base.BaseTO;
import com.mall.demo.model.privilege.User;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.List;

@Entity
@ApiModel
public class Comment extends BaseTO {

    public Comment() {
    }

    public Comment(Long id) {
        this.id=id;
    }

    //内容
    @ApiModelProperty(required = true, example = "内容不错")
    @NotBlank(message="评论内容不能为空")
    private String content;

    /**
     * 类型 0 文章的评论 1 评论的评论 2 评论的回复 @
     */

    @ApiModelProperty(hidden = true)
    @Column(name = "level",length = 1)
    private String level;

    @ApiModelProperty(hidden = true)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_user_id")
    private User author;

    @ApiModelProperty(hidden = true)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id")
    private Article article;

    @ApiModelProperty(notes = "文章主键", example = "1")
    @Transient
    private Long articleId;

    @ApiModelProperty(hidden = true)
    @OneToMany
    @JoinColumn(name = "parent_id",nullable = true)
    private List<Comment> children;

    @ApiModelProperty(hidden = true)
    @ManyToOne
    @JoinColumn(name = "parent_id")
    @NotFound(action= NotFoundAction.IGNORE)
    private Comment parent;

    @ApiModelProperty(notes = "父评论主键", example = "1")
    @Transient
    private Long parentId;

    @ApiModelProperty(hidden = true)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_uid")
    private User toUser;

    @ApiModelProperty(notes = "被评论的用户主键", example = "2")
    @Transient
    @NotBlank(message="被评论用户必填")
    private Long toUid;

    public String getContent() {
        return content;
    }


    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }


    public Article getArticle() {
        return article;
    }


    public void setArticle(Article article) {
        this.article = article;
    }

    public List<Comment> getChildren() {
        return children;
    }

    public void setChildren(List<Comment> children) {
        this.children = children;
    }

    public Comment getParent() {
        return parent;
    }

    public void setParent(Comment parent) {
        this.parent = parent;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        User currentUser = UserUtils.getCurrentUser();
        if(currentUser != null){
            author = currentUser;
        }
        this.author = author;
    }

    public User getToUser() {
        return toUser;
    }

    public void setToUser(User toUser) {
        this.toUser = toUser;
    }

    @ApiModelProperty(hidden = true)
    public String getTime() {
        if(this.createDate != null) {
            return DateFormatUtils.format(this.createDate, DateUtils.DATE_TIME_TO_MINUTE);
        }
        return null;
    }

    public Long getArticleId() {
        return articleId;
    }

    public void setArticleId(Long articleId) {
        if(articleId != null){
            article = new Article(articleId);
        }
        this.articleId = articleId;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        if(parentId != null){
            parent = new Comment(parentId);
        }
        this.parentId = parentId;
    }

    public Long getToUid() {
        return toUid;
    }

    public void setToUid(Long toUid) {
        if(toUid != null){
            toUser = new User(toUid);
        }
        this.toUid = toUid;
    }
}
