package com.drstrong.health.product.controller.chinese;

import com.drstrong.health.product.facade.ChineseRemoteFacade;
import com.drstrong.health.product.model.request.chinese.QueryChineseSkuRequest;
import com.drstrong.health.product.model.response.chinese.ChineseSkuExtendVO;
import com.drstrong.health.product.model.response.chinese.ChineseSkuInfoVO;
import com.drstrong.health.product.model.response.result.ResultVO;
import com.drstrong.health.product.remote.api.chinese.ChineseRemoteApi;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 中药对外业务的远程接口
 *
 * @author liuqiuyi
 * @date 2022/8/3 19:28
 */
@Validated
@RestController
@RequestMapping("/inner/chinese")
@Slf4j
@Api(tags = {"中药对外业务的远程接口"})
public class ChineseRemoteController implements ChineseRemoteApi {
	@Resource
	ChineseRemoteFacade chineseRemoteFacade;

	@Override
	public ResultVO<List<ChineseSkuInfoVO>> keywordSearch(String keyword, Long agencyId) {
		return ResultVO.success(chineseRemoteFacade.keywordSearch(keyword, agencyId, null));
	}

	@Override
	public ResultVO<List<ChineseSkuExtendVO>> queryStoreSku(QueryChineseSkuRequest chineseSkuRequest) {
		return ResultVO.success(chineseRemoteFacade.queryStoreSku(chineseSkuRequest));
	}
}
