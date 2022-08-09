package com.drstrong.health.product.remote.api.chinese;

import com.drstrong.health.product.model.request.chinese.ChineseMedicineSearchRequest;
import com.drstrong.health.product.model.response.chinese.ChineseMedicineInfoResponse;
import com.drstrong.health.product.model.response.chinese.ChineseMedicineResponse;
import com.drstrong.health.product.model.response.chinese.ChineseMedicineSearchVO;
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

    /**
     * 新建、编辑药材  药材名字不允许重复
     *
     * @param chineseMedicineVO 药材相关信息
     * @return 相应信息（成功、失败）
     */
    @ApiOperation("新建/编辑药材")
    @PostMapping("/save")
    ResultVO<String> addMedicine(@RequestBody @Valid ChineseMedicineVO chineseMedicineVO);

    /**
     * 删除药材
     *
     * @param medicineCode 药材编码
     * @param userId 用户id
     * @return 相应信息（成功、失败）
     */
    @ApiOperation("删除药材")
    @DeleteMapping("/delete")
    ResultVO<String> deleteMedicine(@RequestParam("medicineCode") @NotBlank(message = "药材编码不能为空") String medicineCode,
                                    @RequestParam(value = "userId") @NotNull(message = "用户id不能为空") Long userId);
    /**
     *
     * 药材信息分页展示
     * @param chineseMedicineSearchRequest 请求体
     * @return 返回展示页
     */
    @ApiOperation("药材信息分页展示")
    @PostMapping("/searchList")
    ResultVO<ChineseMedicineSearchVO> queryMedicinePage(@RequestBody ChineseMedicineSearchRequest chineseMedicineSearchRequest);

    /**
     *
     * 所有药材查询  用于新增药材添加相反药材时检索所有药材
     * @param chineseMedicineSearchRequest 请求体
     * @return 药材名字 药材编码
     */
    @ApiOperation("所有药材查询")
    @PostMapping("/searchAll")
    ResultVO<List<ChineseMedicineInfoResponse>> queryMedicineAll(@RequestBody ChineseMedicineSearchRequest chineseMedicineSearchRequest);

    /**
     * 相反药材查询
     * @param medicineCode  药材编码
     * @return 相反药材信息
     */
    @ApiOperation("相反药材信息分查询")
    @GetMapping("/conflictList")
    ResultVO<List<ChineseMedicineResponse>> queryConflictMedicine(@RequestParam("medicineCode") @NotBlank(message = "药材编码不能为空") String medicineCode);
}
