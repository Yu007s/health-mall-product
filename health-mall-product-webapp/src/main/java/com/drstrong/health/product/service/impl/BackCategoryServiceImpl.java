package com.drstrong.health.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.drstrong.health.product.dao.BackCategoryMapper;
import com.drstrong.health.product.model.BaseTree;
import com.drstrong.health.product.model.entity.category.BackCategoryEntity;
import com.drstrong.health.product.model.request.category.CategoryQueryRequest;
import com.drstrong.health.product.service.BackCategoryService;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * 后台分类 service impl
 *
 * @author liuqiuyi
 * @date 2021/12/9 16:46
 */
@Service
@Slf4j
public class BackCategoryServiceImpl implements BackCategoryService {

	@Resource
	BackCategoryMapper backCategoryMapper;

	/**
	 * 查询所有的后台分类
	 *
	 * @param categoryIdList 分类 id 集合
	 * @return 后台分类的集合
	 * @author liuqiuyi
	 * @date 2021/12/7 20:37
	 */
	@Override
	public List<BackCategoryEntity> queryByIdList(Set<Long> categoryIdList) {
		if (CollectionUtils.isEmpty(categoryIdList)) {
			return Lists.newArrayList();
		}
		LambdaQueryWrapper<BackCategoryEntity> backWrapper = new LambdaQueryWrapper<>();
		// 之前老表的删除状态是 -1
		backWrapper.in(BackCategoryEntity::getId, categoryIdList).ne(BackCategoryEntity::getStatus, -1);
		return backCategoryMapper.selectList(backWrapper);
	}

	/**
	 * 根据 id 查询后台分类
	 *
	 * @param categoryId 后台分类 id
	 * @return 后台分类信息
	 * @author liuqiuyi
	 * @date 2021/12/13 14:37
	 */
	@Override
	public BackCategoryEntity queryById(Long categoryId) {
		if (Objects.isNull(categoryId)) {
			return new BackCategoryEntity();
		}
		LambdaQueryWrapper<BackCategoryEntity> backWrapper = new LambdaQueryWrapper<>();
		// 之前老表的删除状态是 -1
		backWrapper.eq(BackCategoryEntity::getId, categoryId).ne(BackCategoryEntity::getStatus, -1);
		return backCategoryMapper.selectOne(backWrapper);
	}

	/**
	 * 查询后台分类集合
	 *
	 * @param categoryQueryRequest 查询条件
	 * @return 后台分类的集合, 按照树形结构排列
	 * @author liuqiuyi
	 * @date 2021/12/7 20:37
	 */
	@Override
	public List<BackCategoryEntity> queryByParam(CategoryQueryRequest categoryQueryRequest) {
		return null;
	}
}
