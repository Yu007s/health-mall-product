package com.drstrong.health.product.facade.product;

import com.drstrong.health.product.model.dto.product.FrequentlyUsedProductInfoVO;
import com.drstrong.health.product.model.dto.product.ProductDetailInfoVO;
import com.drstrong.health.product.model.dto.product.ProductListInfoVO;
import com.drstrong.health.product.model.request.product.SearchWesternRequestParamBO;

import java.util.List;
import java.util.Set;

/**
 * huangpeng
 * 2023/7/11 21:03
 */
public interface ProductBussinessFacade {

    List<ProductListInfoVO> searchProductList(SearchWesternRequestParamBO searchWesternRequestParamBO);

    ProductDetailInfoVO queryProductDetail(String skuCode);

    List<FrequentlyUsedProductInfoVO> getFrequentlyUsedProductList(Set<String> skuCodes);
}
