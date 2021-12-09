package com.drstrong.health.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.drstrong.health.product.dao.CategoryRelationMapper;
import com.drstrong.health.product.model.entity.category.CategoryRelationEntity;
import com.drstrong.health.product.model.enums.DelFlagEnum;
import com.drstrong.health.product.service.CategoryRelationService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.stream.Collectors.*;

/**
 * 前后台分类
 *
 * @author liuqiuyi
 * @date 2021/12/9 16:13
 */
@Service
@Slf4j
public class CategoryRelationServiceImpl implements CategoryRelationService {
	@Resource
	CategoryRelationMapper categoryRelationMapper;

	/**
	 * 根据前台分类 id 获取关联信息
	 *
	 * @param frontCategoryIdList 前台分类 id 集合
	 * @return 分类关联信息
	 * @author liuqiuyi
	 * @date 2021/12/9 16:32
	 */
	@Override
	public List<CategoryRelationEntity> getRelationByFrontCategoryIds(Set<Long> frontCategoryIdList) {
		if (CollectionUtils.isEmpty(frontCategoryIdList)) {
			return Lists.newArrayList();
		}
		LambdaQueryWrapper<CategoryRelationEntity> frontWrapper = new LambdaQueryWrapper<>();
		frontWrapper.in(CategoryRelationEntity::getFrontCategoryId, frontCategoryIdList).eq(CategoryRelationEntity::getDelFlag, DelFlagEnum.UN_DELETED.getCode());
		return categoryRelationMapper.selectList(frontWrapper);
	}

	/**
	 * 根据前台分类 id 获取关联信息,并且组装为 map
	 * <p>注意:前台分类能对应多个后台分类</>
	 *
	 * @param frontCategoryIdList 前台分类 id 集合
	 * @return 分类的对应关系, key = 前台分类 id,value=后台分类id集合
	 * @author liuqiuyi
	 * @date 2021/12/9 16:36
	 */
	@Override
	public Map<Long, List<Long>> getFrontAndBackCategoryToMap(Set<Long> frontCategoryIdList) {
		List<CategoryRelationEntity> relationByFrontCategoryIds = getRelationByFrontCategoryIds(frontCategoryIdList);
		if (CollectionUtils.isEmpty(relationByFrontCategoryIds)) {
			return Maps.newHashMap();
		}
		return relationByFrontCategoryIds.stream().collect(groupingBy(CategoryRelationEntity::getFrontCategoryId, mapping(CategoryRelationEntity::getBackCategoryId, toList())));
	}
}
