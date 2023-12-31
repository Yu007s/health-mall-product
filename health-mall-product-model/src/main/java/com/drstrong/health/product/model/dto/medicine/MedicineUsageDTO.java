package com.drstrong.health.product.model.dto.medicine;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author liuqiuyi
 * @date 2023/7/11 10:10
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@ApiModel("药品的用法用量 DTO")
public class MedicineUsageDTO implements Serializable {
    private static final long serialVersionUID = -2304766643750793896L;

    private static final String MC = "每次";
    private static final String C_COMMA = "，";

    @ApiModelProperty("商品类型")
    private Integer productType;

    @ApiModelProperty("skuId")
    private Long skuId;

    @ApiModelProperty("sku编码")
    private String skuCode;

    @ApiModelProperty("sku名称")
    private String skuName;

    @ApiModelProperty("sku状态，和UpOffEnum中一致")
    private Integer skuStatus;

    @ApiModelProperty("药材code")
    private String medicineCode;

    @ApiModelProperty("关联id,pms_western_medicine_specifications 表主键 或 pms_agreement_prescription_medicine 表主键")
    private Long relationId;

    @ApiModelProperty("关联类型 1：西药规格 2：协定方")
    private Integer relationType;

    @ApiModelProperty("外包装单位（盒,瓶...)")
    private String packingUnit;

    @ApiModelProperty("用药频次")
    private String medicationFrequency;

    @ApiModelProperty("每次几片,几毫克,几粒等等:有适量的情况")
    private String eachDosageCount;

    @ApiModelProperty("药品服用单位")
    private String eachDoseUnit;

    @ApiModelProperty("服用时间")
    private String usageTime;

    @ApiModelProperty("服用方式")
    private String usageMethod;

    /**
     * 设置用法用量
     *
     * @author liuqiuyi
     * @date 2023/8/1 14:47
     */
    public String getMedicineUsage(String separate) {
        // 如果传入的分隔符为空，则默认取中文逗号
        separate = ObjectUtil.defaultIfNull(separate, C_COMMA);
        // 组装规则 medication_frequency + 每次 + each_dosage_count + each_dose_unit + usage_time + usage_method
        return ObjectUtil.defaultIfNull(medicationFrequency, CharSequenceUtil.EMPTY) + separate +
                MC +
                ObjectUtil.defaultIfNull(eachDosageCount, CharSequenceUtil.EMPTY) +
                ObjectUtil.defaultIfNull(eachDoseUnit, CharSequenceUtil.EMPTY) + separate +
                ObjectUtil.defaultIfNull(usageTime, CharSequenceUtil.EMPTY) +
                ObjectUtil.defaultIfNull(usageMethod, CharSequenceUtil.EMPTY);
    }

    /**
     * 判断是否存在用法用量
     *
     * @author liuqiuyi
     * @date 2023/8/1 14:48
     */
    public Boolean hasMedicineUsage() {
        return StrUtil.isNotBlank(medicationFrequency) && StrUtil.isNotBlank(usageMethod);
    }
}
