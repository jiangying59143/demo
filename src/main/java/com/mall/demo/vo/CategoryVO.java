package com.mall.demo.vo;


import com.mall.demo.model.blog.Category;

public class CategoryVO extends Category {

    /**
     *
     */
    private static final long serialVersionUID = -2975739216517528014L;


    private int articles;

    public int getArticles() {
        return articles;
    }

    public void setArticles(int articles) {
        this.articles = articles;
    }


}
