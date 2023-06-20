package com.drstrong.health.product.controller.medicine;

import com.drstrong.health.product.facade.medicine.MedicineWarehouseFacadeHolder;
import com.drstrong.health.product.model.dto.medicine.MedicineWarehouseBaseDTO;
import com.drstrong.health.product.model.request.medicine.MedicineWarehouseQueryRequest;
import com.drstrong.health.product.model.response.PageVO;
import com.drstrong.health.product.model.response.result.ResultVO;
import com.drstrong.health.product.remote.api.medicine.MedicineWarehouseRemoteApi;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

//    @Override
    @PostMapping({"/page/query"})
    public ResultVO<PageVO<MedicineWarehouseBaseDTO>> pageQuery1(@RequestBody @Valid MedicineWarehouseQueryRequest medicineWarehouseQueryRequest) {
        PageVO<MedicineWarehouseBaseDTO> baseDTOPageVO = medicineWarehouseFacadeHolder.getMedicineWarehouseFacade(medicineWarehouseQueryRequest.getProductType()).pageQuery(medicineWarehouseQueryRequest);
        return ResultVO.success(baseDTOPageVO);
    }

    //    @Override
//    public <T extends MedicineWarehouseBaseDTO> ResultVO<PageVO<MedicineWarehouseBaseDTO>> pageQuery(@Valid MedicineWarehouseQueryRequest medicineWarehouseQueryRequest) {
//        PageVO<MedicineWarehouseBaseDTO> medicineWarehouseBaseDTOPageVO = medicineWarehouseFacadeHolder.getMedicineWarehouseFacade(medicineWarehouseQueryRequest.getProductType()).pageQuery(medicineWarehouseQueryRequest);
//        return ResultVO.success(medicineWarehouseBaseDTOPageVO);
//    }
}
