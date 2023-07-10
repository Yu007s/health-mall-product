package com.drstrong.health.product.model.response.product.v3.recommend;

import com.drstrong.health.product.model.response.product.v3.ProductManageQueryVO;
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

/**
 * @author liuqiuyi
 * @date 2023/7/10 16:35
 */
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel("sku推荐信息 响应值")
public class SkuRecommendManageResponse extends ProductManageQueryVO implements Serializable {
    private static final long serialVersionUID = -3389121773094281299L;

    @ApiModelProperty("sku编码")
    private String skuCode;

    @ApiModelProperty("sku名称")
    private String skuName;

    @ApiModelProperty("用法用量")
    private String usageDosage;

    @ApiModelProperty("搜索关键字")
    private List<String> keywordList;

    @ApiModelProperty("店铺id")
    private Long storeId;

    @ApiModelProperty("店铺名称")
    private String storeName;
}
