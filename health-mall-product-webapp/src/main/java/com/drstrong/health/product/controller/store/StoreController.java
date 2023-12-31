package com.drstrong.health.product.controller.store;

import com.drstrong.health.product.dao.store.OldFreightPostageMapper;
import com.drstrong.health.product.dao.store.StoreLinkSupplierMapper;
import com.drstrong.health.product.facade.postage.StorePostageFacade;
import com.drstrong.health.product.model.entity.store.OldAreaFreight;
import com.drstrong.health.product.model.entity.store.StoreEntity;
import com.drstrong.health.product.model.request.store.SaveStorePostageRequest;
import com.drstrong.health.product.model.request.store.SaveStoreSupplierPostageRequest;
import com.drstrong.health.product.model.request.store.StoreInfoDetailSaveRequest;
import com.drstrong.health.product.model.request.store.StoreSearchRequest;
import com.drstrong.health.product.model.response.result.BusinessException;
import com.drstrong.health.product.model.response.result.ResultVO;
import com.drstrong.health.product.model.response.store.StoreInfoEditResponse;
import com.drstrong.health.product.model.response.store.StoreInfoResponse;
import com.drstrong.health.product.model.response.store.StoreQueryResponse;
import com.drstrong.health.product.model.response.store.v3.StorePostageVO;
import com.drstrong.health.product.remote.api.store.StoreFacade;
import com.drstrong.health.product.remote.api.store.StoreRemoteApi;
import com.drstrong.health.product.service.store.StoreService;
import com.google.common.collect.Lists;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;

/**
 * 备忘  店铺id  与名字  对应  存在redis中
 *
 * @Author xieYueFeng
 * @Date 2022/07/30/14:14
 */
@RestController
@RequestMapping("/inner/product/chinese/store")
public class StoreController implements StoreFacade, StoreRemoteApi {

    @Resource
    private StoreService storeService;
    @Resource
    StoreLinkSupplierMapper storeLinkSupplierMapper;
    @Resource
    private OldFreightPostageMapper oldFreightPostageMapper;

    @Resource
    StorePostageFacade storePostageFacade;


    @Override
    public ResultVO<String> savaStore(@RequestBody @Valid StoreInfoDetailSaveRequest store)  {
        String msg;
        if (store.getStoreId() == null) {
            Long storeId = storeService.save(store);
            storeService.saveStoreDefaultPostage(store.getSupplierIds(), storeId);
            msg = "新增店铺成功";
        } else {
            storeService.update(store);
            msg = "修改店铺成功";
        }
        return ResultVO.success(msg);
    }

    @Override
    public ResultVO<List<StoreInfoResponse>> queryStore(@RequestBody StoreSearchRequest storeSearchRequest) {
        List<StoreInfoResponse> query = storeService.query(storeSearchRequest.getStoreId(),storeSearchRequest.getStoreName(),
                storeSearchRequest.getAgencyId(), storeSearchRequest.getStoreTypeName());
        return ResultVO.success(query);
    }

    @Override
    public ResultVO<StoreInfoEditResponse> queryStoreDetail(@RequestParam("storeId") @NotNull(message = "店铺id不能为空") Long storeId) {
        StoreInfoEditResponse storeInfoEditResponse = storeService.queryById(storeId);
        return ResultVO.success(storeInfoEditResponse);
    }


    @Override
    public ResultVO<StoreQueryResponse> queryStoreInfo() {
        StoreQueryResponse storeQueryResponse = storeService.queryStoreConInfo();
        return ResultVO.success(storeQueryResponse);
    }

    @Override
    public List<StoreInfoResponse> queryStoreBySupplierId(@RequestParam("supplierId") @NotNull(message = "供应商id不能为空") Long supplierId) {
        return storeLinkSupplierMapper.findStoreBySupplierId(supplierId);
    }

    @Override
    public List<OldAreaFreight> searchOldTLPostage() {
        return oldFreightPostageMapper.searchOldTLPostage();
    }

    @Override
    public Long getStoreByAgencyId(@RequestParam("agencyId") @NotNull Long agencyId) {
        StoreEntity store = storeService.getStoreByAgencyIdOrStoreId(agencyId, null);
        if(store == null){
            throw new BusinessException("错误的互联网医院id");
        }
        return store.getId();
    }

    @Override
    public ResultVO<List<StoreInfoResponse>> queryByIds(Set<Long> storeIds) {
        List<StoreEntity> storeEntityList = storeService.listByIds(storeIds);
        List<StoreInfoResponse> responseList = Lists.newArrayListWithCapacity(storeEntityList.size());
        storeEntityList.forEach(storeEntity -> {
            StoreInfoResponse storeInfoResponse = new StoreInfoResponse();
            storeInfoResponse.setId(storeEntity.getId());
            storeInfoResponse.setStoreName(storeEntity.getStoreName());
            responseList.add(storeInfoResponse);
        });
        return ResultVO.success(responseList);
    }

    @Override
    public ResultVO<StorePostageVO> queryStorePostage(@NotNull(message = "店铺id不能为空") Long storeId) {
        return ResultVO.success(storePostageFacade.queryStorePostage(storeId));
    }

    @Override
    public ResultVO<Void> saveStorePostage(@RequestBody @Valid SaveStorePostageRequest saveStorePostageRequest) {
        storePostageFacade.saveStorePostage(saveStorePostageRequest);
        return ResultVO.success();
    }

    @Override
    public ResultVO<Void> saveStoreSupplierPostage(@RequestBody @Valid SaveStoreSupplierPostageRequest saveStoreSupplierPostageRequest) {
        storePostageFacade.saveStoreSupplierPostage(saveStoreSupplierPostageRequest);
        return ResultVO.success();
    }
}
