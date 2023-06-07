package com.drstrong.health.product.remote.api.incentive;

import com.drstrong.health.product.model.request.incentive.SaveEarningNameRequest;
import com.drstrong.health.product.model.request.incentive.SaveOrUpdateSkuPolicyRequest;
import com.drstrong.health.product.model.response.result.ResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

@Api("健康商城-商品服务-激励的远程接口")
@FeignClient(value = "health-mall-product", path = "/inner/incentive/manage")
public interface IncentiveManageRemoteApi {

	@ApiOperation("收益名称保存")
	@PostMapping("/save/earning-name")
	ResultVO<Void> saveEarningName(@RequestBody @Valid SaveEarningNameRequest saveEarningNameRequest);

	@ApiOperation("sku激励政策保存或更新")
	@PostMapping("/save-or-update/sku/policy")
	ResultVO<Void> saveOrUpdateSkuPolicy(@RequestBody @Valid SaveOrUpdateSkuPolicyRequest saveOrUpdateSkuPolicyRequest);
}
