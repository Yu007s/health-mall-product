package com.drstrong.health.product.service.store;

import com.drstrong.health.product.model.entity.store.StoreEntity;
import com.drstrong.health.product.model.request.store.QueryStoreRequest;
import com.drstrong.health.product.model.request.store.StoreInfoDetailSaveRequest;
import com.drstrong.health.product.model.response.store.StoreInfoEditResponse;
import com.drstrong.health.product.model.response.store.StoreInfoResponse;
import com.drstrong.health.product.model.response.store.StoreQueryResponse;

import java.util.List;
import java.util.Set;

/**
 * @Author xieYueFeng
 * @Date 2022/07/30/9:15
 */
public interface StoreService {

    /**
     * 保存店铺信息
     *
     * @param store  店铺信息
     */
    Long save(StoreInfoDetailSaveRequest store);

	/**
	 * 供应链三期需求,新店铺保存时,需要添加默认的邮费信息
	 * <p> 新店铺满 300 包邮,店铺的供应商满 88 包邮,供应商的区域满 22 包邮 </>
	 *
	 * @author liuqiuyi
	 * @date 2023/6/13 14:51
	 */
	void saveStoreDefaultPostage(List<Long> supplierIds, Long storeId);

	/**
	 * 保存店铺信息
	 *
	 * @param store  店铺信息
	 */
	void update(StoreInfoDetailSaveRequest store);

	/**
	 * 查询店铺信息
	 * @param storeId 店铺id
	 * @param storeName 店铺名字
	 * @param agencyId 互联网医院id
	 * @param storeType 店铺类型名字
	 * @return 店铺信息返回值
	 */
    List<StoreInfoResponse> query(Long storeId,String storeName,Long agencyId, String storeType);

    /**
     * 通过店铺id查找店铺详细信息
     *
     * @param storeId 店铺id
     * @return 店铺详情（带有发票信息详情页面）
     */
    StoreInfoEditResponse queryById(Long storeId);

    /**
     * 根据店铺id集合查询店铺信息
     *
     * @param storeIds 店铺id集合
     * @return 店铺信息集合
     * @author liuqiuyi
     * @date 2022/8/1 15:26
     */
    List<StoreEntity> listByIds(Set<Long> storeIds);

	/**
	 * 根据店铺id集合查询店铺信息
	 *
	 * @param storeId 店铺id
	 * @return 店铺信息集合
	 * @author liuqiuyi
	 * @date 2022/8/1 15:26
	 */
    StoreEntity getById(Long storeId);

	/**
	 * 根据互联网医院 id，获取店铺信息
	 *
	 * @param agencyId 互联网医院 id
	 * @param storeId 店铺 id
	 * @return 店铺信息
	 * @author liuqiuyi
	 * @date 2022/8/3 19:47
	 */
	StoreEntity getStoreByAgencyIdOrStoreId(Long agencyId, Long storeId);


	/**
	 * 店铺查询页面返回相应信息
	 * @return  互联网医院 名字 id 店铺类型名字
	 */
	 StoreQueryResponse queryStoreConInfo();

	/**
	 * 根据互联网医院 id 获取店铺 id
	 *
	 * @param agencyIds 互联网医院 id
	 * @return 店铺信息
	 * @author liuqiuyi
	 * @date 2022/8/8 19:57
	 */
	List<StoreEntity> getStoreByAgencyIds(Set<Long> agencyIds);

	/**
	 * 根据条件查询店铺信息
	 *
	 * @author liuqiuyi
	 * @date 2023/7/14 10:09
	 */
	List<StoreEntity> queryStoreByParam(QueryStoreRequest queryStoreRequest);
}
