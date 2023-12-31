package com.drstrong.health.product.service.product.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.drstrong.health.product.dao.product.ProductSkuMapper;
import com.drstrong.health.product.model.entity.product.ProductBasicsInfoEntity;
import com.drstrong.health.product.model.entity.product.ProductSkuEntity;
import com.drstrong.health.product.model.enums.DelFlagEnum;
import com.drstrong.health.product.model.enums.ErrorEnums;
import com.drstrong.health.product.model.enums.UpOffEnum;
import com.drstrong.health.product.model.request.product.QuerySkuRequest;
import com.drstrong.health.product.model.response.product.SkuBaseInfoVO;
import com.drstrong.health.product.model.response.result.BusinessException;
import com.drstrong.health.product.remote.pro.PharmacyGoodsRemoteProService;
import com.drstrong.health.product.service.product.ProductBasicsInfoService;
import com.drstrong.health.product.service.product.ProductSkuService;
import com.drstrong.health.product.util.BigDecimalUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toMap;

/**
 * @author liuqiuyi
 * @date 2021/12/13 17:11
 */
@Slf4j
@Service
public class ProductSkuServiceImpl extends ServiceImpl<ProductSkuMapper, ProductSkuEntity> implements ProductSkuService {
	@Resource
	ProductSkuMapper productSkuMapper;

	@Resource
	ProductBasicsInfoService productBasicsInfoService;

	@Resource
	PharmacyGoodsRemoteProService pharmacyGoodsRemoteProService;

	/**
	 * 批量保存 sku 信息
	 *
	 * @param skuEntityList 入参信息
	 * @author liuqiuyi
	 * @date 2021/12/13 17:11
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void batchSave(List<ProductSkuEntity> skuEntityList) {
		if (CollectionUtils.isEmpty(skuEntityList)) {
			return;
		}
		productSkuMapper.batchInsert(skuEntityList);
	}

	@Override
	public boolean saveOrUpdateBatch(Collection<ProductSkuEntity> entityList, int batchSize) {
		return super.saveOrUpdateBatch(entityList, batchSize);
	}

	/**
	 * 根据 商品id 查询 sku 集合
	 *
	 * @param productId 商品 id
	 * @return 商品 sku 集合
	 * @author liuqiuyi
	 * @date 2021/12/13 21:13
	 */
	@Override
	public List<ProductSkuEntity> queryByProductId(Long productId, UpOffEnum upOffEnum) {
		if (Objects.isNull(productId)) {
			return Lists.newArrayList();
		}
		LambdaQueryWrapper<ProductSkuEntity> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.eq(ProductSkuEntity::getProductId, productId).eq(ProductSkuEntity::getDelFlag, DelFlagEnum.UN_DELETED.getCode());
		if (Objects.nonNull(upOffEnum)) {
			queryWrapper.eq(ProductSkuEntity::getState, upOffEnum.getCode());
		}
		return productSkuMapper.selectList(queryWrapper);
	}

	/**
	 * 根据 商品id 查询 sku 集合,转成 skuMap
	 *
	 * @param productId 商品 id
	 * @return 商品 sku 集合,key=skuId,value=sku 信息
	 * @author liuqiuyi
	 * @date 2021/12/13 21:13
	 */
	@Override
	public Map<Long, ProductSkuEntity> queryByProductIdToMap(Long productId, UpOffEnum upOffEnum) {
		List<ProductSkuEntity> productSkuEntities = queryByProductId(productId, upOffEnum);
		return productSkuEntities.stream().collect(toMap(ProductSkuEntity::getId, dto -> dto, (v1, v2) -> v1));
	}

	/**
	 * 根据 商品id 集合,查询 sku 集合
	 *
	 * @param productIdList 商品 id 集合
	 * @return sku 信息
	 * @author liuqiuyi
	 * @date 2021/12/14 10:51
	 */
	@Override
	public List<ProductSkuEntity> queryByProductIdList(Set<Long> productIdList) {
		if (CollectionUtils.isEmpty(productIdList)) {
			return Lists.newArrayList();
		}
		LambdaQueryWrapper<ProductSkuEntity> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.in(ProductSkuEntity::getProductId, productIdList).eq(ProductSkuEntity::getDelFlag, DelFlagEnum.UN_DELETED.getCode());
		return productSkuMapper.selectList(queryWrapper);
	}

