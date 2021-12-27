package com.drstrong.health.product.remote.api.category;

import com.drstrong.health.product.model.entity.category.BackCategoryEntity;
import com.drstrong.health.product.model.entity.category.CategoryAttributeEntity;
import com.drstrong.health.product.model.entity.product.CategoryAttributeItemEntity;
import com.drstrong.health.product.model.response.result.ResultVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * 数据同步的远程接口
 * <p> 仅作数据同步使用 </>
 *
 * @author liuqiuyi
 * @date 2021/12/27 11:05
 */
@FeignClient(value = "health-mall-product", path = "/data/sync")
public interface DataSyncFacade {

	/**
	 * 同步 p_category 表的数据
	 *
	 * @author liuqiuyi
	 * @date 2021/12/27 11:10
	 */
	@PostMapping("/p_category/sync")
	ResultVO<Object> categorySync(@RequestBody List<BackCategoryEntity> categoryEntityList);

	/**
	 * 同步 p_category_attribute 表的数据
	 *
	 * @author liuqiuyi
	 * @date 2021/12/27 11:15
	 */
	@PostMapping("p_category_attribute/sync")
	ResultVO<Object> categoryAttributeSync(@RequestBody List<CategoryAttributeEntity> entityList);

	/**
	 * 同步 p_category_attribute_item 表
	 *
	 * @author liuqiuyi
	 * @date 2021/12/27 11:16
	 */
	@PostMapping("p_category_attribute_item/sync")
	ResultVO<Object> categoryAttributeItem(@RequestBody List<CategoryAttributeItemEntity> entityList);
}
