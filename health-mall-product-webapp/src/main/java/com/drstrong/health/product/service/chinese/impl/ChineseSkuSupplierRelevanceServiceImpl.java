package com.drstrong.health.product.service.chinese.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.drstrong.health.product.model.entity.chinese.ChineseSkuSupplierRelevanceEntity;
import com.drstrong.health.product.dao.chinese.ChineseSkuSupplierRelevanceMapper;
import com.drstrong.health.product.model.enums.DelFlagEnum;
import com.drstrong.health.product.service.chinese.ChineseSkuSupplierRelevanceService;
import cn.strong.mybatis.plus.extend.CustomServiceImpl;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 中药sku和供应商关联表 服务实现类
 * </p>
 *
 * @author mybatis plus generator
 * @since 2022-08-01
 */
@Service
@Slf4j
public class ChineseSkuSupplierRelevanceServiceImpl extends CustomServiceImpl<ChineseSkuSupplierRelevanceMapper, ChineseSkuSupplierRelevanceEntity> implements ChineseSkuSupplierRelevanceService {

    /**
     * 根据 skuCode 集合查询关联关系
     *
     * @param skuCodes skuCode 集合
     * @return 店铺和供应商的关联关系
     * @author liuqiuyi
     * @date 2022/8/1 14:39
     */
    @Override
    public List<ChineseSkuSupplierRelevanceEntity> listQueryBySkuCodeList(Set<String> skuCodes) {
        if (CollectionUtils.isEmpty(skuCodes)) {
            return Lists.newArrayList();
        }
        LambdaQueryWrapper<ChineseSkuSupplierRelevanceEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(ChineseSkuSupplierRelevanceEntity::getDelFlag, DelFlagEnum.UN_DELETED.getCode())
                .in(ChineseSkuSupplierRelevanceEntity::getSkuCode, skuCodes);
        return list(queryWrapper);
    }
}
