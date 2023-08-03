package com.drstrong.health.product.controller.chinese;


import com.drstrong.health.product.model.enums.ProductTypeEnum;
import com.drstrong.health.product.model.request.chinese.ChineseMedicineSearchRequest;
import com.drstrong.health.product.model.response.chinese.ChineseMedicineInfoResponse;
import com.drstrong.health.product.model.response.chinese.ChineseMedicineResponse;
import com.drstrong.health.product.model.response.chinese.ChineseMedicineSearchVO;
import com.drstrong.health.product.model.response.chinese.ChineseMedicineVO;
import com.drstrong.health.product.model.response.result.ResultVO;
import com.drstrong.health.product.remote.api.chinese.ChineseMedicineRemoteApi;
import com.drstrong.health.product.remote.model.SkuChineseAgencyDTO;
import com.drstrong.health.product.service.chinese.ChineseMedicineService;
import com.drstrong.health.product.service.chinese.ChineseSkuInfoService;
import com.drstrong.health.product.utils.ChangeEventSendUtil;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.List;

/**
 * @Author xieYueFeng
 * @Date 2022/07/30/10:12
 */
@Validated
@Slf4j
@RestController
@RequestMapping("/inner/product/chinese/medicine")
public class ChineseMedicineController implements ChineseMedicineRemoteApi {
    @Resource
    private ChineseMedicineService chineseMedicineService;
    @Resource
    private ChineseSkuInfoService chineseSkuInfoService;

    @Resource
    ChangeEventSendUtil changeEventSendUtil;

    @Override
    @ApiOperation("新建/编辑药材")
    public ResultVO<String> addMedicine(@RequestBody @Valid ChineseMedicineVO chineseMedicineVO)  {
        String medicineCode = chineseMedicineService.saveOrUpdate(chineseMedicineVO);
        changeEventSendUtil.sendMedicineWarehouseChangeEvent(medicineCode, ProductTypeEnum.CHINESE);
        return ResultVO.success("成功");
    }

    @Override
    @ApiOperation("删除药材")
    public ResultVO<String> deleteMedicine(@RequestParam("medicineCode") @NotBlank(message = "药材编码不能为空")String medicineCode,
                                    @RequestParam(value = "userId") @NotNull(message = "用户id不能为空") Long userId) {
        chineseMedicineService.removeByCode(medicineCode, userId);
        return ResultVO.success();
    }



    @Override
    @ApiOperation("药材信息分页展示")
    public ResultVO<ChineseMedicineSearchVO> queryMedicinePage(@RequestBody ChineseMedicineSearchRequest chineseMedicineSearchRequest) {
        ChineseMedicineSearchVO chineseMedicineSearchVO = chineseMedicineService.queryPage(chineseMedicineSearchRequest.getMedicineCode(),
                chineseMedicineSearchRequest.getMedicineName(), chineseMedicineSearchRequest.getPageNo(), chineseMedicineSearchRequest.getPageSize());
        return ResultVO.success(chineseMedicineSearchVO);
    }

    @Override
    public ResultVO<List<ChineseMedicineResponse>> queryMedicineExport(ChineseMedicineSearchRequest chineseMedicineSearchRequest) {
        return ResultVO.success(chineseMedicineService.queryMedicineExport(chineseMedicineSearchRequest.getMedicineCode(), chineseMedicineSearchRequest.getMedicineName()));
    }

    @ApiOperation("所有药材查询")
    @Override
    public ResultVO<List<ChineseMedicineInfoResponse>> queryMedicineAll(@RequestBody ChineseMedicineSearchRequest chineseMedicineSearchRequest) {
        List<ChineseMedicineInfoResponse> chineseMedicineInfoList = chineseMedicineService.queryAll(chineseMedicineSearchRequest.getMedicineName(),chineseMedicineSearchRequest.getMedicineCode());
        return ResultVO.success(chineseMedicineInfoList);
    }


    @Override
    @ApiOperation("相反药材信息展示")
    public ResultVO<List<ChineseMedicineResponse>> queryConflictMedicine(@RequestParam("medicineCode") @NotBlank(message="药材编码不能为空") String medicineCode) {
        List<ChineseMedicineResponse> chineseMedicineResponses = chineseMedicineService.queryForConflict(medicineCode);
        return ResultVO.success(chineseMedicineResponses);
    }

    @Override
    @ApiOperation("获取中药SKU")
    public ResultVO<List<SkuChineseAgencyDTO>> listSkuChineseAgencyDTO(Integer skuStatus, Collection<Long> medicineIds) {
        return ResultVO.success( chineseSkuInfoService.listSkuChineseAgencyDTO(skuStatus,medicineIds));
    }
}
