package com.drstrong.health.product.facade.incentive;

import com.drstrong.health.product.model.request.incentive.SaveEarningNameRequest;
import com.drstrong.health.product.model.request.incentive.SaveOrUpdateSkuPolicyRequest;

/**
 * @author liuqiuyi
 * @date 2023/6/7 14:41
 */
public interface SkuIncentivePolicyFacade {
	/**
	 * 保存店铺下的收益名称
	 *
	 * @author liuqiuyi
	 * @date 2023/6/7 11:42
	 */
	void saveEarningName(SaveEarningNameRequest saveEarningNameRequest);

	/**
	 * 保存或者更新 sku 的激励政策
	 *
	 * @author liuqiuyi
	 * @date 2023/6/7 16:52
	 */
	void saveOrUpdateSkuPolicy(SaveOrUpdateSkuPolicyRequest saveOrUpdateSkuPolicyRequest);
}
