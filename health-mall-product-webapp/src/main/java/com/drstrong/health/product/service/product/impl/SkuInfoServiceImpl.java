package com.drstrong.health.product.service.product.impl;

import cn.strong.mybatis.plus.extend.CustomServiceImpl;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.drstrong.health.product.dao.product.SkuInfoMapper;
import com.drstrong.health.product.model.entity.product.SkuInfoEntity;
import com.drstrong.health.product.model.enums.DelFlagEnum;
import com.drstrong.health.product.model.enums.ErrorEnums;
import com.drstrong.health.product.model.response.result.BusinessException;
import com.drstrong.health.product.service.product.SkuInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

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
public class SkuInfoServiceImpl extends CustomServiceImpl<SkuInfoMapper, SkuInfoEntity> implements SkuInfoService {

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
        LambdaQueryChainWrapper<SkuInfoEntity> queryChainWrapper = lambdaQuery()
                .ge(SkuInfoEntity::getSkuCode, skuCode)
                .ge(SkuInfoEntity::getDelFlag, DelFlagEnum.UN_DELETED.getCode())
                .last("limit 1");
        return getOne(queryChainWrapper);
    }
}
