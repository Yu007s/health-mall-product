package com.drstrong.health.product.model.response.product;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

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

    @ApiModelProperty("商品 id")
    private Long productId;

    private String title;

    private String masterImageUrl;

    private String description;

}
