package com.drstrong.health.product.service.impl;

import com.drstrong.health.product.model.dto.CommAttributeDTO;
import com.drstrong.health.product.model.entity.product.ProductBasicsInfoEntity;
import com.drstrong.health.product.model.entity.product.ProductSkuEntity;
import com.drstrong.health.product.model.enums.UpOffEnum;
import com.drstrong.health.product.model.request.product.QuerySkuRequest;
import com.drstrong.health.product.model.request.product.QuerySpuRequest;
import com.drstrong.health.product.remote.cms.CmsRemoteProService;
import com.drstrong.health.product.remote.model.ProductSkuInfoDTO;
import com.drstrong.health.product.remote.pro.PharmacyGoodsRemoteProService;
import com.drstrong.health.product.service.ProductBasicsInfoService;
import com.drstrong.health.product.service.ProductRemoteService;
import com.drstrong.health.product.service.ProductSkuService;
import com.drstrong.health.product.util.BigDecimalUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * 商品远程接口实现类
 *
 * @author liuqiuyi
 * @date 2021/12/22 15:04
 */
@Slf4j
@Service
public class ProductRemoteServiceImpl implements ProductRemoteService {
	@Resource
	ProductSkuService productSkuService;

	@Resource
	ProductBasicsInfoService productBasicsInfoService;

	@Resource
	PharmacyGoodsRemoteProService pharmacyGoodsRemoteProService;

	@Resource
	CmsRemoteProService cmsRemoteProService;

	/**
	 * 根据 skuId 查询商品 sku 信息集合
	 *
	 * @param skuIds 商品 skuId 集合
	 * @return 商品 sku 信息
	 * @author liuqiuyi
	 * @date 2021/12/22 15:03
	 */
	@Override
	public List<ProductSkuInfoDTO> getSkuInfoBySkuIds(Set<Long> skuIds) {
		log.info("ProductRemoteServiceImpl.getSkuInfoBySkuIds param:{}", skuIds);
		if (CollectionUtils.isEmpty(skuIds)) {
			return Lists.newArrayList();
		}
		// 1.查询 sku 信息是否存在
		List<ProductSkuEntity> productSkuEntityList = productSkuService.queryBySkuIdOrCode(skuIds, null, UpOffEnum.UP);
		if (CollectionUtils.isEmpty(productSkuEntityList)) {
			return Lists.newArrayList();
		}
		return getProductSkuInfoDTOList(productSkuEntityList);
	}

	private List<ProductSkuInfoDTO> getProductSkuInfoDTOList(List<ProductSkuEntity> productSkuEntityList) {
		// 2.查询商品信息
		Set<Long> productIdList = Sets.newHashSetWithExpectedSize(productSkuEntityList.size());
		Set<Long> skuIds = Sets.newHashSetWithExpectedSize(productSkuEntityList.size());
		productSkuEntityList.forEach(productSkuEntity -> {
			productIdList.add(productSkuEntity.getProductId());
			skuIds.add(productSkuEntity.getId());
		});
		QuerySpuRequest querySpuRequest = new QuerySpuRequest();
		querySpuRequest.setProductIdList(productIdList);
		Map<Long, ProductBasicsInfoEntity> productIdInfoMap = productBasicsInfoService.queryProductByParamToMap(querySpuRequest);
		// 3.查询库存信息
		Map<Long, Integer> skuIdStockNumMap = pharmacyGoodsRemoteProService.getSkuStockNumToMap(skuIds);
		// 4.查询商品属性,从 cms 接口中获取
		Map<Integer, CommAttributeDTO> commAttributeByIdListToMap = cmsRemoteProService.getCommAttributeByIdListToMap();
		// 5.组装返回值
		return buildSkuInfoResult(productSkuEntityList, productIdInfoMap, skuIdStockNumMap, commAttributeByIdListToMap);
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
	public List<String> searchSkuNameByName(String content, Integer count) {
		log.info("invoke ProductRemoteServiceImpl.searchSkuNameByName param:{},{}", content, count);
		if (StringUtils.isBlank(content)) {
			return Lists.newArrayList();
		}
		if (Objects.isNull(count)) {
			count = 10;
		}
		return productSkuService.searchSkuNameByName(content, count);
	}

	/**
	 * 根据名称模糊查询 sku 信息
	 *
	 * @param content 商品 sku 名称
	 * @return sku 集合
	 * @author liuqiuyi
	 * @date 2021/12/23 21:12
	 */
	@Override
	public List<ProductSkuInfoDTO> searchSkuDetail(String content) {
		log.info("invoke ProductRemoteServiceImpl.searchSkuDetail param:{}", content);
		if (StringUtils.isBlank(content)) {
			return Lists.newArrayList();
		}
		QuerySkuRequest querySkuRequest = new QuerySkuRequest();
		querySkuRequest.setProductName(content);
		querySkuRequest.setUpOffEnum(UpOffEnum.UP);
		List<ProductSkuEntity> productSkuEntityList = productSkuService.querySkuByParam(querySkuRequest);
		if (CollectionUtils.isEmpty(productSkuEntityList)) {
			log.info("invoke ProductRemoteServiceImpl.searchSkuDetail search result is null. param:{}", content);
			return Lists.newArrayList();
		}
		return getProductSkuInfoDTOList(productSkuEntityList);
	}

	private List<ProductSkuInfoDTO> buildSkuInfoResult(List<ProductSkuEntity> productSkuEntityList, Map<Long, ProductBasicsInfoEntity> productIdInfoMap,
													   Map<Long, Integer> skuIdStockNumMap, Map<Integer, CommAttributeDTO> commAttributeByIdListToMap) {
		List<ProductSkuInfoDTO> resultSkuInfoList = Lists.newArrayListWithCapacity(productSkuEntityList.size());
		for (ProductSkuEntity productSkuEntity : productSkuEntityList) {
			ProductBasicsInfoEntity basicsInfoEntity = productIdInfoMap.getOrDefault(productSkuEntity.getProductId(), new ProductBasicsInfoEntity());
			Integer stockNum = skuIdStockNumMap.getOrDefault(productSkuEntity.getId(), 0);
			// 商品属性
			CommAttributeDTO commAttributeDTO = commAttributeByIdListToMap.getOrDefault(productSkuEntity.getCommAttribute(), new CommAttributeDTO());

			ProductSkuInfoDTO infoDTO = new ProductSkuInfoDTO();
			infoDTO.setProductId(productSkuEntity.getProductId());
			infoDTO.setSpuCode(basicsInfoEntity.getSpuCode());
			infoDTO.setMasterImageUrl(basicsInfoEntity.getMasterImageUrl());
			infoDTO.setProductName(basicsInfoEntity.getTitle());
			infoDTO.setSkuId(productSkuEntity.getId());
			infoDTO.setSkuCode(productSkuEntity.getSkuCode());
			infoDTO.setPackValue(productSkuEntity.getPackValue());
			infoDTO.setPrice(BigDecimalUtil.F2Y(productSkuEntity.getSkuPrice().longValue()));
			infoDTO.setStoreId(productSkuEntity.getSourceId());
			infoDTO.setStoreName(productSkuEntity.getSourceName());
			infoDTO.setUpOffStatus(productSkuEntity.getState());
			infoDTO.setStockNum(stockNum);
			infoDTO.setCommAttribute(commAttributeDTO.getCommAttribute());
			infoDTO.setCommAttributeName(commAttributeDTO.getCommAttributeName());
			infoDTO.setCommAttributeIcon(commAttributeDTO.getCommAttributeIcon());
			resultSkuInfoList.add(infoDTO);
		}
		return resultSkuInfoList;
	}
}
