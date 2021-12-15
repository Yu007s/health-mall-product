package com.drstrong.health.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.drstrong.health.product.dao.ProductSkuMapper;
import com.drstrong.health.product.model.dto.ProductSkuDTO;
import com.drstrong.health.product.model.entity.product.ProductSkuEntity;
import com.drstrong.health.product.model.enums.DelFlagEnum;
import com.drstrong.health.product.model.request.product.QuerySkuRequest;
import com.drstrong.health.product.model.response.PageVO;
import com.drstrong.health.product.model.response.product.ProductSkuVO;
import com.drstrong.health.product.service.ProductSkuService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import static java.util.stream.Collectors.groupingBy;

/**
 * @author liuqiuyi
 * @date 2021/12/13 17:11
 */
@Slf4j
@Service
public class ProductSkuServiceImpl implements ProductSkuService {
	@Resource
	ProductSkuMapper productSkuMapper;

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
	 * 根据 商品id 集合,查询 sku 集合
	 *
	 * @param productIdList 商品 id 集合
	 * @return sku 信息
	 * @author liuqiuyi
	 * @date 2021/12/14 10:51
	 */
	@Override
	public List<ProductSkuDTO> queryByProductIdList(Set<Long> productIdList) {
		if (CollectionUtils.isEmpty(productIdList)) {
			return Lists.newArrayList();
		}
		LambdaQueryWrapper<ProductSkuEntity> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.in(ProductSkuEntity::getProductId, productIdList).eq(ProductSkuEntity::getDelFlag, DelFlagEnum.UN_DELETED.getCode());
		List<ProductSkuEntity> productSkuEntities = productSkuMapper.selectList(queryWrapper);
		if (CollectionUtils.isEmpty(productSkuEntities)) {
			return Lists.newArrayList();
		}
		List<ProductSkuDTO> skuDTOList = Lists.newArrayListWithCapacity(productSkuEntities.size());
		for (ProductSkuEntity productSkuEntity : productSkuEntities) {
			ProductSkuDTO productSkuDTO = new ProductSkuDTO();
			BeanUtils.copyProperties(productSkuEntity, productSkuDTO);
			skuDTOList.add(productSkuDTO);
		}
		return skuDTOList;
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
	public Map<Long, List<ProductSkuDTO>> queryByProductIdListToMap(Set<Long> productIdList) {
		List<ProductSkuDTO> productSkuEntities = queryByProductIdList(productIdList);
		if (CollectionUtils.isEmpty(productSkuEntities)) {
			return Maps.newHashMap();
		}
		return productSkuEntities.stream().collect(groupingBy(ProductSkuDTO::getProductId));
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
//			productSkuVO.setPrice(record.getSkuPrice());
			productSkuVO.setCreateTime(record.getCreatedAt());
			productSkuVO.setUpdateTime(record.getChangedAt());
			productSkuVO.setSkuState(record.getState());
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
	public ProductSkuEntity queryBySkuIdOrCode(Long skuId, String skuCode) {
		if (Objects.isNull(skuId) && Objects.isNull(skuCode)) {
			return null;
		}
		LambdaQueryWrapper<ProductSkuEntity> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.eq(ProductSkuEntity::getDelFlag, DelFlagEnum.UN_DELETED.getCode());
		if (Objects.nonNull(skuId)) {
			queryWrapper.eq(ProductSkuEntity::getId, skuId);
		}
		if (Objects.nonNull(skuCode)) {
			queryWrapper.eq(ProductSkuEntity::getSkuCode, skuCode);
		}
		return productSkuMapper.selectOne(queryWrapper);
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
