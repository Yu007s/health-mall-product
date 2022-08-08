package com.drstrong.health.product.controller.store;

import com.drstrong.health.product.dao.store.StoreLinkSupplierMapper;
import com.drstrong.health.product.model.request.store.StoreInfoDetailSaveRequest;
import com.drstrong.health.product.model.response.area.ProvinceAreaInfo;
import com.drstrong.health.product.model.response.result.ResultVO;
import com.drstrong.health.product.model.response.store.StoreQueryResponse;
import com.drstrong.health.product.model.response.store.StoreAddResponse;
import com.drstrong.health.product.model.response.store.StoreInfoEditResponse;
import com.drstrong.health.product.model.response.store.StoreInfoResponse;
import com.drstrong.health.product.remote.api.store.StoreFacade;
import com.drstrong.health.product.remote.api.store.StoreRemoteApi;
import com.drstrong.health.product.service.area.AreaService;
import com.drstrong.health.product.service.store.StoreService;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * 备忘  店铺id  与名字  对应  存在redis中
 *
 * @Author xieYueFeng
 * @Date 2022/07/30/14:14
 */
@RestController
@RequestMapping("/product/chinese/store")
public class StoreController implements StoreFacade, StoreRemoteApi {

    @Resource
    private StoreService storeService;
    @Resource
    StoreLinkSupplierMapper storeLinkSupplierMapper;

    @Resource
    private AreaService areaService;

    @Override
    public ResultVO<String> savaStore(@RequestBody @Valid StoreInfoDetailSaveRequest store)  {
        String msg;
        if (!StringUtils.isNotBlank(store.getStoreId())) {
            storeService.save(store);
            msg = "新增店铺成功";
        } else {
            storeService.update(store);
            msg = "修改店铺成功";
        }
        return ResultVO.success(msg);
    }

    @Override
    public ResultVO<List<StoreInfoResponse>> queryStore(@RequestParam(value = "storeId",required = false) Long storeId,@RequestParam(value = "storeName",required = false) String storeName,
                                                        @RequestParam(value = "agencyId",required = false) Long agencyId, @RequestParam(value = "storeTypeId",required = false) Integer storeTypeId) {
        List<StoreInfoResponse> query = storeService.query(storeId,storeName,agencyId, storeTypeId);
        return ResultVO.success(query);
    }

    @Override
    public ResultVO<StoreInfoEditResponse> queryStoreDetail(@RequestParam("storeId") @NotBlank(message = "店铺id不能为空") Long storeId) {
        StoreInfoEditResponse storeInfoEditResponse = storeService.queryById(storeId);
        return ResultVO.success(storeInfoEditResponse);
    }

    @Override
    public ResultVO<StoreAddResponse> queryAddStoreInfo() {
        StoreAddResponse storeAddResponse = storeService.queryStoreAddInfo();
        return ResultVO.success(storeAddResponse);
    }

    @Override
    public ResultVO<StoreQueryResponse> queryStoreInfo() {
        StoreQueryResponse storeQueryResponse = storeService.queryStoreConInfo();
        return ResultVO.success(storeQueryResponse);
    }

    @Override
    public ResultVO<List<ProvinceAreaInfo>> queryAll() {
        List<ProvinceAreaInfo> lists = areaService.queryAll();
        return ResultVO.success(lists);
    }


    @Override
    public List<StoreInfoResponse> queryStoreBySupplierId(@RequestParam("supplierId") @NotBlank(message = "供应商id不能为空") Long supplierId) {
        return storeLinkSupplierMapper.findStoreBySupplierId(supplierId);
    }

}
