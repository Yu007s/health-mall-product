package com.drstrong.health.product.service.product;

import com.drstrong.health.product.model.request.product.QuerySkuRequest;
import com.drstrong.health.product.model.request.product.QuerySkuStockRequest;
import com.drstrong.health.product.model.response.PageVO;
import com.drstrong.health.product.model.response.product.ProductSkuStockVO;
import com.drstrong.health.product.model.response.product.ProductSkuVO;

import java.util.List;

/**
 * 商品 sku 管理端 service
 *
 * @author liuqiuyi
 * @date 2022/1/7 16:32
 */
public interface ProductSkuManageService {
	/**
	 * 根据条件分页查询 sku 信息
	 *
	 * @param querySkuRequest 查询参数
	 * @return sku 信息
	 * @author liuqiuyi
	 * @date 2021/12/14 14:04
	 */
	PageVO<ProductSkuVO> pageQuerySkuByParam(QuerySkuRequest querySkuRequest);

	PageVO<ProductSkuStockVO> pageQuerySkuStockByParam(QuerySkuStockRequest querySkuStockRequest);

	List<ProductSkuStockVO> searchSkuStock(QuerySkuStockRequest querySkuStockRequest);

	/**
	 * 获取下一个 sku 编码,参照之前的逻辑
	 *
	 * @param productId 商品 id
	 * @param spuCode   spu 编码
	 * @return 生成的 sku 编码
	 * @author liuqiuyi
	 * @date 2021/12/16 14:48
	 */
	String createNextSkuCode(String spuCode, Long productId);
}
