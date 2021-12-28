package com.drstrong.health.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.drstrong.health.product.dao.ProductBasicsInfoMapper;
import com.drstrong.health.product.model.entity.category.BackCategoryEntity;
import com.drstrong.health.product.model.entity.product.*;
import com.drstrong.health.product.model.entity.store.StoreEntity;
import com.drstrong.health.product.model.enums.*;
import com.drstrong.health.product.model.request.PageRequest;
import com.drstrong.health.product.model.request.product.ProductSearchRequest;
import com.drstrong.health.product.model.request.product.QuerySpuRequest;
import com.drstrong.health.product.model.request.product.SaveProductRequest;
import com.drstrong.health.product.model.response.PageVO;
import com.drstrong.health.product.model.response.product.*;
import com.drstrong.health.product.model.response.result.BusinessException;
import com.drstrong.health.product.remote.pro.PharmacyGoodsRemoteProService;
import com.drstrong.health.product.service.*;
import com.drstrong.health.product.util.BigDecimalUtil;
import com.drstrong.health.product.util.DateUtil;
import com.drstrong.health.product.util.RedisKeyUtils;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toMap;

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
	CategoryAttributeItemService categoryAttributeItemService;

	@Resource
	BackCategoryService backCategoryService;

	@Resource
	IRedisService redisService;

	@Resource
	StoreService storeService;

	@Resource
	PharmacyGoodsRemoteProService pharmacyGoodsRemoteProService;

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
	 * 根据条件,查询商品基础信息,转成 map 结构
	 *
	 * @param querySpuRequest 查询条件
	 * @return 商品基础信息集合 map.key = 商品 id,value = 商品基础信息
	 * @author liuqiuyi
	 * @date 2021/12/16 00:10
	 */
	@Override
	public Map<Long, ProductBasicsInfoEntity> queryProductByParamToMap(QuerySpuRequest querySpuRequest) {
		List<ProductBasicsInfoEntity> basicsInfoEntityList = queryProductByParam(querySpuRequest);
		if (CollectionUtils.isEmpty(basicsInfoEntityList)) {
			return Maps.newHashMap();
		}
		return basicsInfoEntityList.stream().collect(toMap(ProductBasicsInfoEntity::getId, dto -> dto, (v1, v2) -> v1));
	}

	/**
	 * 根据 productId 或者 spuCode 查询商品基本信息
	 *
	 * @param productId 商品 id
	 * @param spuCode   spu编码
	 * @author liuqiuyi
	 * @date 2021/12/16 20:15
	 */
	@Override
	public ProductBasicsInfoEntity getByProductIdOrSpuCode(Long productId, String spuCode, UpOffEnum upOffEnum) {
		if (Objects.isNull(productId) && StringUtils.isBlank(spuCode)) {
			return null;
		}
		QuerySpuRequest querySpuRequest = new QuerySpuRequest();
		querySpuRequest.setProductId(productId);
		querySpuRequest.setSpuCode(spuCode);
		querySpuRequest.setUpOffEnum(upOffEnum);
		List<ProductBasicsInfoEntity> basicsInfoEntityList = queryProductByParam(querySpuRequest);
		return CollectionUtils.isEmpty(basicsInfoEntityList) ? null : basicsInfoEntityList.get(0);
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
	public Long saveOrUpdateProduct(SaveProductRequest saveProductRequest) {
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
		// 1.分页查询 spu 信息
		Page<ProductBasicsInfoEntity> infoEntityPage = pageQueryProductByParam(querySpuRequest);
		if (Objects.isNull(infoEntityPage) || CollectionUtils.isEmpty(infoEntityPage.getRecords())) {
			return PageVO.newBuilder().pageNo(querySpuRequest.getPageNo()).pageSize(querySpuRequest.getPageSize()).totalCount(0).result(Lists.newArrayList()).build();
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
		return PageVO.newBuilder().pageNo(querySpuRequest.getPageNo()).pageSize(querySpuRequest.getPageSize()).totalCount((int) infoEntityPage.getTotal()).result(spuVOList).build();
	}

	@Override
	public Integer getCountBySPUCode(String spuCode) {
		LambdaQueryWrapper<ProductBasicsInfoEntity> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.eq(ProductBasicsInfoEntity::getSpuCode, spuCode).eq(ProductBasicsInfoEntity::getDelFlag, 0).eq(ProductBasicsInfoEntity::getState, 1);
		return productBasicsInfoMapper.selectCount(queryWrapper);
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

	/**
	 * 小程序 - 根据 spuCode 获取商品详细信息
	 *
	 * @param spuCode spu编码
	 * @return spu信息
	 * @author liuqiuyi
	 * @date 2021/12/16 19:55
	 */
	@Override
	public ProductDetailVO getSpuInfo(String spuCode) {
		if (StringUtils.isBlank(spuCode)) {
			return new ProductDetailVO();
		}
		// 1.查询 spu 信息
		ProductBasicsInfoEntity productEntity = getByProductIdOrSpuCode(null, spuCode, UpOffEnum.UP);
		if (Objects.isNull(productEntity)) {
			throw new BusinessException(ErrorEnums.PRODUCT_NOT_EXIST);
		}
		// 2.查询扩展信息
		ProductExtendEntity extendEntity = productExtendService.queryByProductId(productEntity.getId());
		// 3.查询 sku 信息
		List<ProductSkuEntity> productSkuEntityList = productSkuService.queryByProductId(productEntity.getId(), UpOffEnum.UP);
		if (CollectionUtils.isEmpty(productSkuEntityList)) {
			throw new BusinessException(ErrorEnums.PRODUCT_NOT_EXIST);
		}
		productSkuEntityList.sort(Comparator.comparing(ProductSkuEntity::getSkuPrice));
		// 3.查询库存信息
		Set<Long> skuIdList = productSkuEntityList.stream().map(ProductSkuEntity::getId).collect(Collectors.toSet());
		Map<Long, Integer> skuStockNumToMap = pharmacyGoodsRemoteProService.getSkuStockNumToMap(skuIdList);
		// 4.调用商品购物车接口,设置用户购物车是否有商品
		// TODO 购物车
		// 5.组装返回值
		return buildProductDetailVO(productEntity, extendEntity, productSkuEntityList, skuStockNumToMap);
	}

	/**
	 * 分页查询搜索的内容,只返回商品名称
	 *
	 * @param content 搜索条件
	 * @param count   返回的个数
	 * @return 搜索结果
	 * @author liuqiuyi
	 * @date 2021/12/17 15:49
	 */
	@Override
	public List<String> searchByName(String content, Integer count) {
		if (StringUtils.isBlank(content)) {
			return Lists.newArrayList();
		}
		if (Objects.isNull(count)) {
			count = 10;
		}
		return productBasicsInfoMapper.likeProductTitle(content, count);
	}

	/**
	 * 模糊查询商品标题和品牌名
	 *
	 * @param content 查询内容
	 * @param count   个数
	 * @return 满足条件的标题集合
	 * @author liuqiuyi
	 * @date 2021/12/17 16:12
	 */
	@Override
	public List<ProductBasicsInfoEntity> searchTitleAndBrandName(String content, Integer count) {
		if (StringUtils.isBlank(content)) {
			return Lists.newArrayList();
		}
		if (Objects.isNull(count)) {
			count = 10;
		}
		return productBasicsInfoMapper.likeTitleAndBrandName(content, count);
	}

	/**
	 * 分页查询搜索结果
	 *
	 * @param productSearchRequest 搜索条件
	 * @return 搜索结果
	 * @author liuqiuyi
	 * @date 2021/12/18 14:05
	 */
	@Override
	public PageVO<ProductSearchDetailVO> pageSearchDetail(ProductSearchRequest productSearchRequest) {
		if (StringUtils.isBlank(productSearchRequest.getContent())) {
			return PageVO.newBuilder().pageNo(productSearchRequest.getPageNo()).pageSize(productSearchRequest.getPageSize()).totalCount(0).result(new ArrayList<>()).build();
		}
		// 1.模糊查询商品信息
		QuerySpuRequest querySpuRequest = new QuerySpuRequest();
		querySpuRequest.setPageNo(productSearchRequest.getPageNo());
		querySpuRequest.setPageSize(productSearchRequest.getPageSize());
		querySpuRequest.setUpOffEnum(UpOffEnum.UP);
		Page<ProductBasicsInfoEntity> infoEntityPage = pageQueryProductByParam(querySpuRequest);
		if (Objects.isNull(infoEntityPage) || CollectionUtils.isEmpty(infoEntityPage.getRecords())) {
			return PageVO.newBuilder().pageNo(productSearchRequest.getPageNo()).pageSize(productSearchRequest.getPageSize()).totalCount(0).result(new ArrayList<>()).build();
		}
		// 2.查询商品 sku 信息
		Map<Long, List<ProductSkuEntity>> productIdSkusMap = buildSkuMap(infoEntityPage.getRecords());
		// 3.判断库存是否充足
		Map<Long, Integer> spuStockNumMap = getSpuStockNum(productIdSkusMap);
		// 5.组装返回值信息
		List<ProductSearchDetailVO> detailVOList = Lists.newArrayListWithCapacity(infoEntityPage.getRecords().size());
		for (ProductBasicsInfoEntity infoEntity : infoEntityPage.getRecords()) {
			ProductSearchDetailVO detailVO = new ProductSearchDetailVO();
			detailVO.setSpuCode(infoEntity.getSpuCode());
			detailVO.setMasterImageUrl(infoEntity.getMasterImageUrl());
			detailVO.setProductName(infoEntity.getTitle());
			Map<String, BigDecimal> priceSectionMap = productSkuService.getPriceSectionMap(productIdSkusMap.getOrDefault(infoEntity.getId(), Lists.newArrayList()));
			detailVO.setLowPrice(priceSectionMap.get("lowPrice"));
			detailVO.setHasInventory(spuStockNumMap.get(infoEntity.getId()) > 0);
			detailVOList.add(detailVO);
		}
		return PageVO.newBuilder().pageNo(productSearchRequest.getPageNo()).pageSize(productSearchRequest.getPageSize()).totalCount((int) infoEntityPage.getTotal()).result(detailVOList).build();
	}

	@Override
	public PageVO<ProductRecommendVO> pageSearchRecommend(PageRequest pageRequest) {
		Page<ProductBasicsInfoEntity> page = new Page<>(pageRequest.getPageNo(), pageRequest.getPageSize());
		LambdaQueryWrapper<ProductBasicsInfoEntity> basicsInfoWrapper = new LambdaQueryWrapper<>();
		basicsInfoWrapper.eq(ProductBasicsInfoEntity::getDelFlag, DelFlagEnum.UN_DELETED.getCode())
				.eq(ProductBasicsInfoEntity::getState, UpOffEnum.UP.getCode())
				.orderByAsc(ProductBasicsInfoEntity::getSort);
		Page<ProductBasicsInfoEntity> resultPage = productBasicsInfoMapper.selectPage(page, basicsInfoWrapper);
		List<ProductBasicsInfoEntity> records = resultPage.getRecords();
		List<ProductRecommendVO> recommendVOS = Lists.newArrayListWithCapacity(records.size());
		List<Long> productIds = records.stream().map(ProductBasicsInfoEntity::getId).collect(Collectors.toList());
		Map<Long, String> proDesMap = productExtendService.queryListByProductIds(productIds).stream().collect(toMap(ProductExtendEntity::getProductId, ProductExtendEntity::getDescription));
		Map<Long, List<ProductSkuEntity>> spuSkuMap = productSkuService.queryByProductIdListToMap(productIds.stream().collect(Collectors.toSet()));
		Map<Long, BigDecimal> spuLowPriceMap = getSpuLowPriceMap(spuSkuMap);
		records.forEach(r -> {
			ProductRecommendVO recommendVO = new ProductRecommendVO();
			recommendVO.setSpuCode(r.getSpuCode());
			recommendVO.setProductName(r.getTitle());
			recommendVO.setMasterImageUrl(r.getMasterImageUrl());
			recommendVO.setDescription(proDesMap.get(r.getId()));
			recommendVO.setLowPrice(spuLowPriceMap.getOrDefault(r.getId(), BigDecimal.ZERO));
			recommendVOS.add(recommendVO);
		});
		return PageVO.newBuilder().pageNo(pageRequest.getPageNo()).pageSize(pageRequest.getPageSize()).totalCount((int) resultPage.getTotal()).result(recommendVOS).build();
	}

	@Override
	public void updateState(Set<Long> spuIdList, Integer state, String userId) {
		ProductBasicsInfoEntity productBasicsInfoEntity = new ProductBasicsInfoEntity();
		productBasicsInfoEntity.setState(state);
		productBasicsInfoEntity.setChangedAt(LocalDateTime.now());
		productBasicsInfoEntity.setChangedBy(userId);
		LambdaUpdateWrapper<ProductBasicsInfoEntity> updateWrapper = new LambdaUpdateWrapper<>();
		updateWrapper.eq(ProductBasicsInfoEntity::getDelFlag, DelFlagEnum.UN_DELETED)
				.eq(ProductBasicsInfoEntity::getState, (ProductStateEnum.HAS_PUT.getCode()).equals(state) ? ProductStateEnum.UN_PUT.getCode() : ProductStateEnum.HAS_PUT.getCode())
				.in(ProductBasicsInfoEntity::getId, spuIdList);
		productBasicsInfoMapper.update(productBasicsInfoEntity, updateWrapper);
	}

	private Map<Long, BigDecimal> getSpuLowPriceMap(Map<Long, List<ProductSkuEntity>> spuSkuMap) {
		Map<Long, BigDecimal> spuLowPriceMap = new HashMap<>();
		spuSkuMap.forEach((k, v) -> {
			Integer lowPrice = v.stream().min(Comparator.comparing(ProductSkuEntity::getSkuPrice)).get().getSkuPrice();
			spuLowPriceMap.put(k, BigDecimalUtil.F2Y(lowPrice.longValue()));
		});
		return spuLowPriceMap;
	}

	/**
	 * 校验 spu 是否有库存
	 * <p> 查询的是 spu 下所有 sku 的库存 </>
	 *
	 * @return Map.key = spuId, map.value = sku 的库存总数
	 * @author liuqiuyi
	 * @date 2021/12/21 15:06
	 */
	public Map<Long, Integer> getSpuStockNum(Map<Long, List<ProductSkuEntity>> productIdSkusMap) {
		if (CollectionUtils.isEmpty(productIdSkusMap)) {
			return Maps.newHashMap();
		}
		// 1.获取 skuId,查询库存信息
		Set<Long> skuIdList = productIdSkusMap.values().stream().flatMap(Collection::stream).map(ProductSkuEntity::getId).collect(Collectors.toSet());
		Map<Long, Integer> skuIdStockNumMap = pharmacyGoodsRemoteProService.getSkuStockNumToMap(skuIdList);
		// 2.组装返回值
		Map<Long, Integer> spuIdStockNumMap = Maps.newHashMapWithExpectedSize(productIdSkusMap.size());
		for (Map.Entry<Long, List<ProductSkuEntity>> spuIdSkuEntityListEntry : productIdSkusMap.entrySet()) {
			Integer stockNum = 0;
			for (ProductSkuEntity productSkuEntity : spuIdSkuEntityListEntry.getValue()) {
				stockNum += skuIdStockNumMap.getOrDefault(productSkuEntity.getId(), 0);
			}
			spuIdStockNumMap.put(spuIdSkuEntityListEntry.getKey(), stockNum);
		}
		return spuIdStockNumMap;
	}

	private ProductDetailVO buildProductDetailVO(ProductBasicsInfoEntity productEntity, ProductExtendEntity extendEntity, List<ProductSkuEntity> productSkuEntityList, Map<Long, Integer> skuStockNumToMap) {
		long inventoryNum = 0L;
		if (!CollectionUtils.isEmpty(skuStockNumToMap)) {
			IntSummaryStatistics summaryStatistics = skuStockNumToMap.values().stream().mapToInt((s) -> s).summaryStatistics();
			inventoryNum = summaryStatistics.getSum();
		}
		ProductDetailVO productDetailVO = new ProductDetailVO();
		productDetailVO.setSpuCode(productEntity.getSpuCode());
		productDetailVO.setProductName(productEntity.getTitle());
		productDetailVO.setImageUrlList(Lists.newArrayList(extendEntity.getImageUrl().split(",")));
		productDetailVO.setDetailUrlList(Lists.newArrayList(extendEntity.getDetailUrl().split(",")));
		productDetailVO.setStoreId(productEntity.getSourceId());
		productDetailVO.setStoreName(productEntity.getSourceName());
		productDetailVO.setInventoryNum(inventoryNum);
		ProductSkuEntity lowSku = productSkuEntityList.get(0);
		ProductSkuEntity highSku = productSkuEntityList.get(productSkuEntityList.size() - 1);
		productDetailVO.setLowPrice(BigDecimalUtil.F2Y(lowSku.getSkuPrice().longValue()));
		productDetailVO.setHighPrice(BigDecimalUtil.F2Y(highSku.getSkuPrice().longValue()));
		productDetailVO.setPackName(lowSku.getPackName());
		productDetailVO.setPackValue(lowSku.getPackValue());
//		productDetailVO.setHasUnpaidFlag();
		return productDetailVO;
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
			// TODO 这里需要 cms 提供远程接口
			packInfoResponse.setCommAttributeName(CommAttributeEnum.getValueByCode(productSkuEntity.getCommAttribute()));
			packInfoResponse.setSkuCode(productSkuEntity.getSkuCode());
			packInfoList.add(packInfoResponse);
		}
		productManageVO.setPackInfoList(packInfoList);

		return productManageVO;
	}

	private void saveOrUpdateSku(SaveProductRequest saveProductRequest, StoreEntity storeEntity, ProductBasicsInfoEntity infoEntity) {
		Map<Long, ProductSkuEntity> skuIdEntityMap = productSkuService.queryByProductIdToMap(infoEntity.getId(), null);

		List<ProductSkuEntity> skuEntityList = Lists.newArrayListWithCapacity(saveProductRequest.getPackInfoList().size());
		for (SaveProductRequest.PackInfoRequest packInfoRequest : saveProductRequest.getPackInfoList()) {
			ProductSkuEntity skuEntity = new ProductSkuEntity();
			if (Objects.nonNull(packInfoRequest.getSkuId()) && skuIdEntityMap.containsKey(packInfoRequest.getSkuId())) {
				// 更新
				skuEntity = skuIdEntityMap.get(packInfoRequest.getSkuId());
				if (Objects.equals(UpOffEnum.UP.getCode(), skuEntity.getState())) {
					throw new BusinessException(ErrorEnums.UPDATE_NOT_ALLOW);
				}
			} else {
				skuEntity.setSkuCode(productSkuService.createNextSkuCode(infoEntity.getSpuCode(), infoEntity.getId()));
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
		if (Objects.isNull(extendEntity)) {
			extendEntity = new ProductExtendEntity();
			extendEntity.setCreatedBy(saveProductRequest.getUserId());
		}
		extendEntity.setProductId(productId);
		extendEntity.setDescription(saveProductRequest.getDescription());
		extendEntity.setApprovalNumber(saveProductRequest.getApprovalNumber());
		extendEntity.setVendorName(saveProductRequest.getVendorName());
		extendEntity.setImageUrl(Joiner.on(",").join(saveProductRequest.getImageUrlList()));
		extendEntity.setDetailUrl(Joiner.on(",").join(saveProductRequest.getDetailUrlList()));
		extendEntity.setChangedBy(saveProductRequest.getUserId());
		extendEntity.setChangedAt(LocalDateTime.now());
		productExtendService.saveOrUpdate(extendEntity);
	}

	private ProductBasicsInfoEntity saveProductBaseInfo(SaveProductRequest saveProductRequest, StoreEntity storeEntity) {
		ProductBasicsInfoEntity infoEntity = new ProductBasicsInfoEntity();
		if (Objects.nonNull(saveProductRequest.getProductId()) && !Objects.equals(saveProductRequest.getProductId(), 0L)) {
			infoEntity = queryBasicsInfoByProductId(saveProductRequest.getProductId());
			if (Objects.isNull(infoEntity)) {
				throw new BusinessException(ErrorEnums.PRODUCT_NOT_EXIST);
			}
		} else {
			infoEntity.setSpuCode(getNextProductNumber(ProductTypeEnum.PRODUCT));
			infoEntity.setCreatedBy(saveProductRequest.getUserId());
		}
		// 更新分类下的商品数量
		updateCategoryProductNum(infoEntity.getId(), saveProductRequest.getCategoryId(), infoEntity.getCategoryId());
		// 更新商品基础信息
		BeanUtils.copyProperties(saveProductRequest, infoEntity);
		infoEntity.setMasterImageUrl(saveProductRequest.getImageUrlList().get(0));
		infoEntity.setSourceId(storeEntity.getId());
		infoEntity.setSourceName(storeEntity.getName());
		infoEntity.setChangedBy(saveProductRequest.getUserId());
		infoEntity.setChangedAt(LocalDateTime.now());
		saveOrUpdate(infoEntity);
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

	private LambdaQueryWrapper<ProductBasicsInfoEntity> buildQuerySpuParam(QuerySpuRequest querySpuRequest) {
		LambdaQueryWrapper<ProductBasicsInfoEntity> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.eq(ProductBasicsInfoEntity::getDelFlag, DelFlagEnum.UN_DELETED.getCode());
		if (Objects.nonNull(querySpuRequest.getUpOffEnum())) {
			queryWrapper.eq(ProductBasicsInfoEntity::getState, querySpuRequest.getUpOffEnum().getCode());
		}
		if (StringUtils.isNotBlank(querySpuRequest.getSpuCode())) {
			queryWrapper.eq(ProductBasicsInfoEntity::getSpuCode, querySpuRequest.getSpuCode());
		}
		if (Objects.nonNull(querySpuRequest.getProductId())) {
			queryWrapper.eq(ProductBasicsInfoEntity::getId, querySpuRequest.getProductId());
		}
		if (!CollectionUtils.isEmpty(querySpuRequest.getProductIdList())) {
			queryWrapper.in(ProductBasicsInfoEntity::getId, querySpuRequest.getProductIdList());
		}
		if (StringUtils.isNotBlank(querySpuRequest.getProductName())) {
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
