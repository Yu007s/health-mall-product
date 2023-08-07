package com.drstrong.health.product.model.enums;

import com.google.common.collect.Sets;

import java.util.Objects;
import java.util.Set;

/**
 * 上下架的枚举
 *
 * @author liuqiuyi
 * @date 2021/12/16 10:10
 */
public enum UpOffEnum {
    /**
     *
     */
    DOWN(0, "已下架"),
    UP(1, "已上架"),
    SCHEDULED_UP(2, "预约上架中"),
    SCHEDULED_DOWN(3, "预约下架中");

    private Integer code;

    private String value;

    /**
     * 获取上架状态
     * <p> 已上架、预约下架中  </>
     *
     * @author liuqiuyi
     * @date 2023/7/17 10:26
     */
    public static Set<Integer> getIsUpCode() {
        return Sets.newHashSet(UP.code, SCHEDULED_DOWN.code);
    }

    /**
     * 获取下架状态
     * <p> 已下架、预约上架中  </>
     *
     * @author liuqiuyi
     * @date 2023/7/17 10:26
     */
    public static Set<Integer> getIsDownCode() {
        return Sets.newHashSet(DOWN.code, SCHEDULED_UP.code);
    }

    /**
     * 校验是否为上架状态
     * <p> 已上架、预约下架中  </>
     *
     * @author liuqiuyi
     * @date 2023/7/17 10:24
     */
    public static Boolean checkIsUp(Integer code) {
        return Objects.equals(UP.code, code) || Objects.equals(SCHEDULED_DOWN.code, code);
    }

    /**
     * 校验是否为下架状态
     * <p> 已下架、预约上架中  </>
     *
     * @author liuqiuyi
     * @date 2023/7/17 10:24
     */
    public static Boolean checkIsDown(Integer code) {
        return Objects.equals(DOWN.code, code) || Objects.equals(SCHEDULED_UP.code, code);
    }

    public static UpOffEnum getEnumByCode(Integer code) {
        if (Objects.isNull(code)) {
            return null;
        }
        for (UpOffEnum upOffEnum : UpOffEnum.values()) {
            if (Objects.equals(upOffEnum.getCode(), code)) {
                return upOffEnum;
            }
        }
        return null;
    }

    public static String getValueByCode(Integer code) {
        for (UpOffEnum upOffEnum : UpOffEnum.values()) {
            if (Objects.equals(upOffEnum.getCode(), code)) {
                return upOffEnum.getValue();
            }
        }
        return null;
    }

    UpOffEnum(Integer code, String value) {
        this.code = code;
        this.value = value;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
