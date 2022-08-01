package com.drstrong.health.product.controller.chineseMedicine;

import com.drstrong.health.product.model.request.ChineseMedicine.ChineseMedicineRequest;
import com.drstrong.health.product.model.response.chineseMedicine.ChineseMedicineVO;
import com.drstrong.health.product.model.response.result.ResultVO;
import com.drstrong.health.product.remote.api.chineseMedicine.ChineseMedicineFacade;
import com.drstrong.health.product.service.chineseMedicine.ChineseMedicineService;
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
@RequestMapping("/product/chineseMedicine")
public class ChineseMedicineController implements ChineseMedicineFacade {
    @Resource
    ChineseMedicineService  chineseMedicineService;

    /**
     * 新建药材  药材名字不允许重复  不允许店铺名字重复
     *
     * @param chineseMedicineVO 药材相关信息
     * @return 相应信息（成功、失败）
     */
    @ApiOperation("新建药材")
    @PostMapping("/add")
    ResultVO<String> addMedicine(@RequestBody @Valid ChineseMedicineVO chineseMedicineVO){
        return null;
    }
    /**
     * 编辑药材
     *
     * @param chineseMedicineVO 药材相关信息
     * @return 相应信息（成功、失败）
     */
    @ApiOperation("编辑药材")
    @PostMapping("/update")
    ResultVO<String> updateMedicine(@RequestBody @Valid ChineseMedicineVO chineseMedicineVO) {
        return null;
    }

    /**
     * 删除药材
     *
     * @param medicineId 药材id
     * @return 相应信息（成功、失败）
     */
    @ApiOperation("删除药材")
    @DeleteMapping("/delete")
    ResultVO<String> deleteMedicine(@RequestParam Long medicineId){
        return null;
    }

    /**
     * 药材信息展示分页页面
     *
     * @param chineseMedicineRequest 分页查询请求参数
     * @return 药材信息列表
     */
    @ApiOperation("药材信息分页展示")
    @GetMapping("/queryPage")
    public ResultVO<List<ChineseMedicineVO>> queryMedicinePage(@RequestBody ChineseMedicineRequest chineseMedicineRequest) {
        List<ChineseMedicineVO> chineseMedicineVOList = chineseMedicineService.queryPage(chineseMedicineRequest);
        return ResultVO.success(chineseMedicineVOList);
    }
}
