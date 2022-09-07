package com.drstrong.health.product.dao.store;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.drstrong.health.product.model.entity.store.DeliveryPriorityEntity;
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
public interface StoreDeliveryPriorityMapper extends BaseMapper<DeliveryPriorityEntity> {
    void updateBatch(List<DeliveryPriorityEntity> entityList);

}
