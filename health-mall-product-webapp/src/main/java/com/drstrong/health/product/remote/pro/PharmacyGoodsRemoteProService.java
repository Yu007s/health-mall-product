package com.drstrong.health.product.remote.pro;

import cn.strong.common.base.Result;
import com.alibaba.fastjson.JSON;
import com.drstrong.health.ware.model.vo.SkuHasStockListVO;
import com.drstrong.health.ware.model.vo.SkuHasStockVO;
import com.drstrong.health.ware.model.vo.SkuStockNumListVO;
import com.drstrong.health.ware.model.vo.SkuStockNumVO;
import com.drstrong.health.ware.remote.api.PharmacyGoodsRemoteApi;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;

import static java.util.stream.Collectors.toMap;

/**
 * 库存远程调用
 *
 * @author liuqiuyi
 * @date 2021/12/22 15:18
 */
@Service
@Slf4j
public class PharmacyGoodsRemoteProService {
	@Resource
	PharmacyGoodsRemoteApi pharmacyGoodsRemoteApi;


	/**
	 * 获取 sku 是否有库存
	 *
	 * @param skuIdList skuId
	 * @return map.key = skuId, map.value=是否有库存
	 * @author liuqiuyi
	 * @date 2021/12/22 15:36
	 */
	public Map<Long, Boolean> getSkuHasStockToMap(Set<Long> skuIdList) {
		if (CollectionUtils.isEmpty(skuIdList)) {
			log.info("invoke PharmacyGoodsRemotePro.getSkuHasStockToMap param is null");
			return Maps.newHashMap();
		}
		try {
			log.info("invoke pharmacyGoodsRemoteApi.getSkuHasStock param:{}", skuIdList);
			Result<SkuHasStockListVO> stockListVoResult = pharmacyGoodsRemoteApi.getSkuHasStock(Lists.newArrayList(skuIdList));
			log.info("invoke pharmacyGoodsRemoteApi.getSkuHasStock result:{}", JSON.toJSONString(stockListVoResult));
			if (Objects.isNull(stockListVoResult) || Objects.equals(Boolean.FALSE, stockListVoResult.getSuccess())) {
				log.info("invoke pharmacyGoodsRemoteApi.getSkuHasStock return failed:{}", JSON.toJSONString(stockListVoResult));
				return Maps.newHashMap();
			}
			List<SkuHasStockVO> skuHasStockVOList = Optional.ofNullable(stockListVoResult.getData()).map(SkuHasStockListVO::getSkuHasStockList).orElse(Lists.newArrayList());
			return skuHasStockVOList.stream().collect(toMap(SkuHasStockVO::getSkuId, SkuHasStockVO::getHasStock, (o, n) -> o));
		} catch (Exception e) {
			log.info("invoke pharmacyGoodsRemoteApi.getSkuHasStock an error occurred,param:{}", skuIdList, e);
			throw e;
		}
	}

	/**
	 * 获取 sku 的库存数
	 *
	 * @param skuIdList skuId
	 * @return map.key = skuId, map.value=库存数
	 * @author liuqiuyi
	 * @date 2021/12/22 15:36
	 */
	public Map<Long, Integer> getSkuStockNumToMap(Set<Long> skuIdList) {
		if (CollectionUtils.isEmpty(skuIdList)) {
			log.info("invoke PharmacyGoodsRemotePro.getSkuStockNumToMap param is null");
			return Maps.newHashMap();
		}
		try {
			log.info("invoke pharmacyGoodsRemoteApi.getSkuStockNum param:{}", skuIdList);
			Result<SkuStockNumListVO> numListVoResult = pharmacyGoodsRemoteApi.getSkuStockNum(Lists.newArrayList(skuIdList));
			log.info("invoke pharmacyGoodsRemoteApi.getSkuStockNum result:{}", JSON.toJSONString(numListVoResult));
			if (Objects.isNull(numListVoResult) || Objects.equals(Boolean.FALSE, numListVoResult.getSuccess())) {
				log.info("invoke pharmacyGoodsRemoteApi.getSkuStockNum return failed:{}", JSON.toJSONString(numListVoResult));
				return Maps.newHashMap();
			}
			List<SkuStockNumVO> skuHasStockVOList = Optional.ofNullable(numListVoResult.getData()).map(SkuStockNumListVO::getSkuStockNumList).orElse(Lists.newArrayList());
			return skuHasStockVOList.stream().collect(toMap(SkuStockNumVO::getSkuId, SkuStockNumVO::getStockNum, (o, n) -> o));
		} catch (Exception e) {
			log.info("invoke pharmacyGoodsRemoteApi.getSkuStockNum an error occurred,param:{}", skuIdList, e);
			throw e;
		}
	}
}
