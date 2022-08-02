package com.drstrong.health.product.model.entity.chinese;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.drstrong.health.product.model.entity.category.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 中药材别名表
 * </p>
 *
 * @author xieYueFeng
 * @since 2022-07-207 17:12:26
 */
@Data
@TableName("chinese_medicine_alias")
@EqualsAndHashCode(callSuper = true)
public class ChineseMedicineAliasEntity extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 4548784478951378478L;

   /**
    * 主键
    * */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

   /**
    * 中药材id
    * */
    private Long medicineId;

   /**
    * 别名名称
    * */
    private String aliasName;

   /**
    * 汉语简拼
    * */
    private String pinyin;

}
