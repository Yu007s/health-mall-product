package com.drstrong.health.product.model.request.product.v3;

import com.drstrong.health.product.model.enums.ProductTypeEnum;
import com.drstrong.health.product.model.request.PageRequest;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * @author liuqiuyi
 * @date 2023/5/31 16:59
 */
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel("商品管理查询的入参")
public class ProductManageQueryRequest extends PageRequest implements Serializable {
    private static final long serialVersionUID = 1842569972108814648L;

    /**
     * @see ProductTypeEnum
     */
    @ApiModelProperty("商品类型")
    private Integer productType;

    @ApiModelProperty("规格编码")
    private String medicineCode;

    @ApiModelProperty("sku 名称")
    private String skuName;

    @ApiModelProperty("sku 编码")
    private String skuCode;

    @ApiModelProperty("sku编码/sku名称")
    private String key;

    @ApiModelProperty("sku 状态")
    private Integer skuStatus;

    @ApiModelProperty("sku 状态")
    private Set<Integer> skuStatusList;

    @ApiModelProperty("供应商 id")
    private Long supplierId;

    @ApiModelProperty("店铺id,和agencyId任传其一")
    private Long storeId;

    @ApiModelProperty("互联网医院id,和storeId任传其一")
    private Long agencyId;

    @ApiModelProperty("sku分类")
    private Long categoryId;

    @ApiModelProperty("sku禁售city")
    private Integer cityId;

    @ApiModelProperty("店铺id列表")
    private List<Long> storeIds;
}
