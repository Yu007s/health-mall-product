package com.drstrong.health.product.model.response.chinese;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @Author xieYueFeng
 * @Date 2022/07/27/10:14
 */
@Data
@ApiModel("中药材库新增、编辑页面展示信息")
public class ChineseMedicineVO implements Serializable {
    private static final long serialVersionUID = 5341894162186315254L;
    /**
     * 中药材编号
     */
    private Long id;

    /**
     * 药材名称
     */
    @NotNull
    private String name;

    /**
     * 药材别名
     */
    private List<String> aliName;

    /**
     * 最大剂量
     */
    @NotNull
    private BigDecimal maxDosage;
    /**
     * 判断是否可以删除标志
     */
    private Integer canDelete;
}
