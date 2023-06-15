package com.drstrong.health.product.remote.api.medicine;

import com.drstrong.health.product.model.request.medicine.AddOrUpdateMedicineRequest;
import com.drstrong.health.product.model.request.medicine.AddOrUpdateMedicineSpecRequest;
import com.drstrong.health.product.model.request.medicine.WesternMedicineRequest;
import com.drstrong.health.product.model.response.PageVO;
import com.drstrong.health.product.model.response.medicine.*;
import com.drstrong.health.product.model.response.result.ResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.List;


/**
 * 西药的远程接口
 *
 * @author zzw
 * @date 2023/6/7 16:41
 */
@Api("健康商城-安全用药-西药目录")
@FeignClient(value = "health-mall-product", path = "/inner/product/western/medicine")
public interface WesternMedicineRemoteApi {


    @ApiOperation("保存/修改西药")
    @PostMapping("/save-or-update")
    ResultVO<Long> saveOrUpdateMedicine(@RequestBody @Valid AddOrUpdateMedicineRequest medicineRequest);


    @ApiOperation("西药详情")
    @GetMapping("/queryById")
    ResultVO<WesternMedicineInfoVO> queryMedicineDetailInfo(@RequestParam("id") Long id);


    @ApiOperation("西药分页列表")
    @PostMapping("/page/info")
    ResultVO<PageVO<WesternMedicineVO>> queryMedicinePageInfo(@RequestBody WesternMedicineRequest westernMedicineRequest);


    @ApiOperation("西药操作日志分页")
    @PostMapping("/operationLog/page/list")
    ResultVO<PageVO<WesternMedicineLogVO>> queryMedicineOperationLogByPage(@RequestBody WesternMedicineRequest westernMedicineRequest);


    @ApiOperation("保存/修改西药规格")
    @PostMapping("/spec/save-or-update")
    ResultVO<Long> saveOrUpdateMedicineSpec(@RequestBody @Valid AddOrUpdateMedicineSpecRequest specRequest);

    @ApiOperation("西药规格详情")
    @GetMapping("/spec/queryById")
    ResultVO<WesternMedicineSpecInfoVO> queryMedicineSpecInfo(@RequestParam("id") Long id);

    @ApiOperation("西药规格分页")
    @PostMapping("/spec/page/list")
    ResultVO<WesternMedicineSimpleInfoVO> queryMedicineSpecByPage(@RequestBody WesternMedicineRequest westernMedicineRequest);


    @ApiOperation("西药excel数据")
    @PostMapping("/excel/data")
    ResultVO<List<WesternMedicineExcelVO>> queryMedicineExcelData(@RequestBody WesternMedicineRequest westernMedicineRequest);
}
