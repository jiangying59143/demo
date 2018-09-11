package com.mall.demo.service;

import com.mall.demo.model.blog.ArticleBody4;
import com.mall.demo.model.blog.ArticleImage;

public interface ArticleBody4Service {

    ArticleBody4 save(ArticleBody4 articleBody4);

    Integer findMaxOrderArticleId(Long articleId);

}
