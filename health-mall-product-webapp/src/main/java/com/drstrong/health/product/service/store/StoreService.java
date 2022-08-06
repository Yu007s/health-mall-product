package com.drstrong.health.product.service.store;

import com.drstrong.health.product.model.entity.store.StoreEntity;
import com.drstrong.health.product.model.request.store.StoreInfoDetailSaveRequest;
import com.drstrong.health.product.model.request.store.StoreSearchRequest;
import com.drstrong.health.product.model.response.store.StoreAddResponse;
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
     * @param userId 当前操作用户id
     */
    void save(StoreInfoDetailSaveRequest store, Long userId) throws Exception;
	/**
	 * 保存店铺信息
	 *
	 * @param store  店铺信息
	 * @param userId 当前操作用户id
	 */
	void update(StoreInfoDetailSaveRequest store, Long userId) throws Exception;

    /**
     * 店铺展示 当查询条件都为空  返回所有店铺
     *
     * @param storeSearchRequest 查询请求参数
     * @return 店铺信息列表
     */
    List<StoreInfoResponse> query(StoreSearchRequest storeSearchRequest);

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
	 * 店铺新增页面查询相关信息  互联网医院 店铺 供应商
	 * @return StoreAddResponse
	 */
	 StoreAddResponse queryStoreAddInfo();

	/**
	 * 店铺查询页面返回相应信息
	 * @return  互联网医院 名字 id 店铺类型名字
	 */
	 StoreQueryResponse queryStoreConInfo();
}
