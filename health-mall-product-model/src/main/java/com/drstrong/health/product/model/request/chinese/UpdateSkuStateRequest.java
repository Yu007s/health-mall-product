package com.drstrong.health.product.model.request.chinese;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Set;

/**
 * @author liuqiuyi
 * @date 2022/8/2 14:15
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel("保存/更新中药sku的入参")
public class UpdateSkuStateRequest implements Serializable {
    private static final long serialVersionUID = -6964632529439063141L;

    @ApiModelProperty("sku编码，保存时非必填，修改时必填")
    @NotNull(message = "sku编码不能为空")
    @Size(max = 200, message = "skuCode数量不能超过200")
    private Set<String> skuCodeList;

    @ApiModelProperty("sku 状态。0-下架，1-上架")
    @NotNull(message = "sku 操作状态不能为空")
    @Max(value = 1, message = "sku 状态取值不正确")
    private Integer skuState;

    @ApiModelProperty(value = "操作人id", hidden = true)
    private Long operatorId;
}
