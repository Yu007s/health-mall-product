package com.drstrong.health.product.service.impl;

import com.drstrong.health.product.dao.FrontCategoryMapper;
import com.drstrong.health.product.model.entity.category.FrontCategoryEntity;
import com.drstrong.health.product.model.response.category.FrontCategoryResponse;
import com.drstrong.health.product.service.FrontCategoryService;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;

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

	/**
	 * 查询所有的前台分类
	 *
	 * @return 前台分类的集合
	 * @author liuqiuyi
	 * @date 2021/12/7 20:37
	 */
	@Override
	public List<FrontCategoryResponse> queryAll() {
		List<FrontCategoryEntity> frontCategoryEntityList = frontCategoryMapper.selectList(null);
		if (CollectionUtils.isEmpty(frontCategoryEntityList)) {
			return Lists.newArrayList();
		}
		List<FrontCategoryResponse> categoryResponseList = Lists.newArrayListWithCapacity(frontCategoryEntityList.size());
		frontCategoryEntityList.forEach(frontCategoryEntity -> {
			FrontCategoryResponse frontCategoryResponse = new FrontCategoryResponse();
			BeanUtils.copyProperties(frontCategoryEntity, frontCategoryResponse);
			categoryResponseList.add(frontCategoryResponse);
		});
		return categoryResponseList;
	}
}
