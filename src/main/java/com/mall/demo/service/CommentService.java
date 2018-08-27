package com.mall.demo.service;

import com.mall.demo.model.blog.Comment;

import java.util.List;

public interface CommentService {

    List<Comment> findAll();

    Comment getCommentById(Long id);

    Long saveComment(Comment comment);

    void deleteCommentById(Long id);

    List<Comment> listCommentsByArticle(Long id);

    Comment saveCommentAndChangeCounts(Comment comment);

    void deleteCommentByIdAndChangeCounts(Long id);


}
