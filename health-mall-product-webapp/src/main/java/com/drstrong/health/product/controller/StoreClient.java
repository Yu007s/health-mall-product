package com.drstrong.health.product.controller;

import com.drstrong.health.product.model.request.store.*;
import com.drstrong.health.product.model.response.PageVO;
import com.drstrong.health.product.model.response.area.AreaInfoResponse;
import com.drstrong.health.product.model.response.result.ResultVO;
import com.drstrong.health.product.model.response.store.StoreInfoResponse;
import com.drstrong.health.product.model.response.store.StoreSkuResponse;
import com.drstrong.health.product.model.response.store.ThreeSkuInfoResponse;
import com.drstrong.health.product.remote.api.store.StoreRemoteApi;
import com.drstrong.health.product.remote.model.StorePostageDTO;
import com.drstrong.health.product.service.AreaService;
import com.drstrong.health.product.service.StoreService;
import com.drstrong.health.product.service.StoreThreeRelevanceService;
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
@RequestMapping("/product/store")
@Slf4j
public class StoreClient implements StoreRemoteApi {

	@Resource
	private StoreService storeService;
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
	public ResultVO<Object> add(StoreAddOrUpdateRequest storeAddOrUpdateRequest,String userId) {
		storeService.add(storeAddOrUpdateRequest,userId);
		return ResultVO.success();
	}

	@Override
	public ResultVO<Object> update(StoreAddOrUpdateRequest storeAddOrUpdateRequest,String userId) {
		storeService.update(storeAddOrUpdateRequest,userId);
		return ResultVO.success();
	}

	@Override
	public ResultVO<Object> updateState(StoreIdRequest storeIdRequest,String userId) {
		storeService.disable(storeIdRequest,userId);
		return ResultVO.success();
	}

	@Override
	public ResultVO<StorePostage> getPostage(Long storeId) {
		StorePostage storePostage = storeService.getPostage(storeId);
		return ResultVO.success(storePostage);
	}

	@Override
	public ResultVO<Object> updatePostage(StorePostage storePostage,String userId) {
		storeService.updatePostage(storePostage,userId);
		return ResultVO.success();
	}

	@Override
	public ResultVO<PageVO<StoreSkuResponse>> pageSkuList(StoreSkuRequest storeSkuRequest) {
		PageVO<StoreSkuResponse> pageVO = storeThreeRelevanceService.pageSkuList(storeSkuRequest);
		return ResultVO.success(pageVO);
	}

	@Override
	public ResultVO<Object> updatePurchasePrice(UpdateThreeRequest updateThreeRequest,String userId) {
		storeThreeRelevanceService.updatePurchasePrice(updateThreeRequest,userId);
		return ResultVO.success();
	}

	@Override
	public ResultVO<Object> relevanceAdd(RelevanceThreeRequest relevanceThreeRequest,String userId) {
		storeThreeRelevanceService.relevanceAdd(relevanceThreeRequest,userId);
		return ResultVO.success();
	}

	@Override
	public ResultVO<Object> updateSkuState(UpdateSkuRequest updateSkuRequest,String userId) {
		storeThreeRelevanceService.updateSkuState(updateSkuRequest,userId);
		return ResultVO.success();
	}

	@Override
	public ResultVO<List<StoreSkuResponse>> searchSkuList(StoreSkuRequest storeSkuRequest) {
		List<StoreSkuResponse> storeSkuResponses = storeThreeRelevanceService.searchSkuList(storeSkuRequest);
		return ResultVO.success(storeSkuResponses);
	}


	@Override
	public List<StorePostageDTO> getStorePostageByIds(Set<Long> storeIds, String areaName) {
		return storeService.getStorePostageByIds(storeIds,areaName);
	}

	@Override
	public ResultVO<List<AreaInfoResponse>> queryAllProvince() {
		List<AreaInfoResponse> areaInfoResponses = areaService.queryAllProvince();
		return ResultVO.success(areaInfoResponses);
	}

	@Override
	public List<ThreeSkuInfoResponse> queryBySkuIds(List<Long> skuIds) {
		return storeThreeRelevanceService.queryBySkuIds(skuIds);
	}
}
