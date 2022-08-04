package com.drstrong.health.product.model.enums;


/**
 * 店铺类型枚举类
 *
 * @Author xieYueFeng
 * @Date 2022/07/25/17:00
 */

public enum StoreTypeEnum {
    /**
     * 互联网医院
     */
    INT_HOSPITAL(0,"互联网医院"),
    /**
     * 其它
     */
    OTHER(1,"其它");
    private final Integer code;

    private final String value;

    StoreTypeEnum(Integer code, String value) {
        this.code = code;
        this.value = value;
    }
    public Integer getCode(){
        return code;
    }

    public String getValue(){
        return value;
    }

    public static Integer nameToCode(String name){
        for (StoreTypeEnum value : StoreTypeEnum.values()) {
            boolean equals = value.value.equals(name);
            if(equals) {
                return value.code;
            }
        }
        return null;
    }

}
