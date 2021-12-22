package com.drstrong.health.product.util;

import java.math.BigDecimal;

public class BigDecimalUtil {

    static BigDecimal hundred = new BigDecimal("100");


    public static Long Y2F(BigDecimal bigDecimal) {
        return bigDecimal.multiply(hundred).longValue();
    }

    public static BigDecimal F2Y(Long fen) {
        BigDecimal yuan = BigDecimal.valueOf(fen);
        return yuan.setScale(2, BigDecimal.ROUND_HALF_UP);
    }
}
