package com.drstrong.health.product.model.entity.productstore;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.drstrong.health.product.model.entity.category.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author lsx
 * @projectName health-mall-product
 * @desc 商品店铺
 * @createTime 2021/12/13 10:37
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("product_product_store")
public class StoreEntity extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 8498028506851891110L;

    /**
     * 自增主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 店铺名称
     */
    private String name;

    /**
     * 店铺类型(0-自营,1-其它)
     */
    private Integer storeType;

    /**
     * 店铺编码
     */
    private String code;

    /**
     * 联系人名称
     */
    private String contactName;

    /**
     * 联系人号码
     */
    private String contactMobile;

    /**
     * 商品数量
     */
    private Integer productCount;

    /**
     * 药品数量
     */
    private Integer medicineCount;

    /**
     * 是否设置了邮费，0-未设置，1-已设置
     */
    private Integer setPostage;

    /**
     * 满多少包邮(分)，默认为0
     */
    private Integer freePostage;

    /**
     * 0-启用,1-未启用
     */
    private Integer storeStatus;
}
