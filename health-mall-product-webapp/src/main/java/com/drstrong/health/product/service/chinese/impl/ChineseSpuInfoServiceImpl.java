package com.drstrong.health.product.service.chinese.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.drstrong.health.product.model.entity.chinese.ChineseSpuInfoEntity;
import com.drstrong.health.product.dao.chinese.ChineseSpuInfoMapper;
import com.drstrong.health.product.model.enums.DelFlagEnum;
import com.drstrong.health.product.model.enums.ProductTypeEnum;
import com.drstrong.health.product.service.chinese.ChineseSpuInfoService;
import cn.strong.mybatis.plus.extend.CustomServiceImpl;
import com.drstrong.health.product.service.product.SpuInfoService;
import com.drstrong.health.product.utils.UniqueCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

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
            return null;
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
     * @param medicineCode 中药材code
     * @return spuCode
     * @author liuqiuyi
     * @date 2022/8/1 22:12
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String saveChineseSpu(String medicineCode, String name, String createdBy) {
        if (StringUtils.isBlank(medicineCode)) {
            return null;
        }
        String spuCode = UniqueCodeUtils.getNextSpuCode(ProductTypeEnum.CHINESE);
        ChineseSpuInfoEntity chineseSpuInfoEntity = ChineseSpuInfoEntity.builder()
                .spuCode(spuCode)
                .spuName(name)
                .medicineCode(medicineCode)
                .build();

        spuInfoService.saveSpuInfo(spuCode, ProductTypeEnum.CHINESE, createdBy);
        save(chineseSpuInfoEntity);
        return spuCode;
    }
}
