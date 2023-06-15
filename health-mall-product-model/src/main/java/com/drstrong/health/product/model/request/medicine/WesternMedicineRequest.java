package com.drstrong.health.product.model.request.medicine;

import com.drstrong.health.product.model.request.PageRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;


@ApiModel("西药搜索参数")
@Data
@EqualsAndHashCode(callSuper = true)
public class WesternMedicineRequest extends PageRequest implements Serializable {

    private static final long serialVersionUID = 8036569750867670896L;

    @ApiModelProperty(value = "药品id")
    private Long id;

    @ApiModelProperty(value = "药品编码")
    private String medicineCode;

    @ApiModelProperty(value = "药品名称/通用名/药品编码")
    private String searchName;

    @ApiModelProperty(value = "药品分类id")
    private Long drugClassificationId;

    @ApiModelProperty(value = "药理分类id")
    private Long pharmacologyClassificationId;

    @ApiModelProperty(value = "关联规格")
    private Integer relationSpec;

    @ApiModelProperty(value = "资料完整")
    private Integer dataIntegrity;

    @ApiModelProperty(value = "是否默认用法用量")
    private Integer defaultUsageDosage;

    @ApiModelProperty("创建药品开始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate createStart;

    @ApiModelProperty("创建药品结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate createEnd;

    /**
     * 药品ids 导出使用
     */
    @ApiModelProperty(value = "药品ids，导出使用")
    private List<Long> medicineIds;
}
