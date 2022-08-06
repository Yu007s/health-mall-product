package com.drstrong.health.product.controller.store;

import com.drstrong.health.product.model.request.store.SaveDeliveryRequest;
import com.drstrong.health.product.model.response.result.ResultVO;
import com.drstrong.health.product.model.response.store.delievy.DeliveryPriResponse;
import com.drstrong.health.product.model.response.store.delievy.DeliveryPriorityVO;
import com.drstrong.health.product.service.store.StoreDeliveryPriorityService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author xieYueFeng
 * @Date 2022/08/05/14:44
 */
@RestController
@RequestMapping("/product/chinese/store/delivery")
public class DeliveryController {
    @Resource
    private StoreDeliveryPriorityService storeDeliveryPriorityService;
    @GetMapping ("query")
    public ResultVO<DeliveryPriorityVO> getDeliveryInfo(@RequestParam Long storeId) {
        DeliveryPriorityVO deliveryPriorityVO = storeDeliveryPriorityService.queryByStoreId(storeId);
        return ResultVO.success(deliveryPriorityVO);
    }
    @GetMapping ("queryByAreaId")
    public List<Long> getDeliveryInfoByArea(@RequestParam Long storeId,@RequestParam Long areaId) {
        return storeDeliveryPriorityService.queryByStoreIdAndArea(storeId, areaId);
    }

    @PostMapping("save")
    ResultVO<String> saveDeliveryInfo(@RequestBody SaveDeliveryRequest saveDeliveryRequest, @RequestParam Long userId){
        storeDeliveryPriorityService.save(saveDeliveryRequest,userId);
        return ResultVO.success();
    }
}
