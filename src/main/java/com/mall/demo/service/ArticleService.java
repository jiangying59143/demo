package com.mall.demo.service;

import com.mall.demo.model.blog.Article;
import com.mall.demo.vo.ArticleVo;
import com.mall.demo.vo.PageVo;

import java.util.List;

public interface ArticleService {

    List<Article> listArticles(PageVo page);

    List<Article> listArticles(ArticleVo article, PageVo page);

    List<Article> findAll();

    Article getArticleById(Long id);

    Long publishArticle(Article article);

    Long saveArticle(Article article);

    Long updateArticle(Article article);

    void deleteArticleById(Long id);

    List<Article> listArticlesByTag(Integer id);

    List<Article> listArticlesByCategory(Long id);

    Article getArticleAndAddViews(Long id);

    List<Article> listHotArticles(int limit);

    List<Article> listNewArticles(int limit);

    List<ArticleVo> listArchives();

}
