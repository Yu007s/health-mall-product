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
 * 中药材别名表
 * </p>
 *
 * @author xieYueFeng
 * @since 2022-07-207 17:12:26
 */
@Getter
@Setter
@TableName("chinese_medicine_alias")
@ApiModel(value = "ChineseMedicineAlias对象", description = "中药材别名表")
@Deprecated
public class ChineseMedicineAlias implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("中药材id")
    @TableField("medicine_id")
    private Long medicineId;

    @ApiModelProperty("别名名称")
    @TableField("name")
    private String name;

    @ApiModelProperty("汉语简拼")
    @TableField("pinyin")
    private String pinyin;

    @ApiModelProperty("创建时间")
    @TableField("created_at")
    private LocalDateTime createdAt;

    @ApiModelProperty("创建人")
    @TableField("created_by")
    private Long createdBy;

    @ApiModelProperty("修改时间")
    @TableField("changed_at")
    private LocalDateTime changedAt;

    @ApiModelProperty("修改人")
    @TableField("changed_by")
    private Long changedBy;


}
