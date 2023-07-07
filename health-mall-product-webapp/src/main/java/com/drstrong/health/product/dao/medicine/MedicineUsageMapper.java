package com.drstrong.health.product.dao.medicine;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.drstrong.health.product.model.entity.medication.MedicineUsageEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 药品规格用法用量 Mapper 接口
 * </p>
 *
 * @author zzw
 * @since 2023-06-07
 */
@Mapper
public interface MedicineUsageMapper extends BaseMapper<MedicineUsageEntity> {

}
