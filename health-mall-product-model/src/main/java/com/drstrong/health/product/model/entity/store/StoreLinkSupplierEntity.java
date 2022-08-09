package com.drstrong.health.product.model.entity.store;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.drstrong.health.product.model.entity.category.BaseEntity;
import com.drstrong.health.product.model.entity.category.BaseStandardEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author xieYueFeng
 * @since 2022-07-206 15:59:45
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("pms_store_link_supplier")
public class StoreLinkSupplierEntity extends BaseStandardEntity implements Serializable {

    private static final long serialVersionUID = 1515483874456584741L;

    /**
     * 主键id 自增
     */
    @TableId("id")
    private Long id;

    /**
     * 店铺id  此列设置索引
     */
    @TableField("store_id")
    private Long storeId;

    /**
     * 供应商id
     */
    @TableField("supplier_id")
    private Long supplierId;

}
