package com.drstrong.health.product.service.store;

import com.drstrong.health.product.model.entity.store.StoreInvoiceEntity;

/**
 * @Author xieYueFeng
 * @Date 2022/07/30/9:54
 */
public interface
StoreInvoiceService {
    StoreInvoiceEntity getByStoreId(Long storeId);
    void removeByStoreId(Long storeId);

}
