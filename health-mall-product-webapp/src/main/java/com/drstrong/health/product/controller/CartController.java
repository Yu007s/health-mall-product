package com.drstrong.health.product.controller;

import com.drstrong.health.product.model.response.result.ResultVO;
import com.drstrong.health.product.model.request.cart.AddCartRequest;
import com.drstrong.health.product.model.request.cart.UpdateCartRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 购物车 controller
 *
 * @author liuqiuyi
 * @date 2021/12/7 13:56
 */
@RestController
@RequestMapping("/order/car")
@Slf4j
@Api(tags = {"购物车"}, description = "购物车")
public class CartController {

	@ApiOperation(value = "加入购物车", notes = "本期只支持三方商品加入购物车,处方药和健康辅助方,依旧使用老的 b2c 购物车")
	@PostMapping("/add")
	public ResultVO<Object> add(@RequestBody AddCartRequest addCartRequest) {

		return ResultVO.success();
	}

	@ApiOperation(value = "查询购物车中的商品", notes = "从 b2c 购物车,商城购物车中查询所有信息,不分页")
	@GetMapping("/query")
	public ResultVO<List<?>> query() {

		return ResultVO.success();
	}

	@ApiOperation(value = "修改购物车", notes = "处方药和健康辅助方,不支持修改")
	@PostMapping("/update")
	public ResultVO<Object> update(@RequestBody UpdateCartRequest updateCartRequest) {

		return ResultVO.success();
	}

	@ApiOperation(value = "购物车删除")
	@PostMapping("/deleteById")
	public ResultVO<Object> delete(@RequestBody UpdateCartRequest updateCartRequest) {

		return ResultVO.success();
	}
}
