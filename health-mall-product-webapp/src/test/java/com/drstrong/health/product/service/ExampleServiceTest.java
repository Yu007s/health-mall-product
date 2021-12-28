package com.drstrong.health.product.service;

import com.drstrong.health.product.SpringBootTests;
import com.drstrong.health.product.model.request.store.UpdateSkuRequest;
import com.drstrong.health.product.mq.model.SkuStateStockMqEvent;
import com.drstrong.health.product.service.impl.StoreThreeRelevanceServiceImpl;
import com.drstrong.health.product.utils.MqMessageUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * @author <a href="mailto:wf2311@163.com">wf2311</a>
 * @since 2021/6/21 17:22.
 */
public class ExampleServiceTest extends SpringBootTests {

    @Resource
    private StoreThreeRelevanceServiceImpl storeThreeRelevanceService;
    @Resource
    private MqMessageUtil mqMessageUtil;
    @DisplayName("测试样例2")
    public void test1() {
        System.out.println("test for health-mall-product");
    }

    @Test
    public void testMqToWare(){
        SkuStateStockMqEvent stateStockMqEvent = new SkuStateStockMqEvent();
        stateStockMqEvent.setUserId("888");
        stateStockMqEvent.setState(1);
        stateStockMqEvent.setSkuIdList(Collections.EMPTY_LIST);
        mqMessageUtil.sendMsg(MqMessageUtil.SKU_STATE_STOCK_TOPIC,MqMessageUtil.SKU_STATE_STOCK_TAG,stateStockMqEvent);
    }

    @Test
    public void testPostage(){
        UpdateSkuRequest updateSkuRequest = new UpdateSkuRequest();
        updateSkuRequest.setState(1);
        List<Long> skuIds = new ArrayList<>();
        skuIds.add(11L);
        updateSkuRequest.setSkuIdList(skuIds);
        storeThreeRelevanceService.updateSkuState(updateSkuRequest,"888");
    }
}
