package com.drstrong.health.product.service.product.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.drstrong.health.product.constants.ProductConstant;
import com.drstrong.health.product.dao.product.ProductBasicsInfoMapper;
import com.drstrong.health.product.model.dto.CommAttributeDTO;
import com.drstrong.health.product.model.entity.category.BackCategoryEntity;
import com.drstrong.health.product.model.entity.product.*;
import com.drstrong.health.product.model.entity.store.StoreEntity;
import com.drstrong.health.product.model.enums.*;
import com.drstrong.health.product.model.request.product.QuerySpuRequest;
import com.drstrong.health.product.model.request.product.SaveProductRequest;
import com.drstrong.health.product.model.response.PageVO;
import com.drstrong.health.product.model.response.product.PackInfoResponse;
import com.drstrong.health.product.model.response.product.ProductManageVO;
import com.drstrong.health.product.model.response.product.ProductSpuVO;
import com.drstrong.health.product.model.response.product.PropertyInfoResponse;
import com.drstrong.health.product.model.response.result.BusinessException;
import com.drstrong.health.product.remote.cms.CmsRemoteProService;
import com.drstrong.health.product.service.category.BackCategoryService;
import com.drstrong.health.product.service.category.CategoryAttributeItemService;
import com.drstrong.health.product.service.product.*;
import com.drstrong.health.product.service.redis.IRedisService;
import com.drstrong.health.product.service.store.StoreService;
import com.drstrong.health.product.util.BigDecimalUtil;
import com.drstrong.health.product.util.DateUtil;
import com.drstrong.health.product.util.RedisKeyUtils;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 商品管理端 service
 *
 * @author liuqiuyi
 * @date 2022/1/7 16:22
 */
@Service
@Slf4j
public class ProductManageServiceImpl implements ProductManageService {
	@Resource
	ProductBasicsInfoService productBasicsInfoService;

	@Resource
	ProductBasicsInfoMapper productBasicsInfoMapper;

	@Resource
	ProductExtendService productExtendService;

	@Resource
	ProductAttributeService productAttributeService;

	@Resource
	ProductSkuService productSkuService;

	@Resource
	ProductSkuManageService productSkuManageService;

	@Resource
	CategoryAttributeItemService categoryAttributeItemService;

	@Resource
	BackCategoryService backCategoryService;

	@Resource
	IRedisService redisService;

	@Resource
	StoreService storeService;

	@Resource
	CmsRemoteProService cmsRemoteProService;

