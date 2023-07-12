package com.drstrong.health.product.model.dto.product;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * huangpeng
 * 2023/7/12 10:38
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductAgreementDetailVO implements Serializable {

    private static final long serialVersionUID = 7881713589559446114L;

    /**
     * 处方
     */
    @ApiModelProperty(value = "处方")
    private String prescriptions;

    /**
     * 功效
     */
    @ApiModelProperty(value = "功效")
    private String efficacy;

    /**
     * 服法
     */
    @ApiModelProperty(value = "服法")
    private String usageMethod;

    /**
     * 包装(0.25g*12片*2板/盒)
     */
    @ApiModelProperty(value = "包装")
    private String packingSpec;

    /**
     * 图片
     */
    @ApiModelProperty(value = "图片")
    private String imageInfo;


}
