package com.drstrong.health.product.controller.store;

import com.drstrong.health.product.model.request.store.SaveDeliveryRequest;
import com.drstrong.health.product.model.request.store.StoreInfoDetailSaveRequest;
import com.drstrong.health.product.model.request.store.StoreSearchRequest;
import com.drstrong.health.product.model.response.area.AreaInfoResponse;
import com.drstrong.health.product.model.response.result.ResultVO;
import com.drstrong.health.product.model.response.store.DeliveryPriorityVO;
import com.drstrong.health.product.model.response.store.StoreAddResponse;
import com.drstrong.health.product.model.response.store.StoreInfoEditResponse;
import com.drstrong.health.product.model.response.store.StoreInfoResponse;
import com.drstrong.health.product.remote.api.store.StoreFacade;
import com.drstrong.health.product.service.area.AreaService;
import com.drstrong.health.product.service.store.StoreService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * 备忘  店铺id  与名字  对应  存在redis中
 * @Author xieYueFeng
 * @Date 2022/07/30/14:14
 */
@RestController
@RequestMapping("/product/chineseStore")
public class StoreController implements StoreFacade {

    @Resource
    private StoreService storeService;

    @Resource
    private AreaService areaService;

    /**
     * 新增店铺信息  做店铺名字重复校验  不允许店铺名字重复
     *
     * @param store  店铺相关信息
     * @param userId 用户id
     * @return 相应信息（成功、失败）
     */
    @ApiOperation("新增/修改店铺信息")
    @PostMapping("/save")
    public ResultVO<String> savaStore(@RequestBody @Valid StoreInfoDetailSaveRequest store, @RequestParam Long userId) {
        storeService.save(store, userId);
        String msg;
        if (store.getId() == null) {
            msg = "新增店铺成功";
        } else {
            msg = "修改店铺成功";
        }
        return ResultVO.success(msg);
    }

    /**
     * 基于搜索条件获取店铺的基本信息
     *
     * @param storeSearchRequest 店铺查询参数  不能全部为null
     * @return 符合条件的店铺基本信息列表
     */
    @ApiOperation("获取符合条件的店铺基本信息列表")
    @GetMapping("/query")
    public ResultVO<List<StoreInfoResponse>> queryStore(@RequestBody StoreSearchRequest storeSearchRequest) {
        List<StoreInfoResponse> query = storeService.query(storeSearchRequest);
        return ResultVO.success(query);
    }

    /**
     * 获取店铺详细信息
     *
     * @param storeId 店铺id
     * @return 店铺信息详情
     */
    @ApiOperation("获取店铺详细信息")
    @GetMapping("/queryById")
    public ResultVO<StoreInfoEditResponse> queryStoreDetail(@RequestParam Long storeId) {
        StoreInfoEditResponse storeInfoEditResponse = storeService.queryById(storeId);
        return ResultVO.success(storeInfoEditResponse);
    }

    /**
     * 配送优先级页面根据店铺id查询所有的供应商
     *
     * @param storeId 店铺id
     * @return 供应商列表
     */
    @ApiOperation("根据店铺id查询所有的供应商")
    @GetMapping("/querySupplier")
    public ResultVO<DeliveryPriorityVO> queryStoreDelivery(@RequestParam String storeId) {
        return null;
    }

    /**
     * 保存配送优先级信息
     *
     * @param storeId 店铺id
     * @return 供应商列表
     */
    @ApiOperation("保存配送优先级信息")
    @GetMapping("/saveDelivery")
    public ResultVO<String> saveDelivery(@RequestBody SaveDeliveryRequest storeId) {
        return null;
    }

    /**
     * 查询所有的省份信息
     *
     * @return 所有的省份信息
     */
    @ApiOperation("查询所有的省份信息")
    @GetMapping("/queryAllProvince")
    public ResultVO<List<AreaInfoResponse>> queryProvince() {
        List<AreaInfoResponse> areaInfoResponses = areaService.queryAllProvince();
        return ResultVO.success(areaInfoResponses);
    }

    /**
     * 根据区域id查询具体的区域信息
     *
     * @return 所有的省份信息
     */
    @ApiOperation("查询具体的区域信息")
    @GetMapping("/queryCities")
    public ResultVO<List<AreaInfoResponse>> queryCity(Long areaId) {
       return null;
    }


}
