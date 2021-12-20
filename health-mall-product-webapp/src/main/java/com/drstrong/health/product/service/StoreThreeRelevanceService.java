package com.drstrong.health.product.service;

import com.drstrong.health.product.model.request.store.RelevanceThreeRequest;
import com.drstrong.health.product.model.request.store.StoreSkuRequest;
import com.drstrong.health.product.model.request.store.UpdateSkuRequest;
import com.drstrong.health.product.model.request.store.UpdateThreeRequest;
import com.drstrong.health.product.model.response.PageVO;
import com.drstrong.health.product.model.response.store.StoreSkuResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author lsx
 * @projectName health-mall-product
 * @desc 三方药店关联商品服务
 * @createTime 2021/12/14 21:10
 * @since TODO
 */
public interface StoreThreeRelevanceService {

    void updatePurchasePrice(UpdateThreeRequest updateThreeRequest,String userId);

    PageVO<StoreSkuResponse> pageSkuList(StoreSkuRequest storeSkuRequest);

    void relevanceAdd(RelevanceThreeRequest relevanceThreeRequest, String userId);

    void updateSkuState(UpdateSkuRequest updateSkuRequest, String userId);

    void exportStoreSku(StoreSkuRequest storeSkuRequest, HttpServletRequest request, HttpServletResponse response);
}
