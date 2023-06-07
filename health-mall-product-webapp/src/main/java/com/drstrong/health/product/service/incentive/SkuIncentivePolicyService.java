package com.drstrong.health.product.service.incentive;

import com.baomidou.mybatisplus.extension.service.IService;
import com.drstrong.health.product.model.entity.incentive.SkuIncentivePolicyEntity;

/**
 * @author liuqiuyi
 * @date 2023/6/7 16:33
 */
public interface SkuIncentivePolicyService extends IService<SkuIncentivePolicyEntity> {
	/**
	 * 根据 skuCode 查询激励政策
	 *
	 * @author liuqiuyi
	 * @date 2023/6/7 16:58
	 */
	SkuIncentivePolicyEntity queryBySkuCode(String skuCode);
}
