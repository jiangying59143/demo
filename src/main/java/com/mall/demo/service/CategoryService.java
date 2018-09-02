package com.mall.demo.service;


import com.mall.demo.model.blog.Category;
import com.mall.demo.model.privilege.User;
import com.mall.demo.vo.CategoryVO;

import java.util.List;

public interface CategoryService {

    List<Category> findAll();

    Category getCategoryById(Long id);

    Category getCategoryByName(String categoryName);

    Long saveCategory(String categoryName);

    Long updateCategory(Category category);

    void deleteCategoryById(Long id);

    List<Category> listRecommendCat(User user);

}
