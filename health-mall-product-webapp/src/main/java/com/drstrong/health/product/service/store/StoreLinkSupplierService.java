package com.drstrong.health.product.service.store;


import com.drstrong.health.product.model.entity.store.StoreLinkSupplierEntity;
import com.drstrong.health.product.model.response.store.StoreInfoResponse;

import java.util.List;

/**
 * @Author xieYueFeng
 * @Date 2022/07/30/9:36
 */
public interface StoreLinkSupplierService  {
    /**
     * 批量存储店铺关联供应商
     * @param linkSupplierEntities 店铺关联供应商实体类列表
     */
    void saveBatch(List<StoreLinkSupplierEntity > linkSupplierEntities);

    /**
     * 查询关联供应商信息  根据店铺id
     * @param storeId 店铺id
     * @return 返回查询所得
     */
    List<StoreLinkSupplierEntity> queryByStoreId(Long storeId);

    /**
     * 查询店铺信息  根据供应商id
     * @param supplierId 供应商id
     * @return 查询所得店铺信息
     */
     List<StoreInfoResponse> queryBySupplierId(Long supplierId) ;
}
