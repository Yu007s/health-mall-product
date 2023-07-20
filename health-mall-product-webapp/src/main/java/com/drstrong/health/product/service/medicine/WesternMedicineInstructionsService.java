package com.drstrong.health.product.service.medicine;

import com.baomidou.mybatisplus.extension.service.IService;
import com.drstrong.health.product.model.entity.medication.WesternMedicineInstructionsEntity;
import com.drstrong.health.product.model.request.medicine.AddOrUpdateMedicineRequest;
import com.drstrong.health.product.model.request.medicine.MedicineInstructionsRequest;

import java.util.List;

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
     * @param medicineRequest
     */
    void saveOrUpdateInstructions(AddOrUpdateMedicineRequest medicineRequest);


    /**
     * 通过药品id查询说明
     *
     * @param medicineId
     * @return
     */
    WesternMedicineInstructionsEntity queryByMedicineId(Long medicineId);

    List<WesternMedicineInstructionsEntity> queryByMedicineIdList(List<Long> medicineIdList);
}
