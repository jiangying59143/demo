package com.mall.demo.service.impl;

import com.google.common.collect.Lists;
import com.mall.demo.common.utils.IKAnalyzerUtil;
import com.mall.demo.common.utils.UserUtils;
import com.mall.demo.model.blog.Article;
import com.mall.demo.model.blog.ArticleImage;
import com.mall.demo.model.blog.SearchKeyWord;
import com.mall.demo.model.blog.UserSearchHistory;
import com.mall.demo.model.privilege.User;
import com.mall.demo.repository.ArticleImageRepository;
import com.mall.demo.repository.ArticleRepository;
import com.mall.demo.repository.SearchKeyWordsRepository;
import com.mall.demo.repository.UserSearchHisRepository;
import com.mall.demo.service.ArticleImageService;
import com.mall.demo.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ArticleImageServiceImpl implements ArticleImageService {

    @Autowired
    private ArticleImageRepository articleImageRepository;

    @Override
    public ArticleImage save(ArticleImage articleImage) {
        return articleImageRepository.save(articleImage);
    }
}
