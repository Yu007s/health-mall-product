package com.drstrong.health.product.remote.api.product;

import com.drstrong.health.product.model.dto.product.SkuProhibitAreaInfoDTO;
import com.drstrong.health.product.model.request.product.v3.SaveOrUpdateSkuProhibitAreaRequest;
import com.drstrong.health.product.model.response.result.ResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Api("健康商城-商品服务-sku禁售区域")
@FeignClient(value = "health-mall-product", path = "/inner/prohibit/area/manage")
public interface SkuProhibitAreaManageRemoteApi {

	@ApiOperation("查询 sku 的禁售区域信息")
	@GetMapping("/query-by-code")
	ResultVO<List<SkuProhibitAreaInfoDTO>> queryBySkuCode(@RequestParam("skuCode") String skuCode);

	@ApiOperation("保存或者更新 sku 的禁售区域信息")
	@PostMapping("/save-or-update/area")
	ResultVO<Void> saveOrUpdateArea(@RequestBody SaveOrUpdateSkuProhibitAreaRequest saveOrUpdateSkuProhibitAreaRequest);
}
