package com.drstrong.health.product.model.entity.chinese;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import cn.strong.mybatis.model.MyBatisPlusBaseModel;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

import com.drstrong.health.product.model.entity.category.BaseEntity;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * <p>
 * 中药 spu 信息
 * </p>
 *
 * @author mybatis plus generator
 * @since 2022-08-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("pms_chinese_spu_info")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChineseSpuInfoEntity extends BaseEntity implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * spu编码
     */
    private String spuCode;

    /**
     * spu名称
     */
    private String spuName;

    /**
     * 药材库code
     */
    @TableField("medicineCode")
    private String medicineCode;

    /**
     * 版本号
     */
    private Integer version;
}
