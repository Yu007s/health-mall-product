package com.drstrong.health.product.model.entity.zStore;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.drstrong.health.product.model.entity.category.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author xieYueFeng
 * @since 2022-07-206 11:11:19
 */
@Data
@TableName("product_zstore")
@EqualsAndHashCode(callSuper = true)
public class StoreEntity extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 4221654848942976232L;

    /**
     * 主键id  自增
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 店铺名称
     */
    @TableField("name")
    private String name;

    /**
     * 店铺类型： 0 互联网医院 1 其它
     */
    @TableField("store_type")
    private Integer storeType;

    /**
     * 关联互联网医院的id
     */
    @TableField("agency_id")
    private Long agencyId;

    /**
     * 乐观锁
     */
    private Integer version;

}
