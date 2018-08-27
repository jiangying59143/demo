package com.mall.demo.repository.wrapper;

import com.mall.demo.model.blog.Article;
import com.mall.demo.vo.ArticleVo;
import com.mall.demo.vo.PageVo;

import java.util.List;

public interface ArticleWrapper {
    List<Article> listArticles(PageVo page);

    List<Article> listArticles(ArticleVo article, PageVo page);

    List<ArticleVo> listArchives();

}
