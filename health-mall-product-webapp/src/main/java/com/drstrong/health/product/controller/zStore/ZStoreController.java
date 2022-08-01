package com.drstrong.health.product.controller.zStore;

import com.drstrong.health.product.model.request.zStore.StoreRequest;
import com.drstrong.health.product.model.response.result.ResultVO;
import com.drstrong.health.product.model.response.zStore.StoreInfoDetailVO;
import com.drstrong.health.product.model.response.zStore.StoreInfoResponse;
import com.drstrong.health.product.remote.api.zStore.StoreFacade;
import com.drstrong.health.product.service.zStore.StoreService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * @Author xieYueFeng
 * @Date 2022/07/30/14:14
 */
@RestController
@RequestMapping("/product/zStore")
public class ZStoreController implements StoreFacade {

    @Resource(name = "zStoreService")
    StoreService storeService;

    /**
     * 新增店铺信息  做店铺名字重复校验  不允许店铺名字重复
     *
     * @param store 店铺相关信息
     * @param userId 用户id
     * @return 相应信息（成功、失败）
     */
    @ApiOperation("新增/修改店铺信息")
    @PostMapping("/save")
    ResultVO<String> savaStore(@RequestBody @Valid StoreInfoDetailVO store, @RequestParam String userId) {
        storeService.save(store,userId);
        String msg ;
        if (store.getId() == null) {
            msg = "新增店铺成功";
        }else{
            msg = "修改店铺成功";
        }
        return ResultVO.success(msg);
    }

    /**
     * 基于搜索条件获取店铺的基本信息
     *
     * @param storeRequest 店铺查询参数  不能全部为null
     * @return 符合条件的店铺基本信息列表
     */
    @ApiOperation("获取符合条件的店铺基本信息列表")
    @GetMapping("/query")
    ResultVO<List<StoreInfoResponse>> queryStore(@RequestBody StoreRequest storeRequest) {
        return null;
    }

    /**
     * 获取店铺详细信息
     *
     * @param storeId 店铺id
     * @return 店铺信息详情
     */
    @ApiOperation("获取店铺详细信息")
    @GetMapping("/queryById")
    ResultVO<StoreInfoDetailVO> queryStoreDetail(@RequestParam String storeId) {
        return null;
    }
}
