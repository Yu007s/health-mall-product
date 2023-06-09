package com.drstrong.health.product.controller.medicine;


import com.drstrong.health.product.model.request.medicine.AddOrUpdateMedicineRequest;
import com.drstrong.health.product.model.request.medicine.WesternMedicineRequest;
import com.drstrong.health.product.model.response.PageVO;
import com.drstrong.health.product.model.response.medicine.WesternMedicineVO;
import com.drstrong.health.product.model.response.result.ResultVO;
import com.drstrong.health.product.remote.api.medicine.WesternMedicineRemoteApi;
import com.drstrong.health.product.service.medicine.WesternMedicineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * <p>
 * 西/成药品库 前端控制器
 * </p>
 *
 * @author zzw
 * @since 2023-06-07
 */
@RestController
@RequestMapping("/inner/product/western/medicine")
public class WesternMedicineController implements WesternMedicineRemoteApi {

    @Autowired
    private WesternMedicineService westernMedicineService;

    @Override
    public ResultVO<Void> saveOrUpdateMedicine(@Valid AddOrUpdateMedicineRequest medicineRequest) {
        westernMedicineService.saveOrUpdateMedicine(medicineRequest);
        return ResultVO.success();
    }

    @Override
    public ResultVO<Void> queryMedicineDetailInfo(Long id) {
        return null;
    }

    @Override
    public ResultVO<PageVO<WesternMedicineVO>> queryMedicinePageInfo(WesternMedicineRequest westernMedicineRequest) {
        return ResultVO.success(westernMedicineService.queryMedicinePageList(westernMedicineRequest));
    }
}

