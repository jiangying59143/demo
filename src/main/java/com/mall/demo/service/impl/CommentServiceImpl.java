package com.mall.demo.service.impl;

import com.mall.demo.common.utils.UserUtils;
import com.mall.demo.model.blog.Article;
import com.mall.demo.model.blog.Comment;
import com.mall.demo.repository.ArticleRepository;
import com.mall.demo.repository.CommentRepository;
import com.mall.demo.repository.UserRepository;
import com.mall.demo.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private UserRepository userRepository;


    @Autowired
    private CommentRepository commentRepository;

    @Override
    public List<Comment> findAll() {
        return commentRepository.findAll();
    }

    @Override
    public Comment getCommentById(Long id) {
        return commentRepository.getOne(id);
    }

    @Override
    @Transactional
    public Long saveComment(Comment comment) {
        return commentRepository.save(comment).getId();
    }


    @Override
    @Transactional
    public void deleteCommentById(Long id) {
        commentRepository.deleteById(id);
    }

    @Override
    public List<Comment> listCommentsByArticle(Long id) {
        Article a = new Article();
        a.setId(id);
        return commentRepository.findByArticleAndLevelOrderByCreateDateDesc(a, "0");
    }

    @Override
    @Transactional
    public Comment saveCommentAndChangeCounts(Comment comment) {

        int count = 1;
        Article a = articleRepository.getOne(comment.getArticle().getId());
        comment.setAuthor(UserUtils.getCurrentUser());
        comment.setCreateDate(new Date());

        //设置level
        if(null == comment.getParent()){
            comment.setLevel("0");
        }else{
            if(null == comment.getToUser()){
                comment.setLevel("1");
            }else{
                comment.setLevel("2");
            }
        }

        return commentRepository.save(comment);

    }

    @Override
    @Transactional
    public void deleteCommentByIdAndChangeCounts(Long id) {
        int count = 1;
        Comment c = commentRepository.getOne(id);
        Article a = c.getArticle();
        commentRepository.delete(c);
    }


}
