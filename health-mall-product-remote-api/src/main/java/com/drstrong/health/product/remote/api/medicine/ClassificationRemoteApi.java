package com.drstrong.health.product.remote.api.medicine;

import com.drstrong.health.product.model.response.medicine.FixedClassificationVO;
import com.drstrong.health.product.model.response.medicine.MedicineClassificationVO;
import com.drstrong.health.product.model.response.result.ResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import java.util.List;


/**
 * 药品分类远程接口
 *
 * @author zzw
 * @date 2023/6/6 16:41
 */
@Api("健康商城-安全用药-药品分类远程接口")
@FeignClient(value = "health-mall-product", path = "/inner/product/MedicineClassification")
public interface ClassificationRemoteApi {


    /**
     * 按分类类型获取药品分类
     *
     * @param classificationType
     * @return
     */
    @ApiOperation("根据类型获取药品分类")
    @GetMapping("/query-by-type")
    ResultVO<List<MedicineClassificationVO>> getClassificationByType(@RequestParam("classificationType") @NotBlank(message = "分类类型不能为空") Integer classificationType);


    /**
     * 获取所有分类集合
     *
     * @return
     */
    @ApiOperation("获取所有药品分类")
    @GetMapping("/searchAll")
    ResultVO<List<FixedClassificationVO>> getAllClassification();
}
