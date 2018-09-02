package com.mall.demo.repository;

import com.mall.demo.model.blog.UserSearchHistory;
import com.mall.demo.model.privilege.User;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserSearchHisRepository extends JpaRepository<UserSearchHistory,Long> {
    Page<UserSearchHistory> getUserSearchHistoriesByUser(User user, Pageable pageable);

    UserSearchHistory getUserSearchHistoryByUserAndContent(User user, String content);
}
