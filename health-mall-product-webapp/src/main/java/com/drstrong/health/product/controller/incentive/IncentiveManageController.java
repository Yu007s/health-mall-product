package com.drstrong.health.product.controller.incentive;

import com.drstrong.health.product.facade.incentive.IncentivePolicyConfigFacade;
import com.drstrong.health.product.model.request.incentive.SaveEarningNameRequest;
import com.drstrong.health.product.model.response.result.ResultVO;
import com.drstrong.health.product.remote.api.incentive.IncentiveManageRemoteApi;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @author liuqiuyi
 * @date 2023/6/7 15:03
 */
@RestController
@RequestMapping("/inner/incentive/manage")
public class IncentiveManageController implements IncentiveManageRemoteApi {
	@Resource
	IncentivePolicyConfigFacade incentivePolicyConfigFacade;

	@Override
	public ResultVO<Void> saveEarningName(@RequestBody @Valid SaveEarningNameRequest saveEarningNameRequest) {
		incentivePolicyConfigFacade.saveEarningName(saveEarningNameRequest);
		return ResultVO.success();
	}
}
