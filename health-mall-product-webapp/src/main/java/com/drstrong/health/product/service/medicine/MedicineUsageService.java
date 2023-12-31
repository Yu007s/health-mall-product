package com.drstrong.health.product.service.medicine;

import com.baomidou.mybatisplus.extension.service.IService;
import com.drstrong.health.product.model.entity.medication.MedicineUsageEntity;
import com.drstrong.health.product.model.request.medicine.AddOrUpdateMedicineSpecRequest;
import com.drstrong.health.product.model.request.medicine.MedicineUsageRequest;

/**
 * <p>
 * 药品规格用法用量 服务类
 * </p>
 *
 * @author zzw
 * @since 2023-06-07
 */
public interface MedicineUsageService extends IService<MedicineUsageEntity> {


    /**
     * 新增-修改药品规格用法用量
     *
     * @param medicineUsageRequest
     */
    void saveOrUpdateUsage(MedicineUsageRequest medicineUsageRequest);


    /**
     * 取消用法用量
     *
     * @param relationId
     * @param relationType
     */
    void cancelMedicineUsage(Long relationId, Integer relationType);


    /**
     * 查询药品规格用法用量
     *
     * @param relationId
     * @param relationType
     * @return
     */
    MedicineUsageEntity getMedicineUsageBySpecId(Long relationId, Integer relationType);

}
