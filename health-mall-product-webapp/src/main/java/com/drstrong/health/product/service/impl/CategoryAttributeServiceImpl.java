package com.drstrong.health.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.drstrong.health.product.dao.CategoryAttributeItemMapper;
import com.drstrong.health.product.model.entity.category.BackCategoryEntity;
import com.drstrong.health.product.model.entity.product.CategoryAttributeItemEntity;
import com.drstrong.health.product.model.response.product.CategoryAttributeItemVO;
import com.drstrong.health.product.service.BackCategoryService;
import com.drstrong.health.product.service.CategoryAttributeService;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toMap;

/**
 * 后台分类关联的属性值 service
 *
 * @author liuqiuyi
 * @date 2021/12/13 14:35
 */
@Slf4j
@Service
public class CategoryAttributeServiceImpl implements CategoryAttributeService {

	@Resource
	BackCategoryService backCategoryService;

	@Resource
	CategoryAttributeItemMapper categoryAttributeItemMapper;

	/**
	 * 根据 id 集合查询商品关联的属性
	 *
	 * @param attributeIdList 属性 id 集合
	 * @return 关联的属性集合
	 * @author liuqiuyi
	 * @date 2021/12/13 20:57
	 */
	@Override
	public List<CategoryAttributeItemEntity> queryByAttributeIdList(Set<Long> attributeIdList, Long categoryId) {
		if (CollectionUtils.isEmpty(attributeIdList) || Objects.isNull(categoryId)) {
			log.error("invoke CategoryAttributeServiceImpl.queryByAttributeIdList param is null ,param:{},{}", attributeIdList, categoryId);
			return Lists.newArrayList();
		}
		LambdaQueryWrapper<CategoryAttributeItemEntity> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.eq(CategoryAttributeItemEntity::getCategoryId, categoryId).in(CategoryAttributeItemEntity::getAttributeId, attributeIdList);
		return categoryAttributeItemMapper.selectList(queryWrapper);
	}

	/**
	 * 根据 id 集合查询商品关联的属性,并转成 map结构
	 *
	 * @param attributeIdList 属性 id 集合
	 * @return 属性的 map 结构 ,key = id,value = 属性值
	 * @author liuqiuyi
	 * @date 2021/12/13 21:08
	 */
	@Override
	public Map<Long, CategoryAttributeItemEntity> queryByIdListToMap(Set<Long> attributeIdList, Long categoryId) {
		List<CategoryAttributeItemEntity> attributeItemEntityList = queryByAttributeIdList(attributeIdList, categoryId);
		return attributeItemEntityList.stream().collect(toMap(CategoryAttributeItemEntity::getAttributeId, dto -> dto, (v1, v2) -> v1));
	}

	/**
	 * 查找指定分类的属性项
	 * <p> 参照之前的代码 </>
	 *
	 * @param categoryId 分类 id
	 * @return 商品属性返回值
	 * @author liuqiuyi
	 * @date 2021/12/13 14:34
	 */
	@Override
	public List<CategoryAttributeItemVO> getItems(Long categoryId) {
		BackCategoryEntity category = backCategoryService.queryById(categoryId);
		if (category == null) {
			return Lists.newArrayList();
		}
		//获取该分类和所有上级分类的属性项
		Set<String> categoryIdItems = StringUtils.commaDelimitedListToSet(category.getIdPath());
		Set<Long> categoryIds = categoryIdItems.stream().map(Long::parseLong).collect(Collectors.toSet());

		// 这里为空直接返回,避免下面的查询语句出错
		if (CollectionUtils.isEmpty(categoryIds)) {
			return Lists.newArrayList();
		}

		QueryWrapper<CategoryAttributeItemEntity> query = Wrappers.query();
		query.in("category_id", categoryIds);
		query.orderByAsc("order_number", "id");
		List<CategoryAttributeItemEntity> items = categoryAttributeItemMapper.selectList(query);
		List<CategoryAttributeItemVO> itemVOList = new ArrayList<>();
		items.forEach(item -> {
			CategoryAttributeItemVO itemVO = new CategoryAttributeItemVO();
			BeanUtils.copyProperties(item, itemVO);
			itemVO.setOptions(convertAsOptions(item.getAttributeOptions()));
			itemVOList.add(itemVO);
		});
		return itemVOList;
	}

	private static List<Map<String, Object>> convertAsOptions(String attributeOptions) {
		if (StringUtils.hasText(attributeOptions)) {
			String[] items = StringUtils.commaDelimitedListToStringArray(attributeOptions);
			List<Map<String, Object>> options = new ArrayList<>();
			for (String item : items) {
				Map<String, Object> option = new HashMap<>(4);
				option.put("label", item);
				option.put("value", item);
				options.add(option);
			}
			return options;
		}
		return Collections.emptyList();
	}
}
