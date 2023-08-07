package com.drstrong.health.product.model.dto.stock;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * sku可用库存返回值
 *
 * @author liuqiuyi
 * @date 2023/7/10 11:14
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel("sku可用库存返回值")
public class SkuCanStockDTO implements Serializable {
    private static final long serialVersionUID = -6220783287775038055L;

    @ApiModelProperty("sku编码")
    private String skuCode;

    @ApiModelProperty("供应商ID")
    private Long supplierId;

    @ApiModelProperty("供应商名称")
    private String supplierName;

    @ApiModelProperty("可销虚拟库存(毫克)")
    private Long availableQuantity;
}
