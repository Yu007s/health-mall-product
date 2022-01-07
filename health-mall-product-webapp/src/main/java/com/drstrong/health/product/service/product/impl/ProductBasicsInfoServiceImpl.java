package com.drstrong.health.product.service.product.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.drstrong.health.product.dao.product.ProductBasicsInfoMapper;
import com.drstrong.health.product.model.entity.product.ProductBasicsInfoEntity;
import com.drstrong.health.product.model.entity.product.ProductExtendEntity;
import com.drstrong.health.product.model.entity.product.ProductSkuEntity;
import com.drstrong.health.product.model.enums.DelFlagEnum;
import com.drstrong.health.product.model.enums.ErrorEnums;
import com.drstrong.health.product.model.enums.UpOffEnum;
import com.drstrong.health.product.model.request.PageRequest;
import com.drstrong.health.product.model.request.product.ProductSearchRequest;
import com.drstrong.health.product.model.request.product.QuerySpuRequest;
import com.drstrong.health.product.model.response.PageVO;
import com.drstrong.health.product.model.response.product.ProductDetailVO;
import com.drstrong.health.product.model.response.product.ProductRecommendVO;
import com.drstrong.health.product.model.response.product.ProductSearchDetailVO;
import com.drstrong.health.product.model.response.result.BusinessException;
import com.drstrong.health.product.remote.pro.PharmacyGoodsRemoteProService;
import com.drstrong.health.product.remote.pro.ShopCartRemoteProService;
import com.drstrong.health.product.service.product.ProductBasicsInfoService;
import com.drstrong.health.product.service.product.ProductExtendService;
import com.drstrong.health.product.service.product.ProductSkuService;
import com.drstrong.health.product.util.BigDecimalUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
	ProductSkuService productSkuService;

	@Resource
	PharmacyGoodsRemoteProService pharmacyGoodsRemoteProService;

	@Resource
	ShopCartRemoteProService shopCartRemoteProService;

	/**
	 * TableId 注解存在更新记录，否插入一条记录
	 *
	 * @param entity 实体对象
	 * @return boolean
	 */
	@Override
	public boolean saveOrUpdate(ProductBasicsInfoEntity entity) {
		return super.saveOrUpdate(entity);
	}

	@Override
	public boolean updateBatchById(Collection<ProductBasicsInfoEntity> entityList) {
		return super.updateBatchById(entityList);
	}

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
	 * 小程序 - 根据 spuCode 获取商品详细信息
	 *
	 * @param spuCode spu编码
	 * @return spu信息
	 * @author liuqiuyi
	 * @date 2021/12/16 19:55
	 */
	@Override
	public ProductDetailVO getSpuInfo(String spuCode, Long userId) {
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
		// 4.组装返回值
		ProductDetailVO productDetailVO = buildProductDetailVO(productEntity, extendEntity, productSkuEntityList, skuStockNumToMap);
		// 5.调用商品购物车接口,设置用户购物车是否有商品
		Boolean hasProduct = shopCartRemoteProService.checkHasProduct(userId);
		productDetailVO.setHasUnpaidFlag(hasProduct);
		return productDetailVO;
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
		log.info("ProductBasicsInfoServiceImpl.searchByName param:{}", content);
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
		log.info("ProductBasicsInfoServiceImpl.pageSearchDetail param:{}", productSearchRequest);
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
		return productDetailVO;
	}


	@Override
	public LambdaQueryWrapper<ProductBasicsInfoEntity> buildQuerySpuParam(QuerySpuRequest querySpuRequest) {
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
			LocalDateTime localDateTime = LocalDateTime.of(querySpuRequest.getCreateStart(), LocalTime.MIN);
			queryWrapper.gt(ProductBasicsInfoEntity::getCreatedAt, localDateTime);
		}
		if (Objects.nonNull(querySpuRequest.getCreateEnd())) {
			LocalDateTime localDateTime = LocalDateTime.of(querySpuRequest.getCreateEnd(), LocalTime.MAX);
			queryWrapper.lt(ProductBasicsInfoEntity::getCreatedAt, localDateTime);
		}
		if (!CollectionUtils.isEmpty(querySpuRequest.getBackCategoryIdList())) {
			queryWrapper.in(ProductBasicsInfoEntity::getCategoryId, querySpuRequest.getBackCategoryIdList());
		}
		return queryWrapper;
	}
}
