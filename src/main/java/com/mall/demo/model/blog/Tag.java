package com.mall.demo.model.blog;

import com.mall.demo.model.base.BaseEntity;

import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;

@Entity
public class Tag extends BaseEntity<Integer> {

    @NotBlank
    private String tagname;

    @NotBlank
    private String avatar;


    public String getTagname() {
        return tagname;
    }

    public void setTagname(String tagname) {
        this.tagname = tagname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }


}
