package com.mall.demo.model.blog;


import com.mall.demo.model.base.BaseEntity;

import javax.persistence.Entity;

/**
 * 点赞
 */

@Entity
public class ArticleThumbsUpCount extends BaseEntity<Long> {

    //赞数
    private Long thumbsUpCount;

    public Long getThumbsUpCount() {
        return thumbsUpCount;
    }

    public void setThumbsUpCount(Long thumbsUpCount) {
        this.thumbsUpCount = thumbsUpCount;
    }
}
