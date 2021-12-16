package com.drstrong.health.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.drstrong.health.product.dao.ProductSkuMapper;
import com.drstrong.health.product.model.entity.product.ProductSkuEntity;
import com.drstrong.health.product.model.enums.DelFlagEnum;
import com.drstrong.health.product.model.enums.UpOffEnum;
import com.drstrong.health.product.model.request.product.QuerySkuRequest;
import com.drstrong.health.product.model.response.PageVO;
import com.drstrong.health.product.model.response.product.ProductSkuVO;
import com.drstrong.health.product.service.IRedisService;
import com.drstrong.health.product.service.ProductSkuService;
import com.drstrong.health.product.util.BigDecimalUtil;
import com.drstrong.health.product.util.RedisKeyUtils;
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
import java.time.LocalDateTime;
import java.util.*;

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
	IRedisService redisService;

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
	public List<ProductSkuEntity> queryByProductId(Long productId) {
		if (Objects.isNull(productId)) {
			return Lists.newArrayList();
		}
		LambdaQueryWrapper<ProductSkuEntity> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.eq(ProductSkuEntity::getProductId, productId).eq(ProductSkuEntity::getDelFlag, DelFlagEnum.UN_DELETED.getCode());
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
	public Map<Long, ProductSkuEntity> queryByProductIdToMap(Long productId) {
		List<ProductSkuEntity> productSkuEntities = queryByProductId(productId);
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
	 * 根据条件分页查询 sku 信息
	 *
	 * @param querySkuRequest 查询参数
	 * @return sku 信息
	 * @author liuqiuyi
	 * @date 2021/12/14 14:04
	 */
	@Override
	public PageVO<ProductSkuVO> pageQuerySkuByParam(QuerySkuRequest querySkuRequest) {
		Page<ProductSkuEntity> queryPage = new Page<>(querySkuRequest.getPageNo(), querySkuRequest.getPageSize());
		LambdaQueryWrapper<ProductSkuEntity> queryWrapper = buildQuerySkuParam(querySkuRequest);
		Page<ProductSkuEntity> productSkuEntityPage = productSkuMapper.selectPage(queryPage, queryWrapper);
		if (Objects.isNull(productSkuEntityPage) || CollectionUtils.isEmpty(productSkuEntityPage.getRecords())) {
			return PageVO.emptyPageVo(querySkuRequest.getPageNo(), querySkuRequest.getPageSize());
		}
		List<ProductSkuVO> productSkuVOList = Lists.newArrayListWithCapacity(productSkuEntityPage.getRecords().size());
		for (ProductSkuEntity record : productSkuEntityPage.getRecords()) {
			ProductSkuVO productSkuVO = new ProductSkuVO();
			BeanUtils.copyProperties(record, productSkuVO);
			productSkuVO.setSkuId(record.getId());
			productSkuVO.setPrice(BigDecimalUtil.F2Y(record.getSkuPrice().longValue()));
			productSkuVO.setCreateTime(record.getCreatedAt());
			productSkuVO.setUpdateTime(record.getChangedAt());
			productSkuVO.setSkuState(record.getState());
			productSkuVO.setStoreId(record.getSourceId());
			productSkuVO.setStoreName(record.getSourceName());
			productSkuVOList.add(productSkuVO);
		}
		return PageVO.toPageVo(productSkuVOList, productSkuEntityPage.getTotal(), productSkuEntityPage.getSize(), productSkuEntityPage.getCurrent());
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

	@Override
	public void updateState(List<Long> skuIdList, Integer state, Long userId) {
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
	 * 获取下一个 sku 编码,参照之前的逻辑
	 *
	 * @param productId 商品 id
	 * @return 生成的 sku 编码
	 * @author liuqiuyi
	 * @date 2021/12/16 14:48
	 */
	@Override
	public String createNextSkuCode(String spuCode, Long productId) {
		if (Objects.isNull(productId)) {
			return null;
		}
		long serialNumber = redisService.incr(RedisKeyUtils.getSkuNum(productId));
		return spuCode + "-" + serialNumber;
	}

	private LambdaQueryWrapper<ProductSkuEntity> buildQuerySkuParam(QuerySkuRequest querySkuRequest) {
		LambdaQueryWrapper<ProductSkuEntity> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.eq(ProductSkuEntity::getDelFlag, DelFlagEnum.UN_DELETED.getCode());
		if (Objects.nonNull(querySkuRequest.getSkuCode())) {
			queryWrapper.eq(ProductSkuEntity::getSkuCode, querySkuRequest.getSkuCode());
		}
		if (Objects.nonNull(querySkuRequest.getProductName())) {
			queryWrapper.like(ProductSkuEntity::getSkuName, querySkuRequest.getProductName());
		}
		if (Objects.nonNull(querySkuRequest.getStoreId())) {
			queryWrapper.eq(ProductSkuEntity::getSourceId, querySkuRequest.getStoreId());
		}
		if (Objects.nonNull(querySkuRequest.getCreateStart())) {
			queryWrapper.ge(ProductSkuEntity::getCreatedAt, querySkuRequest.getCreateStart());
		}
		if (Objects.nonNull(querySkuRequest.getCreateEnd())) {
			queryWrapper.le(ProductSkuEntity::getCreatedAt, querySkuRequest.getCreateEnd());
		}
		if (Objects.nonNull(querySkuRequest.getPriceStart())) {
			queryWrapper.ge(ProductSkuEntity::getSkuPrice, querySkuRequest.getPriceStart());
		}
		if (Objects.nonNull(querySkuRequest.getPriceEnd())) {
			queryWrapper.le(ProductSkuEntity::getSkuPrice, querySkuRequest.getPriceEnd());
		}
		return queryWrapper;
	}
}
