package com.drstrong.health.product.controller.scheduled;

import com.drstrong.health.product.facade.sku.SkuScheduledConfigFacade;
import com.drstrong.health.product.model.response.result.ResultVO;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 定时任务触发入口
 *
 * @author liuqiuyi
 * @date 2023/6/16 11:47
 */
@Slf4j
@RestController
@RequestMapping("/inner/product/task")
public class ScheduledJobController {
	@Resource
	SkuScheduledConfigFacade skuScheduledConfigFacade;

	@ApiOperation("sku定时上下架")
	@GetMapping({"/do/sku/up-or-down"})
	public ResultVO<Void> doScheduledSkuUpDown() {
		skuScheduledConfigFacade.doScheduledSkuUpDown();
		return ResultVO.success();
	}
}
