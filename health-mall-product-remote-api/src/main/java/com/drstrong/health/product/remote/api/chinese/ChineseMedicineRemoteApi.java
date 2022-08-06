package com.drstrong.health.product.remote.api.chinese;

import com.drstrong.health.product.model.response.chinese.ChineseMedicineInfoResponse;
import com.drstrong.health.product.model.response.chinese.ChineseMedicineResponse;
import com.drstrong.health.product.model.response.chinese.ChineseMedicineVO;
import com.drstrong.health.product.model.response.result.ResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @Author xieYueFeng
 * @Date 2022/08/06/16:27
 */
@Api("健康商城-商品服务-安全用药中药材管理页面远程接口")
@FeignClient(value = "health-mall-product", path = "/product/chinese/medicine")
public interface ChineseMedicineRemoteApi {

    @ApiOperation("新建/编辑药材")
    @PostMapping("/save")
    ResultVO<String> addMedicine(@RequestBody @Valid ChineseMedicineVO chineseMedicineVO,
                                 @RequestParam(value = "userId") @NotNull(message = "用户id不能为空") Long userId) ;

    @ApiOperation("删除药材")
    @DeleteMapping("/delete")
    ResultVO<String> deleteMedicine(@RequestParam("medicineCode") @NotBlank(message = "药材编码不能为空") String medicineCode,
                                    @RequestParam(value = "userId") @NotNull(message = "用户id不能为空") Long userId);

    @ApiOperation("药材信息分页展示")
    @GetMapping("/searchList")
    ResultVO<List<ChineseMedicineResponse>> queryMedicinePage(@RequestParam(value = "medicineCode", required = false) String medicineCode, @RequestParam(value = "medicineName", required = false) String medicineName,
                                                                     @RequestParam(value = "pageNo") Integer pageNo, @RequestParam(value = "pageSize") Integer pageSize);

    @ApiOperation("所有药材查询")
    @GetMapping("/searchAll")
    ResultVO<List<ChineseMedicineInfoResponse>> queryMedicineAll(@RequestParam(value = "medicineName", required = false) String medicineName,
                                                                        @RequestParam(value = "medicineCode", required = false) String medicineCode);

    @ApiOperation("相反药材信息分页展示")
    @GetMapping("/conflictList")
    ResultVO<List<ChineseMedicineResponse>> queryConflictMedicine(@RequestParam("medicineCode") @NotBlank(message = "药材编码不能为空") String medicineCode,
                                                                         @RequestParam(value = "pageNo",required = false)  Integer pageNo, @RequestParam(value = "pageSize",required = false) Integer pageSize);
}
