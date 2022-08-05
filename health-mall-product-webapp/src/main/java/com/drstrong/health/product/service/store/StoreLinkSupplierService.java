package com.drstrong.health.product.service.store;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.drstrong.health.product.model.entity.store.StoreEntity;
import com.drstrong.health.product.model.entity.store.StoreLinkSupplierEntity;
import com.drstrong.health.product.model.response.store.StoreInfoResponse;

import java.util.List;

/**
 * @Author xieYueFeng
 * @Date 2022/07/30/9:36
 */
public interface StoreLinkSupplierService  {
    void saveBatch(List<StoreLinkSupplierEntity > linkSupplierEntities);
    List<StoreLinkSupplierEntity> queryByStoreId(Long storeId);
    public List<StoreInfoResponse> queryBySupplierId(Long supplierId) ;
}
