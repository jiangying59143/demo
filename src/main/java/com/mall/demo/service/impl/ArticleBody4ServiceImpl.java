package com.mall.demo.service.impl;

import com.mall.demo.model.blog.ArticleBody4;
import com.mall.demo.model.blog.ArticleImage;
import com.mall.demo.repository.ArticleBody4Repository;
import com.mall.demo.repository.ArticleImageRepository;
import com.mall.demo.service.ArticleBody4Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ArticleBody4ServiceImpl implements ArticleBody4Service {

    @Autowired
    private ArticleBody4Repository articleBody4Repository;

    @Transactional
    @Override
    public ArticleBody4 save(ArticleBody4 articleBody4) {
        return articleBody4Repository.save(articleBody4);
    }

    @Override
    public Integer findMaxOrderArticleId(Long articleId){
        return articleBody4Repository.findMaxOrderArticleId(articleId);
    }
}
