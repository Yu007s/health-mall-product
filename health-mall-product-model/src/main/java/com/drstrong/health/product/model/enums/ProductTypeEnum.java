package com.drstrong.health.product.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;
import java.util.stream.Stream;

/**
 * 商品类型
 *
 * @author liuqiuyi
 * @date 2021/12/16 14:04
 */
@Getter
@AllArgsConstructor
public enum ProductTypeEnum {
    /**
     *
     */
    PRODUCT(0, "商品", "P"),
    MEDICINE(1, "西/成药", "M"),
    CHINESE(2, "中药", "Z"),
    AGREEMENT(3, "协定方(预制)", "X"),
    HEALTH(4, "健康用品", "H"),
    PACKAGE(5, "活动套餐", "T"),
    ;

    private final Integer code;
    private final String value;
    private final String mark;

    /**
     * 检查商品类型(不包含健康用品和活动套餐)
     *
     * @param productType
     * @return
     */
    public static Boolean checkWesternProduct(Integer productType) {
        if (productType == null) {
            return false;
        }
        if (PRODUCT.getCode().equals(productType) || MEDICINE.getCode().equals(productType)
                || CHINESE.getCode().equals(productType) || AGREEMENT.getCode().equals(productType)) {
            return true;
        }
        return false;
    }

    public static ProductTypeEnum getEnumByCode(Integer code) {
        return Stream.of(ProductTypeEnum.values())
                .filter(productTypeEnum -> Objects.equals(productTypeEnum.getCode(), code))
                .findFirst().orElse(null);
    }

    public static String getValueByCode(Integer code) {
        return Stream.of(ProductTypeEnum.values())
                .filter(productTypeEnum -> Objects.equals(productTypeEnum.getCode(), code))
                .map(ProductTypeEnum::getValue)
                .findFirst()
                .orElse("");
    }
}
