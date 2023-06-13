package com.drstrong.health.product.service.medicine;

import com.baomidou.mybatisplus.extension.service.IService;
import com.drstrong.health.product.model.entity.medication.AgreementPrescriptionMedicineEntity;
import com.drstrong.health.product.model.request.medicine.AddOrUpdateAgreementRequest;
import com.drstrong.health.product.model.response.medicine.AgreementPrescriptionInfoVO;

/**
 * <p>
 * 协定方(预制) 服务类
 * </p>
 *
 * @author zzw
 * @since 2023-06-13
 */
public interface AgreementPrescriptionMedicineService extends IService<AgreementPrescriptionMedicineEntity> {

    /**
     * 保存/修改协定方
     *
     * @param request
     */
    Long saveOrUpdateAgreementPrescription(AddOrUpdateAgreementRequest request);


    /**
     * 协定方详情
     * @param id
     * @return
     */
    AgreementPrescriptionInfoVO queryAgreementPrescriptionInfo(Long id);
}
