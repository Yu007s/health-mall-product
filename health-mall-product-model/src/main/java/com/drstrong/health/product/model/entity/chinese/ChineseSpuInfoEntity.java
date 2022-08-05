package com.drstrong.health.product.model.entity.chinese;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.drstrong.health.product.model.entity.category.BaseStandardEntity;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;

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
public class ChineseSpuInfoEntity extends BaseStandardEntity implements Serializable {

    private static final long serialVersionUID = 1L;

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
    private String medicineCode;

    /**
     * 店铺id
     */
    private Long storeId;
}
