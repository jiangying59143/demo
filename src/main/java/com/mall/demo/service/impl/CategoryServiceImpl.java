package com.mall.demo.service.impl;

import com.mall.demo.model.blog.Category;
import com.mall.demo.model.blog.UserCategory;
import com.mall.demo.model.privilege.User;
import com.mall.demo.repository.CategoryRepository;
import com.mall.demo.repository.UserCategoryRepository;
import com.mall.demo.service.CategoryService;
import com.mall.demo.vo.CategoryVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserCategoryRepository userCategoryRepository;

    @Override
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    @Override
    public Category getCategoryById(Long id) {
        return categoryRepository.getOne(id);
    }

    @Override
    public Category getCategoryByName(String categoryName) {
        return categoryRepository.getCategoryByCategoryName(categoryName);
    }

    @Override
    @Transactional
    public Long saveCategory(String categoryName) {
        Category category = new Category(categoryName);
        return categoryRepository.save(category).getId();
    }

    @Override
    @Transactional
    public Long updateCategory(Category category) {
        Category oldCategory = categoryRepository.getOne(category.getId());
        oldCategory.setCategoryName(category.getCategoryName());
        return oldCategory.getId();
    }

    @Override
    @Transactional
    public void deleteCategoryById(Long id) {
        categoryRepository.deleteById(id);
    }

    @Override
    public List<Category> listRecommendCat(User user) {
        List<UserCategory> userCategories = userCategoryRepository
                .getUserCategoriesByUserOrderByCreateDateAsc(user);
        List<Category> categories1 = categoryRepository.findAll();
        return categories1.stream()
                .filter((Category c) ->
                    !userCategories.stream().map(userCategory->{return userCategory.getCategory();}).collect(Collectors.toList()).contains(c)
                )
                .collect(Collectors.toList());
    }

}
