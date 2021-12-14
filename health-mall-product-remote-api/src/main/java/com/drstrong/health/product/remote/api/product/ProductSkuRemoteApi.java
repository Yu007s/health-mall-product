package com.drstrong.health.product.remote.api.product;

import com.drstrong.health.product.remote.model.ProductSkuInfoDTO;

import java.util.List;

/**
 * sku 远程接口
 *
 * @author liuqiuyi
 * @date 2021/12/14 17:22
 */
public interface ProductSkuRemoteApi {
	/**
	 * 根据 skuId 集合,获取 sku 信息
	 *
	 * @param skuIds skuId 集合
	 * @return sku 信息
	 * @author liuqiuyi
	 * @date 2021/12/14 17:21
	 */
	List<ProductSkuInfoDTO> getSkuInfoBySkuIds(List<Long> skuIds);
}
