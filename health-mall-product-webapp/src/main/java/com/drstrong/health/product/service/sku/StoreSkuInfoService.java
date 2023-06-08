package com.drstrong.health.product.service.sku;

import com.baomidou.mybatisplus.extension.service.IService;
import com.drstrong.health.product.model.entity.sku.StoreSkuInfoEntity;

/**
 * @author liuqiuyi
 * @date 2023/6/8 14:47
 */
public interface StoreSkuInfoService extends IService<StoreSkuInfoEntity> {
	/**
	 * 根据编码查询 sku 信息
	 *
	 * @author liuqiuyi
	 * @date 2023/6/8 15:22
	 */
	StoreSkuInfoEntity queryBySkuCode(String skuCode);
}
