package com.drstrong.health.product.service.zStore;

import com.drstrong.health.product.model.request.zStore.StoreRequest;
import com.drstrong.health.product.model.response.zStore.StoreInfoDetailVO;
import com.drstrong.health.product.model.response.zStore.StoreInfoResponse;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @Author xieYueFeng
 * @Date 2022/07/30/9:15
 */
public interface StoreService {

    /**
     * 更新/保存店铺信息
     *
     * @param store  店铺信息
     * @param userId 当前操作用户id
     */
    void save(StoreInfoDetailVO store, String userId);

    /**
     * 店铺展示 当查询条件都为空  返回所有店铺
     *
     * @param storeRequest 查询请求参数
     * @return 店铺信息列表
     */
    List<StoreInfoResponse> query(StoreRequest storeRequest);

    /**
     * 通过店铺id查找店铺详细信息
     *
     * @param storeId 店铺id
     * @return 店铺详情（带有发票信息详情页面）
     */
    StoreInfoDetailVO queryById(Long storeId);
}
