package com.drstrong.health.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.drstrong.health.product.dao.ProductBasicsInfoMapper;
import com.drstrong.health.product.model.entity.category.BackCategoryEntity;
import com.drstrong.health.product.model.entity.product.*;
import com.drstrong.health.product.model.enums.DelFlagEnum;
import com.drstrong.health.product.model.enums.ErrorEnums;
import com.drstrong.health.product.model.request.product.SaveProductRequest;
import com.drstrong.health.product.model.response.product.ProductManageVO;
import com.drstrong.health.product.model.response.result.BusinessException;
import com.drstrong.health.product.service.*;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 商品基础信息 service
 *
 * @author liuqiuyi
 * @date 2021/12/13 15:49
 */
@Service
@Slf4j
public class ProductBasicsInfoServiceImpl extends ServiceImpl<ProductBasicsInfoMapper, ProductBasicsInfoEntity> implements ProductBasicsInfoService {
	@Resource
	ProductBasicsInfoMapper productBasicsInfoMapper;

	@Resource
	ProductExtendService productExtendService;

	@Resource
	ProductAttributeService productAttributeService;

	@Resource
	ProductSkuService productSkuService;

	@Resource
	CategoryAttributeService categoryAttributeService;

	@Resource
	BackCategoryService backCategoryService;

	/**
	 * 根据商品 id 查询商品基础信息
	 *
	 * @param productId 商品 id
	 * @return 商品基础信息
	 * @author liuqiuyi
	 * @date 2021/12/13 20:30
	 */
	@Override
	public ProductBasicsInfoEntity queryBasicsInfoByProductId(Long productId) {
		if (Objects.isNull(productId)) {
			return new ProductBasicsInfoEntity();
		}
		LambdaQueryWrapper<ProductBasicsInfoEntity> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.eq(ProductBasicsInfoEntity::getId, productId).eq(ProductBasicsInfoEntity::getDelFlag, DelFlagEnum.UN_DELETED.getCode());
		return productBasicsInfoMapper.selectOne(queryWrapper);
	}

	/**
	 * 保存或者更新商品信息
	 *
	 * @param saveProductRequest 保存商品
	 * @author liuqiuyi
	 * @date 2021/12/13 15:48
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveOrUpdateProduct(SaveProductRequest saveProductRequest) {
		// 1.校验店铺 id 是否存在
		// TODO 需要调用店铺的接口
		// 2.保存基础信息
		ProductBasicsInfoEntity infoEntity = saveProductBaseInfo(saveProductRequest);
		// 3.保存扩展信息
		saveProductExtend(saveProductRequest, infoEntity.getId());
		// 4.保存商品分类属性信息
		saveAttribute(saveProductRequest, infoEntity.getId());
		// 5.保存 sku 信息
		List<ProductSkuEntity> skuEntityList = Lists.newArrayListWithCapacity(saveProductRequest.getPackInfoList().size());
		for (SaveProductRequest.PackInfoRequest packInfoRequest : saveProductRequest.getPackInfoList()) {
			ProductSkuEntity skuEntity = new ProductSkuEntity();
			skuEntity.setProductId(infoEntity.getId());
//			skuEntity.setSkuCode();
//			skuEntity.setSkuName();
			skuEntity.setPackName(packInfoRequest.getPackName());
			skuEntity.setPackValue(packInfoRequest.getPackValue());
//			skuEntity.setSkuPrice(packInfoRequest.getPrice());
			skuEntity.setCommAttribute(packInfoRequest.getCommAttribute());
			skuEntity.setCreatedBy(saveProductRequest.getUserId());
			skuEntity.setChangedBy(saveProductRequest.getUserId());
			skuEntityList.add(skuEntity);
		}
		productSkuService.batchSave(skuEntityList);
	}

	/**
	 * 根据商品 id,查询管理端商品信息
	 *
	 * @param productId 商品 id
	 * @return 商品管理端信息
	 * @author liuqiuyi
	 * @date 2021/12/13 20:26
	 */
	@Override
	public ProductManageVO queryManageProductInfo(Long productId) {
		if (Objects.isNull(productId)) {
			return new ProductManageVO();
		}
		// 1.查询基本信息
		ProductBasicsInfoEntity basicsInfoEntity = queryBasicsInfoByProductId(productId);
		if (Objects.isNull(basicsInfoEntity) || Objects.isNull(basicsInfoEntity.getId())) {
			throw new BusinessException(ErrorEnums.PRODUCT_NOT_EXIST.getCode());
		}
		// 2.查询分类名称
		BackCategoryEntity backCategoryEntity = backCategoryService.queryById(basicsInfoEntity.getCategoryId());
		// 3.查询扩展信息
		ProductExtendEntity extendEntity = productExtendService.queryByProductId(productId);
		// 4.查询属性信息
		List<ProductAttributeEntity> attributeEntityList = productAttributeService.queryByProductId(productId);
		Set<Long> attributeItemIdList = attributeEntityList.stream().map(ProductAttributeEntity::getAttributeItemId).collect(Collectors.toSet());
		Map<Long, CategoryAttributeItemEntity> idEntityMap = categoryAttributeService.queryByIdListToMap(attributeItemIdList);
		// 5.查询 sku 信息
		List<ProductSkuEntity> skuEntityList = productSkuService.queryByProductId(productId);
		// 6.组装返回值

		return null;
	}

