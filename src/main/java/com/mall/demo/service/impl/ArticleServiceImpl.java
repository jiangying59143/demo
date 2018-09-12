package com.mall.demo.service.impl;

import com.google.common.collect.Lists;
import com.mall.demo.common.utils.IKAnalyzerUtil;
import com.mall.demo.common.utils.UserUtils;
import com.mall.demo.model.blog.Article;
import com.mall.demo.model.blog.Category;
import com.mall.demo.model.blog.SearchKeyWord;
import com.mall.demo.model.blog.UserSearchHistory;
import com.mall.demo.model.privilege.User;
import com.mall.demo.repository.ArticleRepository;
import com.mall.demo.repository.SearchKeyWordsRepository;
import com.mall.demo.repository.UserSearchHisRepository;
import com.mall.demo.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private UserSearchHisRepository userSearchHisRepository;

    @Autowired
    private SearchKeyWordsRepository searchKeyWordsRepository;

    @Override
    public List<Article> findAll() {
        return articleRepository.findAll();
    }

    @Override
    public Article getArticleById(Long id) {
        return articleRepository.getOne(id);
    }

    @Override
    @Transactional
    public Long publishArticle(Article article) {
        if(null != article.getId()){
            return this.updateArticle(article);
        }else{
            return this.saveArticle(article);
        }
    }

    @Override
    @Transactional
    public Long saveArticle(Article article) {
        User currentUser = UserUtils.getCurrentUser();
        if (null != currentUser) {
            article.setAuthor(currentUser);
        }
        article.setWeight(Article.Article_Common);
        return articleRepository.save(article).getId();
    }

    @Override
    @Transactional
    public Long updateArticle(Article article) {
        Article oldArticle = articleRepository.getOne(article.getId());
        oldArticle.setTitle(article.getTitle());
        oldArticle.setCategoryList(article.getCategoryList());
        return oldArticle.getId();
    }

    @Override
    @Transactional
    public void deleteArticleById(Long id) {
        articleRepository.deleteById(id);
    }

    @Override
    public List<Article> listArticlesByCategory(Long id, String title) {
        User currentUser = UserUtils.getCurrentUser();
        if(id==1){
            if(currentUser != null) {
                this.addSearchHistory(currentUser,title);
                return articleRepository.findArticlesByUserIdAndTitle(currentUser.getId(), title);
            }else {
                return articleRepository.findArticlesByTitleContaining(title);
            }
        }
        if(currentUser != null) {
            this.addSearchHistory(currentUser,title);
            return articleRepository.findByCategoryAndTitle(currentUser.getId(), id, title);
        }else{
            return articleRepository.findByCategoryAndTitle(id, title);
        }
    }

    @Override
    @Transactional
    public Article getArticleAndAddViews(Long id) {
        int count = 1;
        Optional<Article> optionalArticle = articleRepository.findById(id);
        Article article =  optionalArticle.get();
        return article;
    }

    @Override
    public List<Article> listHotArticles(int limit) {

        return articleRepository.findOrderByViewsAndLimit(limit);
    }

    @Override
    public List<Article> listNewArticles(int limit) {
        return articleRepository.findOrderByCreateDateAndLimit(limit);
    }

    @Override
    @Transactional
    public void addSearchHistory(User user, String searchContent){
        UserSearchHistory userSearchHistory = userSearchHisRepository.getUserSearchHistoryByUserAndContent(user, searchContent);
        if(userSearchHistory == null) {
            userSearchHistory = new UserSearchHistory();
            userSearchHistory.setUser(user);
            userSearchHistory.setContent(searchContent);
            userSearchHistory = userSearchHisRepository.save(userSearchHistory);
        }
        List<String> keywords = IKAnalyzerUtil.getSplitWords(searchContent);
        keywords = IKAnalyzerUtil.filter(keywords);
        for (String keyword : keywords) {
            List<SearchKeyWord> searchKeyWords = searchKeyWordsRepository
                    .getSearchKeyWordsByWord(keyword);
            if(searchKeyWords != null && searchKeyWords.size() > 0){
                SearchKeyWord searchKeyWord = searchKeyWords.get(0);
                searchKeyWord.setCount(searchKeyWord.getCount()+1);
            }else {
                SearchKeyWord searchKeyWord = new SearchKeyWord();
                searchKeyWord.setWord(keyword);
                searchKeyWord.setCount(1);
                searchKeyWord.setUserSearchHistoryList(Arrays.asList(userSearchHistory));
                searchKeyWordsRepository.save(searchKeyWord);
            }
        }
    }

    @Override
    public Page<UserSearchHistory> listSearchHistory(Long userId, int pageNo, int pageCount) {
        Pageable pageable = PageRequest.of(pageNo,pageCount, Sort.Direction.DESC,"createDate");
        Page<UserSearchHistory> page = userSearchHisRepository
                .getUserSearchHistoriesByUser(new User(userId), pageable);
        return page;
    }

    @Override
    public List<UserSearchHistory> listSearchGuess(int pageNo, int pageCount) {
        Pageable pageable = PageRequest.of(pageNo,pageCount, Sort.Direction.DESC,"count");
        Page<SearchKeyWord> page = searchKeyWordsRepository.findAll(pageable);
        List<SearchKeyWord> list = Lists.newArrayList(page.iterator());
        List<UserSearchHistory> userSearchHistories = list.stream().map(skw-> {
            if(skw.getUserSearchHistoryList() != null && skw.getUserSearchHistoryList().size() > 0){
                return skw.getUserSearchHistoryList().get(0);
            }
            return null;
        }).collect(Collectors.toList());
        userSearchHistories = userSearchHistories.stream().filter(ush-> ush != null).distinct().collect(Collectors.toList());
        return userSearchHistories;
    }

}
