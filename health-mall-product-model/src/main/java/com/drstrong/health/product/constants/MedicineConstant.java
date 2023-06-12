package com.drstrong.health.product.constants;

public class MedicineConstant {

    /**
     * 药品数据完整
     */
    public static final int DATA_INTEGRITY = 1;

    /**
     * 药品数据不完整
     */
    public static final int DATA_NO_INTEGRITY = 0;

    public static final int NO_USE_USAGE_DOSAGE = 0;

    public static final int USE_USAGE_DOSAGE = 1;

    /**
     * 保存药品
     */
    public static final String SAVE_WESTERN_MEDICINE = "saveWesternMedicine";

    /**
     * 更新药品
     */
    public static final String UPDATE_WESTERN_MEDICINE = "updateWesternMedicine";

    /**
     * 药品编码生成key(旧)
     */
    public static final String SERIAL_NUMBER_REDIS_KEY = "naiterui-b2c|product_serial_number";

    /**
     * 药品规格编码生成key(旧)
     */
    public static final String SPEC_SERIAL_NUMBER_REDIS_KEY = "naiterui-b2c|product_sku_serial_number_";
}