package com.drstrong.health.product.model.entity.chinese;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.drstrong.health.product.model.entity.category.BaseEntity;
import com.drstrong.health.product.model.entity.category.BaseStandardEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 中药材冲反库
 * </p>
 *
 * @author xieYueFeng
 * @since 2022-07-207 17:12:26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("product_chinese_medicine_conflict")
public class ChineseMedicineConflictEntity extends BaseStandardEntity implements Serializable {

    private static final long serialVersionUID = 4535146937887478478L;

    /**
     * 主键
     **/
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 中药材code
     **/
    private String medicineCode;

    /**
     * 中药材相反药材code，以”,“分开
     **/
    private String medicineConflictCodes;

}
