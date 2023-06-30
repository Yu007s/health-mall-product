package com.drstrong.health.product.controller.incentive;

import com.drstrong.health.product.facade.incentive.SkuIncentivePolicyFacade;
import com.drstrong.health.product.model.request.incentive.SaveEarningNameRequest;
import com.drstrong.health.product.model.request.incentive.SaveOrUpdateSkuPolicyRequest;
import com.drstrong.health.product.model.response.incentive.SkuIncentivePolicyDetailVO;
import com.drstrong.health.product.model.response.incentive.excel.SkuIncentivePolicyDetailExcelVO;
import com.drstrong.health.product.model.response.result.ResultVO;
import com.drstrong.health.product.remote.api.incentive.IncentiveManageRemoteApi;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * sku 激励政策相关
 *
 * @author liuqiuyi
 * @date 2023/6/7 15:03
 */
@RestController
@RequestMapping("/inner/incentive/manage")
public class IncentiveManageController implements IncentiveManageRemoteApi {
	@Resource
	SkuIncentivePolicyFacade skuIncentivePolicyFacade;

	@Override
	public ResultVO<Void> saveEarningName(@RequestBody @Valid SaveEarningNameRequest saveEarningNameRequest) {
		skuIncentivePolicyFacade.saveEarningName(saveEarningNameRequest);
		return ResultVO.success();
	}

	@Override
	public ResultVO<Void> saveOrUpdateSkuPolicy(@RequestBody @Valid SaveOrUpdateSkuPolicyRequest saveOrUpdateSkuPolicyRequest) {
		skuIncentivePolicyFacade.saveOrUpdateSkuPolicy(saveOrUpdateSkuPolicyRequest);
		return ResultVO.success();
	}

	@Override
	public ResultVO<SkuIncentivePolicyDetailVO> queryPolicyDetailBySkuCode(String skuCode) {
		return ResultVO.success(skuIncentivePolicyFacade.queryPolicyDetailBySkuCode(skuCode));
	}

	@Override
	public ResultVO<SkuIncentivePolicyDetailExcelVO> queryAllSkuPolicyDetailToExcelVO(Long storeId, Integer productType) {
		return ResultVO.success(skuIncentivePolicyFacade.querySkuPolicyDetailToExcelVO(storeId, productType));
	}
}
