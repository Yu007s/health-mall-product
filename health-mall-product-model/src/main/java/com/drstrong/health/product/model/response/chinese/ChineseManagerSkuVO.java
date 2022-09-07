package com.drstrong.health.product.model.response.chinese;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

/**
 * @author liuqiuyi
 * @date 2022/8/1 10:57
 */
@Data
@ApiModel("中药管理页面返回值")
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChineseManagerSkuVO implements Serializable {
    private static final long serialVersionUID = 3740737744156485985L;

    @ApiModelProperty("中药材编码")
    private String medicineCode;

    @ApiModelProperty("sku 编码")
    private String skuCode;

    @ApiModelProperty("sku 名称")
    private String skuName;

    @ApiModelProperty("店铺id")
    private Long storeId;

    @ApiModelProperty("店铺名称")
    private String storeName;

    @ApiModelProperty("供应商id集合")
    private Set<Long> supplierIdList;

    @ApiModelProperty("供应商名称")
    private List<String> supplierName;

    @ApiModelProperty("价格/克,单位：元")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal price;

    @ApiModelProperty("sku状态，0-未上架，1-已上架")
    private Integer skuState;

    @ApiModelProperty("sku状态名称")
    private String skuStateName;
}
