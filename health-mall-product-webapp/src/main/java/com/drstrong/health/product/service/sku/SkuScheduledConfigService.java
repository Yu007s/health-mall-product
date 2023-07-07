package com.drstrong.health.product.service.sku;

import com.baomidou.mybatisplus.extension.service.IService;
import com.drstrong.health.product.model.entity.sku.SkuScheduledConfigEntity;

import java.util.List;
import java.util.Set;

/**
 * @author liuqiuyi
 * @date 2023/6/14 16:21
 */
public interface SkuScheduledConfigService extends IService<SkuScheduledConfigEntity> {
	/**
	 * 根据 skuCode 查询
	 *
	 * @author liuqiuyi
	 * @date 2023/6/14 16:54
	 */
	SkuScheduledConfigEntity getBySkuCode(String skuCode, Integer scheduledStatus);

	/**
	 * 根据activityPackageCode查询
	 * @param activityPackageCode
	 * @param scheduledStatus
	 * @return
	 */
	List<SkuScheduledConfigEntity> getByActivityPackageCode(String activityPackageCode, Integer scheduledStatus);

	/**
	 * 根据 skuCode 查询
	 *
	 * @author liuqiuyi
	 * @date 2023/6/14 16:54
	 */
	List<SkuScheduledConfigEntity> listBySkuCode(Set<String> skuCodeList, Set<Integer> scheduledStatusList);

	/**
	 * 根据 skuCode 批量更新定时任务执行状态
	 *
	 * @author liuqiuyi
	 * @date 2023/6/14 16:25
	 */
	void batchUpdateScheduledStatusByCodes(Set<String> skuCodeList, Integer scheduledStatus, Long operatorId);
}
