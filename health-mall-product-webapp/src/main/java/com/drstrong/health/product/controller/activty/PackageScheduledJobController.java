package com.drstrong.health.product.controller.activty;

import com.drstrong.health.product.facade.activty.PackageManageFacade;
import com.drstrong.health.product.model.response.result.ResultVO;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 套餐定时上下架
 *
 * @author huangpeng
 * @date 2023/7/18 11:47
 */
@Slf4j
@RestController
@RequestMapping("/inner/package/task")
public class PackageScheduledJobController {

	@Autowired
	private PackageManageFacade packageManageFacade;

	@ApiOperation("sku定时上下架")
	@GetMapping("up-or-down")
	public ResultVO<Void> doScheduledSkuUpDown() {
		packageManageFacade.doScheduledUpDown();
		return ResultVO.success();
	}
}
