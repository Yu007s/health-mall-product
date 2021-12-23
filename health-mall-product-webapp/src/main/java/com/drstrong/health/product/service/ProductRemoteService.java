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

	/**
	 * 分页查询搜索的内容,只返回商品名称
	 *
	 * @param content 搜索条件
	 * @param count   返回的个数
	 * @return 搜索结果
	 * @author liuqiuyi
	 * @date 2021/12/17 15:49
	 */
	List<String> searchSkuNameByName(String content, Integer count);

	/**
	 * 根据名称模糊查询 sku 信息
	 *
	 * @param content 商品 sku 名称
	 * @return sku 集合
	 * @author liuqiuyi
	 * @date 2021/12/23 21:12
	 */
	List<ProductSkuInfoDTO> searchSkuDetail(String content);
}
