package com.drstrong.health.product.facade.incentive;

import com.drstrong.health.product.model.request.incentive.SaveEarningNameRequest;
import com.drstrong.health.product.model.request.incentive.SaveOrUpdateSkuPolicyRequest;
import com.drstrong.health.product.model.response.incentive.SkuIncentivePolicyDetailVO;
import com.drstrong.health.product.model.response.incentive.excel.SkuIncentivePolicyDetailExcelVO;

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

	/**
	 * 根据 skuCode 查询激励政策信息
	 *
	 * @author liuqiuyi
	 * @date 2023/6/8 14:30
	 */
	SkuIncentivePolicyDetailVO queryPolicyDetailBySkuCode(String skuCode);

	/**
	 * 查询所有的 sku 政策信息
	 *
	 * @author liuqiuyi
	 * @date 2023/6/13 16:54
	 */
	SkuIncentivePolicyDetailExcelVO querySkuPolicyDetailToExcelVO(Long storeId, Integer productType);
}
