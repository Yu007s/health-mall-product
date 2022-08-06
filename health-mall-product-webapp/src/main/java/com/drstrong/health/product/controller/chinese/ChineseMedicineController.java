package com.drstrong.health.product.controller.chinese;


import com.drstrong.health.product.model.response.chinese.ChineseMedicineInfoResponse;
import com.drstrong.health.product.model.response.chinese.ChineseMedicineResponse;
import com.drstrong.health.product.model.response.chinese.ChineseMedicineVO;
import com.drstrong.health.product.model.response.result.ResultVO;
import com.drstrong.health.product.remote.api.chinese.ChineseMedicineFacade;
import com.drstrong.health.product.service.chinese.ChineseMedicineService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @Author xieYueFeng
 * @Date 2022/07/30/10:12
 */
@Validated
@Slf4j
@RestController
@RequestMapping("/product/chinese/medicine")
public class ChineseMedicineController implements ChineseMedicineFacade {
    @Resource
    private ChineseMedicineService chineseMedicineService;

    /**
     * 新建、编辑药材  药材名字不允许重复
     *
     * @param chineseMedicineVO 药材相关信息
     * @return 相应信息（成功、失败）
     */
    @ApiOperation("新建/编辑药材")
    @PostMapping("/save")
    public ResultVO<String> addMedicine(@RequestBody @Valid ChineseMedicineVO chineseMedicineVO, @RequestParam(value = "userId") @NotNull(message = "用户id不能为空") Long userId)  {
        try {
            chineseMedicineService.save(chineseMedicineVO, userId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return ResultVO.success("成功");
    }

    /**
     * 删除药材
     *
     * @param medicineCode 药材编码
     * @return 相应信息（成功、失败）
     */
    @ApiOperation("删除药材")
    @DeleteMapping("/delete")
    public ResultVO<String> deleteMedicine(@RequestParam("medicineCode") @NotBlank(message = "药材编码不能为空")String medicineCode,
                                    @RequestParam(value = "userId") @NotNull(message = "用户id不能为空") Long userId) {
        String msg = "当前中药材已关联SKU或者不存在";
        boolean b = chineseMedicineService.removeByCode(medicineCode, userId);
        if (b) {
            msg = "删除中药材成功";
        }
        return ResultVO.success(msg);
    }


    /**
     *
     * @param medicineCode
     * @param medicineName
     * @param pageNo
     * @param pageSize
     * @return
     */
    @ApiOperation("药材信息分页展示")
    @GetMapping("/searchList")
    public ResultVO<List<ChineseMedicineResponse>> queryMedicinePage(@RequestParam(value = "medicineCode",required = false) String medicineCode,@RequestParam(value = "medicineName",required = false)String medicineName,
                                                                     @RequestParam(value = "pageNo")Integer pageNo,@RequestParam(value = "pageSize") Integer pageSize) {
        List<ChineseMedicineResponse> chineseMedicineVOList = chineseMedicineService.queryPage(medicineCode,medicineName,pageNo,pageSize);
        return ResultVO.success(chineseMedicineVOList);
    }

    /**
     * 所有药材查询  用于新增药材添加相反药材时检索所有药材
     */
    @ApiOperation("所有药材查询")
    @GetMapping("/searchAll")
    public ResultVO<List<ChineseMedicineInfoResponse>> queryMedicineAll(@RequestParam(value = "medicineName" ,required = false) String medicineName,
                                                                        @RequestParam(value = "medicineCode" ,required = false) String medicineCode) {
        List<ChineseMedicineInfoResponse> chineseMedicineInfoList = chineseMedicineService.queryAll(medicineName,medicineCode);
        return ResultVO.success(chineseMedicineInfoList);
    }

    /**
     * 所有药材查询  用于新增药材添加相反药材时检索所有药材
     */
    @ApiOperation("相反药材信息分页展示")
    @GetMapping("/conflictList")
    public ResultVO<List<ChineseMedicineResponse>> queryConflictMedicine(@RequestParam("medicineCode") @NotBlank(message="药材编码不能为空") String medicineCode,
                                                                         @RequestParam(value = "pageNo",required = false) Integer pageNo,
                                                                         @RequestParam(value = "pageSize",required = false) Integer pageSize) {
        List<ChineseMedicineResponse> chineseMedicineResponses = chineseMedicineService.queryPageForConflict(medicineCode, pageNo, pageSize);
        return ResultVO.success(chineseMedicineResponses);
    }
}
