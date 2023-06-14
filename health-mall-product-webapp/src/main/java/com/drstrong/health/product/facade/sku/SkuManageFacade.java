package com.drstrong.health.product.facade.sku;

import com.drstrong.health.product.model.dto.product.StoreSkuDetailDTO;
import com.drstrong.health.product.model.request.product.v3.ProductManageQueryRequest;
import com.drstrong.health.product.model.request.product.v3.SaveOrUpdateStoreSkuRequest;
import com.drstrong.health.product.model.response.PageVO;
import com.drstrong.health.product.model.response.product.v3.AgreementSkuInfoVO;

import java.util.List;

/**
 * @author liuqiuyi
 * @date 2023/6/10 09:25
 */
public interface SkuManageFacade {
	/**
	 * 保存或者更新 sku 信息(目前不包括中药)
	 *
	 * @author liuqiuyi
	 * @date 2023/6/10 09:32
	 */
	void saveOrUpdateStoreProduct(SaveOrUpdateStoreSkuRequest saveOrUpdateStoreProductRequest);

	/**
	 * 根据skuCode 查询 sku 信息
	 *
	 * @author liuqiuyi
	 * @date 2023/6/10 15:20
	 */
	StoreSkuDetailDTO queryDetailByCode(String skuCode);

	/**
	 * 协定方|西药 管理页面,列表展示
	 *
	 * @author liuqiuyi
	 * @date 2023/6/13 10:23
	 */
	PageVO<AgreementSkuInfoVO> querySkuManageInfo(ProductManageQueryRequest productManageQueryRequest);

	/**
	 * 协定方|西药 管理页面,所有数据查询,用于导出
	 *
	 * @author liuqiuyi
	 * @date 2023/6/14 11:23
	 */
	List<AgreementSkuInfoVO> listSkuManageInfo(ProductManageQueryRequest productManageQueryRequest);
}
