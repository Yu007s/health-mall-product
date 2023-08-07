package com.drstrong.health.product.remote.api.product;

import com.drstrong.health.product.model.dto.product.ProductListInfoVO;
import com.drstrong.health.product.model.dto.product.StoreSkuDetailDTO;
import com.drstrong.health.product.model.request.chinese.UpdateSkuStateRequest;
import com.drstrong.health.product.model.request.product.SearchWesternRequestParamBO;
import com.drstrong.health.product.model.request.product.v3.ProductManageQueryRequest;
import com.drstrong.health.product.model.request.product.v3.SaveOrUpdateStoreSkuRequest;
import com.drstrong.health.product.model.request.product.v3.ScheduledSkuUpDownRequest;
import com.drstrong.health.product.model.response.PageVO;
import com.drstrong.health.product.model.response.product.v3.AgreementSkuInfoVO;
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

@Api("健康商城-商品服务-商品管理页面的远程接口")
@FeignClient(value = "health-mall-product", path = "/inner/sku/manage")
public interface SkuManageRemoteApi {

	@ApiOperation("sku管理页面查询")
	@PostMapping("/page/query")
	ResultVO<PageVO<AgreementSkuInfoVO>> pageQuerySkuManageInfo(@RequestBody ProductManageQueryRequest productManageQueryRequest);

	@ApiOperation("sku管理页面查询所有数据,导出需要")
	@PostMapping("/query/all")
	ResultVO<List<AgreementSkuInfoVO>> listSkuManageInfo(@RequestBody ProductManageQueryRequest productManageQueryRequest);

	@ApiOperation("保存或更新店铺的 sku 信息(不包含中药)")
	@PostMapping("/store/save-or-update/sku")
	ResultVO<Void> saveOrUpdateStoreProduct(@RequestBody @Valid SaveOrUpdateStoreSkuRequest saveOrUpdateStoreProductRequest);

	@ApiOperation("根据skuCode查询详情(不包含中药)")
	@GetMapping("/query/by-code")
	ResultVO<StoreSkuDetailDTO> queryDetailByCode(@RequestParam("skuCode") String skuCode);

	@ApiOperation("批量上下架(不包含中药)")
	@PostMapping("/update/sku-status")
	ResultVO<Void> updateSkuStatus(@RequestBody @Valid UpdateSkuStateRequest updateSkuStateRequest);

	@ApiOperation("预约上下架(不包含中药)")
	@PostMapping("/scheduled/up-or-down")
	ResultVO<Void> scheduledSkuUpDown(@RequestBody @Valid ScheduledSkuUpDownRequest scheduledSkuUpDownRequest);
}
