package com.drstrong.health.product.model.response.chinese;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
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

    @ApiModelProperty("中药材编码")
    private String medicineCode;

    @NotBlank(message = "药材名称不能为空")
    @ApiModelProperty("药材名称")
    private String name;

    @ApiModelProperty("药材别名列表")
    private String aliNames;

    @ApiModelProperty("药材相反药材编码列表")
    private List<String> conflictMedicineCodes;

    @NotNull(message = "最大剂量不为空")
    @ApiModelProperty("最大剂量")
    private BigDecimal maxDosage;

    @ApiModelProperty("用户id")
    private Long userId;


    /**
     * 剂型 0-配方颗粒 1-饮片 默认0
     */
    @NotNull(message = "剂型不能为空")
    @ApiModelProperty("剂型")
    private Integer dosageForm;


}
