package com.drstrong.health.product.controller.medicine;

import com.drstrong.health.product.model.request.medicine.AddOrUpdateAgreementRequest;
import com.drstrong.health.product.model.response.medicine.AgreementPrescriptionInfoVO;
import com.drstrong.health.product.model.response.result.ResultVO;
import com.drstrong.health.product.remote.api.medicine.AgreementPrescriptionRemoteApi;
import com.drstrong.health.product.service.medicine.AgreementPrescriptionMedicineService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 协定方(预制) 前端控制器
 * </p>
 *
 * @author zzw
 * @since 2023-06-13
 */
@RestController
@RequestMapping("/inner/product/agreement/prescription/medicine")
@Slf4j
public class AgreementPrescriptionMedicineController implements AgreementPrescriptionRemoteApi {

    @Autowired
    private AgreementPrescriptionMedicineService agreementPrescriptionMedicineService;

    @Override
    public ResultVO<Long> saveOrUpdateAgreementPrescription(AddOrUpdateAgreementRequest request) {
        return null;
    }

    @Override
    public ResultVO<AgreementPrescriptionInfoVO> queryAgreementPrescriptionInfo(Long id) {
        return null;
    }
}

