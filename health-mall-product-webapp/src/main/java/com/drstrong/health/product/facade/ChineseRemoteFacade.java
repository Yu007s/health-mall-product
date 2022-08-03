package com.drstrong.health.product.facade;

import com.drstrong.health.product.model.response.chinese.ChineseSkuInfoVO;

import java.util.List;

/**
 * 重要远程接口的门面层
 *
 * @author liuqiuyi
 * @date 2022/8/3 19:32
 */
public interface ChineseRemoteFacade {

	/**
	 * 根据关键字和互联网医院 id 模糊搜索药材
	 *
	 * @param agencyId 互联网医院 id
	 * @param keyword  搜索关键字
	 * @param storeId  店铺id
	 * @return 中药材基本信息
	 * @author liuqiuyi
	 * @date 2022/8/3 19:34
	 */
	List<ChineseSkuInfoVO> keywordSearch(String keyword, Long agencyId, Long storeId);
}
