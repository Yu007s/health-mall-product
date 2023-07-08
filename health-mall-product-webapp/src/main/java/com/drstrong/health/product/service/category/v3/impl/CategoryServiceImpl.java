package com.drstrong.health.product.service.category.v3.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.drstrong.health.product.dao.category.v3.CategoryMapper;
import com.drstrong.health.product.model.entity.category.v3.CategoryEntity;
import com.drstrong.health.product.model.entity.sku.StoreSkuInfoEntity;
import com.drstrong.health.product.service.category.v3.CategoryService;
import com.drstrong.health.product.service.sku.StoreSkuInfoService;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author liuqiuyi
 * @date 2023/6/12 15:47
 */
@Slf4j
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, CategoryEntity> implements CategoryService {

	@Autowired
	private StoreSkuInfoService storeSkuInfoService;


	/**
	 * 获取西药的所有分类
	 *
	 * @author liuqiuyi
	 * @date 2023/6/12 15:50
	 */
	@Override
	public List<CategoryEntity> queryWesternAll() {
		LambdaQueryWrapper<CategoryEntity> queryWrapper = new LambdaQueryWrapper<CategoryEntity>()
				.eq(CategoryEntity::getStatus, CategoryEntity.STATUS_ENABLE)
				.eq(CategoryEntity::getParentId, 0)
				.orderByAsc(CategoryEntity::getOrderNumber)
				.orderByAsc(CategoryEntity::getId);
		return baseMapper.selectList(queryWrapper);
	}

	/**
	 * 获取健康用品的所有分类
	 *
	 * @author liuqiuyi
	 * @date 2023/6/12 15:51
	 */
	@Override
	public List<CategoryEntity> queryHealthAll() {
		LambdaQueryWrapper<CategoryEntity> queryWrapper = new LambdaQueryWrapper<CategoryEntity>()
				.eq(CategoryEntity::getStatus, CategoryEntity.STATUS_ENABLE)
				.gt(CategoryEntity::getLevel, 0)
				.orderByAsc(CategoryEntity::getOrderNumber)
				.orderByAsc(CategoryEntity::getId);
		return baseMapper.selectList(queryWrapper);
	}

	/**
	 * 根据 id 查询
	 *
	 * @param ids
	 * @author liuqiuyi
	 * @date 2023/6/12 16:27
	 */
	@Override
	public List<CategoryEntity> listByIds(List<Long> ids) {
		if (CollectionUtil.isEmpty(ids)) {
			return Lists.newArrayList();
		}
		return baseMapper.selectBatchIds(ids);
	}

	@Override
	public List<CategoryEntity> queryWesternCategory() {
		LambdaQueryWrapper<CategoryEntity> queryWrapper = new LambdaQueryWrapper<CategoryEntity>()
				.eq(CategoryEntity::getStatus, CategoryEntity.STATUS_ENABLE)
				.eq(CategoryEntity::getParentId, 0)
				.orderByAsc(CategoryEntity::getOrderNumber)
				.orderByAsc(CategoryEntity::getId);
		List<CategoryEntity> categoryEntities = baseMapper.selectList(queryWrapper);
		if (CollectionUtils.isEmpty(categoryEntities)) {
			return Lists.newArrayList();
		}
		List<CategoryEntity> result = new ArrayList<>();
		for (CategoryEntity categoryEntity : categoryEntities) {
			//判断该分类下是否包含已上架的药品，没有则不展示该分类
			List<StoreSkuInfoEntity> storeSkuInfoEntities = storeSkuInfoService.queryStoreSkuInfoByCategory(categoryEntity.getId());
			if (CollectionUtil.isNotEmpty(storeSkuInfoEntities)) {
				result.add(categoryEntity);
			}
		}
		return result;
	}

	@Override
	public List<CategoryEntity> queryHealthCategory() {
		LambdaQueryWrapper<CategoryEntity> queryWrapper = new LambdaQueryWrapper<CategoryEntity>()
				.eq(CategoryEntity::getStatus, CategoryEntity.STATUS_ENABLE)
				.gt(CategoryEntity::getLevel, 0)
				.orderByAsc(CategoryEntity::getOrderNumber)
				.orderByAsc(CategoryEntity::getId);
		List<CategoryEntity> categoryEntities = baseMapper.selectList(queryWrapper);
		if (CollectionUtils.isEmpty(categoryEntities)) {
			return Lists.newArrayList();
		}

		List<CategoryEntity> result = new ArrayList<>();
		for (CategoryEntity categoryEntity : categoryEntities) {
			//判断该分类下是否包含已上架的药品，没有则不展示该分类
			List<StoreSkuInfoEntity> storeSkuInfoEntities = storeSkuInfoService.queryStoreSkuInfoByCategory(categoryEntity.getId());
			if (CollectionUtil.isNotEmpty(storeSkuInfoEntities)) {
				result.add(categoryEntity);
			}
		}
		return result;
	}

}
