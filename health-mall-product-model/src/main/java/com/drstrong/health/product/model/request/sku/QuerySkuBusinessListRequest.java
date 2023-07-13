package com.drstrong.health.product.model.request.sku;

import com.drstrong.health.product.model.request.PageRequest;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;

/**
 * huangpeng
 * 2023/7/13 10:20
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@ApiModel("sku分页列表查询的入参")
public class QuerySkuBusinessListRequest extends PageRequest implements Serializable {

    private static final long serialVersionUID = 1232569972108814648L;

    @ApiModelProperty("商品类型(1-中西药，3-协定方)")
    private Integer productType;

    @ApiModelProperty("店铺id")
    private Long storeId;

    @ApiModelProperty("sku编码/规格名称")
    private String key;
}
