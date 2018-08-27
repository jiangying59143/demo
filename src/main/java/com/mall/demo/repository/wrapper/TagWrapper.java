package com.mall.demo.repository.wrapper;

import com.mall.demo.vo.TagVO;

import java.util.List;

public interface TagWrapper {

    List<TagVO> findAllDetail();

    TagVO getTagDetail(Integer tagId);
}
