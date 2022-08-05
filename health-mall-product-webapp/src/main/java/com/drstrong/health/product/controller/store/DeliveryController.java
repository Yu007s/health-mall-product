package com.drstrong.health.product.controller.store;

import com.drstrong.health.product.model.response.result.ResultVO;
import com.drstrong.health.product.model.response.store.delievy.DeliveryPriorityVO;
import com.drstrong.health.product.service.store.StoreDeliveryPriorityService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Author xieYueFeng
 * @Date 2022/08/05/14:44
 */
@RestController
@RequestMapping("/product/chinese/store/delivery")
public class DeliveryController {
    @Resource
    StoreDeliveryPriorityService storeDeliveryPriorityService;
    @GetMapping ("query")
    ResultVO<DeliveryPriorityVO> getDeliveryInfo(@RequestParam Long storeId) {
        DeliveryPriorityVO deliveryPriorityVO = storeDeliveryPriorityService.queryByStoreId(storeId);
        return ResultVO.success(deliveryPriorityVO);
    }
}
