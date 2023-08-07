package com.drstrong.health.product.model.dto.medicine;

import com.drstrong.health.product.model.entity.medication.AgreementPrescriptionMedicineEntity;
import com.drstrong.health.product.model.entity.medication.WesternMedicineEntity;
import com.drstrong.health.product.model.entity.medication.WesternMedicineInstructionsEntity;
import com.drstrong.health.product.model.entity.medication.WesternMedicineSpecificationsEntity;
import com.drstrong.health.product.model.entity.sku.StoreSkuInfoEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * huangpeng
 * 2023/7/24 17:17
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@ApiModel("药品详情")
public class ProductDetailInfoDTO implements Serializable {

    private static final long serialVersionUID = 7547816185330183482L;

    @ApiModelProperty("sku编码")
    private String skuCode;

    @ApiModelProperty("sku类型")
    private Integer productType;

    @ApiModelProperty("sku基本信息")
    private StoreSkuInfoEntity storeSkuInfoEntity;

    @ApiModelProperty("西药商品库")
    private WesternMedicineEntity westernMedicineEntity;

    @ApiModelProperty("西药说明")
    private WesternMedicineInstructionsEntity westernMedicineInstructionsEntity;

    @ApiModelProperty("西药规格")
    private WesternMedicineSpecificationsEntity westernMedicineSpecificationsEntity;

    @ApiModelProperty("协定方说明")
    private AgreementPrescriptionMedicineEntity agreementPrescriptionMedicineEntity;
}
