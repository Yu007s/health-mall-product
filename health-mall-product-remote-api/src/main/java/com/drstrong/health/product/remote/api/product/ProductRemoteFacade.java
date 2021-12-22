package com.drstrong.health.product.remote.api.product;

import com.drstrong.health.product.model.request.product.ProductSearchRequest;
import com.drstrong.health.product.model.response.PageVO;
import com.drstrong.health.product.remote.model.ProductSkuInfoDTO;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Set;

/**
 * sku 远程接口
 *
 * @author liuqiuyi
 * @date 2021/12/14 17:22
 */
@FeignClient(value = "health-mall-product", path = "/product/remote")
public interface ProductRemoteFacade {
	/**
	 * 根据 skuId 集合,获取 sku 信息
	 * <p>集合大小不超过 200</>
	 *
	 * @param skuIds skuId 集合
	 * @return sku 信息
	 * @author liuqiuyi
	 * @date 2021/12/14 17:21
	 */
	@ApiOperation("根据 skuId 集合,获取 sku 信息")
	@PostMapping("getSkuBySkuIds")
	List<ProductSkuInfoDTO> getSkuInfoBySkuIds(@RequestBody Set<Long> skuIds);

	/**
	 * 搜索sku的名称,只返回sku名称
	 *
	 * @param content 搜索条件
	 * @param count   返回的个数
	 * @return 搜索结果
	 * @author liuqiuyi
	 * @date 2021/12/17 15:49
	 */
	@ApiOperation("搜索sku的名称,只返回sku名称")
	@GetMapping("/searchSkuNameByName")
	List<String> searchSkuNameByName(@RequestParam("content") String content, @RequestParam("count") Integer count);

	/**
	 * 分页查询sku搜索结果
	 *
	 * @param productSearchRequest 搜索条件
	 * @return 搜索结果
	 * @author liuqiuyi
	 * @date 2021/12/18 14:05
	 */
	@ApiOperation("分页查询sku搜索结果")
	@PostMapping("/pageSearchSkuDetail")
	PageVO<ProductSkuInfoDTO> pageSearchSkuDetail(@RequestBody ProductSearchRequest productSearchRequest);
}
