package com.mall.demo.repository;

import com.mall.demo.model.blog.Article;
import com.mall.demo.model.blog.ArticleBlackList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ArticleBlackRepository extends JpaRepository<ArticleBlackList, Long> {

}
