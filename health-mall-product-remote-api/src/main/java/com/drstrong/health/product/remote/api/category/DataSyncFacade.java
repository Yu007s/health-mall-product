package com.drstrong.health.product.remote.api.category;

import com.drstrong.health.product.model.entity.category.BackCategoryEntity;
import com.drstrong.health.product.model.entity.category.CategoryAttributeEntity;
import com.drstrong.health.product.model.entity.product.CategoryAttributeItemEntity;
import com.drstrong.health.product.model.response.result.BusinessException;
import com.drstrong.health.product.model.response.result.ResultVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 数据同步的远程接口
 * <p> 仅作数据同步使用 </>
 *
 * @author liuqiuyi
 * @date 2021/12/27 11:05
 */
@FeignClient(value = "health-mall-product", path = "/inner/product/data/sync")
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

	/**
	 * p_category 保存或更新分类
	 *
	 * @author liuqiuyi
	 * @date 2021/12/28 09:42
	 */
	@PostMapping("p_category/saveEntity")
	ResultVO<BackCategoryEntity> categorySaveEntity(@RequestBody BackCategoryEntity entity) throws BusinessException;

	/**
	 * p_category 删除分类
	 *
	 * @param categoryId 分类ID
	 */
	@GetMapping("p_category/deleteEntity")
	ResultVO<BackCategoryEntity> categoryDeleteEntity(@RequestParam("categoryId") Long categoryId) throws BusinessException;

	/**
	 * p_category 更新分类状态
	 *
	 * @param categoryId 分类ID
	 * @param status     状态值：0-禁用，1-启用
	 */
	@GetMapping("p_category/updateStatus")
	ResultVO<Object> categoryUpdateStatus(@RequestParam("categoryId") Long categoryId, @RequestParam("status") Integer status);

	/**
	 * p_category_attribute_item 保存分类属性项
	 *
	 * @author liuqiuyi
	 * @date 2021/12/28 09:50
	 */
	@PostMapping("p_category_attribute_item/saveItem")
	ResultVO<CategoryAttributeItemEntity> categoryAttributeItemSaveItem(@RequestBody CategoryAttributeItemEntity vo) throws BusinessException;

	/**
	 * p_category_attribute_item 删除分类属性项
	 *
	 * @author liuqiuyi
	 * @date 2021/12/28 09:50
	 */
	@GetMapping("p_category_attribute_item/deleteItem")
	ResultVO<CategoryAttributeItemEntity> categoryAttributeItemDeleteItem(@RequestParam("attributeItemId") Long attributeItemId) throws BusinessException;
}
