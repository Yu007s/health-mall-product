package com.drstrong.health.product.util;

import java.math.BigDecimal;

public class BigDecimalUtil {

    static BigDecimal hundred = new BigDecimal("100");
    static BigDecimal thousand = new BigDecimal("1000");


    public static Long Y2F(BigDecimal bigDecimal) {
        return bigDecimal.multiply(hundred).longValue();
    }

    public static BigDecimal F2Y(Long fen) {
        BigDecimal yuan = BigDecimal.valueOf(fen).divide(hundred);
        return yuan.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    public static Long g2mg(BigDecimal bigDecimal) {
        return bigDecimal.multiply(thousand).longValue();
    }

    public static BigDecimal mg2g(Long fen) {
        BigDecimal yuan = BigDecimal.valueOf(fen).divide(thousand);
        return yuan.setScale(2, BigDecimal.ROUND_HALF_UP);
    }
}
