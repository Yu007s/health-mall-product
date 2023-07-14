package com.drstrong.health.product.facade.store;

import com.drstrong.health.product.model.request.store.AgencyStoreVO;
import com.drstrong.health.product.model.request.store.QueryStoreRequest;

import java.util.List;

/**
 * @author liuqiuyi
 * @date 2023/7/14 10:06
 */
public interface StoreBusinessFacade {
    /**
     * 根据条件查询店铺信息
     *
     * @author liuqiuyi
     * @date 2023/7/14 10:07
     */
    List<AgencyStoreVO> queryStoreByParam(QueryStoreRequest queryStoreRequest);
}
