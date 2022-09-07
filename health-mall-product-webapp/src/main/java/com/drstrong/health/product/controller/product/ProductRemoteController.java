package com.drstrong.health.product.controller.product;

import com.drstrong.health.product.model.entity.product.ProductSkuEntity;
import com.drstrong.health.product.model.entity.product.ProductSkuRevenueEntity;
import com.drstrong.health.product.model.enums.ErrorEnums;
import com.drstrong.health.product.model.response.result.BusinessException;
import com.drstrong.health.product.model.response.result.ResultVO;
import com.drstrong.health.product.remote.api.product.ProductRemoteFacade;
import com.drstrong.health.product.remote.model.*;
import com.drstrong.health.product.remote.model.request.QueryProductRequest;
import com.drstrong.health.product.service.product.ProductRemoteService;
import com.drstrong.health.product.service.product.ProductSkuRevenueService;
import com.drstrong.health.product.service.product.ProductSkuService;
import com.google.common.collect.Lists;
import com.naiterui.ehp.bp.bo.b2c.cms.CmsSkuBO;
import com.naiterui.ehp.bp.bo.b2c.cms.ProductBO;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.*;

/**
 * 商品模块远程接口
 *
 * @author liuqiuyi
 * @date 2021/12/22 14:50
 */
@Validated
@RestController
@RequestMapping("/product/remote")
@Slf4j
@Api(tags = {"商品远程接口"})
public class ProductRemoteController implements ProductRemoteFacade {
	@Resource
	ProductRemoteService productRemoteService;

	@Resource
	private ProductSkuService productSkuService;

	@Resource
	private ProductSkuRevenueService productSkuRevenueService;

	/**
	 * 根据 skuId 集合,获取 sku 信息
	 *
	 * @param queryProductRequest 请求参数
	 * @return sku 信息
	 * @author liuqiuyi
	 * @date 2021/12/14 17:21
	 */
	@Override
	@Deprecated
	public List<ProductSkuInfoDTO> getSkuInfoBySkuIds(QueryProductRequest queryProductRequest) {
//		return productRemoteService.getSkuInfoBySkuIds(queryProductRequest);
		return Lists.newArrayList();
	}

	/**
	 * 根据 skuId 集合,获取 sku 信息(包含已删除的数据)
	 * <p> 包含 delFlag 为 1 的数据 </>
	 *
	 * @param queryProductRequest 查询参数
	 * @return sku 信息
	 * @author liuqiuyi
	 * @date 2022/1/10 16:54
	 */
	@Override
	@Deprecated
	public List<ProductSkuInfoDTO> getSkuInfoBySkuIdsContainDel(QueryProductRequest queryProductRequest) {
//		return productRemoteService.getSkuInfoBySkuIdsContainDel(queryProductRequest);
		return Lists.newArrayList();
	}

	/**
	 * 分页查询搜索的内容,只返回商品名称
	 *
	 * @param content 搜索条件
	 * @param count   返回的个数
	 * @return 搜索结果
	 * @author liuqiuyi
	 * @date 2021/12/17 15:49
	 */
	@Override
	@Deprecated
	public List<SearchNameResultDTO> searchSpuNameByName(String content, Integer count) {
//		return productRemoteService.searchSpuNameByName(content, count);
		return Lists.newArrayList();
	}

	/**
	 * 查询sku搜索结果
	 * <p> 目前主要是空中药房使用,因两边数据未打通,无法分页查询 </>
	 *
	 * @param content 搜索内容
	 * @return 搜索结果
	 * @author liuqiuyi
	 * @date 2021/12/18 14:05
	 */
	@Override
	@Deprecated
	public List<ProductSkuInfoDTO> searchSkuDetail(@RequestParam("content") String content) {
//		return productRemoteService.searchSkuDetail(content);
		return Lists.newArrayList();
	}

