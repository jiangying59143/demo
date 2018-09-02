package com.mall.demo.service.impl;

import com.mall.demo.model.blog.Category;
import com.mall.demo.model.blog.UserCategory;
import com.mall.demo.model.privilege.User;
import com.mall.demo.repository.CategoryRepository;
import com.mall.demo.repository.UserCategoryRepository;
import com.mall.demo.repository.UserRepository;
import com.mall.demo.service.UserCategoryService;
import org.hibernate.criterion.Example;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserCategoryServiceImpl implements UserCategoryService {

    @Autowired
    private UserCategoryRepository userCategoryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public Long save(long userId, long categoryId) {
        User user = userRepository.getOne(userId);
        Category category = categoryRepository.getOne(categoryId);
        UserCategory userCategory = new UserCategory();
        userCategory.setUser(user);
        userCategory.setCategory(category);
        userCategory = userCategoryRepository.save(userCategory);
        return userCategory.getId();
    }

    @Transactional
    @Override
    public void delete(Long userId, Long categoryId) {
        userCategoryRepository.deleteUserCategorysByUserAndCategory(new User(userId), new Category(categoryId));
    }

    @Override
    public List<UserCategory> listUserCategory(long userId) {
        List<UserCategory> results = userCategoryRepository.getUserCategoriesByUserOrderByCreateDateAsc(new User(userId));
        return results;
    }

    @Override
    public List<UserCategory> listRecommendCategory(long userId) {
        List<UserCategory> results = userCategoryRepository.getUserCategoriesByUserOrderByCreateDateAsc(new User(userId));
        return results;
    }
}