	/**
	 * 保存或者更新商品信息
	 *
	 * @param saveProductRequest 保存商品
	 * @author liuqiuyi
	 * @date 2021/12/13 15:48
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public Long saveOrUpdateProduct(SaveProductRequest saveProductRequest) {
		checkProductRequest(saveProductRequest);
		// 1.校验店铺 id 是否存在
		StoreEntity storeEntity = storeService.getByStoreId(saveProductRequest.getStoreId());
		if (Objects.isNull(storeEntity)) {
			throw new BusinessException(ErrorEnums.STORE_NOT_EXIST);
		}
		// 2.保存或更新基础信息
		ProductBasicsInfoEntity infoEntity = saveProductBaseInfo(saveProductRequest, storeEntity);
		// 3.保存或更新扩展信息
		saveProductExtend(saveProductRequest, infoEntity.getId());
		// 4.保存商品分类属性信息
		saveAttribute(saveProductRequest, infoEntity.getId());
		// 5.保存或更新 sku 信息
		saveOrUpdateSku(saveProductRequest, storeEntity, infoEntity);
		return infoEntity.getId();
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
		ProductBasicsInfoEntity basicsInfoEntity = productBasicsInfoService.queryBasicsInfoByProductId(productId);
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
		Map<Long, CategoryAttributeItemEntity> idEntityMap = categoryAttributeItemService.queryByIdListToMap(attributeItemIdList, basicsInfoEntity.getCategoryId());
		// 5.查询 sku 信息
		List<ProductSkuEntity> skuEntityList = productSkuService.queryByProductId(productId, null);
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
		Page<ProductBasicsInfoEntity> queryPage = new Page<>(querySpuRequest.getPageNo(), querySpuRequest.getPageSize());
		LambdaQueryWrapper<ProductBasicsInfoEntity> queryWrapper = productBasicsInfoService.buildQuerySpuParam(querySpuRequest);
		queryWrapper.orderByDesc(ProductBasicsInfoEntity::getId);
		// 1.分页查询 spu 信息
		Page<ProductBasicsInfoEntity> infoEntityPage = productBasicsInfoMapper.selectPage(queryPage, queryWrapper);
		if (Objects.isNull(infoEntityPage) || CollectionUtils.isEmpty(infoEntityPage.getRecords())) {
			return PageVO.newBuilder().pageNo(querySpuRequest.getPageNo()).pageSize(querySpuRequest.getPageSize()).totalCount(0).result(Lists.newArrayList()).build();
		}
		// 2.获取 productId 集合,查询 sku 信息
		List<ProductBasicsInfoEntity> basicsInfoEntityList = infoEntityPage.getRecords();
		Map<Long, List<ProductSkuEntity>> productIdSkuListMap = productBasicsInfoService.buildSkuMap(basicsInfoEntityList);
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
		return PageVO.newBuilder().pageNo(querySpuRequest.getPageNo()).pageSize(querySpuRequest.getPageSize()).totalCount((int) infoEntityPage.getTotal()).result(spuVOList).build();
	}

	@Override
	public Integer getCountBySPUCode(String spuCode) {
		LambdaQueryWrapper<ProductBasicsInfoEntity> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.eq(ProductBasicsInfoEntity::getSpuCode, spuCode).eq(ProductBasicsInfoEntity::getDelFlag, 0).eq(ProductBasicsInfoEntity::getState, 1);
		return productBasicsInfoMapper.selectCount(queryWrapper);
	}

	@Override
	public void updateState(Set<Long> spuIdList, Integer state, String userId) {
		ProductBasicsInfoEntity productBasicsInfoEntity = new ProductBasicsInfoEntity();
		productBasicsInfoEntity.setState(state);
		productBasicsInfoEntity.setChangedAt(LocalDateTime.now());
		productBasicsInfoEntity.setChangedBy(userId);
		LambdaUpdateWrapper<ProductBasicsInfoEntity> updateWrapper = new LambdaUpdateWrapper<>();
		updateWrapper.eq(ProductBasicsInfoEntity::getDelFlag, DelFlagEnum.UN_DELETED.getCode())
				.eq(ProductBasicsInfoEntity::getState, (ProductStateEnum.HAS_PUT.getCode()).equals(state) ? ProductStateEnum.UN_PUT.getCode() : ProductStateEnum.HAS_PUT.getCode())
				.in(ProductBasicsInfoEntity::getId, spuIdList);
		productBasicsInfoMapper.update(productBasicsInfoEntity, updateWrapper);
	}

	/**
	 * 生成商品或者药品编码,参照之前的方法
	 *
	 * @author liuqiuyi
	 * @date 2021/12/16 14:13
	 */
	@Override
	public String getNextProductNumber(ProductTypeEnum productTypeEnum) {
		// 生成规则：药品编码M开头 + 建码日期六位：年后两位+月份+日期（190520）+ 5位顺序码    举例：M19052000001
		StringBuilder number = new StringBuilder();
		number.append(productTypeEnum.getMark());
		number.append(DateUtil.formatDate(new Date(), "yyMMdd"));
		long serialNumber = redisService.incr(RedisKeyUtils.getSerialNum());
		number.append(String.format("%05d", serialNumber));
		return number.toString();
	}

