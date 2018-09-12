package com.mall.demo.model.blog;

import com.mall.demo.model.base.BaseEntity;

import javax.persistence.*;

@Entity
public class ArticleVideoBody extends BaseEntity<Long> {
    public ArticleVideoBody(String fileName) {
        this.fileName = fileName;
    }

    public ArticleVideoBody() {
    }


    @Lob
    @Basic(fetch = FetchType.LAZY)
    private String fileName;

    @Transient
    private String path;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
