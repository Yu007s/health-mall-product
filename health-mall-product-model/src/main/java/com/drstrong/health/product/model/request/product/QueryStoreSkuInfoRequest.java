package com.drstrong.health.product.model.request.product;

import com.drstrong.health.product.model.request.PageRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * huangpeng
 * 2023/7/7 20:03
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class QueryStoreSkuInfoRequest extends PageRequest implements Serializable {

    private static final long serialVersionUID = 1842569972108814648L;

    @ApiModelProperty("条件")
    private String key;

    @ApiModelProperty("店铺ID")
    private Integer storeId;

    @ApiModelProperty("药品类型")
    private Integer productType;

    @ApiModelProperty("sku状态")
    private Integer skuStatus;
}
