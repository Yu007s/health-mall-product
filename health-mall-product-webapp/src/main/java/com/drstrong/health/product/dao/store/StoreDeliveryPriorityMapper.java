package com.drstrong.health.product.dao.store;

import com.drstrong.health.product.model.entity.store.DeliveryPriorityEntity;
import cn.strong.mybatis.plus.extend.CustomBaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author mybatis plus generator
 * @since 2022-08-05
 */
@Mapper
public interface StoreDeliveryPriorityMapper extends CustomBaseMapper<DeliveryPriorityEntity> {
    void updateBatch(List<DeliveryPriorityEntity> entityList);

}
