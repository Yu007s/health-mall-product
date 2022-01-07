package com.drstrong.health.product.service.product.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.drstrong.health.product.dao.product.ProductSkuMapper;
import com.drstrong.health.product.model.dto.CommAttributeDTO;
import com.drstrong.health.product.model.entity.product.ProductBasicsInfoEntity;
import com.drstrong.health.product.model.entity.product.ProductSkuEntity;
import com.drstrong.health.product.model.entity.product.ProductSkuRevenueEntity;
import com.drstrong.health.product.model.enums.DelFlagEnum;
import com.drstrong.health.product.model.enums.UpOffEnum;
import com.drstrong.health.product.model.request.product.QuerySkuRequest;
import com.drstrong.health.product.model.request.product.QuerySkuStockRequest;
import com.drstrong.health.product.model.request.product.QuerySpuRequest;
import com.drstrong.health.product.model.response.PageVO;
import com.drstrong.health.product.model.response.product.ProductSkuStockVO;
import com.drstrong.health.product.model.response.product.ProductSkuVO;
import com.drstrong.health.product.remote.cms.CmsRemoteProService;
import com.drstrong.health.product.remote.pro.PharmacyGoodsRemoteProService;
import com.drstrong.health.product.service.product.ProductBasicsInfoService;
import com.drstrong.health.product.service.product.ProductSkuManageService;
import com.drstrong.health.product.service.product.ProductSkuRevenueService;
import com.drstrong.health.product.service.product.ProductSkuService;
import com.drstrong.health.product.service.redis.IRedisService;
import com.drstrong.health.product.util.BigDecimalUtil;
import com.drstrong.health.product.util.RedisKeyUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 商品 sku 管理端 service
 *
 * @author liuqiuyi
 * @date 2022/1/7 16:32
 */
@Slf4j
@Service
public class ProductSkuManageServiceImpl implements ProductSkuManageService {

	@Resource
	ProductSkuService productSkuService;

	@Resource
	ProductSkuMapper productSkuMapper;

	@Resource
	ProductBasicsInfoService productBasicsInfoService;

	@Resource
	ProductSkuRevenueService productSkuRevenueService;

	@Resource
	PharmacyGoodsRemoteProService pharmacyGoodsRemoteProService;

	@Resource
	CmsRemoteProService cmsRemoteProService;

