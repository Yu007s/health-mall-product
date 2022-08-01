package com.drstrong.health.product.model.response.chinese;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author liuqiuyi
 * @date 2022/7/25 17:21
 */
@Data
@ApiModel("保存/更新中药sku的入参")
public class SaveOrUpdateSkuVO implements Serializable{
    private static final long serialVersionUID = -1921937978175838893L;

    @ApiModelProperty("sku编码，保存时非必填，修改时必填")
    private String skuCode;

    @ApiModelProperty("药材编码")
    private String medicineCode;

    @ApiModelProperty("药材名称")
    private String medicineName;

    @ApiModelProperty("sku 名称")
    private String skuName;

    @ApiModelProperty("价格")
    private BigDecimal price;

    @ApiModelProperty("店铺id")
    private Long storeId;

    @ApiModelProperty("店铺名称")
    private String storeName;

    @ApiModelProperty("供应商信息集合")
    private List<SupplierInfo> supplierInfoList;

    @Data
    @ApiModel("供应商信息")
    public static class SupplierInfo implements Serializable {
        private static final long serialVersionUID = -7015324758417521372L;

        @ApiModelProperty("供应商id")
        private Long supplierId;

        @ApiModelProperty("供应商名称")
        private String supplierName;

        @ApiModelProperty("库存类型。0-实物库存，1-无限库存，2-虚拟库存")
        private Integer stockType;

        @ApiModelProperty("虚拟库存数")
        private Integer virtualQuantity;
    }
}
