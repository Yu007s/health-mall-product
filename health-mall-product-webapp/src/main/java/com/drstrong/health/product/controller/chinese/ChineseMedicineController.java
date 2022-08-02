package com.drstrong.health.product.controller.chinese;

import com.drstrong.health.product.model.request.chinese.ChineseMedicineRequest;
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
@RequestMapping("/product/chineseMedicine")
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
    public ResultVO<List<ChineseMedicineVO>> queryMedicinePage(@RequestParam ChineseMedicineRequest chineseMedicineRequest) {
        List<ChineseMedicineVO> chineseMedicineVOList = chineseMedicineService.queryPage(chineseMedicineRequest);
        return ResultVO.success(chineseMedicineVOList);
    }
}
