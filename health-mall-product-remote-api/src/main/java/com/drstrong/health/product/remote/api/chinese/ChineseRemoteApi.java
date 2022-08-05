package com.drstrong.health.product.remote.api.chinese;

import com.drstrong.health.product.model.request.chinese.QueryChineseSkuRequest;
import com.drstrong.health.product.model.response.chinese.ChineseSkuExtendVO;
import com.drstrong.health.product.model.response.chinese.ChineseSkuInfoVO;
import com.drstrong.health.product.model.response.result.ResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.List;

/**
 * 中药的远程接口
 *
 * @author liuqiuyi
 * @date 2022/8/3 16:41
 */
@Api("健康商城-商品服务-中药的远程接口")
@FeignClient(value = "health-mall-product", path = "/inner/chinese")
public interface ChineseRemoteApi {

	@ApiOperation("中药查询接口，用于医生搜索中药材")
	@GetMapping("/keyword/search")
	ResultVO<List<ChineseSkuInfoVO>> keywordSearch(@RequestParam("keyword") String keyword, @RequestParam("agencyId") Long agencyId);

	@ApiOperation("中药 sku,查询店铺的 sku 信息")
	@PostMapping("/sku/query")
	ResultVO<List<ChineseSkuExtendVO>> queryStoreSku(@RequestBody @Valid QueryChineseSkuRequest chineseSkuRequest);
}
