package com.mall.demo.model.blog;

import com.mall.demo.model.base.BaseEntity;
import org.hibernate.annotations.Type;
import org.springframework.data.annotation.Transient;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;

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
