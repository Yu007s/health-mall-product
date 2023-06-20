package com.drstrong.health.product.remote.api.medicine;

import com.drstrong.health.product.model.dto.medicine.MedicineWarehouseBaseDTO;
import com.drstrong.health.product.model.request.medicine.MedicineWarehouseQueryRequest;
import com.drstrong.health.product.model.response.PageVO;
import com.drstrong.health.product.model.response.result.ResultVO;
import io.swagger.annotations.Api;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * 药材库的远程接口
 *
 * @author liuqiuyi
 * @date 2023/6/20 10:58
 */
@Api("健康商城-安全用药-药材库的统一入口")
@FeignClient(value = "health-mall-product", path = "/inner/product/medicine/warehouse")
public interface MedicineWarehouseRemoteApi {
//    /**
//     * 根据类型分页查询药材库
//     *
//     * @author liuqiuyi
//     * @date 2023/6/20 11:01
//     */
//    // TODO 这里不对 刘秋意
//    @PostMapping("/page/query11111")
//    <T extends MedicineWarehouseBaseDTO> ResultVO<PageVO<T>> pageQuery(@RequestBody @Valid MedicineWarehouseQueryRequest medicineWarehouseQueryRequest);
}
