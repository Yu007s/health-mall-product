package com.drstrong.health.product.service.incentive;

import com.baomidou.mybatisplus.extension.service.IService;
import com.drstrong.health.product.model.entity.incentive.IncentivePolicyConfigEntity;

/**
 * @author liuqiuyi
 * @date 2023/6/7 14:33
 */
public interface IncentivePolicyConfigService extends IService<IncentivePolicyConfigEntity> {

	/**
	 * 根据店铺 id,目标类型和名称,查询政策配置
	 *
	 * @author liuqiuyi
	 * @date 2023/6/7 14:36
	 */
	IncentivePolicyConfigEntity queryByStoreIdAndGoalType(Long storeId, Integer goalType, String earningName);


}
