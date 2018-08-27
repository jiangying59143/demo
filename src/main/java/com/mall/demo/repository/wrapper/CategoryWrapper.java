package com.mall.demo.repository.wrapper;

import com.mall.demo.vo.CategoryVO;

import java.util.List;

public interface CategoryWrapper {

    List<CategoryVO> findAllDetail();

    CategoryVO getCategoryDetail(Long categoryId);
}
