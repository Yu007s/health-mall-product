package com.drstrong.health.product.service.medicine;

import com.baomidou.mybatisplus.extension.service.IService;
import com.drstrong.health.product.model.entity.medication.WesternMedicineSpecificationsEntity;
import com.drstrong.health.product.model.request.medicine.AddOrUpdateMedicineSpecRequest;
import com.drstrong.health.product.model.response.medicine.WesternMedicineSpecInfoVO;

/**
 * <p>
 * 西药药品规格 服务类
 * </p>
 *
 * @author zzw
 * @since 2023-06-07
 */
public interface WesternMedicineSpecificationsService extends IService<WesternMedicineSpecificationsEntity> {

    /**
     * 保存/修改西药
     *
     * @param specRequest
     */
    Long saveOrUpdateMedicineSpec(AddOrUpdateMedicineSpecRequest specRequest);

    /**
     * 规格详情
     *
     * @param id
     * @return
     */
    WesternMedicineSpecInfoVO queryMedicineDetailInfo(Long id);
}
