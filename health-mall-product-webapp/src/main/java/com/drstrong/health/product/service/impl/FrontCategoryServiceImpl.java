package com.drstrong.health.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.drstrong.health.product.dao.FrontCategoryMapper;
import com.drstrong.health.product.model.BaseTree;
import com.drstrong.health.product.model.entity.category.BackCategoryEntity;
import com.drstrong.health.product.model.entity.category.CategoryRelationEntity;
import com.drstrong.health.product.model.entity.category.FrontCategoryEntity;
import com.drstrong.health.product.model.enums.DelFlagEnum;
import com.drstrong.health.product.model.enums.ErrorEnums;
import com.drstrong.health.product.model.enums.LevelEnum;
import com.drstrong.health.product.model.request.category.AddOrUpdateFrontCategoryRequest;
import com.drstrong.health.product.model.request.category.CategoryQueryRequest;
import com.drstrong.health.product.model.response.category.FrontCategoryVO;
import com.drstrong.health.product.model.response.result.BusinessException;
import com.drstrong.health.product.service.BackCategoryService;
import com.drstrong.health.product.service.CategoryRelationService;
import com.drstrong.health.product.service.FrontCategoryService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 前台分类 service
 *
 * @author liuqiuyi
 * @date 2021/12/7 20:49
 */
@Service
@Slf4j
public class FrontCategoryServiceImpl implements FrontCategoryService {

	@Resource
	FrontCategoryMapper frontCategoryMapper;

	@Resource
	CategoryRelationService categoryRelationService;

	@Resource
	BackCategoryService backCategoryService;

	/**
	 * 查询所有的前台分类,并组装树形结构
	 *
	 * @param categoryQueryRequest 查询的参数信息
	 * @return 前台分类的集合
	 * @author liuqiuyi
	 * @date 2021/12/7 20:37
	 */
	@Override
	public List<FrontCategoryVO> queryByParamToTree(CategoryQueryRequest categoryQueryRequest) {
		// 1.获取所有的前台分类
		List<FrontCategoryEntity> frontCategoryEntityList = frontCategoryMapper.queryByParam(categoryQueryRequest);
		if (CollectionUtils.isEmpty(frontCategoryEntityList)) {
			return Lists.newArrayList();
		}
		// 组装返回值的树形结构
		List<FrontCategoryVO> frontCategoryVOList = buildResponse(frontCategoryEntityList);
		// 2.获取前台分类关联的后台分类
		Set<Long> frontCategoryIdList = frontCategoryEntityList.stream().map(FrontCategoryEntity::getId).collect(Collectors.toSet());
		Map<Long, List<Long>> frontIdBackIdMap = categoryRelationService.getFrontAndBackCategoryToMap(frontCategoryIdList);
		if (CollectionUtils.isEmpty(frontIdBackIdMap)) {
			return frontCategoryVOList;
		}
		// 3.获取关联的后台分类信息
		Set<Long> backCategoryIdList = frontIdBackIdMap.values().stream().flatMap(Collection::stream).collect(Collectors.toSet());
		List<BackCategoryEntity> backCategoryEntityList = backCategoryService.queryByIdList(backCategoryIdList);
		// 4.组装后台分类的商品数量
		Map<Long, Integer> backIdProductNumMap = BackCategoryEntity.buildCategoryProductCount(backCategoryEntityList, true);
		// 5.组装前台分类对应的商品数量
		Map<Long, Integer> frontIdProductCountMap = buildFrontCategoryProductNumMap(frontIdBackIdMap, backIdProductNumMap);
		// 6.设置返回值
		frontCategoryVOList.forEach(frontCategoryVO -> buildResponseProductNum(frontCategoryVO, frontIdProductCountMap));
		return frontCategoryVOList;
	}

	/**
	 * 根据分类 id 集合查询分类信息
	 *
	 * @param categoryIdList 分类 id 集合
	 * @return 前台分类集合信息
	 * @author liuqiuyi
	 * @date 2021/12/10 17:41
	 */
	@Override
	public List<FrontCategoryEntity> queryByIdList(Set<Long> categoryIdList) {
		if (CollectionUtils.isEmpty(categoryIdList)) {
			return Lists.newArrayList();
		}
		LambdaQueryWrapper<FrontCategoryEntity> wrapper = new LambdaQueryWrapper<>();
		wrapper.in(FrontCategoryEntity::getId, categoryIdList).eq(FrontCategoryEntity::getDelFlag, DelFlagEnum.UN_DELETED.getCode());
		return frontCategoryMapper.selectList(wrapper);
	}

	/**
	 * 根据分类 id 查询分类信息
	 *
	 * @param categoryId 分类 id
	 * @return 前台分类信息
	 * @author liuqiuyi
	 * @date 2021/12/10 17:41
	 */
	@Override
	public FrontCategoryEntity queryById(Long categoryId) {
		if (Objects.isNull(categoryId)) {
			return new FrontCategoryEntity();
		}
		LambdaQueryWrapper<FrontCategoryEntity> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(FrontCategoryEntity::getId, categoryId).eq(FrontCategoryEntity::getDelFlag, DelFlagEnum.UN_DELETED.getCode());
		return frontCategoryMapper.selectOne(wrapper);
	}