	@Resource
	IRedisService redisService;

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
		// 1.分页查询 sku 表
		Page<ProductSkuEntity> queryPage = new Page<>(querySkuRequest.getPageNo(), querySkuRequest.getPageSize());
		LambdaQueryWrapper<ProductSkuEntity> queryWrapper = productSkuService.buildQuerySkuParam(querySkuRequest);
		Page<ProductSkuEntity> productSkuEntityPage = productSkuMapper.selectPage(queryPage, queryWrapper);
		if (Objects.isNull(productSkuEntityPage) || CollectionUtils.isEmpty(productSkuEntityPage.getRecords())) {
			return PageVO.newBuilder().pageNo(querySkuRequest.getPageNo()).pageSize(querySkuRequest.getPageSize()).totalCount(0).result(Lists.newArrayList()).build();
		}
		// 2.获取 id 集合
		List<ProductSkuEntity> skuEntityList = productSkuEntityPage.getRecords();
		Set<Long> skuIdList = Sets.newHashSetWithExpectedSize(skuEntityList.size());
		Set<Long> productIdList = Sets.newHashSetWithExpectedSize(skuEntityList.size());
		skuEntityList.forEach(skuEntity -> {
			skuIdList.add(skuEntity.getId());
			productIdList.add(skuEntity.getProductId());
		});
		// 3.查询税收编码
		Map<Long, ProductSkuRevenueEntity> skuIdRevenueEntityMap = productSkuRevenueService.listSkuRevenueToMap(skuIdList);
		// 4.查询 spu 的主图信息
		QuerySpuRequest querySpuRequest = new QuerySpuRequest();
		querySpuRequest.setProductIdList(productIdList);
		Map<Long, ProductBasicsInfoEntity> productIdEntityMap = productBasicsInfoService.queryProductByParamToMap(querySpuRequest);
		// 3.组装返回值
		List<ProductSkuVO> productSkuVOList = Lists.newArrayListWithCapacity(skuEntityList.size());
		for (ProductSkuEntity record : skuEntityList) {
			ProductSkuVO productSkuVO = new ProductSkuVO();
			BeanUtils.copyProperties(record, productSkuVO);
			productSkuVO.setSkuId(record.getId());
			productSkuVO.setPrice(BigDecimalUtil.F2Y(record.getSkuPrice().longValue()));
			productSkuVO.setCreateTime(record.getCreatedAt());
			productSkuVO.setUpdateTime(record.getChangedAt());
			productSkuVO.setSkuState(record.getState());
			productSkuVO.setSkuStateName(UpOffEnum.getValueByCode(record.getState()));
			productSkuVO.setStoreId(record.getSourceId());
			productSkuVO.setStoreName(record.getSourceName());
			String masterImageUrl = productIdEntityMap.getOrDefault(productSkuVO.getProductId(), new ProductBasicsInfoEntity()).getMasterImageUrl();
			productSkuVO.setMasterImageUrl(masterImageUrl);
			ProductSkuRevenueEntity skuRevenueEntity = skuIdRevenueEntityMap.getOrDefault(productSkuVO.getSkuId(), new ProductSkuRevenueEntity());
			productSkuVO.setRevenueCode(skuRevenueEntity.getRevenueCode());
			productSkuVO.setRevenueRate(skuRevenueEntity.getRevenueRate());
			productSkuVOList.add(productSkuVO);
		}
		return PageVO.newBuilder().pageNo(querySkuRequest.getPageNo()).pageSize(querySkuRequest.getPageSize()).totalCount((int) productSkuEntityPage.getTotal()).result(productSkuVOList).build();
	}

	/**
	 * 根据条件分页查询 sku库存 信息
	 *
	 * @param querySkuStockRequest 查询参数
	 * @return sku 库存信息
	 * @author lsx
	 * @date 2021/12/17 14:04
	 */
	@Override
	public PageVO<ProductSkuStockVO> pageQuerySkuStockByParam(QuerySkuStockRequest querySkuStockRequest) {
		Page<ProductSkuEntity> queryPage = new Page<>(querySkuStockRequest.getPageNo(), querySkuStockRequest.getPageSize());
		LambdaQueryWrapper<ProductSkuEntity> queryWrapper = buildQuerySkuStockParam(querySkuStockRequest);
		Page<ProductSkuEntity> productSkuEntityPage = productSkuMapper.selectPage(queryPage, queryWrapper);
		List<ProductSkuEntity> records = productSkuEntityPage.getRecords();
		if (Objects.isNull(productSkuEntityPage) || CollectionUtils.isEmpty(records)) {
			return PageVO.newBuilder().pageNo(querySkuStockRequest.getPageNo()).pageSize(querySkuStockRequest.getPageSize()).totalCount(0).result(Lists.newArrayList()).build();
		}
		List<ProductSkuStockVO> productSkuStockVOS = Lists.newArrayListWithCapacity(records.size());
		Map<Long, Integer> stockMap = pharmacyGoodsRemoteProService.getSkuStockNumToMap(records.stream().map(ProductSkuEntity::getId).collect(Collectors.toSet()));
		Map<Integer, CommAttributeDTO> commAttributeMap = cmsRemoteProService.getCommAttributeByIdListToMap();
		records.forEach(r -> {
			CommAttributeDTO commAttributeDTO = commAttributeMap.get(r.getCommAttribute());
			ProductSkuStockVO productSkuStockVO = new ProductSkuStockVO();
			BeanUtils.copyProperties(r, productSkuStockVO);
			productSkuStockVO.setSkuId(r.getId());
			productSkuStockVO.setStoreName(r.getSourceName());
			productSkuStockVO.setStockNum(stockMap.get(r.getId()));
			productSkuStockVO.setCommAttributeName(commAttributeDTO == null ? "--" : commAttributeDTO.getCommAttributeName());
			productSkuStockVOS.add(productSkuStockVO);
		});
		return PageVO.newBuilder().pageNo(querySkuStockRequest.getPageNo()).pageSize(querySkuStockRequest.getPageSize()).totalCount((int) productSkuEntityPage.getTotal()).result(productSkuStockVOS).build();
	}

	/**
	 * 根据条件不分页查询 sku库存 信息
	 *
	 * @param querySkuStockRequest 查询参数
	 * @return sku 库存信息
	 * @author lsx
	 * @date 2021/12/17 14:04
	 */
	@Override
	public List<ProductSkuStockVO> searchSkuStock(QuerySkuStockRequest querySkuStockRequest) {
		LambdaQueryWrapper<ProductSkuEntity> queryWrapper = buildQuerySkuStockParam(querySkuStockRequest);
		List<ProductSkuEntity> productSkuEntities = productSkuMapper.selectList(queryWrapper);
		if (CollectionUtils.isEmpty(productSkuEntities)) {
			return Lists.newArrayList();
		}
		List<ProductSkuStockVO> productSkuStockVOS = Lists.newArrayListWithCapacity(productSkuEntities.size());
		Map<Long, Integer> stockMap = pharmacyGoodsRemoteProService.getSkuStockNumToMap(productSkuEntities.stream().map(ProductSkuEntity::getId).collect(Collectors.toSet()));
		Map<Integer, CommAttributeDTO> commAttributeMap = cmsRemoteProService.getCommAttributeByIdListToMap();
		productSkuEntities.forEach(r -> {
			CommAttributeDTO commAttributeDTO = commAttributeMap.get(r.getCommAttribute());
			ProductSkuStockVO productSkuStockVO = new ProductSkuStockVO();
			BeanUtils.copyProperties(r, productSkuStockVO);
			productSkuStockVO.setSkuId(r.getId());
			productSkuStockVO.setStoreName(r.getSourceName());
			productSkuStockVO.setStockNum(stockMap.get(r.getId()));
			productSkuStockVO.setCommAttributeName(commAttributeDTO == null ? "--" : commAttributeDTO.getCommAttributeName());
			productSkuStockVOS.add(productSkuStockVO);
		});
		return productSkuStockVOS;
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

	private LambdaQueryWrapper<ProductSkuEntity> buildQuerySkuStockParam(QuerySkuStockRequest querySkuStockRequest) {
		LambdaQueryWrapper<ProductSkuEntity> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.eq(ProductSkuEntity::getDelFlag, DelFlagEnum.UN_DELETED.getCode());
		if (Objects.nonNull(querySkuStockRequest.getSkuCode())) {
			queryWrapper.eq(ProductSkuEntity::getSkuCode, querySkuStockRequest.getSkuCode());
		}
		if (Objects.nonNull(querySkuStockRequest.getSkuName())) {
			queryWrapper.like(ProductSkuEntity::getSkuName, querySkuStockRequest.getSkuName());
		}
		if (Objects.nonNull(querySkuStockRequest.getStoreId())) {
			queryWrapper.eq(ProductSkuEntity::getSourceId, querySkuStockRequest.getStoreId());
		}
		if (Objects.nonNull(querySkuStockRequest.getCommAttribute())) {
			queryWrapper.eq(ProductSkuEntity::getCommAttribute, querySkuStockRequest.getCommAttribute());
		}
		return queryWrapper;
	}
}
