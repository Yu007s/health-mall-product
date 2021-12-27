package com.drstrong.health.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.drstrong.health.product.dao.ProductAttributeMapper;
import com.drstrong.health.product.model.entity.product.CategoryAttributeItemEntity;
import com.drstrong.health.product.model.entity.product.ProductAttributeEntity;
import com.drstrong.health.product.model.entity.product.ProductBasicsInfoEntity;
import com.drstrong.health.product.model.entity.product.ProductExtendEntity;
import com.drstrong.health.product.model.enums.DelFlagEnum;
import com.drstrong.health.product.model.enums.ErrorEnums;
import com.drstrong.health.product.model.enums.UpOffEnum;
import com.drstrong.health.product.model.response.product.ProductPropertyVO;
import com.drstrong.health.product.model.response.result.BusinessException;
import com.drstrong.health.product.service.CategoryAttributeItemService;
import com.drstrong.health.product.service.ProductAttributeService;
import com.drstrong.health.product.service.ProductBasicsInfoService;
import com.drstrong.health.product.service.ProductExtendService;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 商品关联属性值 service
 *
 * @author liuqiuyi
 * @date 2021/12/13 16:21
 */
@Service
@Slf4j
public class ProductAttributeServiceImpl extends ServiceImpl<ProductAttributeMapper, ProductAttributeEntity> implements ProductAttributeService {
	private static final String BRAND_NAME = "商品品牌";
	private static final String APPROVAL_NUMBER = "批文编号";
	private static final String VENDOR_NAME = "生产厂商";

	@Resource
	ProductAttributeMapper productAttributeMapper;

	@Resource
	ProductBasicsInfoService productBasicsInfoService;

	@Resource
	ProductExtendService productExtendService;

	@Resource
	CategoryAttributeItemService categoryAttributeItemService;

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
	public void deletedByProductId(Long productId, String userId) {
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

	/**
	 * 根据 spuCode 查询商品属性
	 *
	 * @param spuCode   商品编码
	 * @param productId 商品 id
	 * @return 属性集合
	 * @author liuqiuyi
	 * @date 2021/12/17 14:32
	 */
	@Override
	public List<ProductPropertyVO> getPropertyByCode(String spuCode, Long productId) {
		if (StringUtils.isBlank(spuCode) && Objects.isNull(productId)) {
			return Lists.newArrayList();
		}
		// 1.校验 spuCode
		ProductBasicsInfoEntity basicsInfoEntity = productBasicsInfoService.getByProductIdOrSpuCode(productId, spuCode, UpOffEnum.UP);
		if (Objects.isNull(basicsInfoEntity)) {
			throw new BusinessException(ErrorEnums.PRODUCT_NOT_EXIST);
		}
		// 2.查询商品扩展信息
		ProductExtendEntity extendEntity = productExtendService.queryByProductId(basicsInfoEntity.getId());
		// 3.查询属性信息
		List<ProductAttributeEntity> attributeEntityList = queryByProductId(basicsInfoEntity.getId());
		Set<Long> attributeItemIdList = attributeEntityList.stream().map(ProductAttributeEntity::getAttributeItemId).collect(Collectors.toSet());
		Map<Long, CategoryAttributeItemEntity> idEntityMap = categoryAttributeItemService.queryByIdListToMap(attributeItemIdList, basicsInfoEntity.getCategoryId());
		return buildProductPropertyVO(basicsInfoEntity, extendEntity, attributeEntityList, idEntityMap);
	}

	private List<ProductPropertyVO> buildProductPropertyVO(ProductBasicsInfoEntity basicsInfoEntity, ProductExtendEntity extendEntity,
														   List<ProductAttributeEntity> attributeEntityList, Map<Long, CategoryAttributeItemEntity> idEntityMap) {
		List<ProductPropertyVO> productPropertyVOList = Lists.newArrayListWithCapacity(attributeEntityList.size());
		// 1.组装固定属性
		productPropertyVOList.add(new ProductPropertyVO(BRAND_NAME, basicsInfoEntity.getBrandName()));
		productPropertyVOList.add(new ProductPropertyVO(APPROVAL_NUMBER, extendEntity.getApprovalNumber()));
		productPropertyVOList.add(new ProductPropertyVO(VENDOR_NAME, extendEntity.getVendorName()));
		// 2.组装动态属性
		for (ProductAttributeEntity attributeEntity : attributeEntityList) {
			String attributeName = idEntityMap.getOrDefault(attributeEntity.getAttributeItemId(), new CategoryAttributeItemEntity()).getAttributeName();
			productPropertyVOList.add(new ProductPropertyVO(attributeName, attributeEntity.getAttributeValue()));
		}
		return productPropertyVOList;
	}
}
