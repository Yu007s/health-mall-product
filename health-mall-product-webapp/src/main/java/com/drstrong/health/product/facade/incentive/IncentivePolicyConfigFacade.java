package com.drstrong.health.product.facade.incentive;

import com.drstrong.health.product.model.request.incentive.SaveEarningNameRequest;

/**
 * @author liuqiuyi
 * @date 2023/6/7 14:41
 */
public interface IncentivePolicyConfigFacade {
	/**
	 * 保存店铺下的收益名称
	 *
	 * @author liuqiuyi
	 * @date 2023/6/7 11:42
	 */
	void saveEarningName(SaveEarningNameRequest saveEarningNameRequest);
}