	/**
	 * 根据 商品id 集合,查询 sku 集合,组装为 map
	 *
	 * @param productIdList 商品 id 集合
	 * @return Map.key = productId,map.value = sku 集合
	 * @author liuqiuyi
	 * @date 2021/12/14 10:54
	 */
	@Override
	public Map<Long, List<ProductSkuEntity>> queryByProductIdListToMap(Set<Long> productIdList) {
		List<ProductSkuEntity> productSkuEntities = queryByProductIdList(productIdList);
		if (CollectionUtils.isEmpty(productSkuEntities)) {
			return Maps.newHashMap();
		}
		return productSkuEntities.stream().collect(groupingBy(ProductSkuEntity::getProductId));
	}


	/**
	 * 根据条件查询 sku 信息
	 *
	 * @param querySkuRequest 查询条件
	 * @return sku 信息
	 * @author liuqiuyi
	 * @date 2021/12/23 21:17
	 */
	@Override
	public List<ProductSkuEntity> querySkuByParam(QuerySkuRequest querySkuRequest) {
		LambdaQueryWrapper<ProductSkuEntity> queryWrapper = buildQuerySkuParam(querySkuRequest);
		return productSkuMapper.selectList(queryWrapper);
	}


	@Override
	public Map<Long, Integer> searchSkuCountMap(List<Long> storeIds) {
		Map<Long, Integer> result = new HashMap<>();
		LambdaQueryWrapper<ProductSkuEntity> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.eq(ProductSkuEntity::getDelFlag, DelFlagEnum.UN_DELETED.getCode())
				.in(ProductSkuEntity::getSourceId, storeIds);
		List<ProductSkuEntity> productSkuEntities = productSkuMapper.selectList(queryWrapper);
		Map<Long, List<ProductSkuEntity>> map = productSkuEntities.stream().collect(groupingBy(ProductSkuEntity::getSourceId));
		map.forEach((k, v) -> result.put(k, v.size()));
		return result;
	}

	/**
	 * 根据 skuId 集合删除 sku
	 *
	 * @param skuIdList skuId
	 * @param userId    用户 id
	 * @author liuqiuyi
	 * @date 2021/12/29 20:13
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void deleteBySkuIdList(Set<Long> skuIdList, String userId) {
		if (CollectionUtils.isEmpty(skuIdList) || StringUtils.isBlank(userId)) {
			throw new BusinessException(ErrorEnums.PARAM_IS_NOT_NULL);
		}
		productSkuMapper.deleteBySkuIdList(skuIdList, userId);
	}

	/**
	 * 根据 skuId 或者 skuCode 查询 sku 信息
	 *
	 * @param skuId   sku 的 id
	 * @param skuCode sku 编码
	 * @return sku 信息
	 * @author liuqiuyi
	 * @date 2021/12/14 15:05
	 */
	@Override
	public ProductSkuEntity queryBySkuIdOrCode(Long skuId, String skuCode, UpOffEnum upOffEnum) {
		Set<Long> skuIdList = null;
		if (Objects.nonNull(skuId)) {
			skuIdList = Sets.newHashSetWithExpectedSize(2);
			skuIdList.add(skuId);
		}
		Set<String> skuCodeList = null;
		if (StringUtils.isNotBlank(skuCode)) {
			skuCodeList = Sets.newHashSetWithExpectedSize(2);
			skuCodeList.add(skuCode);
		}
		List<ProductSkuEntity> productSkuEntities = queryBySkuIdOrCode(skuIdList, skuCodeList, upOffEnum);
		if (CollectionUtils.isEmpty(productSkuEntities)) {
			return null;
		}
		return productSkuEntities.get(0);
	}

	/**
	 * 根据 skuId 集合或者 skuCode 集合查询 sku 信息
	 *
	 * @param skuIdList   skuId 集合
	 * @param skuCodeList sku编码集合
	 * @return sku 信息
	 * @author liuqiuyi
	 * @date 2021/12/16 09:59
	 */
	@Override
	public List<ProductSkuEntity> queryBySkuIdOrCode(Set<Long> skuIdList, Set<String> skuCodeList, UpOffEnum upOffEnum) {
		if (CollectionUtils.isEmpty(skuCodeList) && CollectionUtils.isEmpty(skuIdList)) {
			return Lists.newArrayList();
		}
		LambdaQueryWrapper<ProductSkuEntity> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.eq(ProductSkuEntity::getDelFlag, DelFlagEnum.UN_DELETED.getCode());
		if (!CollectionUtils.isEmpty(skuIdList)) {
			queryWrapper.in(ProductSkuEntity::getId, skuIdList);
		}
		if (!CollectionUtils.isEmpty(skuCodeList)) {
			queryWrapper.in(ProductSkuEntity::getSkuCode, skuCodeList);
		}
		if (Objects.nonNull(upOffEnum)) {
			queryWrapper.eq(ProductSkuEntity::getState, upOffEnum.getCode());
		}
		return productSkuMapper.selectList(queryWrapper);
	}

