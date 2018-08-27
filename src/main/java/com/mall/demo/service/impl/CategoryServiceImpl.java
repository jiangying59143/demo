package com.mall.demo.service.impl;

import com.mall.demo.model.blog.Category;
import com.mall.demo.repository.CategoryRepository;
import com.mall.demo.service.CategoryService;
import com.mall.demo.vo.CategoryVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;


    @Override
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    @Override
    public Category getCategoryById(Long id) {
        return categoryRepository.getOne(id);
    }

    @Override
    @Transactional
    public Long saveCategory(Category category) {
        return categoryRepository.save(category).getId();
    }

    @Override
    @Transactional
    public Long updateCategory(Category category) {
        Category oldCategory = categoryRepository.getOne(category.getId());
        oldCategory.setCategoryname(category.getCategoryname());
        oldCategory.setAvatar(category.getAvatar());
        oldCategory.setDescription(category.getDescription());
        return oldCategory.getId();
    }

    @Override
    @Transactional
    public void deleteCategoryById(Long id) {
        categoryRepository.deleteById(id);
    }

    @Override
    public List<CategoryVO> findAllDetail() {
        return categoryRepository.findAllDetail();
    }

    @Override
    public CategoryVO getCategoryDetail(Long categoryId) {
        return categoryRepository.getCategoryDetail(categoryId);
    }

}
