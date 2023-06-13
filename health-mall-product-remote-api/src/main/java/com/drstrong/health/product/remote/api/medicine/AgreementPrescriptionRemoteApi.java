package com.drstrong.health.product.remote.api.medicine;

import com.drstrong.health.product.model.request.medicine.AddOrUpdateAgreementRequest;
import com.drstrong.health.product.model.response.medicine.AgreementPrescriptionInfoVO;
import com.drstrong.health.product.model.response.result.ResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;


/**
 * 协定方远程接口
 *
 * @author zzw
 * @date 2023/6/13
 */
@Api("健康商城-安全用药-协定方远程接口")
@FeignClient(value = "health-mall-product", path = "/inner/product/agreement/prescription/medicine")
public interface AgreementPrescriptionRemoteApi {


    /**
     * 保存/修改 协定方
     *
     * @param request
     * @return
     */
    @ApiOperation("保存/修改 协定方")
    @GetMapping("/save-or-update")
    ResultVO<Long> saveOrUpdateAgreementPrescription(@RequestBody AddOrUpdateAgreementRequest request);


    /**
     * 协定方详情信息
     *
     * @return
     */
    @ApiOperation("协定方详情信息")
    @GetMapping("/get-by-id")
    ResultVO<AgreementPrescriptionInfoVO> queryAgreementPrescriptionInfo(@RequestParam("id") Long id);
}
