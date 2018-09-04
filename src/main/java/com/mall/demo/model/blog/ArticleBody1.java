package com.mall.demo.model.blog;

import com.mall.demo.model.base.BaseEntity;
import org.hibernate.annotations.Type;

import javax.persistence.*;

@Entity
public class ArticleBody1 extends BaseEntity<Long> {
    public ArticleBody1(String content) {
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