	/**
	 * 根据 skuId 集合或者 skuCode 集合查询 sku 信息(包含已删除的数据)
	 * <p> 包含 delFlag 为 1 的数据 </>
	 *
	 * @param skuIdList   skuId 集合
	 * @param skuCodeList sku编码集合
	 * @param upOffCode   上架状态(0-未上架,1-已上架)
	 * @return sku 信息
	 * @author liuqiuyi
	 * @date 2022/1/10 16:54
	 */
	@Override
	public List<ProductSkuEntity> queryBySkuIdOrCodeContainDel(Set<Long> skuIdList, Set<String> skuCodeList, Integer upOffCode) {
		if (CollectionUtils.isEmpty(skuCodeList) && CollectionUtils.isEmpty(skuIdList)) {
			return Lists.newArrayList();
		}
		return productSkuMapper.selectListContainDel(skuIdList, skuCodeList, upOffCode);
	}

	@Override
	public void updateState(List<Long> skuIdList, Integer state, String userId) {
		ProductSkuEntity productSkuEntity = new ProductSkuEntity();
		LambdaUpdateWrapper<ProductSkuEntity> updateWrapper = new LambdaUpdateWrapper();
		productSkuEntity.setState(state);
		productSkuEntity.setChangedAt(LocalDateTime.now());
		productSkuEntity.setChangedBy(userId);
		updateWrapper.eq(ProductSkuEntity::getDelFlag, DelFlagEnum.UN_DELETED.getCode())
				.in(ProductSkuEntity::getId, skuIdList);
		productSkuMapper.update(productSkuEntity, updateWrapper);
	}

	/**
	 * 小程序 - 根据 spuCode 查询 sku 集合
	 *
	 * @param spuCode spu编码
	 * @return sku 集合
	 * @author liuqiuyi
	 * @date 2021/12/16 21:00
	 */
	@Override
	public List<SkuBaseInfoVO> listSkuBySpuCode(String spuCode) {
		if (StringUtils.isBlank(spuCode)) {
			return Lists.newArrayList();
		}
		// 1.获取 spu 信息
		ProductBasicsInfoEntity productEntity = productBasicsInfoService.getByProductIdOrSpuCode(null, spuCode, UpOffEnum.UP);
		if (Objects.isNull(productEntity)) {
			throw new BusinessException(ErrorEnums.PRODUCT_NOT_EXIST);
		}
		// 2.获取 sku 信息
		List<ProductSkuEntity> productSkuEntityList = queryByProductId(productEntity.getId(), UpOffEnum.UP);
		if (CollectionUtils.isEmpty(productSkuEntityList)) {
			throw new BusinessException(ErrorEnums.PRODUCT_NOT_EXIST);
		}
		// 3.调用库存
		Set<Long> skuIdList = productSkuEntityList.stream().map(ProductSkuEntity::getId).collect(Collectors.toSet());
		Map<Long, Integer> skuIdStockNumMap = pharmacyGoodsRemoteProService.getSkuStockNumToMap(skuIdList);
		// 4.组装返回值
		List<SkuBaseInfoVO> skuBaseInfoVOList = Lists.newArrayListWithCapacity(productSkuEntityList.size());
		for (ProductSkuEntity productSkuEntity : productSkuEntityList) {
			SkuBaseInfoVO infoVO = new SkuBaseInfoVO();
			BeanUtils.copyProperties(productSkuEntity, infoVO);
			infoVO.setPrice(BigDecimalUtil.F2Y(productSkuEntity.getSkuPrice().longValue()));
			infoVO.setInventoryNum(skuIdStockNumMap.getOrDefault(productSkuEntity.getId(), 0).longValue());
			skuBaseInfoVOList.add(infoVO);
		}
		// 5.整合所有的库存总数,前端需要根据该值进行判断
		long productCount = skuBaseInfoVOList.stream().mapToLong(SkuBaseInfoVO::getInventoryNum).sum();
		skuBaseInfoVOList.forEach(skuBaseInfoVO -> skuBaseInfoVO.setProductInventoryNum(productCount));
		return skuBaseInfoVOList;
	}

