package com.mall.demo.service;


import com.mall.demo.model.blog.Tag;
import com.mall.demo.vo.TagVO;

import java.util.List;

public interface TagService {

    List<Tag> findAll();

    Tag getTagById(Integer id);

    Integer saveTag(Tag tag);

    Integer updateTag(Tag tag);

    void deleteTagById(Integer id);

    List<Tag> listHotTags(int limit);

    List<TagVO> findAllDetail();

    TagVO getTagDetail(Integer tagId);

}
