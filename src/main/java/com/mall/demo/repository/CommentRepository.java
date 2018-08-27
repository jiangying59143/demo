package com.mall.demo.repository;

import com.mall.demo.model.blog.Article;
import com.mall.demo.model.blog.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByArticleAndLevelOrderByCreateDateDesc(Article a, String level);


}
