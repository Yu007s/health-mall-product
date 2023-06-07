package com.drstrong.health.product.controller.medicine;

import com.drstrong.health.product.model.response.medicine.FixedClassificationVO;
import com.drstrong.health.product.model.response.medicine.MedicineClassificationVO;
import com.drstrong.health.product.model.response.result.ResultVO;
import com.drstrong.health.product.remote.api.medicine.ClassificationRemoteApi;
import com.drstrong.health.product.service.medicine.MedicineClassificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * <p>
 * 药品基础分类 前端控制器
 * </p>
 *
 * @since 2023-06-06
 */
@RestController
@RequestMapping("/inner/product/medicine/classification")
@Slf4j
public class MedicineClassificationController implements ClassificationRemoteApi {

    @Autowired
    private MedicineClassificationService medicineClassificationService;

    @Override
    public ResultVO<List<MedicineClassificationVO>> getClassificationByType(@NotBlank(message = "分类类型不能为空") Integer classificationType) {
        return ResultVO.success(medicineClassificationService.getListByType(classificationType));
    }

    @Override
    public ResultVO<List<FixedClassificationVO>> getAllClassification() {
        return ResultVO.success(medicineClassificationService.getAllClassification());
    }
}