	private ProductManageVO buildProductManageVO(ProductBasicsInfoEntity basicsInfoEntity, ProductExtendEntity extendEntity, BackCategoryEntity backCategoryEntity
			, List<ProductAttributeEntity> attributeEntityList, Map<Long, CategoryAttributeItemEntity> idEntityMap, List<ProductSkuEntity> skuEntityList) {

		ProductManageVO productManageVO = new ProductManageVO();
		productManageVO.setProductId(basicsInfoEntity.getId());
		productManageVO.setSpuCode(basicsInfoEntity.getSpuCode());
		productManageVO.setCategoryId(basicsInfoEntity.getCategoryId());
		productManageVO.setCategoryPathName(backCategoryEntity.getName());
		productManageVO.setTitle(basicsInfoEntity.getTitle());
		productManageVO.setBrandName(basicsInfoEntity.getBrandName());
		productManageVO.setAliasName(basicsInfoEntity.getAliasName());
		productManageVO.setDescription(extendEntity.getDescription());
		productManageVO.setStoreId(basicsInfoEntity.getSourceId());
		productManageVO.setStoreName(basicsInfoEntity.getSourceName());
		productManageVO.setImageUrlList(Lists.newArrayList(extendEntity.getImageUrl().split(",")));
		productManageVO.setDetailUrlList(Lists.newArrayList(extendEntity.getDetailUrl().split(",")));


		return productManageVO;
	}

	private void saveAttribute(SaveProductRequest saveProductRequest, Long product) {
		productAttributeService.deletedByProductId(saveProductRequest.getProductId(), saveProductRequest.getUserId());
		List<ProductAttributeEntity> entityList = Lists.newArrayListWithCapacity(saveProductRequest.getPropertyValueList().size());
		for (SaveProductRequest.PropertyInfoRequest propertyInfoRequest : saveProductRequest.getPropertyValueList()) {
			ProductAttributeEntity attributeEntity = new ProductAttributeEntity();
			attributeEntity.setProductId(product);
			attributeEntity.setAttributeItemId(propertyInfoRequest.getAttributeItemId());
			attributeEntity.setAttributeValue(propertyInfoRequest.getAttributeValue());
			attributeEntity.setCreatedBy(saveProductRequest.getUserId());
			attributeEntity.setChangedBy(saveProductRequest.getUserId());
			entityList.add(attributeEntity);
		}
		productAttributeService.batchSave(entityList);
	}

	private void saveProductExtend(SaveProductRequest saveProductRequest, Long productId) {
		ProductExtendEntity extendEntity = productExtendService.queryByProductId(saveProductRequest.getProductId());
		extendEntity.setProductId(productId);
		extendEntity.setDescription(saveProductRequest.getDescription());
		extendEntity.setApprovalNumber(saveProductRequest.getApprovalNumber());
		extendEntity.setVendorName(saveProductRequest.getVendorName());
		extendEntity.setImageUrl(Joiner.on(",").join(saveProductRequest.getImageUrlList()));
		extendEntity.setDetailUrl(Joiner.on(",").join(saveProductRequest.getDetailUrlList()));
		productExtendService.saveOrUpdate(extendEntity);
	}

	private ProductBasicsInfoEntity saveProductBaseInfo(SaveProductRequest saveProductRequest) {
		if (!Objects.isNull(saveProductRequest.getProductId())) {
			ProductBasicsInfoEntity infoEntity = queryBasicsInfoByProductId(saveProductRequest.getProductId());
			if (Objects.isNull(infoEntity)) {
				throw new BusinessException(ErrorEnums.PRODUCT_NOT_EXIST);
			}
		}
		ProductBasicsInfoEntity infoEntity = new ProductBasicsInfoEntity();
		infoEntity.setId(saveProductRequest.getProductId());
		infoEntity.setCategoryId(saveProductRequest.getCategoryId());
		infoEntity.setTitle(saveProductRequest.getTitle());
		infoEntity.setBrandName(saveProductRequest.getBrandName());
		infoEntity.setAliasName(saveProductRequest.getAliasName());
		infoEntity.setSourceId(saveProductRequest.getStoreId());
		infoEntity.setMasterImageUrl(saveProductRequest.getImageUrlList().get(0));
		// TODO 设置店铺名称 和 spuCode
		saveOrUpdate(infoEntity);
		return infoEntity;
	}
}
