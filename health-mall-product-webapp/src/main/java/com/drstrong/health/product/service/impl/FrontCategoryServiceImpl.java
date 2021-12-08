package com.drstrong.health.product.service.impl;

import com.drstrong.health.product.mapper.FrontCategoryMapper;
import com.drstrong.health.product.model.entity.category.FrontCategoryEntity;
import com.drstrong.health.product.model.response.category.FrontCategoryResponse;
import com.drstrong.health.product.service.FrontCategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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

		return null;
	}
}
