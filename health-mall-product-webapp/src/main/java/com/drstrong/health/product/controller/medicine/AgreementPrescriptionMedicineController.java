package com.drstrong.health.product.controller.medicine;

import cn.hutool.core.lang.Pair;
import com.drstrong.health.product.model.enums.ProductTypeEnum;
import com.drstrong.health.product.model.request.medicine.AddOrUpdateAgreementRequest;
import com.drstrong.health.product.model.request.medicine.WesternMedicineRequest;
import com.drstrong.health.product.model.response.PageVO;
import com.drstrong.health.product.model.response.medicine.AgreementPrescriptionInfoVO;
import com.drstrong.health.product.model.response.medicine.AgreementPrescriptionSimpleInfoVO;
import com.drstrong.health.product.model.response.result.ResultVO;
import com.drstrong.health.product.remote.api.medicine.AgreementPrescriptionRemoteApi;
import com.drstrong.health.product.service.medicine.AgreementPrescriptionMedicineService;
import com.drstrong.health.product.utils.ChangeEventSendUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

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

    @Resource
    ChangeEventSendUtil changeEventSendUtil;

    @Override
    public ResultVO<Long> saveOrUpdateAgreementPrescription(AddOrUpdateAgreementRequest request) {
        Pair<Long, String> idCodePair = agreementPrescriptionMedicineService.saveOrUpdateAgreementPrescription(request);
        changeEventSendUtil.sendMedicineWarehouseChangeEvent(idCodePair.getValue(), ProductTypeEnum.AGREEMENT);
        return ResultVO.success(idCodePair.getKey());
    }

    @Override
    public ResultVO<AgreementPrescriptionInfoVO> queryAgreementPrescriptionInfo(Long id) {
        return ResultVO.success(agreementPrescriptionMedicineService.queryAgreementPrescriptionInfo(id));
    }

    @Override
    public ResultVO<PageVO<AgreementPrescriptionSimpleInfoVO>> queryAgreementPrescriptionPageInfo(WesternMedicineRequest westernMedicineRequest) {
        return ResultVO.success(agreementPrescriptionMedicineService.queryAgreementPrescriptionPageInfo(westernMedicineRequest));
    }
}