	private void checkProductRequest(SaveProductRequest saveProductRequest){
		// 校验金额(必须大于 0)
		boolean priceFlag = saveProductRequest.getPackInfoList().stream().anyMatch(packInfoRequest -> packInfoRequest.getPrice().compareTo(new BigDecimal("0")) < 1);
		if (priceFlag) {
			throw new BusinessException(ErrorEnums.PRICE_IS_ERROR);
		}
		// 校验后台分类id 是否存在,并且为三级分类
		BackCategoryEntity backCategoryEntity = backCategoryService.queryById(saveProductRequest.getCategoryId());
		if (Objects.isNull(backCategoryEntity)) {
			throw new BusinessException(ErrorEnums.CATEGORY_NOT_EXIST);
		}
		if (!Objects.equals(ProductConstant.THREE_LEVEL, backCategoryEntity.getLevel())) {
			throw new BusinessException(ErrorEnums.CATEGORY_LEVEL_IS_ERROR);
		}
	}

	private ProductManageVO buildProductManageVO(ProductBasicsInfoEntity basicsInfoEntity, ProductExtendEntity extendEntity, BackCategoryEntity backCategoryEntity
			, List<ProductAttributeEntity> attributeEntityList, Map<Long, CategoryAttributeItemEntity> idEntityMap, List<ProductSkuEntity> skuEntityList) {

		ProductManageVO productManageVO = new ProductManageVO();
		BeanUtils.copyProperties(basicsInfoEntity, productManageVO);
		BeanUtils.copyProperties(extendEntity, productManageVO);
		productManageVO.setProductId(basicsInfoEntity.getId());
		productManageVO.setCategoryPathName(backCategoryEntity.getName());
		productManageVO.setStoreId(basicsInfoEntity.getSourceId());
		productManageVO.setStoreName(basicsInfoEntity.getSourceName());
		productManageVO.setImageUrlList(Lists.newArrayList(extendEntity.getImageUrl().split(",")));
		productManageVO.setDetailUrlList(Lists.newArrayList(extendEntity.getDetailUrl().split(",")));

		List<PropertyInfoResponse> propertyInfoResponseList = Lists.newArrayListWithCapacity(attributeEntityList.size());
		for (ProductAttributeEntity attributeEntity : attributeEntityList) {
			CategoryAttributeItemEntity categoryAttEntity = idEntityMap.getOrDefault(attributeEntity.getAttributeItemId(), new CategoryAttributeItemEntity());
			PropertyInfoResponse propertyInfoResponse = new PropertyInfoResponse();
			BeanUtils.copyProperties(attributeEntity, propertyInfoResponse);
			BeanUtils.copyProperties(categoryAttEntity, propertyInfoResponse);
			propertyInfoResponseList.add(propertyInfoResponse);
		}
		productManageVO.setPropertyValueList(propertyInfoResponseList);

		List<PackInfoResponse> packInfoList = Lists.newArrayListWithCapacity(skuEntityList.size());
		for (ProductSkuEntity productSkuEntity : skuEntityList) {
			PackInfoResponse packInfoResponse = new PackInfoResponse();
			packInfoResponse.setPackName(productSkuEntity.getPackName());
			packInfoResponse.setPackValue(productSkuEntity.getPackValue());
			packInfoResponse.setPrice(BigDecimalUtil.F2Y(productSkuEntity.getSkuPrice().longValue()));
			packInfoResponse.setCommAttributeId(productSkuEntity.getCommAttribute());
			Map<Integer, CommAttributeDTO> commAttributeByIdListToMap = cmsRemoteProService.getCommAttributeByIdListToMap();
			CommAttributeDTO commAttributeDTO = commAttributeByIdListToMap.getOrDefault(productSkuEntity.getCommAttribute(), new CommAttributeDTO());
			packInfoResponse.setCommAttributeName(commAttributeDTO.getCommAttributeName());
			packInfoResponse.setSkuCode(productSkuEntity.getSkuCode());
			packInfoResponse.setSkuId(productSkuEntity.getId());
			packInfoResponse.setSkuState(productSkuEntity.getState());
			packInfoList.add(packInfoResponse);
		}
		productManageVO.setPackInfoList(packInfoList);

		return productManageVO;
	}

