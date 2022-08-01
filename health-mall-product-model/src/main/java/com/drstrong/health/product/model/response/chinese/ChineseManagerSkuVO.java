package com.drstrong.health.product.model.response.chinese;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author liuqiuyi
 * @date 2022/8/1 10:57
 */
@Data
@ApiModel("中药管理页面返回值")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChineseManagerSkuVO implements Serializable {
    private static final long serialVersionUID = 3740737744156485985L;

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("spu 编码")
    private String spuCode;

    @ApiModelProperty("sku 编码")
    private String skuCode;

    @ApiModelProperty("sku 名称")
    private String skuName;

    @ApiModelProperty("店铺id")
    private Long storeId;

    @ApiModelProperty("店铺名称")
    private String storeName;

    @ApiModelProperty("供应商id集合")
    private List<Long> supplierIdList;

    @ApiModelProperty("供应商名称")
    private List<String> supplierName;

    @ApiModelProperty("价格")
    private BigDecimal price;

    @ApiModelProperty("sku状态，0-未上架，1-已上架")
    private Integer skuState;
}
