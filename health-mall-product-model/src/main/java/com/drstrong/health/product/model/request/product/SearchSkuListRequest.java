package com.drstrong.health.product.model.request.product;

import com.drstrong.health.product.model.request.PageRequest;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * huangpeng
 * 2023/7/8 14:18
 */
@Data
@ApiModel("空中药房搜索sku入参")
public class SearchSkuListRequest extends PageRequest implements Serializable {

    private static final long serialVersionUID = 1983456997210814648L;

    @ApiModelProperty("搜索条件")
    private String key;

    @ApiModelProperty("商品类型")
    private Integer productType;

    @ApiModelProperty("商品分类")
    private Long categoryId;

}
