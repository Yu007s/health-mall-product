package com.drstrong.health.product.model.request.chinese;

import com.drstrong.health.product.model.request.PageRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Max;
import java.io.Serializable;

/**
 * 中药管理页面列表查询的入参
 *
 * @author liuqiuyi
 * @date 2022/8/1 10:30
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel("中药管理页面请求入参")
public class ChineseManagerSkuRequest extends PageRequest implements Serializable {
    private static final long serialVersionUID = 3476606891336123039L;

    @ApiModelProperty("sku名称")
    private String skuName;

    @ApiModelProperty("sku编码")
    private String skuCode;

    @ApiModelProperty("sku状态，0-未上架，1-已上架")
    @Max(value = 1,message = "sku状态传值不正确")
    private Integer skuState;

    @ApiModelProperty("供应商id")
    private Long supplierId;

    @ApiModelProperty("店铺id")
    private Long storeId;
}
