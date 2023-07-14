package com.drstrong.health.product.controller.store;

import com.drstrong.health.product.facade.store.StoreBusinessFacade;
import com.drstrong.health.product.model.request.store.AgencyStoreVO;
import com.drstrong.health.product.model.request.store.QueryStoreRequest;
import com.drstrong.health.product.model.response.result.ResultVO;
import com.drstrong.health.product.remote.api.store.StoreBusinessRemoteApi;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author liuqiuyi
 * @date 2023/7/14 10:04
 */
@RestController
@RequestMapping("/inner/product/store")
public class StoreBusinessController implements StoreBusinessRemoteApi {

    @Resource
    StoreBusinessFacade storeBusinessFacade;

    @Override
    public ResultVO<List<AgencyStoreVO>> queryStoreByParam(QueryStoreRequest queryStoreRequest) {
        return ResultVO.success(storeBusinessFacade.queryStoreByParam(queryStoreRequest));
    }
}
