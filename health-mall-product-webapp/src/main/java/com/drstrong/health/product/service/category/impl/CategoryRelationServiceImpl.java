package com.drstrong.health.product.service.category.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.drstrong.health.product.dao.category.CategoryRelationMapper;
import com.drstrong.health.product.model.entity.category.CategoryRelationEntity;
import com.drstrong.health.product.model.enums.DelFlagEnum;
import com.drstrong.health.product.model.enums.ErrorEnums;
import com.drstrong.health.product.model.response.result.BusinessException;
import com.drstrong.health.product.service.category.CategoryRelationService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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
	 * @param frontCategoryId 前台分类 id
	 * @return 分类关联信息
	 * @author liuqiuyi
	 * @date 2021/12/9 16:32
	 */
	@Override
	public List<CategoryRelationEntity> getRelationByFrontCategoryId(Long frontCategoryId) {
		if (Objects.isNull(frontCategoryId)) {
			return Lists.newArrayList();
		}
		LambdaQueryWrapper<CategoryRelationEntity> frontWrapper = new LambdaQueryWrapper<>();
		frontWrapper.eq(CategoryRelationEntity::getFrontCategoryId, frontCategoryId).eq(CategoryRelationEntity::getDelFlag, DelFlagEnum.UN_DELETED.getCode());
		return categoryRelationMapper.selectList(frontWrapper);
	}

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
	public Map<Long, Set<Long>> getFrontAndBackCategoryToMap(Set<Long> frontCategoryIdList) {
		List<CategoryRelationEntity> relationByFrontCategoryIds = getRelationByFrontCategoryIds(frontCategoryIdList);
		if (CollectionUtils.isEmpty(relationByFrontCategoryIds)) {
			return Maps.newHashMap();
		}
		return relationByFrontCategoryIds.stream()
				.collect(groupingBy(CategoryRelationEntity::getFrontCategoryId, mapping(CategoryRelationEntity::getBackCategoryId, toSet())));
	}

	/**
	 * 保存关联信息
	 *
	 * @param relationEntityList 入参集合
	 * @author liuqiuyi
	 * @date 2021/12/10 17:59
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void batchSave(List<CategoryRelationEntity> relationEntityList) {
		if (CollectionUtils.isEmpty(relationEntityList)) {
			log.info("CategoryRelationServiceImpl#save() param is null");
			return;
		}
		Integer num = categoryRelationMapper.batchInsert(relationEntityList);
		if (num <= 0) {
			throw new BusinessException(ErrorEnums.SAVE_UPDATE_NOT_EXIST);
		}
	}

	/**
	 * 根据主键 id 集合进行删除
	 *
	 * @param relationIdList 关联关系的主键 id 集合
	 * @author liuqiuyi
	 * @date 2021/12/13 10:52
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void deletedByIdList(Set<Long> relationIdList) {
		if (CollectionUtils.isEmpty(relationIdList)) {
			log.info("CategoryRelationServiceImpl#deletedByIdList() param is null");
			return;
		}
		categoryRelationMapper.deleteBatchIds(relationIdList);
	}

	/**
	 * 根据前台分类 id 进行逻辑删除
	 *
	 * @param frontCategoryIdList 前台分类 id
	 * @author liuqiuyi
	 * @date 2021/12/13 11:10
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void deletedByFrontCategoryIdList(Set<Long> frontCategoryIdList, String userId) {
		if (CollectionUtils.isEmpty(frontCategoryIdList) || Objects.isNull(userId)) {
			log.error("categoryRelationServiceImpl.deletedByFrontCategoryIdList param is null.param:{},{}", frontCategoryIdList, userId);
			return;
		}
		categoryRelationMapper.deletedByFrontCategoryIdList(frontCategoryIdList, userId);
	}

	/**
	 * 根据前台分类 id 进行逻辑删除
	 *
	 * @param frontCategoryIdList 前台分类 id
	 * @param state               状态
	 * @param userId              用户 id
	 * @author liuqiuyi
	 * @date 2021/12/13 11:10
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateStateByFrontCategoryIdList(Set<Long> frontCategoryIdList, Integer state, String userId) {
		if (CollectionUtils.isEmpty(frontCategoryIdList) || Objects.isNull(userId) || Objects.isNull(state)) {
			log.error("categoryRelationServiceImpl.deletedByFrontCategoryIdList param is null.param:{},{},{}", frontCategoryIdList, state, userId);
			return;
		}
		categoryRelationMapper.updateStateByFrontCategoryIdList(frontCategoryIdList, state, userId);
	}
}
