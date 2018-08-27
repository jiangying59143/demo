package com.mall.demo.model.blog;

import com.alibaba.fastjson.annotation.JSONField;
import com.mall.demo.model.base.BaseTO;
import com.mall.demo.model.privilege.User;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.List;

@Entity
public class Comment extends BaseTO {
    @NotBlank
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_user_id")
    private User author;

    /**
     * 类型 0 文章的评论 1 评论的评论 2 评论的回复 @
     */
    @Column(name = "level",length = 1)
    private String level;

    /**
     * 创建时间
     */
    @JSONField(format = "yyyy.MM.dd HH:mm")
    @Column(name = "create_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id")
    private Article article;

    @OneToMany
    @JoinColumn(name = "parent_id",nullable = true)
    private List<Comment> childrens;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    @NotFound(action= NotFoundAction.IGNORE)
    private Comment parent;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_uid")
    private User toUser;

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

    public List<Comment> getChildrens() {
        return childrens;
    }

    public void setChildrens(List<Comment> childrens) {
        this.childrens = childrens;
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
        this.author = author;
    }

    public User getToUser() {
        return toUser;
    }

    public void setToUser(User toUser) {
        this.toUser = toUser;
    }
}
