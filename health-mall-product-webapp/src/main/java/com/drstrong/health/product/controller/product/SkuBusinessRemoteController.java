package com.drstrong.health.product.controller.product;

import com.drstrong.health.product.facade.sku.SkuBusinessFacadeHolder;
import com.drstrong.health.product.model.dto.medicine.MedicineUsageDTO;
import com.drstrong.health.product.model.response.result.ResultVO;
import com.drstrong.health.product.remote.api.product.SkuBusinessRemoteApi;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

/**
 * @author liuqiuyi
 * @date 2023/7/14 15:32
 */
@Api("sku业务处理的远程接口")
@RestController
@RequestMapping("/inner/sku/business")
public class SkuBusinessRemoteController implements SkuBusinessRemoteApi {
    @Resource
    SkuBusinessFacadeHolder skuBusinessFacadeHolder;

    @Override
    public ResultVO<List<MedicineUsageDTO>> queryMedicineUsageBySkuCode(Set<String> skuCodes) {
        return ResultVO.success(skuBusinessFacadeHolder.queryMedicineUsageBySkuCode(skuCodes));
    }
}
