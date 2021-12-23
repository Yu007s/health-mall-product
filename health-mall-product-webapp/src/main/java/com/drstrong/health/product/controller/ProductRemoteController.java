package com.drstrong.health.product.controller;

import com.drstrong.health.product.model.constans.banner.CommonConstants;
import com.drstrong.health.product.model.enums.ErrorEnums;
import com.drstrong.health.product.model.enums.SourceEnum;
import com.drstrong.health.product.model.request.product.ProductSearchRequest;
import com.drstrong.health.product.model.response.PageVO;
import com.drstrong.health.product.model.response.product.ProductSearchDetailVO;
import com.drstrong.health.product.model.response.result.BusinessException;
import com.drstrong.health.product.remote.api.product.ProductRemoteFacade;
import com.drstrong.health.product.remote.model.ProductSkuInfoDTO;
import com.drstrong.health.product.service.ProductBasicsInfoService;
import com.drstrong.health.product.service.ProductRemoteService;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

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
@Api(tags = {"商品远程接口"}, description = "商品远程接口")
public class ProductRemoteController implements ProductRemoteFacade {
	@Resource
	ProductRemoteService productRemoteService;

	@Resource
	ProductBasicsInfoService productBasicsInfoService;

	/**
	 * 根据 skuId 集合,获取 sku 信息
	 * <p>集合大小不超过 200</>
	 *
	 * @param skuIds skuId 集合
	 * @return sku 信息
	 * @author liuqiuyi
	 * @date 2021/12/14 17:21
	 */
	@Override
	public List<ProductSkuInfoDTO> getSkuInfoBySkuIds(Set<Long> skuIds) {
		if (CollectionUtils.isEmpty(skuIds)) {
			return Lists.newArrayList();
		}
		if (skuIds.size() > CommonConstants.COLLECTION_MAX_SIZE) {
			throw new BusinessException(ErrorEnums.QUERY_SIZE_TOO_BIG);
		}
		return productRemoteService.getSkuInfoBySkuIds(skuIds);
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
	public List<String> searchSkuNameByName(String content, Integer count) {
		return null;
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
	public List<ProductSkuInfoDTO> searchSkuDetail(@RequestParam("content") String content) {
		return null;
	}

}
