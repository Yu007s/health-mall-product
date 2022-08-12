package com.drstrong.health.product.model.request.store;

import com.drstrong.health.product.model.enums.StoreTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * @Author xieYueFeng
 * @Date 2022/07/26/10:12
 */
@Data
@ApiModel("店铺信息请求值 编辑页面详细")
public class StoreInfoDetailSaveRequest implements Serializable {
    private static final long serialVersionUID = 2211348485397306232L;
    @ApiModelProperty("店铺主键id")
    private Long storeId;

    @NotNull
    @ApiModelProperty("店铺名称")
    private String storeName;

    @ApiModelProperty("店铺描述")
    private String storeDes;

    @NotNull
    @ApiModelProperty("关联供应商id集合")
    private List<Long> supplierIds;

    @NotNull
    @ApiModelProperty("店铺类型")
    private String storeTypeName;

    @ApiModelProperty("关联互联网医院的id")
    private Long agencyId;

    @ApiModelProperty("关联互联网医院名字")
    private Long agencyName;

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

    @NotNull
    @ApiModelProperty("收款人")
    private String payee;

    @NotNull
    @ApiModelProperty("复核人")
    private String checker;

    @NotNull
    @ApiModelProperty("应用密钥")
    private String appSecret;

    @NotNull
    @ApiModelProperty("应用key")
    private String appKey;

    @ApiModelProperty("用户id")
    private Long userId;

}
