package com.drstrong.health.product.service.medicine;

import com.baomidou.mybatisplus.extension.service.IService;
import com.drstrong.health.product.model.entity.medication.WesternMedicineInstructionsEntity;
import com.drstrong.health.product.model.request.medicine.AddOrUpdateMedicineRequest;
import com.drstrong.health.product.model.request.medicine.MedicineInstructionsRequest;

/**
 * <p>
 * 西/成药品说明 服务类
 * </p>
 *
 * @author zzw
 * @since 2023-06-07
 */
public interface WesternMedicineInstructionsService extends IService<WesternMedicineInstructionsEntity> {


    /**
     * 保存、更新药品说明
     *
     * @param medicineInstructionsRequest
     */
    void saveOrUpdateInstructions(MedicineInstructionsRequest medicineInstructionsRequest);


    /**
     * 通过药品id查询说明
     *
     * @param medicineId
     * @return
     */
    WesternMedicineInstructionsEntity queryByMedicineId(Long medicineId);
}
