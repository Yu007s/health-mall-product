package com.drstrong.health.product.controller.chinese;

import com.drstrong.health.common.utils.UserUtil;
import com.drstrong.health.product.facade.ChineseAuthFacade;
import com.drstrong.health.product.model.request.chinese.ChineseQueryDosageRequest;
import com.drstrong.health.product.model.response.chinese.ChineseSkuInfoVO;
import com.drstrong.health.product.model.response.result.ResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 中药业务接口,需要经过网关调用,校验登录信息
 *
 * @author liuqiuyi
 * @date 2022/12/8 16:41
 */
@Validated
@RestController
@RequestMapping("/auth/chinese")
@Slf4j
@Api(tags = {"中药对外业务的接口,需要经过网关调用,校验登录信息"})
public class ChineseAuthController {
	@Resource
	ChineseAuthFacade chineseAuthFacade;

	@ApiOperation("查询店铺所有存在倍数限制的中药")
	@GetMapping("/sku/dosage/all")
	public ResultVO<List<ChineseSkuInfoVO>> queryAllDosage(ChineseQueryDosageRequest chineseQueryDosageRequest) {
		chineseQueryDosageRequest.setUcDoctorId(UserUtil.getUserId());
		return ResultVO.success(chineseAuthFacade.queryAllDosage(chineseQueryDosageRequest));
	}
}
