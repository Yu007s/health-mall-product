package com.drstrong.health.product.service.product;

import com.baomidou.mybatisplus.extension.service.IService;
import com.drstrong.health.product.model.entity.product.SpuInfoEntity;
import com.drstrong.health.product.model.enums.ProductTypeEnum;

/**
 * <p>
 * spu信息表 服务类
 * </p>
 *
 * @author mybatis plus generator
 * @since 2022-08-01
 */
public interface SpuInfoService extends IService<SpuInfoEntity> {
    /**
     * 保存 spu 信息
     *
     * @param spuCode         spu编码
     * @param productTypeEnum 商品类型
     * @param createdBy       创建人
     * @author liuqiuyi
     * @date 2022/8/1 22:17
     */
    void saveSpuInfo(String spuCode, ProductTypeEnum productTypeEnum, String createdBy);
}
