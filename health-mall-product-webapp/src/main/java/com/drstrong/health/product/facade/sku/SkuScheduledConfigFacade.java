package com.drstrong.health.product.facade.sku;

import java.util.Set;

/**
 * @author liuqiuyi
 * @date 2023/6/14 16:32
 */
public interface SkuScheduledConfigFacade {
	/**
	 * 批量取消 skuCode 的定时配置
	 *
	 * @author liuqiuyi
	 * @date 2023/6/14 16:53
	 */
	void batchUpdateScheduledStatusToCancelByCodes(Set<String> skuCodeList, Long operatorId, String operatorName);
}
