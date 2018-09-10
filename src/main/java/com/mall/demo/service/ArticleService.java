package com.mall.demo.service;

import com.mall.demo.model.blog.Article;
import com.mall.demo.model.blog.SearchKeyWord;
import com.mall.demo.model.blog.UserSearchHistory;
import com.mall.demo.model.privilege.User;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ArticleService {

    List<Article> findAll();

    Article getArticleById(Long id);

    Long publishArticle(Article article);

    Long saveArticle(Article article);

    Long updateArticle(Article article);

    void deleteArticleById(Long id);

    List<Article> listArticlesByCategory(Long id, String title);

    Article getArticleAndAddViews(Long id);

    List<Article> listHotArticles(int limit);

    List<Article> listNewArticles(int limit);

    void addSearchHistory(User user, String searchContent);

    Page<UserSearchHistory> listSearchHistory(Long userId, int pageNo, int pageCount);

    List<UserSearchHistory> listSearchGuess(int pageNo, int pageCount);
}
