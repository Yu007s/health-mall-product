package com.drstrong.health.product.service.store;

import com.drstrong.health.product.model.request.store.RelevanceThreeRequest;
import com.drstrong.health.product.model.request.store.StoreSkuRequest;
import com.drstrong.health.product.model.request.store.UpdateSkuRequest;
import com.drstrong.health.product.model.request.store.UpdateThreeRequest;
import com.drstrong.health.product.model.response.PageVO;
import com.drstrong.health.product.model.response.store.StoreSkuResponse;
import com.drstrong.health.product.model.response.store.ThreeSkuInfoResponse;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

    List<StoreSkuResponse> searchSkuList(StoreSkuRequest storeSkuRequest);

    List<ThreeSkuInfoResponse> queryBySkuIds(List<Long> skuIds);

    /**
     * 根据 skuId 集合获取三方进货的价格
     *
     * @param skuIds skuId 集合
     * @return key=skuId,value=进货价格
     * @author liuqiuyi
     * @date 2022/1/7 09:22
     */
    Map<Long, BigDecimal> getThreadPriceBySkuIdsToMap(Set<Long> skuIds);
}
