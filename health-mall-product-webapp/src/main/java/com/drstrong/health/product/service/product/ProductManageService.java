package com.drstrong.health.product.service.product;

import com.drstrong.health.product.model.enums.ProductTypeEnum;
import com.drstrong.health.product.model.request.product.QuerySpuRequest;
import com.drstrong.health.product.model.request.product.SaveProductRequest;
import com.drstrong.health.product.model.response.PageVO;
import com.drstrong.health.product.model.response.product.ProductManageVO;
import com.drstrong.health.product.model.response.product.ProductSpuVO;

import java.util.Set;

/**
 * 商品管理端 service
 *
 * @author liuqiuyi
 * @date 2022/1/7 16:19
 */
public interface ProductManageService {

	/**
	 * 保存或者更新商品信息
	 *
	 * @param saveProductRequest 保存商品
	 * @author liuqiuyi
	 * @date 2021/12/13 15:48
	 */
	Long saveOrUpdateProduct(SaveProductRequest saveProductRequest);

	/**
	 * 根据商品 id,查询管理端商品信息
	 *
	 * @param productId 商品 id
	 * @return 商品管理端信息
	 * @author liuqiuyi
	 * @date 2021/12/13 20:26
	 */
	ProductManageVO queryManageProductInfo(Long productId);

	/**
	 * 管理端页面分页查询 spu 信息
	 *
	 * @param querySpuRequest 查询参数
	 * @return 分页返回值
	 * @author liuqiuyi
	 * @date 2021/12/14 10:25
	 */
	PageVO<ProductSpuVO> pageQuerySpuByParam(QuerySpuRequest querySpuRequest);

	/**
	 * 通过 spuCode 查询 已上架条数
	 *
	 * @param spuCode spu_code
	 * @return 上架条目条数
	 */
	Integer getCountBySPUCode(String spuCode);

	/**
	 * 批量更改spu上下架状态
	 *
	 * @param spuIdList
	 * @param state
	 * @param userId
	 */
	void updateState(Set<Long> spuIdList, Integer state, String userId);

	/**
	 * 生成商品或者药品编码,参照之前的方法
	 *
	 * @param productTypeEnum 商品类型
	 * @return 编码
	 * @author liuqiuyi
	 * @date 2021/12/16 14:13
	 */
	String getNextProductNumber(ProductTypeEnum productTypeEnum);
}