	private void saveOrUpdateSku(SaveProductRequest saveProductRequest, StoreEntity storeEntity, ProductBasicsInfoEntity infoEntity) {
		// 获取商品的 sku 列表
		Map<Long, ProductSkuEntity> skuIdEntityMap = productSkuService.queryByProductIdToMap(infoEntity.getId(), null);
		// 组装参数
		List<ProductSkuEntity> skuEntityList = Lists.newArrayListWithCapacity(saveProductRequest.getPackInfoList().size());
		for (SaveProductRequest.PackInfoRequest packInfoRequest : saveProductRequest.getPackInfoList()) {
			ProductSkuEntity skuEntity = new ProductSkuEntity();
			// 如果 sku 信息之前存在,则校验是否下架,未下架不让修改
			if (Objects.nonNull(packInfoRequest.getSkuId()) && skuIdEntityMap.containsKey(packInfoRequest.getSkuId())) {
				skuEntity = skuIdEntityMap.remove(packInfoRequest.getSkuId());
				if (Objects.equals(UpOffEnum.UP.getCode(), skuEntity.getState())) {
					throw new BusinessException(ErrorEnums.UPDATE_NOT_ALLOW);
				}
			} else {
				skuEntity.setSkuCode(productSkuManageService.createNextSkuCode(infoEntity.getSpuCode(), infoEntity.getId()));
				skuEntity.setCreatedBy(saveProductRequest.getUserId());
			}
			skuEntity.setProductId(infoEntity.getId());
			skuEntity.setPackName(packInfoRequest.getPackName());
			skuEntity.setPackValue(packInfoRequest.getPackValue());
			skuEntity.setSkuName(infoEntity.getTitle() + " " + packInfoRequest.getPackName() + packInfoRequest.getPackValue());
			skuEntity.setSkuPrice(BigDecimalUtil.Y2F(packInfoRequest.getPrice()).intValue());
			skuEntity.setCommAttribute(packInfoRequest.getCommAttribute());
			skuEntity.setChangedBy(saveProductRequest.getUserId());
			skuEntity.setChangedAt(LocalDateTime.now());
			skuEntity.setSourceId(storeEntity.getId());
			skuEntity.setSourceName(storeEntity.getName());
			skuEntityList.add(skuEntity);
		}
		productSkuService.saveOrUpdateBatch(skuEntityList, skuEntityList.size());
		// 如果 skuIdEntityMap 中还存在 skuId,则是本次修改时删除的 skuId
		Set<Long> oldSkuIds = skuIdEntityMap.keySet();
		if (!CollectionUtils.isEmpty(oldSkuIds)) {
			productSkuService.deleteBySkuIdList(oldSkuIds, saveProductRequest.getUserId());
		}
	}

	private void saveAttribute(SaveProductRequest saveProductRequest, Long product) {
		productAttributeService.deletedByProductId(saveProductRequest.getProductId(), saveProductRequest.getUserId());
		LocalDateTime dateTime = LocalDateTime.now();
		List<ProductAttributeEntity> entityList = Lists.newArrayListWithCapacity(saveProductRequest.getPropertyValueList().size());
		for (SaveProductRequest.PropertyInfoRequest propertyInfoRequest : saveProductRequest.getPropertyValueList()) {
			ProductAttributeEntity attributeEntity = new ProductAttributeEntity();
			attributeEntity.setProductId(product);
			attributeEntity.setAttributeItemId(propertyInfoRequest.getAttributeItemId());
			attributeEntity.setAttributeValue(propertyInfoRequest.getAttributeValue());
			attributeEntity.setCreatedBy(saveProductRequest.getUserId());
			attributeEntity.setChangedBy(saveProductRequest.getUserId());
			attributeEntity.setChangedAt(dateTime);
			attributeEntity.setCreatedAt(dateTime);
			entityList.add(attributeEntity);
		}
		productAttributeService.batchSave(entityList);
	}

