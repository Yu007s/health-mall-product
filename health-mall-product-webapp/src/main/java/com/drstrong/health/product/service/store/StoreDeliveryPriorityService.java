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
 * 服务类
 * </p>
 *
 * @author mybatis plus generator
 * @since 2022-08-05
 */
public interface StoreDeliveryPriorityService extends IService<DeliveryPriorityEntity> {

    /**
     * 查询店铺配送优先级
     *
     * @param storeId 店铺id
     * @return 店铺配送优先级相关数据
     */
    DeliveryPriorityVO queryByStoreId(Long storeId);

    /**
     * 根据店铺id 和区域id 查找相应的配送优先级
     *
     * @param storeId 店铺id
     * @param areaId  区域id
     * @return 供应商配送优先级
     */
    List<Long> queryByStoreIdAndArea(Long storeId, Long areaId);

    /**
     * 这个接口后续将废弃,为了暂时兼容老逻辑,等供应链三期正式上线后可以去除
     *
     * 保存店铺配送优先级
     * @param saveDeliveryRequest 店铺配送优先级相关数据
     * @param userId 用户id
     */
    @Deprecated
    void save(SaveDeliveryRequest saveDeliveryRequest, Long userId);

    /**
     * 保存店铺配送优先级
     *
     * @author liuqiuyi
     * @date 2023/6/9 13:45
     */
	void saveDeliveryInfoV3(SaveDeliveryRequest saveDeliveryRequest, Long userId);
}
