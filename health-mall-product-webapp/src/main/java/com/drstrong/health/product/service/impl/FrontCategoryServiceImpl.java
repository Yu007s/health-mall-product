package com.drstrong.health.product.service.impl;

import com.drstrong.health.product.dao.FrontCategoryMapper;
import com.drstrong.health.product.model.entity.category.BackCategoryEntity;
import com.drstrong.health.product.model.entity.category.FrontCategoryEntity;
import com.drstrong.health.product.model.enums.LevelEnum;
import com.drstrong.health.product.model.request.category.CategoryQueryRequest;
import com.drstrong.health.product.model.response.category.BaseTree;
import com.drstrong.health.product.model.response.category.FrontCategoryResponse;
import com.drstrong.health.product.service.BackCategoryService;
import com.drstrong.health.product.service.CategoryRelationService;
import com.drstrong.health.product.service.FrontCategoryService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toMap;

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
	 * 查询所有的前台分类
	 *
	 * @param categoryQueryRequest 查询的参数信息
	 * @return 前台分类的集合
	 * @author liuqiuyi
	 * @date 2021/12/7 20:37
	 */
	@Override
	public List<FrontCategoryResponse> queryByParam(CategoryQueryRequest categoryQueryRequest) {
		// 1.获取所有的前台分类
		List<FrontCategoryEntity> frontCategoryEntityList = frontCategoryMapper.queryByParam(categoryQueryRequest);
		if (CollectionUtils.isEmpty(frontCategoryEntityList)) {
			return Lists.newArrayList();
		}
		// 组装返回值
		List<FrontCategoryResponse> frontCategoryResponseList = buildResponse(frontCategoryEntityList);
		// 2.获取前台分类关联的后台分类
		Set<Long> frontCategoryIdList = frontCategoryEntityList.stream().map(FrontCategoryEntity::getId).collect(Collectors.toSet());
		Map<Long, List<Long>> frontIdBackIdMap = categoryRelationService.getFrontAndBackCategoryToMap(frontCategoryIdList);
		if (CollectionUtils.isEmpty(frontIdBackIdMap)) {
			return frontCategoryResponseList;
		}
		// 3.获取所有的后台分类
		Set<Long> backCategoryIdList = frontIdBackIdMap.values().stream().flatMap(Collection::stream).collect(Collectors.toSet());
		List<BackCategoryEntity> backCategoryEntityList = backCategoryService.queryByIdList(backCategoryIdList);
		if (CollectionUtils.isEmpty(backCategoryEntityList)) {
			return frontCategoryResponseList;
		}
		// 4.获取后台分类的商品数量
		// TODO 这里需要优化,前台关联的可能是后台分类的父分类
		Map<Long, Integer> backIdAndNumMap = backCategoryEntityList.stream()
				.filter(backCategoryEntity -> Objects.nonNull(backCategoryEntity.getPNumber()))
				.collect(toMap(BackCategoryEntity::getId, BackCategoryEntity::getPNumber, (v1, v2) -> v1));
		// 6.设置商品数量
		Map<Long, Integer> frontIdProductCountMap = buildFrontCategoryProductNumMap(frontIdBackIdMap, backIdAndNumMap);
		frontCategoryResponseList.forEach(frontCategoryResponse -> {
			frontCategoryResponse.setProductCount(frontIdProductCountMap.getOrDefault(frontCategoryResponse.getId(), 0));
			if (!CollectionUtils.isEmpty(frontCategoryResponse.getChildren())) {
				frontCategoryResponse.getChildren().forEach(children -> {
					FrontCategoryResponse response = (FrontCategoryResponse) children;
					response.setProductCount(frontIdProductCountMap.getOrDefault(response.getId(), 0));
				});
			}
		});
		return frontCategoryResponseList;
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

	private Map<Long, Integer> buildBackIdAndNumMap(List<BackCategoryEntity> backCategoryEntityList){
		return null;

	}
	/**
	 * 组装返回值的树形结构
	 */
	private List<FrontCategoryResponse> buildResponse(List<FrontCategoryEntity> categoryEntityList) {
		List<FrontCategoryResponse> frontCategoryResponseList = Lists.newArrayListWithCapacity(categoryEntityList.size());
		categoryEntityList.forEach(frontCategoryEntity -> {
			FrontCategoryResponse categoryResponse = new FrontCategoryResponse();
			BeanUtils.copyProperties(frontCategoryEntity, categoryResponse);
			categoryResponse.setLevelName(LevelEnum.getValueByCode(categoryResponse.getLevel()));
			categoryResponse.setCategoryName(frontCategoryEntity.getName());
			categoryResponse.setProductCount(0);
			categoryResponse.setCategoryStatus(frontCategoryEntity.getState());
			categoryResponse.setCreateTime(frontCategoryEntity.getCreatedAt());

			frontCategoryResponseList.add(categoryResponse);
		});
		return BaseTree.listToTree(frontCategoryResponseList);
	}
}
