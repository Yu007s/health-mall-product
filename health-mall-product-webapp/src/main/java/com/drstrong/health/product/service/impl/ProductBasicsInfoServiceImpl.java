package com.drstrong.health.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.drstrong.health.product.dao.ProductBasicsInfoMapper;
import com.drstrong.health.product.model.entity.category.BackCategoryEntity;
import com.drstrong.health.product.model.entity.product.*;
import com.drstrong.health.product.model.enums.DelFlagEnum;
import com.drstrong.health.product.model.enums.ErrorEnums;
import com.drstrong.health.product.model.request.product.QuerySpuRequest;
import com.drstrong.health.product.model.request.product.SaveProductRequest;
import com.drstrong.health.product.model.response.PageVO;
import com.drstrong.health.product.model.response.product.PackInfoResponse;
import com.drstrong.health.product.model.response.product.ProductManageVO;
import com.drstrong.health.product.model.response.product.ProductSpuVO;
import com.drstrong.health.product.model.response.product.PropertyInfoResponse;
import com.drstrong.health.product.model.response.result.BusinessException;
import com.drstrong.health.product.service.*;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;
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
	 * 根据条件,分页查询商品基础信息
	 *
	 * @param querySpuRequest 查询入参
	 * @return 商品基础信息
	 * @author liuqiuyi
	 * @date 2021/12/15 21:20
	 */
	@Override
	public Page<ProductBasicsInfoEntity> pageQueryProductByParam(QuerySpuRequest querySpuRequest) {
		Page<ProductBasicsInfoEntity> queryPage = new Page<>(querySpuRequest.getPageNo(), querySpuRequest.getPageSize());
		return productBasicsInfoMapper.selectPage(queryPage, buildQuerySpuParam(querySpuRequest));
	}

	/**
	 * 根据条件,查询商品基础信息
	 *
	 * @param querySpuRequest 查询条件
	 * @return 商品基础信息集合
	 * @author liuqiuyi
	 * @date 2021/12/16 00:10
	 */
	@Override
	public List<ProductBasicsInfoEntity> queryProductByParam(QuerySpuRequest querySpuRequest) {
		return productBasicsInfoMapper.selectList(buildQuerySpuParam(querySpuRequest));
	}

	/**
	 * 根据商品信息,组装 sku map
	 *
	 * @param basicsInfoEntityList 商品基本信息
	 * @return map.key = 商品 id,map.value = sku 集合
	 * @author liuqiuyi
	 * @date 2021/12/16 00:30
	 */
	@Override
	public Map<Long, List<ProductSkuEntity>> buildSkuMap(List<ProductBasicsInfoEntity> basicsInfoEntityList) {
		if (CollectionUtils.isEmpty(basicsInfoEntityList)) {
			return Maps.newHashMap();
		}
		Set<Long> productIdList = basicsInfoEntityList.stream().map(ProductBasicsInfoEntity::getId).collect(Collectors.toSet());
		return productSkuService.queryByProductIdListToMap(productIdList);
	}

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
		return buildProductManageVO(basicsInfoEntity, extendEntity, backCategoryEntity, attributeEntityList, idEntityMap, skuEntityList);
	}

	/**
	 * 分页查询 spu 信息
	 *
	 * @param querySpuRequest 查询参数
	 * @return 分页返回值
	 * @author liuqiuyi
	 * @date 2021/12/14 10:25
	 */
	@Override
	public PageVO<ProductSpuVO> pageQuerySpuByParam(QuerySpuRequest querySpuRequest) {
		// 1.分页查询 spu 信息
		Page<ProductBasicsInfoEntity> infoEntityPage = pageQueryProductByParam(querySpuRequest);
		if (Objects.isNull(infoEntityPage) || CollectionUtils.isEmpty(infoEntityPage.getRecords())) {
			return PageVO.emptyPageVo(querySpuRequest.getPageNo(), querySpuRequest.getPageSize());
		}
		// 2.获取 productId 集合,查询 sku 信息
		List<ProductBasicsInfoEntity> basicsInfoEntityList = infoEntityPage.getRecords();
		Map<Long, List<ProductSkuEntity>> productIdSkuListMap = buildSkuMap(basicsInfoEntityList);
		// 3.组装返回值
		List<ProductSpuVO> spuVOList = Lists.newArrayListWithCapacity(basicsInfoEntityList.size());
		for (ProductBasicsInfoEntity infoEntity : basicsInfoEntityList) {
			ProductSpuVO productSpuVO = new ProductSpuVO();
			productSpuVO.setProductId(infoEntity.getId());
			productSpuVO.setSpuCode(infoEntity.getSpuCode());
			productSpuVO.setSpuName(infoEntity.getTitle());
			productSpuVO.setSpuIcon(infoEntity.getMasterImageUrl());
			List<ProductSkuEntity> skuList = productIdSkuListMap.getOrDefault(infoEntity.getId(), new ArrayList<>());
			productSpuVO.setSkuCount(skuList.size());
			productSpuVO.setStoreId(infoEntity.getSourceId());
			productSpuVO.setStoreName(infoEntity.getSourceName());
			productSpuVO.setCreateTime(infoEntity.getCreatedAt());
			productSpuVO.setUpdateTime(infoEntity.getChangedAt());
			spuVOList.add(productSpuVO);
		}
		return PageVO.toPageVo(spuVOList, infoEntityPage.getTotal(), infoEntityPage.getSize(), infoEntityPage.getCurrent());
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

		List<PropertyInfoResponse> propertyInfoResponseList = Lists.newArrayListWithCapacity(attributeEntityList.size());
		for (ProductAttributeEntity attributeEntity : attributeEntityList) {
			CategoryAttributeItemEntity categoryAttEntity = idEntityMap.getOrDefault(attributeEntity.getAttributeItemId(), new CategoryAttributeItemEntity());

			PropertyInfoResponse propertyInfoResponse = new PropertyInfoResponse();
			propertyInfoResponse.setAttributeItemId(attributeEntity.getAttributeItemId());
			propertyInfoResponse.setAttributeKey(categoryAttEntity.getAttributeKey());
			propertyInfoResponse.setAttributeName(categoryAttEntity.getAttributeName());
			propertyInfoResponse.setAttributeValue(attributeEntity.getAttributeValue());
			propertyInfoResponse.setAttributeType(categoryAttEntity.getAttributeType());
			propertyInfoResponse.setFormType(categoryAttEntity.getFormType());
			propertyInfoResponse.setAttributeOptions(categoryAttEntity.getAttributeOptions());
			propertyInfoResponse.setRequired(categoryAttEntity.getRequired());
			propertyInfoResponse.setOrderNumber(categoryAttEntity.getOrderNumber());

			propertyInfoResponseList.add(propertyInfoResponse);
		}
		productManageVO.setPropertyValueList(propertyInfoResponseList);

		List<PackInfoResponse> packInfoList = Lists.newArrayListWithCapacity(skuEntityList.size());
		for (ProductSkuEntity productSkuEntity : skuEntityList) {
			PackInfoResponse packInfoResponse = new PackInfoResponse();
			packInfoResponse.setPackName(productSkuEntity.getPackName());
			packInfoResponse.setPackValue(productSkuEntity.getPackValue());
//			packInfoResponse.setPrice(productSkuEntity.getSkuPrice());
			packInfoResponse.setCommAttributeId(productSkuEntity.getCommAttribute());
//			packInfoResponse.setCommAttributeName();
			packInfoResponse.setSkuCode(productSkuEntity.getSkuCode());
			packInfoList.add(packInfoResponse);
		}
		productManageVO.setPackInfoList(packInfoList);

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

	@Override
	public Integer getCountBySPUCode(String spuCode) {
		LambdaQueryWrapper<ProductBasicsInfoEntity> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.eq(ProductBasicsInfoEntity::getSpuCode, spuCode).eq(ProductBasicsInfoEntity::getDelFlag, 0).eq(ProductBasicsInfoEntity::getState, 1);
		return productBasicsInfoMapper.selectCount(queryWrapper);
	}

	private LambdaQueryWrapper<ProductBasicsInfoEntity> buildQuerySpuParam(QuerySpuRequest querySpuRequest) {
		LambdaQueryWrapper<ProductBasicsInfoEntity> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.eq(ProductBasicsInfoEntity::getDelFlag, DelFlagEnum.UN_DELETED.getCode());
		if (Objects.nonNull(querySpuRequest.getSpuCode())) {
			queryWrapper.eq(ProductBasicsInfoEntity::getSpuCode, querySpuRequest.getSpuCode());
		}
		if (Objects.nonNull(querySpuRequest.getProductName())) {
			queryWrapper.like(ProductBasicsInfoEntity::getTitle, querySpuRequest.getProductName());
		}
		if (Objects.nonNull(querySpuRequest.getStoreId())) {
			queryWrapper.eq(ProductBasicsInfoEntity::getSourceId, querySpuRequest.getStoreId());
		}
		if (Objects.nonNull(querySpuRequest.getCreateStart())) {
			queryWrapper.gt(ProductBasicsInfoEntity::getCreatedAt, querySpuRequest.getCreateStart());
		}
		if (Objects.nonNull(querySpuRequest.getCreateEnd())) {
			queryWrapper.lt(ProductBasicsInfoEntity::getCreatedAt, querySpuRequest.getCreateEnd());
		}
		if (!CollectionUtils.isEmpty(querySpuRequest.getBackCategoryIdList())) {
			queryWrapper.in(ProductBasicsInfoEntity::getCategoryId, querySpuRequest.getBackCategoryIdList());
		}
		return queryWrapper;
	}
}
