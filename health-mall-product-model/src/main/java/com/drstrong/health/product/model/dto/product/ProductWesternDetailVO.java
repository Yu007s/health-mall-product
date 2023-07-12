package com.drstrong.health.product.model.dto.product;

import com.drstrong.health.product.model.dto.medicine.MedicineImageDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * huangpeng
 * 2023/7/11 20:58
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductWesternDetailVO implements Serializable {

    private static final long serialVersionUID = 7891913589559446114L;

    /**
     * 药品名称
     */
    @ApiModelProperty(value = "药品名称")
    private String medicineName;

    /**
     * 通用名
     */
    @ApiModelProperty(value = "通用名")
    private String commonName;

    /**
     * 汉语简拼
     */
    @ApiModelProperty(value = "汉语简拼")
    private String pinyin;

    /**
     * 成分
     */
    @ApiModelProperty(value = "成分")
    private String ingredients;

    /**
     * 性状
     */
    @ApiModelProperty(value = "性状")
    private String phenotypicTrait;

    /**
     * 功能主治
     */
    @ApiModelProperty(value = "功能主治")
    private String indications;

    /**
     * 用法用量
     */
    @ApiModelProperty(value = "用法用量")
    private String usageDosage;

    /**
     * 不良反应
     */
    @ApiModelProperty(value = "不良反应")
    private String adverseEffects;

    /**
     * 禁忌
     */
    @ApiModelProperty(value = "禁忌")
    private String contraindications;

    /**
     * 注意事项
     */
    @ApiModelProperty(value = "注意事项")
    private String mattersNeedingAttention;

    /**
     * 药品贮藏
     */
    @ApiModelProperty(value = "药品贮藏")
    private String medicineStorage;

    /**
     * 规格(0.25g*12片*2板/盒)
     */
    @ApiModelProperty(value = "规格")
    private String packingSpec;

    /**
     * 有效期
     */
    @ApiModelProperty(value = "有效期")
    private String periodValidity;

    /**
     * 执行标准
     */
    @ApiModelProperty(value = "执行标准")
    private String executionStandard;

    /**
     * 批准文号
     */
    @ApiModelProperty(value = "批准文号")
    private String approvalNumber;

    /**
     * 生产企业
     */
    @ApiModelProperty(value = "生产企业")
    private String productionEnterprise;

    /**
     * 图片
     */
    @ApiModelProperty(value = "图片")
    private List<MedicineImageDTO> specImageInfo;
}
