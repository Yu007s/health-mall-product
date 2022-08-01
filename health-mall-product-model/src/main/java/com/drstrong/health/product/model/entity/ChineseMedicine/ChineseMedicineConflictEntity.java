package com.drstrong.health.product.model.entity.ChineseMedicine;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.drstrong.health.product.model.entity.category.BaseEntity;
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
@TableName("chinese_medicine_conflict")
public class ChineseMedicineConflictEntity extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 4535146937887478478L;

   /**主键**/
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

   /**中药材ID**/
    private Long chineseMedicineId;

   /**中药材冲反ID**/
    private Long chineseMedicineConflictId;

   /**乐观锁**/
    private Integer version;
}
