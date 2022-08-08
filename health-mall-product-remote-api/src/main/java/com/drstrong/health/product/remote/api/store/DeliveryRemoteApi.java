package com.drstrong.health.product.remote.api.store;

import com.drstrong.health.product.model.request.store.SaveDeliveryRequest;
import com.drstrong.health.product.model.response.result.ResultVO;
import com.drstrong.health.product.model.response.store.delievy.DeliveryPriorityVO;
import io.swagger.annotations.Api;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @Author xieYueFeng
 * @Date 2022/08/06/17:32
 */
@Api("健康商城-商品服务-店铺配送优先级管理")
@FeignClient(value = "health-mall-product", path = "/product/chinese/store/delivery")
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
     * 保存店铺配送优先级
     * @param saveDeliveryRequest 配送优先级请求数据
     * @param userId 用户id
     * @return 相关信息
     */
    @PostMapping("/save")
    ResultVO<String> saveDeliveryInfo(@RequestBody SaveDeliveryRequest saveDeliveryRequest, @RequestParam("userId") @NotNull(message = "店铺id不能为空") Long userId);
}
