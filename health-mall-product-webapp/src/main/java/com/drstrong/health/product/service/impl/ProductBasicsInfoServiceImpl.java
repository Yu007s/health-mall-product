package com.drstrong.health.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.drstrong.health.product.dao.ProductBasicsInfoMapper;
import com.drstrong.health.product.model.entity.category.BackCategoryEntity;
import com.drstrong.health.product.model.entity.product.*;
import com.drstrong.health.product.model.entity.store.StoreEntity;
import com.drstrong.health.product.model.enums.*;
import com.drstrong.health.product.model.request.product.ProductSearchRequest;
import com.drstrong.health.product.model.request.product.QuerySpuRequest;
import com.drstrong.health.product.model.request.product.SaveProductRequest;
import com.drstrong.health.product.model.response.PageVO;
import com.drstrong.health.product.model.response.product.*;
import com.drstrong.health.product.model.response.result.BusinessException;
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
	CategoryAttributeService categoryAttributeService;

	@Resource
	BackCategoryService backCategoryService;

	@Resource
	IRedisService redisService;

	@Resource
	StoreService storeService;

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
	public void saveOrUpdateProduct(SaveProductRequest saveProductRequest) {
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
		// TODO 调用库存的接口

		// 4.组装返回值
		return buildProductDetailVO(productEntity, extendEntity, productSkuEntityList);
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
	public List<String> pageSearchByName(String content, Integer count) {
		if (StringUtils.isBlank(content)) {
			return Lists.newArrayList();
		}
		List<String> titleList = productBasicsInfoMapper.likeProductTitle(content, count);
		// 如果商城的搜索结果为空,或者没有达到分页条数
		if (CollectionUtils.isEmpty(titleList) || titleList.size() < count) {
			// TODO 搜索老系统,获取结果 @坤鹏
		}
		return titleList;
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
		querySpuRequest.setProductName(productSearchRequest.getContent());
		querySpuRequest.setPageNo(productSearchRequest.getPageNo());
		querySpuRequest.setPageSize(productSearchRequest.getPageSize());
		querySpuRequest.setUpOffEnum(UpOffEnum.UP);
		Page<ProductBasicsInfoEntity> infoEntityPage = pageQueryProductByParam(querySpuRequest);
		if (Objects.isNull(infoEntityPage) || CollectionUtils.isEmpty(infoEntityPage.getRecords())) {
			return PageVO.newBuilder().pageNo(productSearchRequest.getPageNo()).pageSize(productSearchRequest.getPageSize()).totalCount(0).result(new ArrayList<>()).build();
		}
		// 2.查询商品 sku 信息
		Map<Long, List<ProductSkuEntity>> productIdSkusMap = buildSkuMap(infoEntityPage.getRecords());
		// TODO 判断库存是否充足
		// 3.组装返回值信息
		List<ProductSearchDetailVO> detailVOList = Lists.newArrayListWithCapacity(infoEntityPage.getRecords().size());
		for (ProductBasicsInfoEntity infoEntity : infoEntityPage.getRecords()) {
			ProductSearchDetailVO detailVO = new ProductSearchDetailVO();
			detailVO.setSpuCode(infoEntity.getSpuCode());
			detailVO.setMasterImageUrl(infoEntity.getMasterImageUrl());
			detailVO.setProductName(infoEntity.getTitle());
			Map<String, BigDecimal> priceSectionMap = productSkuService.getPriceSectionMap(productIdSkusMap.getOrDefault(infoEntity.getId(), Lists.newArrayList()));
			detailVO.setLowPrice(priceSectionMap.get("lowPrice"));
			detailVOList.add(detailVO);
		}
		return PageVO.newBuilder().pageNo(productSearchRequest.getPageNo()).pageSize(productSearchRequest.getPageSize()).totalCount((int) infoEntityPage.getTotal()).result(detailVOList).build();
	}

	private ProductDetailVO buildProductDetailVO(ProductBasicsInfoEntity productEntity, ProductExtendEntity extendEntity, List<ProductSkuEntity> productSkuEntityList) {
		ProductDetailVO productDetailVO = new ProductDetailVO();
		productDetailVO.setSpuCode(productEntity.getSpuCode());
		productDetailVO.setProductTitle(productEntity.getTitle());
		productDetailVO.setImageUrlList(Lists.newArrayList(extendEntity.getImageUrl().split(",")));
		productDetailVO.setDetailUrlList(Lists.newArrayList(extendEntity.getDetailUrl().split(",")));
		productDetailVO.setStoreId(productEntity.getSourceId());
		productDetailVO.setStoreName(productEntity.getSourceName());
//		productDetailVO.setInventoryNum();
		ProductSkuEntity lowSku = productSkuEntityList.get(0);
		ProductSkuEntity highSku = productSkuEntityList.get(0);
		productDetailVO.setPriceStart(BigDecimalUtil.F2Y(lowSku.getSkuPrice().longValue()));
		productDetailVO.setPriceEnd(BigDecimalUtil.F2Y(highSku.getSkuPrice().longValue()));
		productDetailVO.setPackName(lowSku.getPackName());
		productDetailVO.setPackValue(lowSku.getPackValue());
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
		BeanUtils.copyProperties(saveProductRequest, infoEntity);
		infoEntity.setMasterImageUrl(saveProductRequest.getImageUrlList().get(0));
		infoEntity.setSourceId(storeEntity.getId());
		infoEntity.setSourceName(storeEntity.getName());
		infoEntity.setChangedBy(saveProductRequest.getUserId());
		infoEntity.setChangedAt(LocalDateTime.now());
		saveOrUpdate(infoEntity);
		return infoEntity;
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
