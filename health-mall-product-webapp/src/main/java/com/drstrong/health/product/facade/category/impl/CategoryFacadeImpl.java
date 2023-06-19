package com.drstrong.health.product.facade.category.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.drstrong.health.product.facade.category.CategoryFacade;
import com.drstrong.health.product.model.entity.category.v3.CategoryEntity;
import com.drstrong.health.product.model.enums.ProductTypeEnum;
import com.drstrong.health.product.model.response.category.v3.CategoryVO;
import com.drstrong.health.product.service.category.v3.CategoryService;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author liuqiuyi
 * @date 2023/6/12 15:58
 */
@Slf4j
@Service
public class CategoryFacadeImpl implements CategoryFacade {
	/**
	 * 西药分类中,需要过滤的id,目前仅有协定方不展示在西药分类中
	 */
	@Value("${western.filter.id}")
	private Long westernFilterId;

	@Resource
	CategoryService categoryService;

	/**
	 * 根据类型获取分类
	 * <p>
	 * 中西药分类的返回值中去除了 协定方
	 * </>
	 *
	 * @param productType
	 * @author liuqiuyi
	 * @date 2023/6/12 16:05
	 */
	@Override
	public List<CategoryVO> queryAllCategoryByProductType(Integer productType) {
		if (ObjectUtil.equals(ProductTypeEnum.CHINESE.getCode(), productType) || ObjectUtil.equals(ProductTypeEnum.MEDICINE.getCode(), productType)) {
			// 1.查询分类,并过滤掉协定方
			List<CategoryEntity> westernCategoryList = categoryService.queryWesternAll();
			westernCategoryList.removeIf(categoryEntity -> ObjectUtil.equals(categoryEntity.getId(), westernFilterId));
			return BeanUtil.copyToList(westernCategoryList, CategoryVO.class);
		}
		// 后续其他类型在扩展....
		return Lists.newArrayList();
	}
}
