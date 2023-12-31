package com.drstrong.health.product.model.entity.store;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;


import com.drstrong.health.product.model.entity.category.BaseStandardEntity;
import com.drstrong.health.product.model.enums.DelFlagEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 *
 * @author mybatis plus generator
 * @since 2022-08-05
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("pms_store_delivery_priority")
public class DeliveryPriorityEntity extends BaseStandardEntity implements Serializable {

    private static final long serialVersionUID = 1548474653267452161L;
    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 对应的店铺的id  此列设置索引
     */
    private Long storeId;

    /**
     * 区域id  默认区域设置为全国
     */
    private Long areaId;

    /**
     * 父级区域id
     */
    private Long parentAreaId;

    /**
     * 该店铺下该地区所有供应商优先级设置  以“,”号隔开,前面的优先级更小
     */
    private String priorities;


	public static DeliveryPriorityEntity buildDefault(Long operatorId) {
		DeliveryPriorityEntity priorityEntity = new DeliveryPriorityEntity();
		priorityEntity.setVersion(1);
		priorityEntity.setDelFlag(DelFlagEnum.UN_DELETED.getCode());
		priorityEntity.setCreatedBy(operatorId);
		priorityEntity.setChangedAt(LocalDateTime.now());
		priorityEntity.setChangedAt(LocalDateTime.now());
		priorityEntity.setChangedBy(operatorId);
		return priorityEntity;
	}
}
