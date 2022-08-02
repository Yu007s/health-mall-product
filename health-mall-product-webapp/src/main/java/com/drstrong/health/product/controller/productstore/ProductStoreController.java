package com.drstrong.health.product.controller.productstore;

import com.drstrong.health.product.model.request.productstore.*;
import com.drstrong.health.product.model.response.PageVO;
import com.drstrong.health.product.model.response.area.AreaInfoResponse;
import com.drstrong.health.product.model.response.result.ResultVO;
import com.drstrong.health.product.model.response.productstore.StoreInfoResponse;
import com.drstrong.health.product.model.response.productstore.StoreSkuResponse;
import com.drstrong.health.product.model.response.productstore.ThreeSkuInfoResponse;
import com.drstrong.health.product.mq.model.product.StoreChangeTypeEnum;
import com.drstrong.health.product.remote.api.productstore.StoreRemoteApi;
import com.drstrong.health.product.remote.model.StorePostageDTO;
import com.drstrong.health.product.service.area.AreaService;
import com.drstrong.health.product.service.productstore.ProductStoreService;
import com.drstrong.health.product.service.productstore.StoreThreeRelevanceService;
import com.yomahub.tlog.core.annotation.TLogAspect;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

/**
 * 店铺管理
 *
 * @author liuqiuyi
 * @date 2021/12/7 09:51
 */
@RestController
@RequestMapping("/product/productStore")
@Slf4j
public class ProductStoreController implements StoreRemoteApi {

	@Resource
	private ProductStoreService storeService;
	@Resource
	private StoreThreeRelevanceService storeThreeRelevanceService;

	@Resource
	private AreaService areaService;

	@Override
	public ResultVO<List<StoreInfoResponse>> queryAllStore() {
		List<StoreInfoResponse> storeInfoResponses = storeService.queryAll();
		return ResultVO.success(storeInfoResponses);
	}

	@Override
	public ResultVO<Object> add(StoreAddOrUpdateRequest storeAddOrUpdateRequest, String userId) {
		storeService.add(storeAddOrUpdateRequest, userId);
		return ResultVO.success();
	}

	@Override
	public ResultVO<Object> update(StoreAddOrUpdateRequest storeAddOrUpdateRequest, String userId) {
		storeService.update(storeAddOrUpdateRequest, userId);
		storeService.sendStoreChangeEvent(storeAddOrUpdateRequest.getStoreId(), userId, StoreChangeTypeEnum.UPDATE_NAME);
		return ResultVO.success();
	}

	@Override
	public ResultVO<Object> updateState(StoreIdRequest storeIdRequest, String userId) {
		storeService.updateState(storeIdRequest, userId);
		storeService.sendStoreChangeEvent(storeIdRequest.getStoreId(), userId, StoreChangeTypeEnum.UPDATE_STATE);
		return ResultVO.success();
	}

	@Override
	public ResultVO<StorePostage> getPostage(Long storeId) {
		StorePostage storePostage = storeService.getPostage(storeId);
		return ResultVO.success(storePostage);
	}

	@Override
	public ResultVO<Object> updatePostage(StorePostage storePostage, String userId) {
		storeService.updatePostage(storePostage, userId);
		return ResultVO.success();
	}

	@Override
	public ResultVO<PageVO<StoreSkuResponse>> pageSkuList(StoreSkuRequest storeSkuRequest) {
		PageVO<StoreSkuResponse> pageVO = storeThreeRelevanceService.pageSkuList(storeSkuRequest);
		return ResultVO.success(pageVO);
	}

	@Override
	public ResultVO<Object> updatePurchasePrice(UpdateThreeRequest updateThreeRequest, String userId) {
		storeThreeRelevanceService.updatePurchasePrice(updateThreeRequest, userId);
		return ResultVO.success();
	}

	@Override
	public ResultVO<Object> relevanceAdd(RelevanceThreeRequest relevanceThreeRequest, String userId) {
		storeThreeRelevanceService.relevanceAdd(relevanceThreeRequest, userId);
		return ResultVO.success();
	}

	@Override
	public ResultVO<Object> updateSkuState(UpdateSkuRequest updateSkuRequest, String userId) {
		storeThreeRelevanceService.updateSkuState(updateSkuRequest, userId);
		return ResultVO.success();
	}

	@Override
	public ResultVO<List<StoreSkuResponse>> searchSkuList(StoreSkuRequest storeSkuRequest) {
		List<StoreSkuResponse> storeSkuResponses = storeThreeRelevanceService.searchSkuList(storeSkuRequest);
		return ResultVO.success(storeSkuResponses);
	}


	@Override
	public List<StorePostageDTO> getStorePostageByIds(Set<Long> storeIds, String areaName) {
		return storeService.getStorePostageByIds(storeIds, areaName);
	}

	@Override
	public ResultVO<List<AreaInfoResponse>> queryAllProvince() {
		List<AreaInfoResponse> areaInfoResponses = areaService.queryAllProvince();
		return ResultVO.success(areaInfoResponses);
	}

	@Override
	@TLogAspect({"skuIds"})
	public List<ThreeSkuInfoResponse> queryBySkuIds(List<Long> skuIds) {
		log.info("远程服务提供：获取sku三方关联信息");
		return storeThreeRelevanceService.queryBySkuIds(skuIds);
	}

	@Override
	public ResultVO<List<StoreInfoResponse>> queryByStoreIds(Set<Long> storeIds) {
		List<StoreInfoResponse> storeInfoResponses = storeService.queryByStoreIds(storeIds);
		return ResultVO.success(storeInfoResponses);
	}
}
