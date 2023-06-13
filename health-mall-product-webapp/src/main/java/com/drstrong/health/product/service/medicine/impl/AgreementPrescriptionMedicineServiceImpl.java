package com.drstrong.health.product.service.medicine.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.drstrong.health.product.dao.medicine.AgreementPrescriptionMedicineMapper;
import com.drstrong.health.product.model.entity.medication.AgreementPrescriptionMedicineEntity;
import com.drstrong.health.product.model.request.medicine.AddOrUpdateAgreementRequest;
import com.drstrong.health.product.model.response.medicine.AgreementPrescriptionInfoVO;
import com.drstrong.health.product.service.medicine.AgreementPrescriptionMedicineService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 协定方(预制) 服务实现类
 * </p>
 *
 * @author zzw
 * @since 2023-06-13
 */
@Service
public class AgreementPrescriptionMedicineServiceImpl extends ServiceImpl<AgreementPrescriptionMedicineMapper, AgreementPrescriptionMedicineEntity> implements AgreementPrescriptionMedicineService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long saveOrUpdateAgreementPrescription(AddOrUpdateAgreementRequest request) {
        return null;
    }

    @Override
    public AgreementPrescriptionInfoVO queryAgreementPrescriptionInfo(Long id) {
        return null;
    }
}