	/**
	 * 添加前台分类
	 *
	 * @param categoryRequest 入参信息
	 * @author liuqiuyi
	 * @date 2021/12/10 17:19
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void add(AddOrUpdateFrontCategoryRequest categoryRequest) {
		FrontCategoryEntity parentCategoryEntity = null;
		if (!Objects.isNull(categoryRequest.getParentId()) && !Objects.equals(0L, categoryRequest.getParentId())) {
			// 添加的不是一级分类,校验上级分类是否存在
			parentCategoryEntity = queryById(categoryRequest.getParentId());
			if (Objects.isNull(parentCategoryEntity) || Objects.isNull(parentCategoryEntity.getId())) {
				throw new BusinessException(ErrorEnums.CATEGORY_NOT_EXIST);
			}
		}
		// 如果关联的后台 id 不为空,进行校验
		if (!CollectionUtils.isEmpty(categoryRequest.getBackCategoryIdList())) {
			List<BackCategoryEntity> backCategoryEntityList = backCategoryService.queryByIdList(categoryRequest.getBackCategoryIdList());
			if (CollectionUtils.isEmpty(backCategoryEntityList) || !Objects.equals(backCategoryEntityList.size(), categoryRequest.getBackCategoryIdList().size())) {
				throw new BusinessException(ErrorEnums.CATEGORY_NOT_EXIST);
			}
		}
		// 1.保存前台分类信息
		FrontCategoryEntity categoryEntity = new FrontCategoryEntity();
		categoryEntity.setName(categoryRequest.getCategoryName());
		categoryEntity.setIcon(categoryRequest.getIconUrl());
		categoryEntity.setLevel(Objects.isNull(parentCategoryEntity) ? 1 : parentCategoryEntity.getLevel() + 1);
		categoryEntity.setParentId(Objects.isNull(parentCategoryEntity) ? 0 : parentCategoryEntity.getId());
		categoryEntity.setSort(categoryRequest.getSort());
		categoryEntity.setCreatedBy(categoryRequest.getUserId());
		categoryEntity.setChangedBy(categoryRequest.getUserId());
		categoryEntity.setState(0);
		try {
			frontCategoryMapper.insert(categoryEntity);
		} catch (DuplicateKeyException e) {
			throw new BusinessException(ErrorEnums.CATEGORY_NAME_IS_EXIST);
		}
		if (CollectionUtils.isEmpty(categoryRequest.getBackCategoryIdList())) {
			return;
		}
		// 2.保存前后台分类的关联信息
		batchSaveRelation(categoryRequest, categoryEntity.getId());
	}

	private void batchSaveRelation(AddOrUpdateFrontCategoryRequest categoryRequest, Long categoryId) {
		List<CategoryRelationEntity> saveRelationList = Lists.newArrayListWithCapacity(categoryRequest.getBackCategoryIdList().size());
		for (Long backCategoryId : categoryRequest.getBackCategoryIdList()) {
			CategoryRelationEntity entity = new CategoryRelationEntity();
			entity.setFrontCategoryId(categoryId);
			entity.setBackCategoryId(backCategoryId);
			entity.setCreatedBy(categoryRequest.getUserId());
			entity.setChangedBy(categoryRequest.getUserId());
			saveRelationList.add(entity);
		}
		categoryRelationService.batchSave(saveRelationList);
	}

	/**
	 * 更新前台分类信息
	 *
	 * @param updateFrontCategoryRequest 更新前台分类的参数
	 * @author liuqiuyi
	 * @date 2021/12/13 10:14
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void update(AddOrUpdateFrontCategoryRequest updateFrontCategoryRequest) {
		FrontCategoryEntity frontEntity = new FrontCategoryEntity();
		frontEntity.setId(updateFrontCategoryRequest.getCategoryId());
		frontEntity.setName(updateFrontCategoryRequest.getCategoryName());
		frontEntity.setSort(updateFrontCategoryRequest.getSort());
		if (StringUtils.isEmpty(updateFrontCategoryRequest.getIconUrl())) {
			frontEntity.setIcon(updateFrontCategoryRequest.getIconUrl());
		}
		int updateNum = 0;
		try {
			updateNum = frontCategoryMapper.updateById(frontEntity);
		} catch (DuplicateKeyException e) {
			throw new BusinessException(ErrorEnums.CATEGORY_NAME_IS_EXIST);
		}
		if (updateNum <= 0) {
			throw new BusinessException(ErrorEnums.SAVE_UPDATE_NOT_EXIST);
		}
		if (CollectionUtils.isEmpty(updateFrontCategoryRequest.getBackCategoryIdList())) {
			return;
		}
		// 判断是添加分类关系还是更新分类关系
		List<CategoryRelationEntity> relationEntityList = categoryRelationService.getRelationByFrontCategoryId(updateFrontCategoryRequest.getCategoryId());
		if (!CollectionUtils.isEmpty(relationEntityList)) {
			// 如果不为空,先删除,在更新(可能存在之前只关联了一个,更新时关联多个的情况)
			Set<Long> relationIdList = relationEntityList.stream().map(CategoryRelationEntity::getId).collect(Collectors.toSet());
			categoryRelationService.deletedByIdList(relationIdList);
		}
		batchSaveRelation(updateFrontCategoryRequest, updateFrontCategoryRequest.getCategoryId());
	}

	/**
	 * 更新前台分类状态
	 *
	 * @param categoryId 前台分类 id
	 * @author liuqiuyi
	 * @date 2021/12/13 11:05
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateStateFront(Long categoryId, Long userId) {
		if (Objects.isNull(categoryId) || Objects.isNull(userId)) {
			log.error("com.drstrong.health.product.service.impl.FrontCategoryServiceImpl#updateStateFront() param is null");
			return;
		}
		FrontCategoryEntity categoryEntity = queryById(categoryId);
		if (Objects.isNull(categoryEntity)) {
			throw new BusinessException(ErrorEnums.CATEGORY_NOT_EXIST);
		}
		if (Objects.equals(0, categoryEntity.getState())) {
			// 更新为启用
			categoryEntity.setState(1);
		} else {
			categoryEntity.setState(0);
		}
		categoryEntity.setChangedBy(userId);
		categoryEntity.setChangedAt(LocalDateTime.now());
		frontCategoryMapper.updateById(categoryEntity);
		categoryRelationService.updateStateByFrontCategoryId(categoryId, categoryEntity.getState(), userId);
	}

	/**
	 * 逻辑删除前台分类状态
	 *
	 * @param categoryId 前台分类 id
	 * @param userId     用户 id
	 * @author liuqiuyi
	 * @date 2021/12/13 11:05
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void deleteFrontCategoryById(Long categoryId, Long userId) {
		if (Objects.isNull(categoryId) || Objects.isNull(userId)) {
			log.error("com.drstrong.health.product.service.impl.FrontCategoryServiceImpl#updateStateFront() param is null");
			return;
		}
		FrontCategoryEntity categoryEntity = queryById(categoryId);
		if (Objects.isNull(categoryEntity)) {
			throw new BusinessException(ErrorEnums.CATEGORY_NOT_EXIST);
		}
		categoryEntity.setChangedAt(LocalDateTime.now());
		categoryEntity.setChangedBy(userId);
		categoryEntity.setDelFlag(DelFlagEnum.IS_DELETED.getCode());
		frontCategoryMapper.updateById(categoryEntity);
		categoryRelationService.deletedByFrontCategoryId(categoryId, userId);
	}


	/**
	 * 设置返回值中,前台分类对应的商品数量
	 */
	private void buildResponseProductNum(FrontCategoryVO frontCategoryVO, Map<Long, Integer> frontIdProductCountMap) {
		if (CollectionUtils.isEmpty(frontCategoryVO.getChildren())) {
			frontCategoryVO.setProductCount(frontIdProductCountMap.getOrDefault(frontCategoryVO.getId(), 0));
		} else {
			for (Object child : frontCategoryVO.getChildren()) {
				FrontCategoryVO childResponse = (FrontCategoryVO) child;
				buildResponseProductNum(childResponse, frontIdProductCountMap);
			}
		}
	}

