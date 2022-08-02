package com.drstrong.health.product.service.productstore;

import com.drstrong.health.product.model.entity.productstore.StoreEntity;
import com.drstrong.health.product.model.enums.CategoryProductNumOperateEnum;
import com.drstrong.health.product.model.request.productstore.StoreAddOrUpdateRequest;
import com.drstrong.health.product.model.request.productstore.StoreIdRequest;
import com.drstrong.health.product.model.request.productstore.StorePostage;
import com.drstrong.health.product.model.response.productstore.StoreInfoResponse;
import com.drstrong.health.product.mq.model.product.StoreChangeTypeEnum;
import com.drstrong.health.product.remote.model.StorePostageDTO;

import java.util.List;
import java.util.Set;

/**
 * @author lsx
 * @projectName health-mall-product
 * @desc 店铺service
 * @createTime 2021/12/13 11:24
 */
public interface ProductStoreService {

    List<StoreInfoResponse> queryAll();

    void add(StoreAddOrUpdateRequest storeAddOrUpdateRequest,String userId);

    void update(StoreAddOrUpdateRequest storeAddOrUpdateRequest,String userId);

    void updateState(StoreIdRequest storeIdRequest, String userId);

    StorePostage getPostage(Long storeId);

    StoreEntity getByStoreId(Long storeId);

    void updatePostage(StorePostage storePostage,String userId);

    List<StoreEntity> querySetPostageByStoreIds(List<Long> storeIds);

    List<StorePostageDTO> getStorePostageByIds(Set<Long> storeIds, String areaName);

    List<StoreInfoResponse> queryByStoreIds(Set<Long> storeIds);

	/**
	 * 增加或者减少 店铺的商品数量
	 *
	 * @param storeId 店铺id
	 * @param count   要累加的商品数量
	 * @author liuqiuyi
	 * @date 2021/12/28 11:08
	 */
	void addOrReduceProductNumById(Long storeId, Integer count, CategoryProductNumOperateEnum operateEnum);

	/**
	 * 发送店铺变更的事件
	 *
	 * @param userId  操作人 id
	 * @param storeId 店铺 id
	 * @author liuqiuyi
	 * @date 2021/12/30 11:03
	 */
	void sendStoreChangeEvent(Long storeId, String userId, StoreChangeTypeEnum storeChangeTypeEnum);
}
