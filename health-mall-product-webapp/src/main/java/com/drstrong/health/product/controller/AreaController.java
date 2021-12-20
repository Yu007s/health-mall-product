package com.drstrong.health.product.controller;

import com.drstrong.health.product.model.response.area.AreaInfoResponse;
import com.drstrong.health.product.model.response.result.ResultVO;
import com.drstrong.health.product.model.response.store.StoreInfoResponse;
import com.drstrong.health.product.service.AreaService;
import com.drstrong.health.product.service.StoreService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 店铺管理
 *
 * @author liuqiuyi
 * @date 2021/12/7 09:51
 */
@RestController
@RequestMapping("/product/area")
@Slf4j
@Api(tags = {"地区管理"}, description = "地区管理")
public class AreaController {

	@Resource
	private AreaService areaService;

	@ApiOperation("获取所有的省份地区")
	@GetMapping("/queryAllProvince")
	public ResultVO<List<AreaInfoResponse>> queryAllProvince() {
		List<AreaInfoResponse> areaInfoResponses = areaService.queryAllProvince();
		return ResultVO.success(areaInfoResponses);
	}

}
