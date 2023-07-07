package com.drstrong.health.product.model.entity.dict;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.drstrong.health.product.model.entity.category.BaseStandardEntity;
import lombok.Data;

import java.io.Serializable;

/**
 * huangpeng
 * 2023/6/9 10:59
 */
@Data
@TableName("mall_dict")
public class DictEntity extends BaseStandardEntity implements Serializable {

    private static final long serialVersionUID = -6225375236337076010L;

    /**
     * 自增主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

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

    /**
     * 字典排序
     */
    private Integer dictSort;

}
