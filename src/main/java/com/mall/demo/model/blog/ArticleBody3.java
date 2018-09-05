package com.mall.demo.model.blog;

import com.mall.demo.model.base.BaseEntity;
import org.hibernate.annotations.Type;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;

@Entity
public class ArticleBody3 extends BaseEntity<Long> {

    public ArticleBody3() {
    }

    public ArticleBody3(String content) {
        this.content = content;
    }

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Type(type = "text")
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
