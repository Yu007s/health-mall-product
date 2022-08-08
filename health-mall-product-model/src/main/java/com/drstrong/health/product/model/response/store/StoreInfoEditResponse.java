package com.drstrong.health.product.model.response.store;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * @Author xieYueFeng
 * @Date 2022/08/02/16:11
 */
@Data
public class StoreInfoEditResponse implements Serializable {
    private static final long serialVersionUID = 1564875342626489461L;
    @ApiModelProperty("店铺主键id")
    private Long id;

    @NotNull
    @ApiModelProperty("店铺名称")
    private String storeName;

    @NotNull
    @ApiModelProperty("关联供应商id集合")
    private List<Long> supplierIds;

    @NotNull
    @ApiModelProperty("店铺类型")
    private String storeTypeName;

    @ApiModelProperty("关联互联网医院的名字")
    private String agencyName;

    @NotNull
    @ApiModelProperty("企业税号  最长50位  只有英文、数字")
    private String enterpriseTaxNumber;

    @NotNull
    @ApiModelProperty("企业开户行、银行账号")
    private String enterpriseBankAccount;

    @NotNull
    @ApiModelProperty("企业电话")
    private String enterpriseTelNumber;

    @NotNull
    @ApiModelProperty("企业地址")
    private String enterpriseAddress;

    @ApiModelProperty("开票员")
    private String drawer;

    @ApiModelProperty("收款人")
    private String payee;

    @ApiModelProperty("复核人")
    private String checker;

    @NotNull
    @ApiModelProperty("应用密钥")
    private String appSecret;

    @NotNull
    @ApiModelProperty("应用key")
    private String appKey;
}
