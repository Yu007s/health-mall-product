package com.drstrong.health.product.service.chinese.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.drstrong.health.product.dao.chinese.ChineseSkuSupplierRelevanceMapper;
import com.drstrong.health.product.model.entity.chinese.ChineseSkuSupplierRelevanceEntity;
import com.drstrong.health.product.model.enums.DelFlagEnum;
import com.drstrong.health.product.model.enums.ErrorEnums;
import com.drstrong.health.product.model.response.result.BusinessException;
import com.drstrong.health.product.service.chinese.ChineseSkuSupplierRelevanceService;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import static java.util.stream.Collectors.*;

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
public class ChineseSkuSupplierRelevanceServiceImpl extends ServiceImpl<ChineseSkuSupplierRelevanceMapper, ChineseSkuSupplierRelevanceEntity> implements ChineseSkuSupplierRelevanceService {

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

    /**
     * 根据 skuCode 集合查询关联关系,并组装成 map
     *
     * @param skuCodes skuCode 集合
     * @return skuCode 和供应商的关联关系
     * @author liuqiuyi
     * @date 2022/10/31 11:53
     */
    @Override
    public Map<String, Set<Long>> getSkuCodeAndSupplierIdsMap(Set<String> skuCodes) {
        return listQueryBySkuCodeList(skuCodes).stream()
                .collect(groupingBy(ChineseSkuSupplierRelevanceEntity::getSkuCode, mapping(ChineseSkuSupplierRelevanceEntity::getSupplierId, toSet())));
    }

    /**
     * 根据 skuCode 删除关联关系
     *
     * @param skuCode sku 编码
     * @author liuqiuyi
     * @date 2022/8/2 11:06
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteBySkuCode(String skuCode, Long operatorId) {
        if (StringUtils.isBlank(skuCode) || Objects.isNull(operatorId)) {
            throw new BusinessException(ErrorEnums.PARAM_IS_NOT_NULL);
        }
        lambdaUpdate()
                .eq(ChineseSkuSupplierRelevanceEntity::getSkuCode, skuCode)
                .eq(ChineseSkuSupplierRelevanceEntity::getDelFlag, DelFlagEnum.UN_DELETED.getCode())
                .set(ChineseSkuSupplierRelevanceEntity::getDelFlag, DelFlagEnum.IS_DELETED.getCode())
                .set(ChineseSkuSupplierRelevanceEntity::getChangedBy, operatorId)
                .update();
    }

    /**
     * 逻辑删除
     *
     * @param skuCodes
     * @param operatorId
     * @author liuqiuyi
     * @date 2022/10/31 14:42
     */
    @Override
    public void deleteRelevanceBySkuCodesAndSupplierId(Set<String> skuCodes, Long supplierId, Long operatorId) {
        if (CollectionUtil.isEmpty(skuCodes) || Objects.isNull(operatorId) || Objects.isNull(supplierId)) {
            log.info("invoke ChineseSkuSupplierRelevanceServiceImpl.deleteRelevanceBySkuCodes() param is null,param:{},{},{}", JSONUtil.toJsonStr(skuCodes), supplierId, operatorId);
            return;
        }
        lambdaUpdate()
                .in(ChineseSkuSupplierRelevanceEntity::getSkuCode, skuCodes)
                .eq(ChineseSkuSupplierRelevanceEntity::getDelFlag, DelFlagEnum.UN_DELETED.getCode())
                .eq(ChineseSkuSupplierRelevanceEntity::getSupplierId, supplierId)
                .set(ChineseSkuSupplierRelevanceEntity::getDelFlag, DelFlagEnum.IS_DELETED.getCode())
                .set(ChineseSkuSupplierRelevanceEntity::getChangedBy, operatorId)
                .update();
    }
}
