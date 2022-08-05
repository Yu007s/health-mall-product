package com.drstrong.health.product.service.product.impl;

import cn.strong.mybatis.plus.extend.CustomServiceImpl;
import com.drstrong.health.product.dao.product.SpuInfoMapper;
import com.drstrong.health.product.model.entity.product.SpuInfoEntity;
import com.drstrong.health.product.model.enums.ErrorEnums;
import com.drstrong.health.product.model.enums.ProductTypeEnum;
import com.drstrong.health.product.model.response.result.BusinessException;
import com.drstrong.health.product.service.product.SpuInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/**
 * <p>
 * spu信息表 服务实现类
 * </p>
 *
 * @author mybatis plus generator
 * @since 2022-08-01
 */
@Slf4j
@Service
public class SpuInfoServiceImpl extends CustomServiceImpl<SpuInfoMapper, SpuInfoEntity> implements SpuInfoService {

    /**
     * 保存 spu 信息
     *
     * @param spuCode         spu编码
     * @param productTypeEnum 商品类型
     * @param createdBy       创建人
     * @author liuqiuyi
     * @date 2022/8/1 22:17
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveSpuInfo(String spuCode, ProductTypeEnum productTypeEnum, Long createdBy) {
        if (StringUtils.isBlank(spuCode) || Objects.isNull(productTypeEnum) || Objects.isNull(createdBy)) {
            throw new BusinessException(ErrorEnums.PARAM_IS_NOT_NULL);
        }
        SpuInfoEntity spuInfoEntity = SpuInfoEntity.builder()
                .spuCode(spuCode)
                .spuType(productTypeEnum.getCode())
                .build();
        spuInfoEntity.setCreatedBy(createdBy);
        spuInfoEntity.setChangedBy(createdBy);
        save(spuInfoEntity);
    }
}
