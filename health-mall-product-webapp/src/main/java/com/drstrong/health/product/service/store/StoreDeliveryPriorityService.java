package com.drstrong.health.product.service.store;

import com.drstrong.health.product.model.entity.store.DeliveryPriorityEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import com.drstrong.health.product.model.request.store.DeliveryPriRequest;
import com.drstrong.health.product.model.request.store.SaveDeliveryRequest;
import com.drstrong.health.product.model.response.store.delievy.DeliveryPriResponse;
import com.drstrong.health.product.model.response.store.delievy.DeliveryPriorityVO;

import java.util.List;

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

    /**
     * 根据店铺id 和区域id 查找相应的配送优先级
     * @param storeId 店铺id
     * @param areaId 区域id
     * @return 供应商配送优先级
     */
    List<Long>  queryByStoreIdAndArea(Long storeId,Long areaId);

    void save(SaveDeliveryRequest saveDeliveryRequest,Long userId);


}
