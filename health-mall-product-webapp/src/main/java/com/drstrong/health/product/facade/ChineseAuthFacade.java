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

	/**
	 * 查询店铺所有存在倍数限制的中药
	 *
	 * @param chineseQueryDosageRequest 参数
	 * @author liuqiuyi
	 * @date 2022/12/9 09:51
	 */
	List<ChineseSkuInfoVO> queryAllDosage(ChineseQueryDosageRequest chineseQueryDosageRequest);

	/**
	 * 根据参数获取店铺id
	 *
	 * @param storeId    店铺 id
	 * @param agencyId   互联网医院 id
	 * @param ucDoctorId 用户中心的医生 id
	 * @author liuqiuyi
	 * @date 2022/12/8 17:36
	 */
	Long queryStoreId(Long storeId, Long agencyId, Long ucDoctorId);
}
