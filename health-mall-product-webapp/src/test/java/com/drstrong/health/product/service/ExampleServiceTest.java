package com.drstrong.health.product.service;

import com.drstrong.health.product.SpringBootTests;
import com.drstrong.health.product.model.response.result.ResultVO;
import com.drstrong.health.product.model.response.store.StoreInfoResponse;
import com.drstrong.health.product.remote.api.store.StoreRemoteApi;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


/**
 * @author <a href="mailto:wf2311@163.com">wf2311</a>
 * @since 2021/6/21 17:22.
 */
public class ExampleServiceTest extends SpringBootTests {

    @Autowired
    private StoreRemoteApi storeRemoteApi;
    @Test
    @DisplayName("测试样例")
    public void test1() {
        System.out.println("test for health-mall-product");
    }

    @Test
    public void testStoreQuery(){
        ResultVO<List<StoreInfoResponse>> listResultVO = storeRemoteApi.queryAllStore();
        listResultVO.getData().forEach(s -> System.out.println(s.getStoreName()));
    }

}
