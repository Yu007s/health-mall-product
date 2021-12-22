package com.drstrong.health.product.service;

import com.drstrong.health.product.remote.model.ProductSkuInfoDTO;

import java.util.List;
import java.util.Set;

/**
 * 远程接口 service 实现
 *
 * @author liuqiuyi
 * @date 2021/12/22 15:00
 */
public interface ProductRemoteService {
	/**
	 * 根据 skuId 查询商品 sku 信息集合
	 *
	 * @param skuIds 商品 skuId 集合
	 * @return 商品 sku 信息
	 * @author liuqiuyi
	 * @date 2021/12/22 15:03
	 */
	List<ProductSkuInfoDTO> getSkuInfoBySkuIds(Set<Long> skuIds);
}
