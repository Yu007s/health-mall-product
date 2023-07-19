package com.drstrong.health.product.controller.product;

import com.drstrong.health.product.facade.product.ProductBussinessFacade;
import com.drstrong.health.product.model.dto.product.FrequentlyUsedProductInfoVO;
import com.drstrong.health.product.model.dto.product.ProductDetailInfoVO;
import com.drstrong.health.product.model.dto.product.ProductListInfoVO;
import com.drstrong.health.product.model.request.product.SearchWesternRequestParamBO;
import com.drstrong.health.product.model.response.result.ResultVO;
import com.drstrong.health.product.remote.api.product.ProductBussinessRemoteApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

/**
 * huangpeng
 * 2023/7/11 21:15
 */
@RestController
@RequestMapping("/inner/product/bussiness")
public class ProductBussinessController implements ProductBussinessRemoteApi {

    @Autowired
    private ProductBussinessFacade productBussinessFacade;

    /**
     * 医生端搜索商品列表(不包含健康药品)
     *
     * @param searchWesternRequestParamBO
     * @return
     */
    @Override
    public ResultVO<List<ProductListInfoVO>> searchProductList(@Valid SearchWesternRequestParamBO searchWesternRequestParamBO) {
        return ResultVO.success(productBussinessFacade.searchProductList(searchWesternRequestParamBO));
    }

    /**
     * 医生端查看药品详情(中西药+协定方)
     *
     * @param skuCode
     * @return
     */
    @Override
    public ResultVO<ProductDetailInfoVO> queryProductDetail(@Valid String skuCode) {
        return ResultVO.success(productBussinessFacade.queryProductDetail(skuCode));
    }

    /**
     * 医生端的常用药列表查询
     * @param skuCodes
     * @return
     */
    @Override
    public ResultVO<List<FrequentlyUsedProductInfoVO>> getFrequentlyUsedProductList(Set<String> skuCodes){
        return ResultVO.success(productBussinessFacade.getFrequentlyUsedProductList(skuCodes));
    }


}
