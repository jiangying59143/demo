package com.mall.demo.model.blog;


import com.mall.demo.model.base.BaseEntity;

import javax.persistence.Entity;

/**
 * 阅读量
 */
@Entity
public class CommentCount extends BaseEntity<Long> {

    public CommentCount() {
    }

    public CommentCount(Long commentCount) {
        this.commentCount = commentCount;
    }

    //阅读量
    private Long commentCount;

    public Long getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(Long commentCount) {
        this.commentCount = commentCount;
    }
}
