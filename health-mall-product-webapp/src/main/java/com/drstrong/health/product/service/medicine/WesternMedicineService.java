package com.drstrong.health.product.service.medicine;

import com.baomidou.mybatisplus.extension.service.IService;
import com.drstrong.health.product.model.entity.medication.WesternMedicineEntity;

/**
 * <p>
 * 西/成药品库 服务类
 * </p>
 *
 * @author zzw
 * @since 2023-06-07
 */
public interface WesternMedicineService extends IService<WesternMedicineEntity> {

    void saveOrUpdateMedicine();
}
