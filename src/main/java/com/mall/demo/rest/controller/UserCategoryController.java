package com.mall.demo.rest.controller;

import com.alibaba.fastjson.support.spring.annotation.FastJsonFilter;
import com.alibaba.fastjson.support.spring.annotation.FastJsonView;
import com.mall.demo.common.annotation.LogAnnotation;
import com.mall.demo.common.constants.ResultCode;
import com.mall.demo.common.result.Result;
import com.mall.demo.common.utils.UserUtils;
import com.mall.demo.model.blog.Category;
import com.mall.demo.model.blog.UserCategory;
import com.mall.demo.model.privilege.User;
import com.mall.demo.service.CategoryService;
import com.mall.demo.service.UserCategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Api(value = "主题", description = "主题")
@RestController
@RequestMapping(value = "/category")
public class UserCategoryController {
    @Autowired
    private UserCategoryService userCategoryService;

    @Autowired
    private CategoryService categoryService;

    @ApiOperation(value="我的主题", notes="我的主题")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "令牌", required = true, dataType = "String", paramType = "header")
    })
    @FastJsonView(
            include = {
                    @FastJsonFilter(clazz = Category.class, props = {"id","categoryName"})})
    @GetMapping("/mine")
    @LogAnnotation(module = "我的主题", operation = "我的主题")
    public Result listMyCategories() {
        User currentUser = UserUtils.getCurrentUser();
        List<UserCategory> categories = userCategoryService.listUserCategory(currentUser.getId());
        List<Category> list = categories.stream().map(UserCategory::getCategory).collect(Collectors.toList());
        return Result.success(list);
    }

    @ApiOperation(value="推荐主题", notes="推荐主题")
    @GetMapping("/recommend")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "令牌", required = true, dataType = "String", paramType = "header")
    })
    @FastJsonView(
            include = {
                    @FastJsonFilter(clazz = Category.class, props = {"id", "categoryName"})})
    @LogAnnotation(module = "推荐主题", operation = "推荐主题")
    public Result listRecommendCategories() {
        User currentUser = UserUtils.getCurrentUser();
        List<Category> categories = categoryService.listRecommendCat(currentUser);
        return Result.success(categories);
    }

    @ApiOperation(value="添加到我的主题", notes="添加到我的主题")
    @GetMapping("/addToMine")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "令牌", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "categoryId", value = "主题主键", required = true, dataType = "Long", paramType = "query")
    })
    @LogAnnotation(module = "添加到我的主题", operation = "添加到我的主题")
    public Result addCategoryToUser(@RequestParam Long categoryId){
        Result r= Result.success();
        if (null == categoryId) {
            r.setResultCode(ResultCode.PARAM_IS_BLANK);
            return r;
        }
        User currentUser = UserUtils.getCurrentUser();
        Long id = userCategoryService.save(currentUser.getId(), categoryId);
        return Result.success();
    }

    @ApiOperation(value="删除我的主题", notes="删除我的主题")
    @GetMapping("/deleteFromMine")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "令牌", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "categoryId", value = "主题主键", required = true, dataType = "Long", paramType = "query")
    })
    @LogAnnotation(module = "删除我的主题", operation = "删除我的主题")
    public Result deleteCategoryFromUser(@RequestParam Long categoryId){
        Result r= Result.success();
        if (null == categoryId) {
            r.setResultCode(ResultCode.PARAM_IS_BLANK);
            return r;
        }
        User currentUser = UserUtils.getCurrentUser();
        userCategoryService.delete(currentUser.getId(), categoryId);
        return Result.success();
    }

}
