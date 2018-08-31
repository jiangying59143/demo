package com.mall.demo.model.blog;

import com.mall.demo.model.base.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class ArticleBodyImageVedio extends BaseEntity<Long> {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_body_id")
    private ArticleBody articleBody;

    private String url;

    //排序号
    private int orderCount;

    public ArticleBody getArticleBody() {
        return articleBody;
    }

    public void setArticleBody(ArticleBody articleBody) {
        this.articleBody = articleBody;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(int orderCount) {
        this.orderCount = orderCount;
    }
}
