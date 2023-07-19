package com.drstrong.health.product.model.request.sku;

import com.drstrong.health.product.model.request.PageRequest;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * huangpeng
 * 2023/7/13 10:20
 */
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel("sku分页列表查询的入参")
public class QuerySkuBusinessListRequest extends PageRequest implements Serializable {

    private static final long serialVersionUID = 1232569972108814648L;

    @NotNull(message = "商品类型不能为空")
    @ApiModelProperty("商品类型(1-中西药，3-协定方)")
    private Integer productType;

    @NotNull(message = "店铺信息不能为空")
    @ApiModelProperty("店铺id")
    private Long storeId;

    @ApiModelProperty("sku编码/规格名称")
    private String key;

    @ApiModelProperty("是否需要查询库存 0-不需要  1-需要")
    private Integer needQueryInventory;
}
