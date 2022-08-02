package com.drstrong.health.product.service.product;

import com.drstrong.health.product.model.entity.product.SkuInfoEntity;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * sku信息表 服务类
 * </p>
 *
 * @author mybatis plus generator
 * @since 2022-08-01
 */
public interface SkuInfoService extends IService<SkuInfoEntity> {
    /**
     * 根据 skuCode 获取sku主表信息
     *
     * @param skuCode sku 编码
     * @return sku 信息
     * @author liuqiuyi
     * @date 2022/8/2 11:29
     */
    SkuInfoEntity getBySkuCode(String skuCode);
}
