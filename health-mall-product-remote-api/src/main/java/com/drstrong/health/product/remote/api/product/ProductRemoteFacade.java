package com.drstrong.health.product.remote.api.product;

import com.drstrong.health.product.remote.model.ProductSkuDetailsDTO;
import com.drstrong.health.product.remote.model.ProductSkuInfoDTO;
import com.drstrong.health.product.remote.model.SearchNameResultDTO;
import com.drstrong.health.product.remote.model.SkuIdAndCodeDTO;
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
	 * 搜索spu的名称,只返回spu名称
	 *
	 * @param content 搜索条件
	 * @param count   返回的个数(不传默认返回 10 条数据)
	 * @return 搜索结果
	 * @author liuqiuyi
	 * @date 2021/12/17 15:49
	 */
	@ApiOperation(value = "搜索spu的名称,只返回spu名称和标题", notes = "和之前空中药房的搜索结果数据结构保持一致")
	@GetMapping("/searchSpuNameByName")
	List<SearchNameResultDTO> searchSpuNameByName(@RequestParam("content") String content, @RequestParam(value = "count", required = false) Integer count);

	/**
	 * 查询sku搜索结果
	 * <p> 目前主要是空中药房使用,因两边数据未打通,无法分页查询 </>
	 *
	 * @param content 搜索内容
	 * @return 搜索结果
	 * @author liuqiuyi
	 * @date 2021/12/18 14:05
	 */
	@ApiOperation("查询sku搜索结果")
	@GetMapping("/searchSkuDetail")
	List<ProductSkuInfoDTO> searchSkuDetail(@RequestParam("content") String content);

	/**
	 * 根据后台分类 id 查询商品信息
	 *
	 * @param categoryId 分类 id
	 * @return 商品详细信息
	 * @author liuqiuyi
	 * @date 2021/12/24 13:54
	 */
	@ApiOperation("根据后台分类 id 查询商品信息")
	@GetMapping("getSkuInfoByCategoryId")
	List<ProductSkuInfoDTO> getSkuInfoByCategoryId(@RequestParam("categoryId") Long categoryId);

	/**
	 * 根据 skuId 或者 skuCode 查询商品详情
	 * <p> 目前主要提供给空中药房调用 </>
	 * <p> 两个入参任传其一 </>
	 *
	 * @param skuId   skuId
	 * @param skuCode sku 编码
	 * @return 商品的 sku 详情
	 * @author liuqiuyi
	 * @date 2021/12/24 11:30
	 */
	@ApiOperation(value = "根据 skuId 或者 skuCode 查询商品详情", notes = "空中药房中需要查询商品详情")
	@GetMapping("/sku/getDetail")
	ProductSkuDetailsDTO getSkuDetail(@RequestParam(value = "skuCode", required = false) String skuCode, @RequestParam(value = "skuId", required = false) Long skuId);

	/**
	 * skuCode 和 skuId 进行转换
	 * <p> 主要用于两者相互查询,两个入参不能同时为空 </>
	 *
	 * @param skuId   skuId
	 * @param skuCode sku编码
	 * @return sku 编码和 id 信息
	 * @author liuqiuyi
	 * @date 2021/12/24 20:07
	 */
	@ApiOperation("skuCode 和 skuId 进行转换")
	@GetMapping("/sku/id/code")
	SkuIdAndCodeDTO getSkuIdOrCode(@RequestParam(value = "skuCode", required = false) String skuCode, @RequestParam(value = "skuId", required = false) Long skuId);
}
