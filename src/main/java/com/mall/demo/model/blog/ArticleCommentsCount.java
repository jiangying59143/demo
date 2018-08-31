package com.mall.demo.model.blog;


import com.mall.demo.model.base.BaseEntity;

import javax.persistence.Entity;

/**
 * 评论量
 */
@Entity
public class ArticleCommentsCount extends BaseEntity<Long> {

     //评论量
    private Long commentsCount;

    public Long getCommentsCount() {
        return commentsCount;
    }

    public void setCommentsCount(Long commentsCount) {
        this.commentsCount = commentsCount;
    }
}
