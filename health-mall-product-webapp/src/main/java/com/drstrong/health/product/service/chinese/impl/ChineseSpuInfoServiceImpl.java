package com.drstrong.health.product.service.chinese.impl;

import cn.strong.mybatis.plus.extend.CustomServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.drstrong.health.product.dao.chinese.ChineseSpuInfoMapper;
import com.drstrong.health.product.model.entity.chinese.ChineseSpuInfoEntity;
import com.drstrong.health.product.model.enums.DelFlagEnum;
import com.drstrong.health.product.model.enums.ErrorEnums;
import com.drstrong.health.product.model.enums.ProductTypeEnum;
import com.drstrong.health.product.model.response.chinese.SaveOrUpdateSkuVO;
import com.drstrong.health.product.model.response.result.BusinessException;
import com.drstrong.health.product.service.chinese.ChineseSpuInfoService;
import com.drstrong.health.product.service.product.SpuInfoService;
import com.drstrong.health.product.utils.UniqueCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * <p>
 * 中药 spu 信息 服务实现类
 * </p>
 *
 * @author mybatis plus generator
 * @since 2022-08-01
 */
@Slf4j
@Service
public class ChineseSpuInfoServiceImpl extends CustomServiceImpl<ChineseSpuInfoMapper, ChineseSpuInfoEntity> implements ChineseSpuInfoService {
    @Resource
    SpuInfoService spuInfoService;

    /**
     * 根据药材 Code 获取 spu 信息
     *
     * @param medicineCode 药材code
     * @return 中药的 spu 信息
     * @author liuqiuyi
     * @date 2022/8/1 22:07
     */
    @Override
    public ChineseSpuInfoEntity getByMedicineCode(String medicineCode) {
        if (StringUtils.isBlank(medicineCode)) {
            throw new BusinessException(ErrorEnums.PARAM_IS_NOT_NULL);
        }
        LambdaQueryWrapper<ChineseSpuInfoEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ChineseSpuInfoEntity::getDelFlag, DelFlagEnum.UN_DELETED.getCode())
                .eq(ChineseSpuInfoEntity::getMedicineCode, medicineCode)
                .last("limit 1");
        return getOne(queryWrapper);
    }

    /**
     * 生成中药的 spu 信息
     *
     * @param saveOrUpdateSkuVO 保存的入参
     * @return spuCode
     * @author liuqiuyi
     * @date 2022/8/1 22:12
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String saveChineseSpu(SaveOrUpdateSkuVO saveOrUpdateSkuVO) {
        String spuCode = UniqueCodeUtils.getNextSpuCode(ProductTypeEnum.CHINESE);
        ChineseSpuInfoEntity chineseSpuInfoEntity = ChineseSpuInfoEntity.builder()
                .spuCode(spuCode)
                .spuName(saveOrUpdateSkuVO.getMedicineName())
                .medicineCode(saveOrUpdateSkuVO.getMedicineCode())
                .storeId(saveOrUpdateSkuVO.getStoreId())
                .build();
        // 保存 spu 主表信息
        spuInfoService.saveSpuInfo(spuCode, ProductTypeEnum.CHINESE, saveOrUpdateSkuVO.getOperatorId());
        // 保存中药的 spu 信息
        save(chineseSpuInfoEntity);
        return spuCode;
    }

    /**
     * 更新中药材code
     *
     * @param spuCode    spu编码
     * @param operatorId 操作人id
     * @author liuqiuyi
     * @date 2022/8/2 11:36
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateMedicineCodeBySpuCode(String spuCode, String medicineCode, Long operatorId) {
        if (StringUtils.isBlank(spuCode) || StringUtils.isBlank(medicineCode) || Objects.isNull(operatorId)) {
            throw new BusinessException(ErrorEnums.PARAM_IS_NOT_NULL);
        }
        lambdaUpdate()
                .eq(ChineseSpuInfoEntity::getSpuCode, spuCode)
                .eq(ChineseSpuInfoEntity::getDelFlag, DelFlagEnum.UN_DELETED.getCode())
                .set(ChineseSpuInfoEntity::getMedicineCode, medicineCode).update();
    }
}
