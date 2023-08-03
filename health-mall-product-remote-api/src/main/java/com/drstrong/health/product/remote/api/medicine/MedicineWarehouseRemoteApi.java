package com.drstrong.health.product.remote.api.medicine;

import com.drstrong.health.product.model.dto.medicine.AgreementPrescriptionMedicineBaseDTO;
import com.drstrong.health.product.model.dto.medicine.ChineseMedicineBaseDTO;
import com.drstrong.health.product.model.dto.medicine.WesternMedicineBaseDTO;
import com.drstrong.health.product.model.dto.medicine.v2.MedicineWarehouseBaseDTO;
import com.drstrong.health.product.model.request.medicine.MedicineCodeRequest;
import com.drstrong.health.product.model.request.medicine.MedicineWarehouseQueryRequest;
import com.drstrong.health.product.model.response.PageVO;
import com.drstrong.health.product.model.response.result.ResultVO;
import io.swagger.annotations.Api;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.List;

/**
 * 药材库的远程接口
 *
 * @author liuqiuyi
 * @date 2023/6/20 10:58
 */
@Api("健康商城-安全用药-药材库的统一入口")
@FeignClient(value = "health-mall-product", path = "/inner/product/medicine/warehouse")
public interface MedicineWarehouseRemoteApi {
    /**
     * 查询西药材库
     *
     * @author liuqiuyi
     * @date 2023/6/20 11:01
     */
    @PostMapping("/page/western/query")
    ResultVO<PageVO<WesternMedicineBaseDTO>> pageWesternQuery(@RequestBody @Valid MedicineWarehouseQueryRequest medicineWarehouseQueryRequest);

    /**
     * 查询协定方库
     *
     * @author liuqiuyi
     * @date 2023/6/20 11:01
     */
    @PostMapping("/page/agreement/query")
    ResultVO<PageVO<AgreementPrescriptionMedicineBaseDTO>> pageAgreementQuery(@RequestBody @Valid MedicineWarehouseQueryRequest medicineWarehouseQueryRequest);

    /**
     * 查询中药材库
     *
     * @author liuqiuyi
     * @date 2023/6/20 11:01
     */
    @PostMapping("/page/chinese/query")
    ResultVO<PageVO<ChineseMedicineBaseDTO>> pageChineseQuery(@RequestBody @Valid MedicineWarehouseQueryRequest medicineWarehouseQueryRequest);

    /**
     * 根据类型查询药材库基础信息
     *
     * @author liuqiuyi
     * @date 2023/8/3 16:26
     */
    @PostMapping("/query-by-type")
    ResultVO<MedicineWarehouseBaseDTO> queryBaseDtoByTypeAndCode(@RequestBody @Valid MedicineCodeRequest medicineCodeRequest);
}
