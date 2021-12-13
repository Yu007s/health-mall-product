package com.drstrong.health.product.service;

import com.drstrong.health.product.model.request.store.StoreAddOrUpdateRequest;
import com.drstrong.health.product.model.request.store.StoreIdRequest;
import com.drstrong.health.product.model.request.store.StorePostage;
import com.drstrong.health.product.model.response.store.StoreInfoResponse;

import java.util.List;

/**
 * @author lsx
 * @projectName health-mall-product
 * @desc 店铺service
 * @createTime 2021/12/13 11:24
 */
public interface StoreService {

    List<StoreInfoResponse> queryAll();

    void add(StoreAddOrUpdateRequest storeAddOrUpdateRequest,Long userId);

    void update(StoreAddOrUpdateRequest storeAddOrUpdateRequest,Long userId);

    void disable(StoreIdRequest storeIdRequest, Long userId);

    StorePostage getPostage(Long storeId);

    void updatePostage(StorePostage storePostage,Long userId);
}
