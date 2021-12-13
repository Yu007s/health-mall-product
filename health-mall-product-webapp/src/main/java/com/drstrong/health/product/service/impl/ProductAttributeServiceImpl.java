package com.drstrong.health.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.drstrong.health.product.dao.ProductAttributeMapper;
import com.drstrong.health.product.model.entity.product.ProductAttributeEntity;
import com.drstrong.health.product.model.enums.DelFlagEnum;
import com.drstrong.health.product.service.ProductAttributeService;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * 商品关联属性值 service
 *
 * @author liuqiuyi
 * @date 2021/12/13 16:21
 */
@Service
@Slf4j
public class ProductAttributeServiceImpl extends ServiceImpl<ProductAttributeMapper, ProductAttributeEntity> implements ProductAttributeService {
	@Resource
	ProductAttributeMapper productAttributeMapper;

	/**
	 * 保存关联关系
	 *
	 * @param saveEntityList 保存入参
	 * @author liuqiuyi
	 * @date 2021/12/13 16:21
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void batchSave(List<ProductAttributeEntity> saveEntityList) {
		if (CollectionUtils.isEmpty(saveEntityList)) {
			return;
		}
		productAttributeMapper.batchInsert(saveEntityList);
	}

	/**
	 * 根据商品 id 进行逻辑删除
	 *
	 * @author liuqiuyi
	 * @date 2021/12/13 19:42
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void deletedByProductId(Long productId, Long userId) {
		if (Objects.isNull(productId) || Objects.isNull(userId)) {
			return;
		}
		ProductAttributeEntity entity = new ProductAttributeEntity();
		entity.setDelFlag(DelFlagEnum.IS_DELETED.getCode());
		entity.setChangedBy(userId);
		entity.setChangedAt(LocalDateTime.now());

		LambdaQueryWrapper<ProductAttributeEntity> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.eq(ProductAttributeEntity::getProductId, productId).eq(ProductAttributeEntity::getDelFlag, DelFlagEnum.UN_DELETED.getCode());
		productAttributeMapper.update(entity, queryWrapper);
	}

	/**
	 * 根据 商品 id 查询关联的属性信息
	 *
	 * @param productId 商品 id
	 * @return 属性信息
	 * @author liuqiuyi
	 * @date 2021/12/13 19:36
	 */
	@Override
	public List<ProductAttributeEntity> queryByProductId(Long productId) {
		if (Objects.isNull(productId)) {
			return Lists.newArrayList();
		}
		LambdaQueryWrapper<ProductAttributeEntity> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.eq(ProductAttributeEntity::getProductId, productId).eq(ProductAttributeEntity::getDelFlag, DelFlagEnum.UN_DELETED.getCode());
		return productAttributeMapper.selectList(queryWrapper);
	}
}
