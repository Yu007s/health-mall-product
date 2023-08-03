package com.drstrong.health.product.model.dto.medicine.v2;

import com.drstrong.health.product.model.enums.ProductTypeEnum;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 药材库的公共返回值
 *
 * @author liuqiuyi
 * @date 2023/6/20 10:16
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel("药材库的公共返回值")
public class MedicineWarehouseBaseDTO implements Serializable {
    private static final long serialVersionUID = 2698630207317303454L;

    /**
     * @see ProductTypeEnum
     */
    @ApiModelProperty("商品类型")
    private Integer productType;

    @ApiModelProperty("商品类型 名称")
    private String productTypeName;

    @ApiModelProperty("协定方基础信息")
    private AgreementPrescriptionMedicineBaseDTO agreementPrescriptionMedicineBaseDTO;

    @ApiModelProperty("中药材库基础信息")
    private ChineseMedicineBaseDTO chineseMedicineBaseDTO;

    @ApiModelProperty("西药材库基础信息")
    private WesternMedicineBaseDTO westernMedicineBaseDTO;
}
