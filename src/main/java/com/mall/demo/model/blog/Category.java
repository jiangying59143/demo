package com.mall.demo.model.blog;

import com.mall.demo.model.base.BaseTO;

import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;

/**
 * 文章分类
 *
 * @author shimh
 * <p>
 * 2018年1月23日
 */
@Entity
public class Category extends BaseTO {

    /**
     *
     */
    private static final long serialVersionUID = 5025313969040054182L;

    @NotBlank
    private String categoryname;

    private String description;

    @NotBlank
    private String avatar;


    public String getCategoryname() {
        return categoryname;
    }

    public void setCategoryname(String categoryname) {
        this.categoryname = categoryname;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

}
