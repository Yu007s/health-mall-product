package com.drstrong.health.product.remote.api.label;

import com.drstrong.health.product.model.dto.label.LabelExtendDTO;
import com.drstrong.health.product.model.response.result.ResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Api("健康商城-商品服务-标签的远程接口")
@FeignClient(value = "health-mall-product", path = "/inner/label/manage")
public interface LabelManageRemoteApi {

	@ApiOperation("标签列表查询")
	@PostMapping("/query/all")
	ResultVO<List<LabelExtendDTO>> queryAll();

	@ApiOperation("标签保存或者更新")
	@PostMapping("/label/save-or-update")
	ResultVO<Void> saveOrUpdate(@RequestBody LabelExtendDTO labelExtendDTO);
}