	private void saveProductExtend(SaveProductRequest saveProductRequest, Long productId) {
		ProductExtendEntity extendEntity = productExtendService.queryByProductId(saveProductRequest.getProductId());
		LocalDateTime dateTime = LocalDateTime.now();
		if (Objects.isNull(extendEntity)) {
			extendEntity = new ProductExtendEntity();
			extendEntity.setCreatedBy(saveProductRequest.getUserId());
			extendEntity.setCreatedAt(dateTime);
		}
		extendEntity.setProductId(productId);
		extendEntity.setDescription(saveProductRequest.getDescription());
		extendEntity.setApprovalNumber(saveProductRequest.getApprovalNumber());
		extendEntity.setVendorName(saveProductRequest.getVendorName());
		extendEntity.setImageUrl(Joiner.on(",").join(saveProductRequest.getImageUrlList()));
		extendEntity.setDetailUrl(Joiner.on(",").join(saveProductRequest.getDetailUrlList()));
		extendEntity.setChangedBy(saveProductRequest.getUserId());
		extendEntity.setChangedAt(dateTime);
		productExtendService.saveOrUpdate(extendEntity);
	}

	private ProductBasicsInfoEntity saveProductBaseInfo(SaveProductRequest saveProductRequest, StoreEntity storeEntity) {
		ProductBasicsInfoEntity infoEntity = new ProductBasicsInfoEntity();
		LocalDateTime dateTime = LocalDateTime.now();
		if (Objects.nonNull(saveProductRequest.getProductId()) && !Objects.equals(saveProductRequest.getProductId(), 0L)) {
			infoEntity = productBasicsInfoService.queryBasicsInfoByProductId(saveProductRequest.getProductId());
			if (Objects.isNull(infoEntity)) {
				throw new BusinessException(ErrorEnums.PRODUCT_NOT_EXIST);
			}
		} else {
			infoEntity.setSpuCode(getNextProductNumber(ProductTypeEnum.PRODUCT));
			infoEntity.setCreatedBy(saveProductRequest.getUserId());
			infoEntity.setCreatedAt(dateTime);
		}
		infoEntity.setChangedAt(dateTime);
		// 更新分类下的商品数量
		updateCategoryProductNum(infoEntity.getId(), saveProductRequest.getCategoryId(), infoEntity.getCategoryId());
		// 更新店铺下商品数量
		updateStoreProductNum(infoEntity.getId(), saveProductRequest.getStoreId(), infoEntity.getSourceId());
		// 更新商品基础信息
		BeanUtils.copyProperties(saveProductRequest, infoEntity);
		infoEntity.setMasterImageUrl(saveProductRequest.getImageUrlList().get(0));
		infoEntity.setSourceId(storeEntity.getId());
		infoEntity.setSourceName(storeEntity.getName());
		infoEntity.setChangedBy(saveProductRequest.getUserId());
		productBasicsInfoService.saveOrUpdate(infoEntity);
		return infoEntity;
	}


	private void updateCategoryProductNum(Long productId, Long newCategoryId, Long oldCategoryId) {
		// 如果是新增商品,增加对应的分类商品数量
		if (Objects.isNull(productId)) {
			backCategoryService.addOrReduceProductNumById(newCategoryId, 1, CategoryProductNumOperateEnum.ADD);
		} else if (!Objects.equals(newCategoryId, oldCategoryId)) {
			// 如果是修改商品,并且修改前后分类 id 不一致,老的分类需要 -1,新的分类需要 +1
			backCategoryService.addOrReduceProductNumById(oldCategoryId, 1, CategoryProductNumOperateEnum.REDUCE);
			backCategoryService.addOrReduceProductNumById(newCategoryId, 1, CategoryProductNumOperateEnum.ADD);
		}
	}

	private void updateStoreProductNum(Long productId, Long newStoreId, Long oldStoreId) {
		// 如果是新增商品,增加对应的店铺的商品数量
		if (Objects.isNull(productId)) {
			storeService.addOrReduceProductNumById(newStoreId, 1, CategoryProductNumOperateEnum.ADD);
		} else if (!Objects.equals(newStoreId, oldStoreId)) {
			// 如果是修改商品,并且修改前后店铺 id 不一致,老的店铺需要 -1,新的店铺需要 +1
			storeService.addOrReduceProductNumById(oldStoreId, 1, CategoryProductNumOperateEnum.REDUCE);
			storeService.addOrReduceProductNumById(newStoreId, 1, CategoryProductNumOperateEnum.ADD);
		}
	}
}
