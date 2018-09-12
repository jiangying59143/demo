package com.mall.demo.repository;

import com.mall.demo.model.blog.Article;
import com.mall.demo.model.blog.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Long> {

    @Query(value = "select a.* from article a " +
            "left join article_category ac on a.id=ac.article_id " +
            "where ac.category_id=:id and UPPER(a.title) like CONCAT('%',UPPER(:title),'%') ", nativeQuery = true)
    List<Article> findByCategoryAndTitle(@Param("id")Long id, @Param("title")String title);

    @Query(value = "select a.* from article a " +
            "left join article_category ac on a.id=ac.article_id " +
            "left join user_category uc on uc.category_id=ac.category_id "+
            "where uc.user_id=:userId and ac.category_id=:id and UPPER(a.title) like CONCAT('%',UPPER(:title),'%') "+
            "and not exists (select 1 from article_black_list abl where abl.article_id=a.id and abl.user_id=:userId)", nativeQuery = true)
    List<Article> findByCategoryAndTitle(@Param("userId")Long userId, @Param("id")Long id, @Param("title")String title);

    @Query(value = "select distinct a.* from article a " +
            "left join article_category ac on a.id=ac.article_id " +
            "left join user_category uc on uc.category_id=ac.category_id "+
            "where uc.user_id=:id and UPPER(a.title) like CONCAT('%',UPPER(:title),'%')" +
            "and not exists (select 1 from article_black_list abl where abl.article_id=a.id and abl.user_id=:id)", nativeQuery = true)
    List<Article> findArticlesByUserIdAndTitle(@Param("id")Long id, @Param("title")String title);

    @Query(value = "select * from me_article order by view_counts desc limit :limit", nativeQuery = true)
    List<Article> findOrderByViewsAndLimit(@Param("limit") int limit);

    @Query(value = "select * from me_article order by create_date desc limit :limit", nativeQuery = true)
    List<Article> findOrderByCreateDateAndLimit(@Param("limit") int limit);

    List<Article> findArticlesByTitleContaining(@Param("title")String title);

}
