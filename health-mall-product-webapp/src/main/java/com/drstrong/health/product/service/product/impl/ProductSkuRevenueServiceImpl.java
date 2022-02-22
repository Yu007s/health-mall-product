package com.drstrong.health.product.service.product.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.drstrong.health.product.dao.product.ProductSkuRevenueMapper;
import com.drstrong.health.product.model.entity.product.ProductSkuEntity;
import com.drstrong.health.product.model.entity.product.ProductSkuRevenueEntity;
import com.drstrong.health.product.model.enums.DelFlagEnum;
import com.drstrong.health.product.model.enums.ErrorEnums;
import com.drstrong.health.product.model.request.product.AddRevenueRequest;
import com.drstrong.health.product.model.response.result.BusinessException;
import com.drstrong.health.product.service.product.ProductSkuRevenueService;
import com.drstrong.health.product.service.product.ProductSkuService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
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

import static java.util.stream.Collectors.toMap;

/**
 * 税收编码
 *
 * @author liuqiuyi
 * @date 2021/12/14 14:57
 */
@Service
@Slf4j
public class ProductSkuRevenueServiceImpl extends ServiceImpl<ProductSkuRevenueMapper, ProductSkuRevenueEntity> implements ProductSkuRevenueService {
	@Resource
	ProductSkuRevenueMapper productSkuRevenueMapper;

	@Resource
	ProductSkuService productSkuService;

	/**
	 * 根据 skuId 或者 skuCode 获取税收编码和税率
	 *
	 * @author liuqiuyi
	 * @date 2021/12/14 15:19
	 */
	@Override
	public ProductSkuRevenueEntity getSkuRevenue(Long skuId, String skuCode) {
		if (Objects.isNull(skuId) && StringUtils.isBlank(skuCode)) {
			return new ProductSkuRevenueEntity();
		}
		LambdaQueryWrapper<ProductSkuRevenueEntity> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.eq(ProductSkuRevenueEntity::getDelFlag, DelFlagEnum.UN_DELETED.getCode());
		if (Objects.nonNull(skuId)) {
			queryWrapper.eq(ProductSkuRevenueEntity::getSkuId, skuId);
		}
		if (StringUtils.isNotBlank(skuCode)) {
			queryWrapper.eq(ProductSkuRevenueEntity::getSkuCode, skuCode);
		}
		return productSkuRevenueMapper.selectOne(queryWrapper);
	}

	/**
	 * 根据 skuId 集合或者 skuCode 集合查询税收编码等信息
	 *
	 * @param skuIdList   skuId 集合
	 * @param skuCodeList sku 编码
	 * @return 税收编码信息
	 * @author liuqiuyi
	 * @date 2021/12/17 10:06
	 */
	@Override
	public List<ProductSkuRevenueEntity> listSkuRevenue(Set<Long> skuIdList, Set<String> skuCodeList) {
		if (CollectionUtils.isEmpty(skuIdList) && CollectionUtils.isEmpty(skuCodeList)) {
			return Lists.newArrayList();
		}
		LambdaQueryWrapper<ProductSkuRevenueEntity> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.eq(ProductSkuRevenueEntity::getDelFlag, DelFlagEnum.UN_DELETED.getCode());
		if (!CollectionUtils.isEmpty(skuIdList)) {
			queryWrapper.in(ProductSkuRevenueEntity::getSkuId, skuIdList);
		}
		if (!CollectionUtils.isEmpty(skuCodeList)) {
			queryWrapper.in(ProductSkuRevenueEntity::getSkuCode, skuCodeList);
		}
		return productSkuRevenueMapper.selectList(queryWrapper);
	}

	/**
	 * 根据 skuId 集合或者 skuCode 集合查询税收编码等信息
	 *
	 * @param skuIdList skuId 集合
	 * @return 税收编码信息Map  key = skuId,value = 税收编码信息
	 * @author liuqiuyi
	 * @date 2021/12/17 10:06
	 */
	@Override
	public Map<Long, ProductSkuRevenueEntity> listSkuRevenueToMap(Set<Long> skuIdList) {
		List<ProductSkuRevenueEntity> revenueEntityList = listSkuRevenue(skuIdList, null);
		if (CollectionUtils.isEmpty(revenueEntityList)) {
			return Maps.newHashMap();
		}
		return revenueEntityList.stream().collect(toMap(ProductSkuRevenueEntity::getSkuId, dto -> dto, (v1, v2) -> v1));
	}

	/**
	 * sku 保存税收编码
	 *
	 * @param addRevenueRequest 入参信息
	 * @author liuqiuyi
	 * @date 2021/12/14 14:49
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void revenueAdd(AddRevenueRequest addRevenueRequest) {
		// 1.校验 sku 是否存在
		ProductSkuEntity productSkuEntity = productSkuService.queryBySkuIdOrCode(addRevenueRequest.getSkuId(), addRevenueRequest.getSkuCode(), null);
		if (Objects.isNull(productSkuEntity)) {
			throw new BusinessException(ErrorEnums.PRODUCT_NOT_EXIST);
		}
		// 2.保存 or 更新
		ProductSkuRevenueEntity skuRevenueEntity = getSkuRevenue(addRevenueRequest.getSkuId(), addRevenueRequest.getSkuCode());
		if (Objects.isNull(skuRevenueEntity)) {
			skuRevenueEntity = new ProductSkuRevenueEntity();
			skuRevenueEntity.setCreatedBy(addRevenueRequest.getUserId());
		}
		skuRevenueEntity.setProductId(productSkuEntity.getProductId());
		skuRevenueEntity.setSkuId(productSkuEntity.getId());
		skuRevenueEntity.setSkuCode(productSkuEntity.getSkuCode());
		skuRevenueEntity.setRevenueCode(addRevenueRequest.getRevenueCode());
		skuRevenueEntity.setRevenueRate(addRevenueRequest.getRevenueRate());
		skuRevenueEntity.setChangedAt(LocalDateTime.now());
		skuRevenueEntity.setChangedBy(addRevenueRequest.getUserId());
		saveOrUpdate(skuRevenueEntity);
	}
}
