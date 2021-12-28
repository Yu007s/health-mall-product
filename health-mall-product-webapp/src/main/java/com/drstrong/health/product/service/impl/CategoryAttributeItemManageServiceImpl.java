package com.drstrong.health.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.drstrong.health.product.dao.CategoryAttributeItemMapper;
import com.drstrong.health.product.dao.CategoryAttributeMapper;
import com.drstrong.health.product.model.entity.category.CategoryAttributeEntity;
import com.drstrong.health.product.model.entity.product.CategoryAttributeItemEntity;
import com.drstrong.health.product.model.response.result.BusinessException;
import com.drstrong.health.product.service.CategoryAttributeItemManageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.Resource;

/**
 * 分类属性管理 service
 * <p> 参照之前的代码,未改动 </>
 *
 * @author liuqiuyi
 * @date 2021/12/27 20:03
 */
@Slf4j
@Service
public class CategoryAttributeItemManageServiceImpl implements CategoryAttributeItemManageService {
	@Resource
	CategoryAttributeMapper categoryAttributeMapper;

	@Resource
	private CategoryAttributeItemMapper categoryAttributeItemMapper;


	private CategoryAttributeEntity getAttribute(Long attributeId) throws BusinessException {
		if (attributeId == null) {
			throw new BusinessException("分类属性ID不能为空");
		}
		CategoryAttributeEntity attribute = categoryAttributeMapper.selectById(attributeId);
		if (attribute == null) {
			throw new BusinessException("分类属性不存在，id=" + attributeId);
		}
		return attribute;
	}

	/**
	 * 保存分类属性项
	 * <p> 参照之前的代码,未改动 </>
	 *
	 * @param vo 分类属性项数据
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public CategoryAttributeItemEntity saveItem(CategoryAttributeItemEntity vo) throws BusinessException {
		Assert.notNull(vo, "分类属性项数据不能为空");
		Long attributeItemId = vo.getId();
		boolean isNew = attributeItemId == null;
		CategoryAttributeItemEntity entity = null;
		if (isNew) {
			//保存
			Long categoryId = vo.getCategoryId();
			Long attributeId = vo.getAttributeId();
			if (categoryId == null) {
				throw new BusinessException("categoryId不能为空");
			}
			if (attributeId == null) {
				throw new BusinessException("attributeId不能为空");
			}
			QueryWrapper<CategoryAttributeItemEntity> query = Wrappers.query();
			query.eq("category_id", categoryId).eq("attribute_id", attributeId);
			if (categoryAttributeItemMapper.selectCount(query) > 0) {
				throw new BusinessException("请不要重复添加分类属性");
			}
			CategoryAttributeEntity attribute = getAttribute(attributeId);
			entity = new CategoryAttributeItemEntity();
			BeanUtils.copyProperties(vo, entity);
			entity.setAttributeId(attribute.getId());
			entity.setAttributeKey(attribute.getAttributeKey());
			entity.setAttributeName(attribute.getAttributeName());
			entity.setAttributeType(attribute.getAttributeType());
			entity.setFormType(attribute.getFormType());
			entity.setAttributeOptions(attribute.getAttributeOptions());
			categoryAttributeItemMapper.insert(entity);
		} else {
			//更新
			entity = getAttributeItem(attributeItemId);
			entity.setRequired(vo.getRequired());
			entity.setOrderNumber(vo.getOrderNumber());
			entity.setChangedAt(vo.getChangedAt());
			entity.setChangedBy(vo.getChangedBy());
			categoryAttributeItemMapper.updateById(entity);
		}
		return entity;
	}

	private CategoryAttributeItemEntity getAttributeItem(Long attributeItemId) throws BusinessException {
		if (attributeItemId == null) {
			throw new BusinessException("分类属性ID不能为空");
		}
		CategoryAttributeItemEntity attributeItem = categoryAttributeItemMapper.selectById(attributeItemId);
		if (attributeItem == null) {
			throw new BusinessException("分类属性项不存在，id=" + attributeItemId);
		}
		return attributeItem;
	}

	/**
	 * 删除分类属性项
	 *
	 * @param attributeItemId 分类属性项ID
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public CategoryAttributeItemEntity deleteItem(Long attributeItemId) throws BusinessException {
		CategoryAttributeItemEntity entity = getAttributeItem(attributeItemId);
		categoryAttributeItemMapper.deleteById(attributeItemId);
		return entity;
	}
}
