package com.drstrong.health.product.remote.api.label;

import com.drstrong.health.product.model.dto.label.LabelExtendDTO;
import com.drstrong.health.product.model.request.label.SaveLabelRequest;
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

@Api("健康商城-商品服务-标签的远程接口")
@FeignClient(value = "health-mall-product", path = "/inner/label/manage")
public interface LabelManageRemoteApi {

	@ApiOperation("标签列表查询")
	@GetMapping("/query/all")
	ResultVO<List<LabelExtendDTO>> queryAll(@RequestParam("storeId") Long storeId, @RequestParam(value = "labelType", required = false) Integer labelType);

	@ApiOperation("标签保存或者更新")
	@PostMapping("/label/save-or-update")
	ResultVO<Void> saveOrUpdate(@RequestBody @Valid SaveLabelRequest saveLabelRequest);
}
