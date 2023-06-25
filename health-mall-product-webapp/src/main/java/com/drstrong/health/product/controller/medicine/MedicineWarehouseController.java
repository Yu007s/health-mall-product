package com.drstrong.health.product.controller.medicine;

import com.drstrong.health.product.facade.medicine.MedicineWarehouseFacadeHolder;
import com.drstrong.health.product.model.dto.medicine.AgreementPrescriptionMedicineBaseDTO;
import com.drstrong.health.product.model.dto.medicine.ChineseMedicineBaseDTO;
import com.drstrong.health.product.model.dto.medicine.WesternMedicineBaseDTO;
import com.drstrong.health.product.model.request.medicine.MedicineWarehouseQueryRequest;
import com.drstrong.health.product.model.response.PageVO;
import com.drstrong.health.product.model.response.result.ResultVO;
import com.drstrong.health.product.remote.api.medicine.MedicineWarehouseRemoteApi;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @author liuqiuyi
 * @date 2023/6/20 11:02
 */
@RestController
@RequestMapping("/inner/product/medicine/warehouse")
public class MedicineWarehouseController implements MedicineWarehouseRemoteApi {

    @Resource
    MedicineWarehouseFacadeHolder medicineWarehouseFacadeHolder;

    @Override
    public ResultVO<PageVO<WesternMedicineBaseDTO>> pageWesternQuery(@Valid MedicineWarehouseQueryRequest medicineWarehouseQueryRequest) {
        return ResultVO.success(medicineWarehouseFacadeHolder.getMedicineWarehouseFacade(medicineWarehouseQueryRequest.getProductType())
                .pageQuery(medicineWarehouseQueryRequest));
    }

    @Override
    public ResultVO<PageVO<AgreementPrescriptionMedicineBaseDTO>> pageAgreementQuery(@Valid MedicineWarehouseQueryRequest medicineWarehouseQueryRequest) {
        return ResultVO.success(medicineWarehouseFacadeHolder.getMedicineWarehouseFacade(medicineWarehouseQueryRequest.getProductType())
                .pageQuery(medicineWarehouseQueryRequest));
    }

    @Override
    public ResultVO<PageVO<ChineseMedicineBaseDTO>> pageChineseQuery(@Valid MedicineWarehouseQueryRequest medicineWarehouseQueryRequest) {
        return ResultVO.success(medicineWarehouseFacadeHolder.getMedicineWarehouseFacade(medicineWarehouseQueryRequest.getProductType())
                .pageQuery(medicineWarehouseQueryRequest));
    }
}
