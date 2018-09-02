package com.mall.demo.model.blog;

import com.mall.demo.model.base.BaseEntity;

import javax.persistence.*;
import java.util.List;

@Entity
public class SearchKeyWord extends BaseEntity<Long> {

    @ManyToMany(fetch= FetchType.EAGER)
    @JoinTable(name = "searchHistoryKeywords", joinColumns = { @JoinColumn(name = "skwId") }, inverseJoinColumns ={@JoinColumn(name = "shId") })
    private List<UserSearchHistory> userSearchHistoryList;

    private String word;

    private long count;

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public List<UserSearchHistory> getUserSearchHistoryList() {
        return userSearchHistoryList;
    }

    public void setUserSearchHistoryList(List<UserSearchHistory> userSearchHistoryList) {
        this.userSearchHistoryList = userSearchHistoryList;
    }
}
