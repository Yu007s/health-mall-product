package com.drstrong.health.product.controller.medicine;


import com.drstrong.health.product.model.request.medicine.AddOrUpdateMedicineRequest;
import com.drstrong.health.product.model.request.medicine.AddOrUpdateMedicineSpecRequest;
import com.drstrong.health.product.model.request.medicine.WesternMedicineRequest;
import com.drstrong.health.product.model.response.PageVO;
import com.drstrong.health.product.model.response.medicine.*;
import com.drstrong.health.product.model.response.result.ResultVO;
import com.drstrong.health.product.remote.api.medicine.WesternMedicineRemoteApi;
import com.drstrong.health.product.service.medicine.WesternMedicineService;
import com.drstrong.health.product.service.medicine.WesternMedicineSpecificationsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 * 西/成药 前端控制器
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

    @Autowired
    private WesternMedicineSpecificationsService specificationsService;

    @Override
    public ResultVO<Long> saveOrUpdateMedicine(@Valid AddOrUpdateMedicineRequest medicineRequest) {
        return ResultVO.success(westernMedicineService.saveOrUpdateMedicine(medicineRequest));
    }

    @Override
    public ResultVO<WesternMedicineInfoVO> queryMedicineDetailInfo(Long id) {
        return ResultVO.success(westernMedicineService.queryMedicineDetailInfo(id));
    }

    @Override
    public ResultVO<PageVO<WesternMedicineVO>> queryMedicinePageInfo(WesternMedicineRequest westernMedicineRequest) {
        return ResultVO.success(westernMedicineService.queryMedicinePageList(westernMedicineRequest));
    }

    @Override
    public ResultVO<PageVO<WesternMedicineLogVO>> queryMedicineOperationLogByPage(WesternMedicineRequest westernMedicineRequest) {
        return ResultVO.success(westernMedicineService.queryMedicineOperationLogByPage(westernMedicineRequest));
    }

    @Override
    public ResultVO<Long> saveOrUpdateMedicineSpec(@Valid AddOrUpdateMedicineSpecRequest specRequest) {
        return ResultVO.success(specificationsService.saveOrUpdateMedicineSpec(specRequest));
    }

    @Override
    public ResultVO<WesternMedicineSpecInfoVO> queryMedicineSpecInfo(Long id) {
        return ResultVO.success(specificationsService.queryMedicineSpecDetailInfo(id));
    }

    @Override
    public ResultVO<WesternMedicineSimpleInfoVO> queryMedicineSpecByPage(WesternMedicineRequest westernMedicineRequest) {
        return ResultVO.success(specificationsService.queryMedicineSpecByPage(westernMedicineRequest));
    }

    @Override
    public ResultVO<List<WesternMedicineExcelVO>> queryMedicineExcelData(WesternMedicineRequest westernMedicineRequest) {
        return ResultVO.success(westernMedicineService.queryMedicineExcelData(westernMedicineRequest));
    }
}

