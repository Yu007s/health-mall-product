package com.drstrong.health.product.service.chinese;

import com.baomidou.mybatisplus.extension.service.IService;
import com.drstrong.health.product.model.entity.chinese.ChineseSkuSupplierRelevanceEntity;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 中药sku和供应商关联表 服务类
 * </p>
 *
 * @author mybatis plus generator
 * @since 2022-08-01
 */
public interface ChineseSkuSupplierRelevanceService extends IService<ChineseSkuSupplierRelevanceEntity> {
    /**
     * 根据 skuCode 集合查询关联关系
     *
     * @param skuCodes skuCode 集合
     * @return 店铺和供应商的关联关系
     * @author liuqiuyi
     * @date 2022/8/1 14:39
     */
    List<ChineseSkuSupplierRelevanceEntity> listQueryBySkuCodeList(Set<String> skuCodes);
}
