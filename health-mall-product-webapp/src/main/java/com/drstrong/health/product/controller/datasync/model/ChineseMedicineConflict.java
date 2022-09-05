package com.drstrong.health.product.controller.datasync.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 中药材冲反库
 * </p>
 *
 * @author xieYueFeng
 * @since 2022-07-207 17:12:26
 */
@Getter
@Setter
@TableName("chinese_medicine_conflict")
@ApiModel(value = "ChineseMedicineConflict对象", description = "中药材冲反库")
public class ChineseMedicineConflict implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("中药材ID")
    @TableField("chinese_medicine_id")
    private Long chineseMedicineId;

    @ApiModelProperty("中药材冲反ID")
    @TableField("chinese_medicine_conflict_id")
    private Long chineseMedicineConflictId;

    @ApiModelProperty("创建时间")
    @TableField("created_at")
    private LocalDateTime createdAt;

    @ApiModelProperty("创建人")
    @TableField("created_by")
    private String createdBy;

    @ApiModelProperty("修改时间")
    @TableField("changed_at")
    private LocalDateTime changedAt;

    @ApiModelProperty("修改人")
    @TableField("changed_by")
    private String changedBy;

    @ApiModelProperty("乐观锁")
    @TableField("version")
    private Integer version;

    @ApiModelProperty(" '作废状态：0 正常 1 作废'")
    @TableField("invalid")
    private Integer invalid;


}
