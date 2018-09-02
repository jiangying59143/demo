package com.mall.demo.service;

import com.mall.demo.model.blog.UserCategory;

import java.util.List;

public interface UserCategoryService {

    Long save(long userId, long categoryId);

    void delete(Long userId, Long categoryId);

    List<UserCategory> listUserCategory(long userId);

    public List<UserCategory> listRecommendCategory(long userId);
}
