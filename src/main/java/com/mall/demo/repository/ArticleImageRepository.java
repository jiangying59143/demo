package com.mall.demo.repository;

import com.mall.demo.model.blog.Article;
import com.mall.demo.model.blog.ArticleImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ArticleImageRepository extends JpaRepository<ArticleImage, Long> {

}
