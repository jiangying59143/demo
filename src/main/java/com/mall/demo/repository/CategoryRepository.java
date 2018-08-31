package com.mall.demo.repository;

import com.mall.demo.model.blog.Category;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author shimh
 * <p>
 * 2018年1月25日
 */
public interface CategoryRepository extends JpaRepository<Category, Long> {

	/*@Query(value="select c.*, count(a.category_id) as articles from me_category c "
			+ "left join me_article a on a.category_id = c.id group by a.category_id",nativeQuery=true)
	List<Category> findAllDetail();*/

}
