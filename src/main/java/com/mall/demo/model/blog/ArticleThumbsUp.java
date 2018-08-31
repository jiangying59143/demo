package com.mall.demo.model.blog;

import com.mall.demo.model.base.BaseTO;
import com.mall.demo.model.privilege.User;

import javax.persistence.*;

/**
 * 踩
 */
@Entity
public class ArticleThumbsUp extends BaseTO {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "thumb_up_article_id")
    private Article article;

    private int orderCount;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    public int getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(int orderCount) {
        this.orderCount = orderCount;
    }
}
