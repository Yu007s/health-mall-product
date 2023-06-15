package com.drstrong.health.product.facade.sku;

import com.drstrong.health.product.model.request.product.v3.ScheduledSkuUpDownRequest;

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

	/**
	 * 保存或者更新 sku 的上下架状态
	 *
	 * @author liuqiuyi
	 * @date 2023/6/15 10:15
	 */
	void saveOrUpdateSkuConfig(ScheduledSkuUpDownRequest scheduledSkuUpDownRequest);
}
