package com.drstrong.health.product.model.response.product;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author lsx
 * @projectName health-mall-product
 * @desc 热门推荐商品返回值
 * @createTime 2021/12/21 21:12
 * @since TODO
 */
@Data
@ApiModel("热门推荐商品返回值")
public class ProductRecommendVO implements Serializable {
    private static final long serialVersionUID = 403157353536984487L;

    @ApiModelProperty("商品标题")
    private String productName;

    @ApiModelProperty("spu 编码")
    private String spuCode;

    @ApiModelProperty("主图地址")
    private String masterImageUrl;

    @ApiModelProperty("商品描述")
    private String description;

    @ApiModelProperty("价格,这里展示的是最低价格")
    private BigDecimal lowPrice;

}
