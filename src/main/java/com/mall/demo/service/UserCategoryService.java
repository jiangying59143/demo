package com.mall.demo.service;

import com.mall.demo.model.blog.Category;
import com.mall.demo.model.blog.UserCategory;
import com.mall.demo.model.privilege.User;

import java.util.List;

public interface UserCategoryService {

    void addRegistrationCategories(long userId);

    Long save(long userId, long categoryId);

    void delete(Long userId, Long categoryId);

    List<UserCategory> listUserCategory(long userId);

    public List<UserCategory> listRecommendCategory(long userId);

    public UserCategory findByUserAndCategory(User user, Category category);
}
