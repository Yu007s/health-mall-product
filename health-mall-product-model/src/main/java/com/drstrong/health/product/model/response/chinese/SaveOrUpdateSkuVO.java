package com.drstrong.health.product.model.response.chinese;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author liuqiuyi
 * @date 2022/7/25 17:21
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel("保存/更新中药sku的入参")
public class SaveOrUpdateSkuVO implements Serializable{
    private static final long serialVersionUID = -1921937978175838893L;

    @ApiModelProperty("sku编码，保存时非必填，修改时必填")
    private String skuCode;

    @ApiModelProperty(value = "老的药材id", hidden = true)
    private Long medicineId;

    @ApiModelProperty("药材编码")
    @NotBlank(message = "药材编码不能为空")
    private String medicineCode;

    @ApiModelProperty("药材名称")
    private String medicineName;

    @ApiModelProperty("sku 名称")
    @NotBlank(message = "sku 名称不能为空")
    private String skuName;

    @ApiModelProperty("价格")
    @NotNull(message = "价格不能为空")
    @Min(value = 0, message = "价格不能小于0")
    @Max(value = 9999, message = "价格不能大于9999")
    private BigDecimal price;

    @ApiModelProperty("店铺id")
    @NotNull(message = "店铺id不能为空")
    private Long storeId;

    @ApiModelProperty("店铺名称")
    private String storeName;

    @ApiModelProperty("供应商信息集合")
    @NotNull(message = "供应商信息集合不能为空")
    @Valid
    private List<SupplierInfo> supplierInfoList;

    @ApiModelProperty(value = "操作人", hidden = true)
    private String operatorId;

    @Data
    @ApiModel("供应商信息")
    public static class SupplierInfo implements Serializable {
        private static final long serialVersionUID = -7015324758417521372L;

        @ApiModelProperty("供应商id")
        @NotNull(message = "供应商id不能为空")
        private Long supplierId;

        @ApiModelProperty("供应商名称")
        @NotBlank(message = "供应商名称不能为空")
        private String supplierName;

        @ApiModelProperty("库存类型。0-实物库存，1-无限库存，2-虚拟库存")
        @Max(value = 2, message = "库存类型不正确")
        @NotNull(message = "库存类型不能为空")
        private Integer stockType;

        @ApiModelProperty("虚拟库存数")
        private Integer virtualQuantity;
    }
}
