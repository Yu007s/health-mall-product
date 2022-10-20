package com.drstrong.health.product.remote.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SkuChineseAgencyDTO {

    /**
     * 未上架
     */
    private final static int NO_SALE = 0;


    /**
     * 已上架
     */
    private final static int SALE = 1;

    private Long id;
    private String skuCode;
    private String skuName;
    private Long oldMedicineId;
    private String medicineCode;
    private Long storeId;
    private Long price;
    /**
     * sku上下架状态；0-未上架，1-已上架
     */
    private Integer skuStatus;
    private Long agencyId;
}
