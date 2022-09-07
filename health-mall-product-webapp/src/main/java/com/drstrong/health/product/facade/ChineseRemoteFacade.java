package com.drstrong.health.product.facade;

import com.drstrong.health.product.model.request.chinese.QueryChineseSkuRequest;
import com.drstrong.health.product.model.request.store.AgencyStoreVO;
import com.drstrong.health.product.model.response.chinese.ChineseMedicineConflictVO;
import com.drstrong.health.product.model.response.chinese.ChineseMedicineInfoResponse;
import com.drstrong.health.product.model.response.chinese.ChineseSkuInfoVO;
import com.drstrong.health.product.model.response.product.ProductInfoVO;

import java.util.List;
import java.util.Set;

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

	/**
	 * 根据条件查询 sku 信息
	 * <p> 仅支持查询同一店铺的 sku 信息 </>
	 *
	 * @param chineseSkuRequest 查询条件
	 * @return 查询结果
	 * @author liuqiuyi
	 * @date 2022/8/4 14:19
	 */
	ProductInfoVO queryStoreSku(QueryChineseSkuRequest chineseSkuRequest);

	/**
	 * 获取所有的中药相反药材,出参和之前的业务结构保持一致
	 *
	 * @author liuqiuyi
	 * @date 2022/8/8 11:44
	 */
	List<ChineseMedicineConflictVO> listAllConflict();

	/**
	 * 根据互联网医院 id 获取店铺信息
	 *
	 * @param agencyIds 互联网医院 id
	 * @return 互联网医院 id 和店铺 id 信息
	 * @author liuqiuyi
	 * @date 2022/8/8 19:53
	 */
	List<AgencyStoreVO> listStoreByAgencyIds(Set<Long> agencyIds);

	/**
	 * 根据店铺 id 获取 互联网医院 id
	 *
	 * @param storeIds 店铺 id
	 * @return 互联网医院 id 和店铺 id 信息
	 * @author liuqiuyi
	 * @date 2022/8/10 16:05
	 */
	List<AgencyStoreVO> listAgencyByStoreIds(Set<Long> storeIds);

	/**
	 * 校验是否有上架的 sku
	 * <p> 用于供应商那边删除中药材时进行校验,如果删除的中药材关联了上架的 sku,则不允许删除 </>
	 *
	 * @author liuqiuyi
	 * @date 2022/8/11 17:35
	 */
	List<ChineseMedicineInfoResponse> checkHasUpChineseByMedicineCodes(Set<String> medicineCodes);
}
