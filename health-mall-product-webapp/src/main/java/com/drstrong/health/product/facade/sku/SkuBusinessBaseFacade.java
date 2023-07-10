package com.drstrong.health.product.facade.sku;


import com.drstrong.health.product.model.dto.sku.SkuInfoSummaryDTO;
import com.drstrong.health.product.model.enums.ProductTypeEnum;
import com.drstrong.health.product.model.request.sku.SkuQueryRequest;

/**
 * sku的公共方法入口，根据不同的商品类型交给不同的实现类处理
 *
 * @author liuqiuyi
 * @date 2023/6/20 10:14
 */
public interface SkuBusinessBaseFacade {

    /**
     * 返回每个处理类的商品类型
     *
     * @author liuqiuyi
     * @date 2023/6/20 10:41
     */
    ProductTypeEnum getProductType();

    /**
     * 根据入参查询sku信息
     *
     * @author liuqiuyi
     * @date 2023/6/20 10:29
     */
    SkuInfoSummaryDTO querySkuByParam(SkuQueryRequest skuQueryRequest);

    /**
     * 根据skuCode查询
     *
     * @author liuqiuyi
     * @date 2023/6/20 14:55
     */
    SkuInfoSummaryDTO queryBySkuCode(String skuCode);
}
