package com.mall.demo.model.blog;


import com.mall.demo.model.base.BaseEntity;

import javax.persistence.Entity;

/**
 * 踩
 */
@Entity
public class ArticleThumbsDownCount extends BaseEntity<Long> {

    //踩数
    private Long thumbsDownCount;

    public Long getThumbsDownCount() {
        return thumbsDownCount;
    }

    public void setThumbsDownCount(Long thumbsDownCount) {
        this.thumbsDownCount = thumbsDownCount;
    }
}
