package com.mall.demo.repository;

import com.mall.demo.model.blog.ArticleBody4;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ArticleBody4Repository extends JpaRepository<ArticleBody4, Long> {
    @Query(value = "select max(order_count) from article_body4 where article_id=:articleId", nativeQuery = true)
    Integer findMaxOrderArticleId(@Param("articleId") Long articleId);
}
