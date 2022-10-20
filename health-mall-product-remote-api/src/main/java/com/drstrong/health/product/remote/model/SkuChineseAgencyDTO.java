package com.drstrong.health.product.remote.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SkuChineseAgencyDTO {
    private Long id;
    private String skuCode;
    private String skuName;
    private Long oldMedicineId;
    private String medicineCode;
    private Long storeId;
    private Long price;
    private Integer skuStatus;
    private Long agencyId;
}
