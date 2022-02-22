package com.drstrong.health.product.model.entity.store;

import com.baomidou.mybatisplus.annotation.TableName;
import com.drstrong.health.product.model.entity.category.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author lsx
 * @projectName health-mall-product
 * @desc 三方药店关联商品表
 * @createTime 2021/12/13 10:37
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("product_three_store_relevance")
public class StoreThreeRelevanceEntity extends BaseEntity implements Serializable {


    private static final long serialVersionUID = -6150873399085003976L;
    /**
     * 自增主键
     */
    private Long id;

    /**
     * 商城skuID
     */
    private Long skuId;

    /**
     * 三方店铺ID
     */
    private Long storeId;

    /**
     * 三方店铺skuId
     */
    private Long threeSkuId;

    /**
     * 三方药店商品进货价
     */
    private Integer threePurchasePrice;


    /**
     * 乐观锁
     */
    private Integer version;
}
