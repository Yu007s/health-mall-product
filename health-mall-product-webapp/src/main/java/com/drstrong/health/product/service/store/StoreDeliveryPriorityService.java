package com.drstrong.health.product.service.store;

import com.drstrong.health.product.model.entity.store.DeliveryPriorityEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import com.drstrong.health.product.model.request.store.DeliveryPriRequest;
import com.drstrong.health.product.model.request.store.SaveDeliveryRequest;
import com.drstrong.health.product.model.response.store.delievy.DeliveryPriorityVO;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author mybatis plus generator
 * @since 2022-08-05
 */
public interface StoreDeliveryPriorityService extends IService<DeliveryPriorityEntity> {

    DeliveryPriorityVO queryByStoreId(Long storeId);

    void save(SaveDeliveryRequest saveDeliveryRequest,Long userId);


}
