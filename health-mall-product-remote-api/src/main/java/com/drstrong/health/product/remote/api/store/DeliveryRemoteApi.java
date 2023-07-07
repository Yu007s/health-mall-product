package com.drstrong.health.product.remote.api.store;

import com.drstrong.health.product.model.request.store.SaveDeliveryRequest;
import com.drstrong.health.product.model.response.area.AreaInfoResponse;
import com.drstrong.health.product.model.response.area.ProvinceAreaInfo;
import com.drstrong.health.product.model.response.result.ResultVO;
import com.drstrong.health.product.model.response.store.delievy.DeliveryPriorityVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @Author xieYueFeng
 * @Date 2022/08/06/17:32
 */
@Api("健康商城-商品服务-店铺配送优先级管理")
@FeignClient(value = "health-mall-product", path = "/inner/product/chinese/store/delivery")
public interface DeliveryRemoteApi {

    /**
     * 根据店铺Id查询配送优先级相关信息
     * @param storeId 店铺id
     * @return 配送优先级展示页面
     */
    @GetMapping("/query")
    ResultVO<DeliveryPriorityVO> getDeliveryInfo(@RequestParam("storeId") @NotNull(message = "店铺id不能为空") Long storeId);

    /**
     * 根据店铺id 配送区域id 查询配送优先级
     * @param storeId 店铺id
     * @param areaId 区域id
     * @return 供应商列表  第一个优先选择  依次类推
     */
    @GetMapping("/queryByAreaId")
    List<Long> getDeliveryInfoByArea(@RequestParam("storeId") @NotNull(message = "店铺id不能为空") Long storeId,
                                     @RequestParam("areaId") @NotNull(message = "区域id不能为空") Long areaId);

    /**
     * 这个接口后续将废弃,为了暂时兼容老逻辑,等供应链三期正式上线后可以去除
     *
     *
     * 保存店铺配送优先级
     * @param saveDeliveryRequest 配送优先级请求数据
     * @param userId 用户id
     * @return 相关信息
     */
    @PostMapping("/save")
    @Deprecated
    ResultVO<String> saveDeliveryInfo(@RequestBody SaveDeliveryRequest saveDeliveryRequest, @RequestParam("userId") @NotNull(message = "用户id不能为空") Long userId);

    /**
     * 保存店铺配送优先级,参照之前接口的出入参
     *
     * @author liuqiuyi
     * @date 2023/6/9 11:25
     */
    @PostMapping("/v3/save")
    ResultVO<String> saveDeliveryInfoV3(@RequestBody @Valid SaveDeliveryRequest saveDeliveryRequest, @RequestParam("userId") @NotNull(message = "用户id不能为空") Long userId);

    /**
     * 查询所有的省市信息
     *
     * @return 所有的省市级信息
     */
    @ApiOperation("查询所有的城市")
    @GetMapping("/queryAll")
    ResultVO<List<ProvinceAreaInfo>> queryAll() ;

    /**
     * 根据区域id查询省id 区域id
     * @param areaId 区域id
     * @return  省级信息
     */
    @GetMapping("/queryProvince")
    @ApiOperation("根据区域id 查询省级信息")
    AreaInfoResponse queryProvince(@RequestParam("areaId") @NotNull(message = "区域id不能为空") Long areaId);
}
