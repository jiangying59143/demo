package com.mall.demo.rest.controller;

import com.alibaba.fastjson.support.spring.annotation.FastJsonFilter;
import com.alibaba.fastjson.support.spring.annotation.FastJsonView;
import com.mall.demo.common.annotation.LogAnnotation;
import com.mall.demo.common.constants.Base;
import com.mall.demo.common.constants.ResultCode;
import com.mall.demo.common.result.Result;
import com.mall.demo.common.utils.UserUtils;
import com.mall.demo.model.blog.Category;
import com.mall.demo.model.blog.UserCategory;
import com.mall.demo.model.privilege.User;
import com.mall.demo.service.CategoryService;
import com.mall.demo.service.UserCategoryService;
import com.mall.demo.vo.CategoryVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

/**
 * 文章分类api
 *
 * @author shimh
 * <p>
 * 2018年1月25日
 */
@Api(value = "全部主题相关操作", description = "全部主题相关操作")
@RestController
@RequestMapping(value = "/category")
public class CategoryController {


    @Autowired
    private CategoryService categoryService;

    @ApiOperation(value="所有文章分类", notes="所有文章分类")
    @GetMapping
    @LogAnnotation(module = "文章分类", operation = "获取所有文章分类")
    public Result listCategorys() {
        List<Category> categorys = categoryService.findAll();
        return Result.success(categorys);
    }

    @ApiOperation(value="根据id获取分类", notes="根据id获取分类")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Oauth-Token", value = "令牌", required = true, dataType = "String", paramType = "header")
    })
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

    @ApiOperation(value="创建分类(admin)", notes="创建分类(admin)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Oauth-Token", value = "令牌", required = true, dataType = "String", paramType = "header")
    })
    @PostMapping("/create")
    @RequiresRoles(Base.ROLE_ADMIN)
    @LogAnnotation(module = "文章分类", operation = "添加文章分类")
    public Result saveCategory(@RequestParam String categoryName) {
        Result r = Result.success();
        if(categoryService.getCategoryByName(categoryName) != null){
            r.setResultCode(ResultCode.DATA_EXIST);
        }
        Long categoryId = categoryService.saveCategory(categoryName);
        r.simple().put("categoryId", categoryId);
        return r;
    }

    @ApiIgnore
    @ApiOperation(value="更新分类", notes="更新分类")
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

    @ApiOperation(value="删除分类(admin)", notes="删除分类(admin)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Oauth-Token", value = "令牌", required = true, dataType = "String", paramType = "header")
    })
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
