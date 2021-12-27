package com.drstrong.health.product.service.impl;

import com.drstrong.health.product.dao.BackCategoryMapper;
import com.drstrong.health.product.dao.CategoryAttributeItemMapper;
import com.drstrong.health.product.dao.CategoryAttributeMapper;
import com.drstrong.health.product.model.entity.category.BackCategoryEntity;
import com.drstrong.health.product.model.entity.category.CategoryAttributeEntity;
import com.drstrong.health.product.model.entity.product.CategoryAttributeItemEntity;
import com.drstrong.health.product.model.enums.ErrorEnums;
import com.drstrong.health.product.model.response.result.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * 数据同步的 service
 *
 * @author liuqiuyi
 * @date 2021/12/27 11:38
 */
@Service
@Slf4j
public class DataSyncServiceImpl {
	@Resource
	BackCategoryMapper backCategoryMapper;

	@Resource
	CategoryAttributeMapper categoryAttributeMapper;

	@Resource
	CategoryAttributeItemMapper categoryAttributeItemMapper;

	/**
	 * 同步 p_category 表的数据
	 *
	 * @param categoryEntityList
	 * @author liuqiuyi
	 * @date 2021/12/27 11:10
	 */
	@Transactional(rollbackFor = Exception.class)
	public void categorySync(List<BackCategoryEntity> categoryEntityList) {
		if (CollectionUtils.isEmpty(categoryEntityList)) {
			throw new BusinessException(ErrorEnums.PARAM_IS_NOT_NULL);
		}
		// 先删除所有,在插入
		backCategoryMapper.delete(null);
		categoryEntityList.forEach(categoryEntity -> backCategoryMapper.syncDateInsertOne(categoryEntity));
		log.info("p_category data sync success!!");
	}

	/**
	 * 同步 p_category_attribute 表的数据
	 *
	 * @param entityList
	 * @author liuqiuyi
	 * @date 2021/12/27 11:15
	 */
	@Transactional(rollbackFor = Exception.class)
	public void categoryAttributeSync(List<CategoryAttributeEntity> entityList) {
		if (CollectionUtils.isEmpty(entityList)) {
			throw new BusinessException(ErrorEnums.PARAM_IS_NOT_NULL);
		}
		categoryAttributeMapper.delete(null);
		entityList.forEach(categoryAttributeEntity -> categoryAttributeMapper.syncDateInsertOne(categoryAttributeEntity));
		log.info("p_category_attribute data sync success!!");
	}


	/**
	 * 同步 p_category_attribute_item 表
	 *
	 * @param entityList
	 * @author liuqiuyi
	 * @date 2021/12/27 11:16
	 */
	@Transactional(rollbackFor = Exception.class)
	public void categoryAttributeItem(List<CategoryAttributeItemEntity> entityList) {
		if (CollectionUtils.isEmpty(entityList)) {
			throw new BusinessException(ErrorEnums.PARAM_IS_NOT_NULL);
		}
		categoryAttributeItemMapper.delete(null);
		entityList.forEach(categoryAttributeItemEntity -> categoryAttributeItemMapper.syncDateInsertOne(categoryAttributeItemEntity));
		log.info("p_category_attribute_item data sync success!!");
	}
}
