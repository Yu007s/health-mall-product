package com.drstrong.health.product.controller.chinese;

import com.drstrong.health.product.model.request.chinese.ChineseManagerSkuRequest;
import com.drstrong.health.product.model.response.PageVO;
import com.drstrong.health.product.model.response.chinese.ChineseManagerSkuVO;
import com.drstrong.health.product.model.response.chinese.SaveOrUpdateSkuVO;
import com.drstrong.health.product.model.response.result.ResultVO;
import com.drstrong.health.product.remote.api.chinese.ChineseManagerRemoteApi;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

/**
 *
 * @author liuqiuyi
 * @date 2022/8/1 11:04
 */
@Validated
@RestController
@RequestMapping("/inner/chinese/manager")
@Slf4j
@Api(tags = {"cms-中药管理页面远程接口"})
public class ChineseManagerController implements ChineseManagerRemoteApi {
    @Override
    public ResultVO<PageVO<ChineseManagerSkuVO>> pageChineseManagerSku(ChineseManagerSkuRequest skuRequest) {
        return null;
    }

    @Override
    public ResultVO<List<ChineseManagerSkuVO>> exportChineseManagerSku(ChineseManagerSkuRequest skuRequest) {
        return null;
    }

    @Override
    public ResultVO<Object> saveSku(SaveOrUpdateSkuVO saveOrUpdateSkuVO) {
        return null;
    }

    @Override
    public ResultVO<SaveOrUpdateSkuVO> getSkuByCode(String skuCode) {
        return null;
    }

    @Override
    public ResultVO<Object> listUpdateSkuState(Set<String> skuCodeList) {
        return null;
    }
}
