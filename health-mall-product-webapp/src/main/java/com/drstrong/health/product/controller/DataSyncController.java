package com.drstrong.health.product.controller;

import com.drstrong.health.product.model.entity.category.BackCategoryEntity;
import com.drstrong.health.product.model.entity.category.CategoryAttributeEntity;
import com.drstrong.health.product.model.entity.product.CategoryAttributeItemEntity;
import com.drstrong.health.product.model.response.result.ResultVO;
import com.drstrong.health.product.remote.api.category.DataSyncFacade;
import com.drstrong.health.product.service.impl.DataSyncServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 数据同步的接口
 *
 * @author liuqiuyi
 * @date 2021/12/27 11:17
 */
@RestController
@RequestMapping("/data/sync")
@Slf4j
public class DataSyncController implements DataSyncFacade {
	@Resource
	DataSyncServiceImpl dataSyncService;

	/**
	 * 同步 p_category 表的数据
	 *
	 * @param categoryEntityList
	 * @author liuqiuyi
	 * @date 2021/12/27 11:10
	 */
	@Override
	public ResultVO<Object> categorySync(List<BackCategoryEntity> categoryEntityList) {
		dataSyncService.categorySync(categoryEntityList);
		return ResultVO.success();
	}

	/**
	 * 同步 p_category_attribute 表的数据
	 *
	 * @param entityList
	 * @author liuqiuyi
	 * @date 2021/12/27 11:15
	 */
	@Override
	public ResultVO<Object> categoryAttributeSync(List<CategoryAttributeEntity> entityList) {
		dataSyncService.categoryAttributeSync(entityList);
		return ResultVO.success();
	}

	/**
	 * 同步 p_category_attribute_item 表
	 *
	 * @param entityList
	 * @author liuqiuyi
	 * @date 2021/12/27 11:16
	 */
	@Override
	public ResultVO<Object> categoryAttributeItem(List<CategoryAttributeItemEntity> entityList) {
		dataSyncService.categoryAttributeItem(entityList);
		return ResultVO.success();
	}
}
