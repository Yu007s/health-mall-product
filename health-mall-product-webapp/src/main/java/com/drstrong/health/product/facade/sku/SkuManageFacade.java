package com.drstrong.health.product.facade.sku;

import com.drstrong.health.product.model.dto.product.StoreSkuDetailDTO;
import com.drstrong.health.product.model.request.product.v3.SaveOrUpdateStoreSkuRequest;

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
}
