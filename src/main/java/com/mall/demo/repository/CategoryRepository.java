package com.mall.demo.repository;

import com.mall.demo.model.blog.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Category getCategoryByCategoryName(String categoryName);
}
