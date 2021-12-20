package com.drstrong.health.product.controller;

import com.drstrong.health.product.model.request.store.*;
import com.drstrong.health.product.model.response.PageVO;
import com.drstrong.health.product.model.response.result.ResultVO;
import com.drstrong.health.product.model.response.store.StoreInfoResponse;
import com.drstrong.health.product.model.response.store.StoreSkuResponse;
import com.drstrong.health.product.remote.api.store.StoreRemoteApi;
import com.drstrong.health.product.remote.model.StorePostageDTO;
import com.drstrong.health.product.service.StoreService;
import com.drstrong.health.product.service.StoreThreeRelevanceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;
import java.util.Set;

/**
 * 店铺管理
 *
 * @author liuqiuyi
 * @date 2021/12/7 09:51
 */
@RestController
@Slf4j
public class StoreClient implements StoreRemoteApi {

	@Resource
	private StoreService storeService;
	@Resource
	private StoreThreeRelevanceService storeThreeRelevanceService;

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
	public ResultVO<Object> update(@RequestBody @Valid StoreAddOrUpdateRequest storeAddOrUpdateRequest,String userId) {
		storeService.update(storeAddOrUpdateRequest,userId);
		return ResultVO.success();
	}

	@Override
	public ResultVO<Object> updateState(@RequestBody @Valid StoreIdRequest storeIdRequest,String userId) {
		storeService.disable(storeIdRequest,userId);
		return ResultVO.success();
	}

	@Override
	public ResultVO<StorePostage> getPostage(Long storeId) {
		StorePostage storePostage = storeService.getPostage(storeId);
		return ResultVO.success(storePostage);
	}

	@Override
	public ResultVO<Object> updatePostage(@RequestBody StorePostage storePostage,String userId) {
		storeService.updatePostage(storePostage,userId);
		return ResultVO.success();
	}

	@Override
	public ResultVO<PageVO<StoreSkuResponse>> pageSkuList(StoreSkuRequest storeSkuRequest) {
		PageVO<StoreSkuResponse> pageVO = storeThreeRelevanceService.pageSkuList(storeSkuRequest);
		return ResultVO.success(pageVO);
	}

	@Override
	public ResultVO<Object> updatePurchasePrice(@RequestBody UpdateThreeRequest updateThreeRequest,String userId) {
		storeThreeRelevanceService.updatePurchasePrice(updateThreeRequest,userId);
		return ResultVO.success();
	}

	@Override
	public ResultVO<Object> relevanceAdd(@RequestBody RelevanceThreeRequest relevanceThreeRequest,String userId) {
		storeThreeRelevanceService.relevanceAdd(relevanceThreeRequest,userId);
		return ResultVO.success();
	}

	@Override
	public ResultVO<Object> updateSkuState(@RequestBody UpdateSkuRequest updateSkuRequest,String userId) {
		storeThreeRelevanceService.updateSkuState(updateSkuRequest,userId);
		return ResultVO.success();
	}

	@Override
	public void exportSku(StoreSkuRequest storeSkuRequest, HttpServletRequest request, HttpServletResponse response) {
		storeThreeRelevanceService.exportStoreSku(storeSkuRequest,request,response);
	}


	@Override
	public List<StorePostageDTO> getStorePostageByIds(Set<Long> storeIds, String areaName) {
		return storeService.getStorePostageByIds(storeIds,areaName);
	}
}