	/**
	 * 根据后台分类 id 查询商品信息
	 *
	 * @param categoryId 分类 id
	 * @return 商品详细信息
	 * @author liuqiuyi
	 * @date 2021/12/24 13:54
	 */
	@Override
	@Deprecated
	public List<ProductSkuInfoDTO> getSkuInfoByCategoryId(Long categoryId) {
//		return productRemoteService.getSkuInfoByCategoryId(categoryId);
		return Lists.newArrayList();
	}

	/**
	 * 根据 skuId 或者 skuCode 查询商品详情
	 * <p> 目前主要提供给空中药房调用 </>
	 * <p> 两个入参任传其一 </>
	 *
	 * @param skuCode sku 编码
	 * @param skuId   skuId
	 * @return 商品的 sku 详情
	 * @author liuqiuyi
	 * @date 2021/12/24 11:30
	 */
	@Override
	@Deprecated
	public ProductSkuDetailsDTO getSkuDetail(String skuCode, Long skuId) {
//		return productRemoteService.getSkuDetail(skuCode, skuId);
		return new ProductSkuDetailsDTO();
	}

	/**
	 * skuCode 和 skuId 进行转换,批量接口
	 * <p> 主要用于两者相互查询,两个入参不能同时为空 </>
	 *
	 * @param queryProductRequest 请求参数
	 * @return sku 编码和 id 信息
	 * @author liuqiuyi
	 * @date 2021/12/24 20:07
	 */
	@Override
	@Deprecated
	public List<SkuIdAndCodeDTO> listSkuIdOrCode(QueryProductRequest queryProductRequest) {
//		return productRemoteService.listSkuIdOrCode(queryProductRequest);
		return Lists.newArrayList();
	}

	@Override
	@Deprecated
	public SkuInvoiceDTO getInvoiceInfoBySkuId(Long skuId) {
		/*if(Objects.isNull(skuId)){
			throw new BusinessException(ErrorEnums.PARAM_IS_NOT_NULL);
		}
		SkuInvoiceDTO skuInvoiceDTO = new SkuInvoiceDTO();
		skuInvoiceDTO.setSkuId(skuId);
		ProductSkuEntity productSkuEntity = productSkuService.queryBySkuIdOrCode(skuId, "", null);
		ProductSkuRevenueEntity skuRevenue = productSkuRevenueService.getSkuRevenue(skuId, "");
		if(Objects.nonNull(productSkuEntity)){
			skuInvoiceDTO.setPackName(productSkuEntity.getPackName()).setPackValue(productSkuEntity.getPackValue());
		}
		if(Objects.nonNull(skuRevenue)){
			skuInvoiceDTO.setRevenueCode(skuRevenue.getRevenueCode()).setRevenueRate(skuRevenue.getRevenueRate());
		}
		return skuInvoiceDTO;*/
		return new SkuInvoiceDTO();
	}

	/**
	 * 根据 skuId 或者 skuCode 集合查询发票所需相关信息
	 *
	 * @param queryProductRequest 查询入参
	 * @return 发票相关信息
	 * @author liuqiuyi
	 * @date 2022/1/10 10:23
	 */
	@Override
	@Deprecated
	public List<SkuInvoiceDTO> listInvoiceBySkuIds(QueryProductRequest queryProductRequest) {
//		return productRemoteService.listInvoiceBySkuIds(queryProductRequest);
		return Lists.newArrayList();
	}

	@Override
	public List<Map<Long, String>> getskuNumber(Set<Long> skuIds, Integer recomType) {
		Map<Long,String>  page =  productRemoteService.getskuNumber(skuIds,recomType);
		List<Map<Long,String>> skuList = new ArrayList<>();
		skuList.add(page);
		return skuList;
	}

	@Override
	public ResultVO<String> addErpInfo(@Valid CmsSkuBO skuVO) {
		productRemoteService.addErpInfo(skuVO);
		return ResultVO.success();
	}

	@Override
	public ResultVO<List<ProductBO>> getProductListByIds(Set<Long> ids) {
		List<ProductBO> productBOList = productRemoteService.getProductListByIds(ids);
		return  ResultVO.success(productBOList);
	}

}
