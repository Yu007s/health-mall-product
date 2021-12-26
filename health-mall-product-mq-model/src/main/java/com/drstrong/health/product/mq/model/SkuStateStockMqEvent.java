package com.drstrong.health.product.mq.model;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * sku上下架通知库存
 */
@Data
public class SkuStateStockMqEvent implements Serializable {

    private static final long serialVersionUID = -1871164642029161691L;

    private Integer state;

    private List<Long> skuIdList;

    private String userId;
}
