package com.mall.demo.service;

import com.mall.demo.model.blog.Article;

import java.util.List;

public interface ArticleService {

    List<Article> findAll();

    Article getArticleById(Long id);

    Long publishArticle(Article article);

    Long saveArticle(Article article);

    Long updateArticle(Article article);

    void deleteArticleById(Long id);

    List<Article> listArticlesByCategory(Long id);

    Article getArticleAndAddViews(Long id);

    List<Article> listHotArticles(int limit);

    List<Article> listNewArticles(int limit);

}
