package com.drstrong.health.product.remote.api.incentive;

import com.drstrong.health.product.model.request.incentive.SaveEarningNameRequest;
import com.drstrong.health.product.model.request.incentive.SaveOrUpdateSkuPolicyRequest;
import com.drstrong.health.product.model.response.incentive.PackageIncentivePolicyDetailVO;
import com.drstrong.health.product.model.response.incentive.SkuIncentivePolicyDetailVO;
import com.drstrong.health.product.model.response.incentive.excel.PackageIncentivePolicyDetailExcelVO;
import com.drstrong.health.product.model.response.incentive.excel.SkuIncentivePolicyDetailExcelVO;
import com.drstrong.health.product.model.response.result.ResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

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

	@ApiOperation("查询sku激励政策")
	@GetMapping("/query/sku/policy")
	ResultVO<SkuIncentivePolicyDetailVO> queryPolicyDetailBySkuCode(@RequestParam("skuCode") String skuCode);

	@ApiOperation("查询所有sku的激励政策,用于excel导出")
	@GetMapping("/query/all/sku/policy")
	ResultVO<SkuIncentivePolicyDetailExcelVO> queryAllSkuPolicyDetailToExcelVO(@RequestParam("storeId") Long storeId, @RequestParam("productType") Integer productType);

	@ApiOperation("查询所有套餐的激励政策,用于excel导出")
	@GetMapping("/query/all/package/policy")
	ResultVO<PackageIncentivePolicyDetailExcelVO> queryAllPackagePolicyDetailToExcelVO(@RequestParam("storeId") Long storeId, @RequestParam("productType") Integer productType);

	@ApiOperation("查询套餐激励政策")
	@GetMapping("/query/package/policy")
	ResultVO<PackageIncentivePolicyDetailVO> queryPolicyDetailByPackageCode(@RequestParam("packageCode") String packageCode);

}
