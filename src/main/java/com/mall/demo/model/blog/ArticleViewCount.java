package com.mall.demo.model.blog;


import com.mall.demo.model.base.BaseEntity;
import com.mall.demo.model.base.BaseTO;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

/**
 * 阅读量
 */
@Entity
public class ArticleViewCount extends BaseEntity<Long> {

    public ArticleViewCount() {
    }

    public ArticleViewCount(Long viewCount) {
        this.viewCount = viewCount;
    }

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id")
    private Article article;

     //阅读量
    private Long viewCount;

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    public Long getViewCount() {
        return viewCount;
    }

    public void setViewCount(Long viewCount) {
        this.viewCount = viewCount;
    }
}
