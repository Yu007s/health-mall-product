package com.drstrong.health.product.service.category.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.drstrong.health.product.dao.category.BackCategoryMapper;
import com.drstrong.health.product.model.BaseTree;
import com.drstrong.health.product.model.entity.category.BackCategoryEntity;
import com.drstrong.health.product.model.enums.CategoryProductNumOperateEnum;
import com.drstrong.health.product.model.enums.ErrorEnums;
import com.drstrong.health.product.model.request.category.CategoryQueryRequest;
import com.drstrong.health.product.model.response.category.BackCategoryVO;
import com.drstrong.health.product.model.response.result.BusinessException;
import com.drstrong.health.product.service.category.BackCategoryService;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;

import static java.util.stream.Collectors.toMap;

/**
 * 后台分类 service impl
 *
 * @author liuqiuyi
 * @date 2021/12/9 16:46
 */
@Service
@Slf4j
public class BackCategoryServiceImpl extends ServiceImpl<BackCategoryMapper, BackCategoryEntity> implements BackCategoryService {

	@Resource
	BackCategoryMapper backCategoryMapper;

	/**
	 * 校验后台分类是否存在
	 *
	 * @param backCategoryIdList 后台分类 id
	 * @author liuqiuyi
	 * @date 2021/12/26 16:07
	 */
	@Override
	public void checkCategoryIsExist(Set<Long> backCategoryIdList) {
		List<BackCategoryEntity> backCategoryEntityList = queryByIdList(backCategoryIdList);
		if (CollectionUtils.isEmpty(backCategoryEntityList) || !Objects.equals(backCategoryEntityList.size(), backCategoryIdList.size())) {
			throw new BusinessException(ErrorEnums.CATEGORY_NOT_EXIST);
		}
	}

	/**
	 * 增加或者减少后台分类 id 的商品数量
	 *
	 * @param categoryId 后台分类id
	 * @param count      要累加的商品数量
	 * @author liuqiuyi
	 * @date 2021/12/28 11:08
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void addOrReduceProductNumById(Long categoryId, Integer count, CategoryProductNumOperateEnum operateEnum) {
		if (Objects.isNull(categoryId) || Objects.isNull(operateEnum)) {
			log.error("BackCategoryServiceImpl.incProductNumById param is null");
			return;
		}
		if (Objects.isNull(count)) {
			count = 1;
		}
		// 查询分类
		BackCategoryEntity categoryEntity = backCategoryMapper.selectById(categoryId);
		if (Objects.isNull(categoryEntity)) {
			throw new BusinessException(ErrorEnums.CATEGORY_NOT_EXIST);
		}
		// 如果是增加操作
		if (Objects.equals(CategoryProductNumOperateEnum.ADD, operateEnum)) {
			categoryEntity.setPNumber(categoryEntity.getPNumber() + count);
		} else {
			int num = categoryEntity.getPNumber() - count;
			if (num < 0) {
				num = 0;
			}
			categoryEntity.setPNumber(num);
		}
		categoryEntity.setChangedAt(new Date());
		backCategoryMapper.updateById(categoryEntity);
	}

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
	 * 根据后台分类 id,获取商品数量,组装成 map
	 *
	 * @param categoryIdList 后台分类 id
	 * @return map.key = 后台分类 id, map.value = 商品数量
	 * @author liuqiuyi
	 * @date 2021/12/26 16:48
	 */
	@Override
	public Map<Long, Integer> getBackIdProductNumMap(Set<Long> categoryIdList) {
		List<BackCategoryEntity> backCategoryEntityList = queryByIdList(categoryIdList);
		return BackCategoryEntity.buildCategoryProductCount(backCategoryEntityList, true);
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
	public List<BackCategoryVO> queryByParamToTree(CategoryQueryRequest categoryQueryRequest) {
		List<BackCategoryEntity> backCategoryEntityList = queryEntityList(categoryQueryRequest);
		// 组装返回值
		List<BackCategoryVO> backCategoryVOList = Lists.newArrayListWithCapacity(backCategoryEntityList.size());
		for (BackCategoryEntity backCategoryEntity : backCategoryEntityList) {
			BackCategoryVO backCategoryVO = new BackCategoryVO();
			BeanUtils.copyProperties(backCategoryEntity, backCategoryVO);
			backCategoryVOList.add(backCategoryVO);
		}
		return BaseTree.listToTree(backCategoryVOList);
	}

	/**
	 * 根据一级分类 id,查询一级分类下所有子分类 id
	 *
	 * @param oneCategoryId 一级分类 id
	 * @return 所有子分类 id 集合(包含一级分类,二级分类,三级分类的 id)
	 * @author liuqiuyi
	 * @date 2022/1/10 11:49
	 */
	@Override
	public Set<Long> getCategoryIdsByOneId(Long oneCategoryId) {
		if (Objects.isNull(oneCategoryId)) {
			return Sets.newHashSet();
		}
		List<BackCategoryEntity> backCategoryEntityList = queryEntityList(new CategoryQueryRequest());
		BaseTree.listToTree(backCategoryEntityList);
		// 组装 map
		Map<Long, BackCategoryEntity> idEntityMap = backCategoryEntityList.stream().collect(toMap(BackCategoryEntity::getId, dto -> dto, (v1, v2) -> v1));
		// 获取 id
		BackCategoryEntity backCategoryEntity = idEntityMap.getOrDefault(oneCategoryId, new BackCategoryEntity());
		return getCategoryId(backCategoryEntity);
	}

	private Set<Long> getCategoryId(BackCategoryEntity backCategoryEntity) {
		Set<Long> categoryIdList = Sets.newHashSet();
		categoryIdList.add(backCategoryEntity.getId());
		if (!CollectionUtils.isEmpty(backCategoryEntity.getChildren())) {
			Set<Long> categoryId = getChildrenCategoryId((List<BaseTree>) backCategoryEntity.getChildren());
			categoryIdList.addAll(categoryId);
		}
		return categoryIdList;
	}

	private Set<Long> getChildrenCategoryId(List<BaseTree> childrenList) {
		if (CollectionUtils.isEmpty(childrenList)) {
			return Sets.newHashSet();
		}
		Set<Long> categoryIds = Sets.newHashSet();
		for (BaseTree children : childrenList) {
			categoryIds.add(children.getId());
			if (!CollectionUtils.isEmpty(children.getChildren())) {
				Set<Long> categoryId = getChildrenCategoryId((List<BaseTree>) children.getChildren());
				categoryIds.addAll(categoryId);
			}
		}
		return categoryIds;
	}

	private List<BackCategoryEntity> queryEntityList(CategoryQueryRequest categoryQueryRequest){
		LambdaQueryWrapper<BackCategoryEntity> backWrapper = new LambdaQueryWrapper<>();
		backWrapper.gt(BackCategoryEntity::getLevel, 0);
		if (Objects.nonNull(categoryQueryRequest.getLevel())) {
			backWrapper.le(BackCategoryEntity::getLevel, categoryQueryRequest.getLevel());
		}
		if (Objects.nonNull(categoryQueryRequest.getState())) {
			backWrapper.eq(BackCategoryEntity::getStatus, categoryQueryRequest.getState());
		}
		backWrapper.orderByAsc(BackCategoryEntity::getOrderNumber).orderByAsc(BackCategoryEntity::getId);
		return backCategoryMapper.selectList(backWrapper);
	}
}
