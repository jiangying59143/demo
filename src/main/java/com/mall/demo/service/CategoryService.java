package com.mall.demo.service;


import com.mall.demo.model.blog.Category;
import com.mall.demo.vo.CategoryVO;

import java.util.List;

public interface CategoryService {

    List<Category> findAll();

    Category getCategoryById(Long id);

    Long saveCategory(Category category);

    Long updateCategory(Category category);

    void deleteCategoryById(Long id);

    List<CategoryVO> findAllDetail();

    CategoryVO getCategoryDetail(Long categoryId);

}
