package com.drstrong.health.product.model.response.zStore;

import com.drstrong.health.product.model.enums.StoreTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Map;

/**
 * @Author xieYueFeng
 * @Date 2022/07/26/10:12
 */
@Data
@ApiModel("店铺信息返回值 编辑页面详细")
public class StoreInfoDetailVO implements Serializable {
    private static final long serialVersionUID = 2211348485397306232L;

    @ApiModelProperty("店铺主键id")
    private Long id;

    @ApiModelProperty("店铺名称")
    @NotNull
    private String name;

    @NotNull
    @ApiModelProperty("关联供应商名字集合")
    private Map<Long,String> supplierMap;

    @NotNull
    @ApiModelProperty("店铺类型")
    private StoreTypeEnum storeType;


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
