package com.drstrong.health.product.service;

import com.drstrong.health.product.remote.model.ProductSkuDetailsDTO;
import com.drstrong.health.product.remote.model.ProductSkuInfoDTO;
import com.drstrong.health.product.remote.model.SearchNameResultDTO;
import com.drstrong.health.product.remote.model.SkuIdAndCodeDTO;

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
	List<SearchNameResultDTO> searchSpuNameByName(String content, Integer count);

	/**
	 * 根据名称模糊查询 sku 信息
	 *
	 * @param content 商品 sku 名称
	 * @return sku 集合
	 * @author liuqiuyi
	 * @date 2021/12/23 21:12
	 */
	List<ProductSkuInfoDTO> searchSkuDetail(String content);

	/**
	 * 根据 skuId 或者 skuCode 查询商品详情
	 * <p> 目前主要提供给空中药房调用 </>
	 * <p> 两个入参任传其一 </>
	 *
	 * @param skuCode sku 编码
	 * @param skuId   skuId
	 * @return 商品的 sku 详情
	 * @author liuqiuyi
	 * @date 2021/12/24 11:30
	 */
	ProductSkuDetailsDTO getSkuDetail(String skuCode, Long skuId);

	/**
	 * 根据后台分类 id 查询商品信息
	 *
	 * @param categoryId 分类 id
	 * @return 商品详细信息
	 * @author liuqiuyi
	 * @date 2021/12/24 13:54
	 */
	List<ProductSkuInfoDTO> getSkuInfoByCategoryId(Long categoryId);

	/**
	 * skuCode 和 skuId 进行转换
	 * <p> 主要用于两者相互查询,两个入参不能同时为空 </>
	 *
	 * @param skuCode sku编码
	 * @param skuId   skuId
	 * @return sku 编码和 id 信息
	 * @author liuqiuyi
	 * @date 2021/12/24 20:07
	 */
	SkuIdAndCodeDTO getSkuIdOrCode(String skuCode, Long skuId);
}
