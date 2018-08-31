package com.mall.demo.rest.controller;

import com.mall.demo.common.annotation.LogAnnotation;
import com.mall.demo.common.constants.Base;
import com.mall.demo.common.constants.ResultCode;
import com.mall.demo.common.result.Result;
import com.mall.demo.model.blog.Category;
import com.mall.demo.service.CategoryService;
import com.mall.demo.vo.CategoryVO;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 文章分类api
 *
 * @author shimh
 * <p>
 * 2018年1月25日
 */
@RestController
@RequestMapping(value = "/categorys")
public class CategoryController {


    @Autowired
    private CategoryService categoryService;

    @GetMapping
    @LogAnnotation(module = "文章分类", operation = "获取所有文章分类")
    public Result listCategorys() {
        List<Category> categorys = categoryService.findAll();

        return Result.success(categorys);
    }

    @GetMapping("/{id}")
    @LogAnnotation(module = "文章分类", operation = "根据id获取文章分类")
    public Result getCategoryById(@PathVariable("id") Long id) {
        Result r = new Result();
        if (null == id) {
            r.setResultCode(ResultCode.PARAM_IS_BLANK);
            return r;
        }
        Category category = categoryService.getCategoryById(id);
        r.setResultCode(ResultCode.SUCCESS);
        r.setData(category);
        return r;
    }

    @PostMapping("/create")
    @RequiresRoles(Base.ROLE_ADMIN)
    @LogAnnotation(module = "文章分类", operation = "添加文章分类")
    public Result saveCategory(@Validated @RequestBody Category category) {
        Long categoryId = categoryService.saveCategory(category);
        Result r = Result.success();
        r.simple().put("categoryId", categoryId);
        return r;
    }

    @PostMapping("/update")
    @RequiresRoles(Base.ROLE_ADMIN)
    @LogAnnotation(module = "文章分类", operation = "修改文章分类")
    public Result updateCategory(@RequestBody Category category) {
        Result r = new Result();
        if (null == category.getId()) {
            r.setResultCode(ResultCode.USER_NOT_EXIST);
            return r;
        }
        Long categoryId = categoryService.updateCategory(category);
        r.setResultCode(ResultCode.SUCCESS);
        r.simple().put("categoryId", categoryId);
        return r;
    }

    @GetMapping("/delete/{id}")
    @RequiresRoles(Base.ROLE_ADMIN)
    @LogAnnotation(module = "文章分类", operation = "删除文章分类")
    public Result deleteCategoryById(@PathVariable("id") Long id) {
        Result r = new Result();

        if (null == id) {
            r.setResultCode(ResultCode.PARAM_IS_BLANK);
            return r;
        }

        categoryService.deleteCategoryById(id);

        r.setResultCode(ResultCode.SUCCESS);
        return r;
    }


}
