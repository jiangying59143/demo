package com.mall.demo.model.blog;

import com.mall.demo.model.base.BaseEntity;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.List;

@Entity
public class ArticleBody extends BaseEntity<Long> {

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Type(type = "text")
    private String content;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "articleBody", orphanRemoval = true)
    private List<ArticleBodyImageVedio> articleBodyImageVedios;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<ArticleBodyImageVedio> getArticleBodyImageVedios() {
        return articleBodyImageVedios;
    }

    public void setArticleBodyImageVedios(List<ArticleBodyImageVedio> articleBodyImageVedios) {
        this.articleBodyImageVedios = articleBodyImageVedios;
    }
}
