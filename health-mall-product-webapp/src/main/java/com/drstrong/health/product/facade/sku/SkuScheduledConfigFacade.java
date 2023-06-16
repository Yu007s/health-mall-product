package com.drstrong.health.product.facade.sku;

import com.drstrong.health.product.model.request.chinese.UpdateSkuStateRequest;
import com.drstrong.health.product.model.request.product.v3.ScheduledSkuUpDownRequest;

/**
 * @author liuqiuyi
 * @date 2023/6/14 16:32
 */
public interface SkuScheduledConfigFacade {
	/**
	 * 批量修改 skuCode 的定时配置
	 *
	 * @author liuqiuyi
	 * @date 2023/6/14 16:53
	 */
	void batchUpdateScheduledStatusByCodes(UpdateSkuStateRequest updateSkuStateRequest);

	/**
	 * 保存或者更新 sku 定时上下架配置
	 *
	 * @author liuqiuyi
	 * @date 2023/6/15 10:15
	 */
	void saveOrUpdateSkuConfig(ScheduledSkuUpDownRequest scheduledSkuUpDownRequest);


	/**
	 * 定时任务 - 执行预约上下架
	 *
	 * @author liuqiuyi
	 * @date 2023/6/15 17:44
	 */
	void doScheduledSkuUpDown();
}
