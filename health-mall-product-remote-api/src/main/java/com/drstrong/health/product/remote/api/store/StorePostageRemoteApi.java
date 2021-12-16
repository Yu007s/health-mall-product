package com.drstrong.health.product.remote.api.store;

import com.drstrong.health.product.remote.model.StorePostageDTO;

import java.util.List;
import java.util.Set;

/**
 * @author lsx
 * @projectName health-mall-product
 * @desc 店铺运费远程服务api
 * @createTime 2021/12/15 15:54
 * @since TODO
 */
public interface StorePostageRemoteApi {

    List<StorePostageDTO> getStorePostageByIds(Set<Long> storeIds, String areaName);
}