	/**
	 * 获取 sku 的最低价格和最高价格
	 *
	 * @author liuqiuyi
	 * @date 2021/12/18 14:50
	 */
	@Override
	public Map<String, BigDecimal> getPriceSectionMap(List<ProductSkuEntity> productSkuEntities) {
		Map<String, BigDecimal> priceMap = Maps.newHashMapWithExpectedSize(4);
		if (CollectionUtils.isEmpty(productSkuEntities)) {
			priceMap.put("lowPrice", new BigDecimal("0"));
			priceMap.put("highPrice", new BigDecimal("0"));
			return priceMap;
		}
		productSkuEntities.sort(Comparator.comparing(ProductSkuEntity::getSkuPrice));
		Integer lowPrice = productSkuEntities.get(0).getSkuPrice();
		Integer highPrice = productSkuEntities.get(productSkuEntities.size() - 1).getSkuPrice();
		priceMap.put("lowPrice", BigDecimalUtil.F2Y(lowPrice.longValue()));
		priceMap.put("highPrice", BigDecimalUtil.F2Y(highPrice.longValue()));
		return priceMap;
	}

	@Override
	public LambdaQueryWrapper<ProductSkuEntity> buildQuerySkuParam(QuerySkuRequest querySkuRequest) {
		LambdaQueryWrapper<ProductSkuEntity> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.eq(ProductSkuEntity::getDelFlag, DelFlagEnum.UN_DELETED.getCode());
		if (!CollectionUtils.isEmpty(querySkuRequest.getProductIdList())) {
			queryWrapper.in(ProductSkuEntity::getProductId, querySkuRequest.getProductIdList());
		}
		if (StringUtils.isNotBlank(querySkuRequest.getSkuCode())) {
			queryWrapper.eq(ProductSkuEntity::getSkuCode, querySkuRequest.getSkuCode());
		}
		if (Objects.nonNull(querySkuRequest.getSkuId())) {
			queryWrapper.eq(ProductSkuEntity::getId, querySkuRequest.getSkuId());
		}
		if (StringUtils.isNotBlank(querySkuRequest.getProductName())) {
			queryWrapper.like(ProductSkuEntity::getSkuName, querySkuRequest.getProductName());
		}
		if (Objects.nonNull(querySkuRequest.getStoreId())) {
			queryWrapper.eq(ProductSkuEntity::getSourceId, querySkuRequest.getStoreId());
		}
		if (Objects.nonNull(querySkuRequest.getCreateStart())) {
			LocalDateTime localDateTime = LocalDateTime.of(querySkuRequest.getCreateStart(), LocalTime.MIN);
			queryWrapper.ge(ProductSkuEntity::getCreatedAt, localDateTime);
		}
		if (Objects.nonNull(querySkuRequest.getCreateEnd())) {
			LocalDateTime localDateTime = LocalDateTime.of(querySkuRequest.getCreateEnd(), LocalTime.MAX);
			queryWrapper.le(ProductSkuEntity::getCreatedAt, localDateTime);
		}
		if (Objects.nonNull(querySkuRequest.getPriceStart())) {
			queryWrapper.ge(ProductSkuEntity::getSkuPrice, BigDecimalUtil.Y2F(querySkuRequest.getPriceStart()));
		}
		if (Objects.nonNull(querySkuRequest.getPriceEnd())) {
			queryWrapper.le(ProductSkuEntity::getSkuPrice, BigDecimalUtil.Y2F(querySkuRequest.getPriceEnd()));
		}
		if (Objects.nonNull(querySkuRequest.getSkuState())) {
			queryWrapper.eq(ProductSkuEntity::getState, querySkuRequest.getSkuState());
		}
		if (Objects.nonNull(querySkuRequest.getUpOffEnum())) {
			queryWrapper.eq(ProductSkuEntity::getState, querySkuRequest.getUpOffEnum().getCode());
		}
		return queryWrapper;
	}
}
