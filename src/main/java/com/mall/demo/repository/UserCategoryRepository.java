package com.mall.demo.repository;

import com.mall.demo.model.blog.Category;
import com.mall.demo.model.blog.UserCategory;
import com.mall.demo.model.privilege.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserCategoryRepository extends JpaRepository<UserCategory, Long> {
    List<UserCategory> getUserCategoriesByUserOrderByCreateDateAsc(User user);

    void deleteUserCategorysByUserAndCategory(User user, Category category);
}
