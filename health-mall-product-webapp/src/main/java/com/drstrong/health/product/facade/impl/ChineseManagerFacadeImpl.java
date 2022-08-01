package com.drstrong.health.product.facade.impl;

import com.drstrong.health.product.facade.ChineseManagerFacade;
import com.drstrong.health.product.model.request.chinese.ChineseManagerSkuRequest;
import com.drstrong.health.product.model.response.PageVO;
import com.drstrong.health.product.model.response.chinese.ChineseManagerSkuVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author liuqiuyi
 * @date 2022/8/1 11:14
 */
@Slf4j
@Service
public class ChineseManagerFacadeImpl implements ChineseManagerFacade {
    /**
     * 中药管理页面，列表查询
     *
     * @param skuRequest 分页查询的入参
     * @return 中药管理列表
     * @author liuqiuyi
     * @date 2022/8/1 11:16
     */
    @Override
    public PageVO<ChineseManagerSkuVO> pageChineseManagerSku(ChineseManagerSkuRequest skuRequest) {
        // 

        return null;
    }
}
