package com.drstrong.health.product.dao.medicine;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.drstrong.health.product.model.entity.medication.MedicineClassificationEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 药品基础分类 Mapper 接口
 * </p>
 *
 * @author zzw
 * @since 2023-06-06
 */
@Mapper
public interface MedicineClassificationMapper extends BaseMapper<MedicineClassificationEntity> {

}
