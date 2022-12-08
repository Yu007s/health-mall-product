package com.drstrong.health.product.facade;

import com.drstrong.health.product.model.request.chinese.ChineseQueryDosageRequest;
import com.drstrong.health.product.model.response.chinese.ChineseSkuInfoVO;

import java.util.List;

/**
 * 中药接口的门面层
 *
 * @author liuqiuyi
 * @date 2022/12/8 16:51
 */
public interface ChineseAuthFacade {

	List<ChineseSkuInfoVO> queryAllDosage(ChineseQueryDosageRequest chineseQueryDosageRequest);

	/**
	 * 获取店铺id
	 *
	 * @author liuqiuyi
	 * @date 2022/12/8 17:36
	 */
	Long queryStoreId(Long storeId, Long agencyId, Long ucDoctorId);
}
