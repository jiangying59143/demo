package com.mall.demo.repository;

import com.mall.demo.model.blog.SearchKeyWord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SearchKeyWordsRepository  extends JpaRepository<SearchKeyWord, Long> {

    List<SearchKeyWord> getSearchKeyWordsByWord(String word);
}
