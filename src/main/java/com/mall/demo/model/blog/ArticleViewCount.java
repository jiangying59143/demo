package com.mall.demo.model.blog;


import com.mall.demo.model.base.BaseEntity;
import com.mall.demo.model.base.BaseTO;

import javax.persistence.Entity;

/**
 * 阅读量
 */
@Entity
public class ArticleViewCount extends BaseEntity<Long> {

     //阅读量
    private Long viewCount;

    public Long getViewCount() {
        return viewCount;
    }

    public void setViewCount(Long viewCount) {
        this.viewCount = viewCount;
    }
}
