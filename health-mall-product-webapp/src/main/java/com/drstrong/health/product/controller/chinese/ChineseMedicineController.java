package com.drstrong.health.product.controller.chinese;

import com.drstrong.health.product.model.request.chinese.ChineseMedicineRequest;
import com.drstrong.health.product.model.response.chinese.ChineseMedicineInfoResponse;
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
import java.util.List;

/**
 * @Author xieYueFeng
 * @Date 2022/07/30/10:12
 */
@Validated
@Slf4j
@RestController
@RequestMapping("/product/medicine")
public class ChineseMedicineController implements ChineseMedicineFacade {
    @Resource
    ChineseMedicineService  chineseMedicineService;

    /**
     * 新建、编辑药材  药材名字不允许重复
     *
     * @param chineseMedicineVO 药材相关信息
     * @return 相应信息（成功、失败）
     */
    @ApiOperation("新建/编辑药材")
    @PostMapping("/save")
    ResultVO<String> addMedicine(@RequestBody @Valid ChineseMedicineVO chineseMedicineVO){
        return null;
    }

    /**
     * 删除药材
     *
     * @param medicineCode 药材编码
     * @return 相应信息（成功、失败）
     */
    @ApiOperation("删除药材")
    @DeleteMapping("/delete")
    ResultVO<String> deleteMedicine(@RequestParam String medicineCode){
        String  msg = "当前中药材已关联SKU或者不存在";
        boolean b = chineseMedicineService.removeByCode(medicineCode);
        if(b){
            msg = "删除中药材成功";
        }
        return ResultVO.success(msg);
    }

    /**
     * 药材信息展示分页
     *
     * @param chineseMedicineRequest 分页查询请求参数
     * @return 药材信息列表
     */
    @ApiOperation("药材信息分页展示")
    @GetMapping("/searchList")
    public ResultVO<List<ChineseMedicineVO>> queryMedicinePage(@RequestParam ChineseMedicineRequest chineseMedicineRequest) {
        List<ChineseMedicineVO> chineseMedicineVOList = chineseMedicineService.queryPage(chineseMedicineRequest);
        return ResultVO.success(chineseMedicineVOList);
    }

    /**
     * 所有药材查询  用于新增药材添加相反药材时检索所有药材
     *
     */
    @ApiOperation("药材信息分页展示")
    @GetMapping("/searchAll")
    public ResultVO<List<ChineseMedicineInfoResponse>> queryMedicineAll(@RequestParam ChineseMedicineRequest chineseMedicineRequest) {
        List<ChineseMedicineInfoResponse> chineseMedicineInfoList = chineseMedicineService.queryAll(chineseMedicineRequest);
        return ResultVO.success(chineseMedicineInfoList);
    }
}
