package com.drstrong.health.product.controller.medicine;


import cn.hutool.core.lang.Pair;
import com.drstrong.health.product.model.enums.ProductTypeEnum;
import com.drstrong.health.product.model.request.medicine.AddOrUpdateMedicineRequest;
import com.drstrong.health.product.model.request.medicine.AddOrUpdateMedicineSpecRequest;
import com.drstrong.health.product.model.request.medicine.WesternMedicineRequest;
import com.drstrong.health.product.model.response.PageVO;
import com.drstrong.health.product.model.response.medicine.*;
import com.drstrong.health.product.model.response.result.ResultVO;
import com.drstrong.health.product.remote.api.medicine.WesternMedicineRemoteApi;
import com.drstrong.health.product.service.medicine.WesternMedicineService;
import com.drstrong.health.product.service.medicine.WesternMedicineSpecificationsService;
import com.drstrong.health.product.utils.ChangeEventSendUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
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

    @Resource
    ChangeEventSendUtil changeEventSendUtil;

    @Override
    public ResultVO<Long> saveOrUpdateMedicine(@RequestBody @Valid AddOrUpdateMedicineRequest medicineRequest) {
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
        Pair<Long, String> idSpecCodePair = specificationsService.saveOrUpdateMedicineSpec(specRequest);
        changeEventSendUtil.sendMedicineWarehouseChangeEvent(idSpecCodePair.getValue(), ProductTypeEnum.MEDICINE);
        return ResultVO.success(idSpecCodePair.getKey());
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

    @Override
    public ResultVO<PageVO<WesternMedicineSpecVO>> queryMedicineSpecInfoPage(WesternMedicineRequest medicineRequest) {
        return ResultVO.success(specificationsService.queryMedicineSpecInfoPage(medicineRequest));
    }
}

