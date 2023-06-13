package com.drstrong.health.product.service.medicine.impl;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.drstrong.health.product.dao.medicine.AgreementPrescriptionMedicineMapper;
import com.drstrong.health.product.model.entity.medication.AgreementPrescriptionMedicineEntity;
import com.drstrong.health.product.model.request.medicine.AddOrUpdateAgreementRequest;
import com.drstrong.health.product.model.response.medicine.AgreementPrescriptionInfoVO;
import com.drstrong.health.product.service.medicine.AgreementPrescriptionMedicineService;
import com.drstrong.health.product.service.medicine.MedicineUsageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

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

    @Autowired
    private MedicineUsageService medicineUsageService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long saveOrUpdateAgreementPrescription(AddOrUpdateAgreementRequest request) {
        AgreementPrescriptionMedicineEntity prescriptionMedicineEntity = BeanUtil.copyProperties(request, AgreementPrescriptionMedicineEntity.class);
        prescriptionMedicineEntity.setImageInfo(JSONUtil.parse(request.getImageInfoList()).toString());
        prescriptionMedicineEntity.setChangedBy(request.getUserId());
        prescriptionMedicineEntity.setChangedAt(LocalDateTime.now());
        if (ObjectUtil.isNull(request.getId())) {
            prescriptionMedicineEntity.setCreatedBy(request.getUserId());
        }
        saveOrUpdate(prescriptionMedicineEntity);
        request.getMedicineUsage().setRelationId(prescriptionMedicineEntity.getId());
        medicineUsageService.saveOrUpdateUsage(request.getMedicineUsage());
        return prescriptionMedicineEntity.getId();
    }

    @Override
    public AgreementPrescriptionInfoVO queryAgreementPrescriptionInfo(Long id) {
        return null;
    }
}