	/**
	 * 组装前台分类对应的商品数量
	 */
	private Map<Long, Integer> buildFrontCategoryProductNumMap(Map<Long, List<Long>> frontIdBackIdMap, Map<Long, Integer> backIdAndNumMap) {
		Map<Long, Integer> frontIdProductCountMap = Maps.newHashMapWithExpectedSize(frontIdBackIdMap.size());
		for (Map.Entry<Long, List<Long>> categoryEntry : frontIdBackIdMap.entrySet()) {
			int productCount = 0;
			for (Long backId : categoryEntry.getValue()) {
				productCount += backIdAndNumMap.getOrDefault(backId, 0);
			}
			frontIdProductCountMap.put(categoryEntry.getKey(), productCount);
		}
		return frontIdProductCountMap;
	}

	/**
	 * 组装返回值的树形结构
	 */
	private List<FrontCategoryVO> buildResponse(List<FrontCategoryEntity> categoryEntityList) {
		List<FrontCategoryVO> frontCategoryVOList = Lists.newArrayListWithCapacity(categoryEntityList.size());
		categoryEntityList.forEach(frontCategoryEntity -> {
			FrontCategoryVO categoryResponse = new FrontCategoryVO();
			BeanUtils.copyProperties(frontCategoryEntity, categoryResponse);
			categoryResponse.setLevelName(LevelEnum.getValueByCode(categoryResponse.getLevel()));
			categoryResponse.setCategoryName(frontCategoryEntity.getName());
			categoryResponse.setProductCount(0);
			categoryResponse.setCategoryStatus(frontCategoryEntity.getState());
			categoryResponse.setCreateTime(frontCategoryEntity.getCreatedAt());

			frontCategoryVOList.add(categoryResponse);
		});
		return BaseTree.listToTree(frontCategoryVOList);
	}
}
