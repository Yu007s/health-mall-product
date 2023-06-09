package com.drstrong.health.product.model.dto;

import lombok.Data;

/**
 * huangpeng
 * 2023/6/9 14:38
 */
@Data
public class DictDTO {

    /**
     * 字典类型
     */
    private String dictType;

    /**
     * 字典名称
     */
    private String dictName;

    /**
     * 字典编码
     */
    private String dictCode;

    /**
     * 字典值
     */
    private String dictValue;

    /**
     * 字典标签（简写）
     */
    private String dictLable;
}
