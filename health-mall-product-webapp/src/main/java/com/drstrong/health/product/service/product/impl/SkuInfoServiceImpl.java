package com.drstrong.health.product.service.product.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.drstrong.health.product.dao.product.SkuInfoMapper;
import com.drstrong.health.product.model.entity.product.SkuInfoEntity;
import com.drstrong.health.product.model.enums.DelFlagEnum;
import com.drstrong.health.product.model.enums.ErrorEnums;
import com.drstrong.health.product.model.response.result.BusinessException;
import com.drstrong.health.product.service.product.SkuInfoService;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

/**
 * <p>
 * sku信息表 服务实现类
 * </p>
 *
 * @author mybatis plus generator
 * @since 2022-08-01
 */
@Slf4j
@Service
public class SkuInfoServiceImpl extends ServiceImpl<SkuInfoMapper, SkuInfoEntity> implements SkuInfoService {

    /**
     * 根据 skuCode 获取sku主表信息
     *
     * @param skuCode sku 编码
     * @return sku 信息
     * @author liuqiuyi
     * @date 2022/8/2 11:29
     */
    @Override
    public SkuInfoEntity getBySkuCode(String skuCode) {
        if (StringUtils.isBlank(skuCode)) {
            throw new BusinessException(ErrorEnums.PARAM_IS_NOT_NULL);
        }
        LambdaQueryWrapper<SkuInfoEntity> queryWrapper = Wrappers.<SkuInfoEntity>lambdaQuery()
                .ge(SkuInfoEntity::getSkuCode, skuCode)
                .ge(SkuInfoEntity::getDelFlag, DelFlagEnum.UN_DELETED.getCode())
                .last("limit 1");
        return getOne(queryWrapper);
    }

    /**
     * 根据 skuCode 集合获取sku主表信息
     *
     * @param skuCodes sku 编码集合
     * @author liuqiuyi
     * @date 2022/8/15 21:49
     */
    @Override
    public List<SkuInfoEntity> getBySkuCodes(List<String> skuCodes) {
        if (CollectionUtils.isEmpty(skuCodes)) {
            return Lists.newArrayList();
        }
        LambdaQueryWrapper<SkuInfoEntity> queryWrapper = Wrappers.<SkuInfoEntity>lambdaQuery()
                .in(SkuInfoEntity::getSkuCode, skuCodes)
                .ge(SkuInfoEntity::getDelFlag, DelFlagEnum.UN_DELETED.getCode());
        return list(queryWrapper);
    }

    /**
     * 根据 skuCode 集合获取sku主表信息,转换成 map
     *
     * @param skuCodes
     * @author liuqiuyi
     * @date 2022/8/15 21:49
     */
    @Override
    public Map<String, SkuInfoEntity> getBySkuCodesToMap(List<String> skuCodes) {
        List<SkuInfoEntity> skuInfoEntityList = getBySkuCodes(skuCodes);
        return skuInfoEntityList.stream().collect(toMap(SkuInfoEntity::getSkuCode, dto -> dto, (v1, v2) -> v1));
    }
}
