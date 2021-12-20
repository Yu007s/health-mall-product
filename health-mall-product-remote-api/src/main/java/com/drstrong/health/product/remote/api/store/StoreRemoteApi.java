package com.drstrong.health.product.remote.api.store;

import com.drstrong.health.product.model.request.store.*;
import com.drstrong.health.product.model.response.PageVO;
import com.drstrong.health.product.model.response.result.ResultVO;
import com.drstrong.health.product.model.response.store.StoreInfoResponse;
import com.drstrong.health.product.model.response.store.StoreSkuResponse;
import com.drstrong.health.product.remote.model.StorePostageDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;
import java.util.Set;

/**
 * @author lsx
 * @projectName health-mall-product
 * @desc 店铺运费远程服务api
 * @createTime 2021/12/15 15:54
 * @since TODO
 */
@FeignClient(value = "health-mall-product-store")
public interface StoreRemoteApi {

    ResultVO<List<StoreInfoResponse>> queryAllStore();

    ResultVO<Object> add(@RequestBody @Valid StoreAddOrUpdateRequest storeAddOrUpdateRequest, @RequestParam("userId") String userId);

    ResultVO<Object> update(@RequestBody @Valid StoreAddOrUpdateRequest storeAddOrUpdateRequest,@RequestParam("userId") String userId);

    ResultVO<Object> updateState(@RequestBody @Valid StoreIdRequest storeIdRequest,@RequestParam("userId") String userId);

    ResultVO<StorePostage> getPostage(@RequestParam("storeId") Long storeId);

    ResultVO<Object> updatePostage(@RequestBody StorePostage storePostage,@RequestParam("userId") String userId);

    ResultVO<PageVO<StoreSkuResponse>> pageSkuList(@RequestBody StoreSkuRequest storeSkuRequest);

    ResultVO<Object> updatePurchasePrice(@RequestBody UpdateThreeRequest updateThreeRequest,@RequestParam("userId") String userId);

    ResultVO<Object> relevanceAdd(@RequestBody RelevanceThreeRequest relevanceThreeRequest,@RequestParam("userId") String userId);

    ResultVO<Object> updateSkuState(@RequestBody UpdateSkuRequest updateSkuRequest,@RequestParam("userId")String userId);

    void exportSku(StoreSkuRequest storeSkuRequest, HttpServletRequest request, HttpServletResponse response);

    List<StorePostageDTO> getStorePostageByIds(@RequestParam("storeIds") Set<Long> storeIds,@RequestParam("areaName") String areaName);
}
                                      