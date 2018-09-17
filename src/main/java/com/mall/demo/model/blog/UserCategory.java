package com.mall.demo.model.blog;

import com.mall.demo.model.base.BaseEntity;
import com.mall.demo.model.base.BaseTO;
import com.mall.demo.model.privilege.User;

import javax.persistence.*;

@Entity
public class UserCategory extends BaseTO {

    public UserCategory() {
    }

    public UserCategory(User user, Category category) {
        this.user = user;
        this.category = category;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    private Category category;

    @Transient
    private String categoryName;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getCategoryName() {
        if(category != null){
            categoryName = category.getCategoryName();
        }
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
