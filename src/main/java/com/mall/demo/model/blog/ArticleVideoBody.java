package com.mall.demo.model.blog;

import com.mall.demo.model.base.BaseEntity;
import org.hibernate.annotations.Type;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;

@Entity
public class ArticleVideoBody extends BaseEntity<Long> {
    public ArticleVideoBody(String url) {
        this.url = url;
    }

    public ArticleVideoBody() {
    }

    @Lob
    @Basic(fetch = FetchType.LAZY)
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
