package com.drstrong.health.product.service.sku;

import com.baomidou.mybatisplus.extension.service.IService;
import com.drstrong.health.product.model.entity.sku.StoreSkuInfoEntity;
import com.drstrong.health.product.model.enums.UpOffEnum;

/**
 * @author liuqiuyi
 * @date 2023/6/8 14:47
 */
public interface StoreSkuInfoService extends IService<StoreSkuInfoEntity> {
	/**
	 * 根据编码查询 sku 信息
	 * <p>
	 * {@link UpOffEnum}
	 *
	 * @author liuqiuyi
	 * @date 2023/6/8 15:22
	 */
	StoreSkuInfoEntity queryBySkuCode(String skuCode, Integer skuStatus);

	/**
	 * 校验 sku 是否存在
	 * <p>
	 * {@link UpOffEnum}
	 *
	 * @author liuqiuyi
	 * @date 2023/6/9 09:32
	 */
	StoreSkuInfoEntity checkSkuExistByCode(String skuCode, Integer skuStatus);
}
