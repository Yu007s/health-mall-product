package com.drstrong.health.product.service.chinese.impl;

import cn.strong.mybatis.plus.extend.CustomServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.drstrong.health.product.dao.chinese.ChineseSkuInfoMapper;
import com.drstrong.health.product.model.entity.chinese.ChineseSkuInfoEntity;
import com.drstrong.health.product.model.entity.chinese.ChineseSpuInfoEntity;
import com.drstrong.health.product.model.entity.product.SkuInfoEntity;
import com.drstrong.health.product.model.enums.DelFlagEnum;
import com.drstrong.health.product.model.enums.ProductStateEnum;
import com.drstrong.health.product.model.enums.ProductTypeEnum;
import com.drstrong.health.product.model.request.chinese.ChineseManagerSkuRequest;
import com.drstrong.health.product.model.response.chinese.SaveOrUpdateSkuVO;
import com.drstrong.health.product.service.chinese.ChineseSkuInfoService;
import com.drstrong.health.product.service.chinese.ChineseSpuInfoService;
import com.drstrong.health.product.utils.UniqueCodeUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * <p>
 * 中药 sku 信息表 服务实现类
 * </p>
 *
 * @author mybatis plus generator
 * @since 2022-08-01
 */
@Service
public class ChineseSkuInfoServiceImpl extends CustomServiceImpl<ChineseSkuInfoMapper, ChineseSkuInfoEntity> implements ChineseSkuInfoService {
    @Resource
    ChineseSkuInfoMapper chineseSkuInfoMapper;

    @Resource
    ChineseSpuInfoService chineseSpuInfoService;

    /**
     * 根据条件分页查询 sku 信息
     *
     * @param queryParam 查询的条件
     * @return sku 信息
     * @author liuqiuyi
     * @date 2022/8/1 14:20
     */
    @Override
    public Page<ChineseSkuInfoEntity> pageQuerySkuByRequest(ChineseManagerSkuRequest queryParam) {
        Page<ChineseSkuInfoEntity> entityPage = new Page<>(queryParam.getPageNo(), queryParam.getPageSize());
        return chineseSkuInfoMapper.pageQuerySkuByRequest(entityPage, queryParam);
    }

    /**
     * 根据店铺id和药材code获取sku信息
     *
     * @param medicineCode 药材 code
     * @param storeId      店铺id
     * @return sku信息
     * @author liuqiuyi
     * @date 2022/8/1 15:56
     */
    @Override
    public ChineseSkuInfoEntity getByMedicineCodeAndStoreId(String medicineCode, Long storeId) {
        LambdaQueryWrapper<ChineseSkuInfoEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ChineseSkuInfoEntity::getDelFlag, DelFlagEnum.UN_DELETED.getCode())
                .eq(StringUtils.isNoneBlank(medicineCode), ChineseSkuInfoEntity::getMedicineCode, medicineCode)
                .eq(Objects.nonNull(storeId), ChineseSkuInfoEntity::getStoreId, storeId)
                .last("limit 1");
        return getOne(queryWrapper);
    }

    /**
     * 根据 skuCode 查询 sku 信息
     *
     * @param skuCode sku 编码
     * @return sku 信息
     * @author liuqiuyi
     * @date 2022/8/1 16:15
     */
    @Override
    public ChineseSkuInfoEntity getBySkuCode(String skuCode) {
        LambdaQueryWrapper<ChineseSkuInfoEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ChineseSkuInfoEntity::getDelFlag, DelFlagEnum.UN_DELETED.getCode())
                .eq(StringUtils.isNoneBlank(skuCode), ChineseSkuInfoEntity::getSkuCode, skuCode)
                .last("limit 1");
        return getOne(queryWrapper);
    }

    /**
     * 保存sku信息
     *
     * @param saveOrUpdateSkuVO 接口入参
     * @author liuqiuyi
     * @date 2022/8/1 15:44
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveSku(SaveOrUpdateSkuVO saveOrUpdateSkuVO){
        // 1.校验 spu 是否存在，如果不存在，生成 spu 信息
        ChineseSpuInfoEntity spuInfoEntity = chineseSpuInfoService.getByMedicineCode(saveOrUpdateSkuVO.getMedicineCode());
        String spuCode;
        if (Objects.isNull(spuInfoEntity)) {
            spuCode = chineseSpuInfoService.saveChineseSpu(saveOrUpdateSkuVO.getMedicineCode(), saveOrUpdateSkuVO.getMedicineName(), saveOrUpdateSkuVO.getCreatedBy());
        } else {
            spuCode = UniqueCodeUtils.getNextSpuCode(ProductTypeEnum.CHINESE);
        }
        String skuCode = UniqueCodeUtils.getNextSkuCode(spuCode, saveOrUpdateSkuVO.getMedicineId());
        // 1.保存中药sku信息
        ChineseSkuInfoEntity saveSkuInfoEntity = ChineseSkuInfoEntity.builder()
                .skuCode(skuCode)
                .skuName(saveOrUpdateSkuVO.getSkuName())
                .oldMedicineId(saveOrUpdateSkuVO.getMedicineId())
                .medicineCode(saveOrUpdateSkuVO.getMedicineCode())
                .storeId(saveOrUpdateSkuVO.getStoreId())
                .price(saveOrUpdateSkuVO.getPrice())
                .skuState(ProductStateEnum.UN_PUT.getCode())
                .build();
        saveSkuInfoEntity.setCreatedBy(saveOrUpdateSkuVO.getCreatedBy());
        saveSkuInfoEntity.setChangedBy(saveOrUpdateSkuVO.getCreatedBy());
        // 2.保存sku主表
        SkuInfoEntity infoEntity = SkuInfoEntity.builder()
                .skuCode(skuCode)
                .skuType(ProductTypeEnum.CHINESE.getCode())
                .spuCode(spuCode)
                .build();
        infoEntity.setCreatedBy(saveOrUpdateSkuVO.getCreatedBy());
        infoEntity.setChangedBy(saveOrUpdateSkuVO.getCreatedBy());

        save(saveSkuInfoEntity);
    }

    /**
     * 更新sku信息
     *
     * @param saveOrUpdateSkuVO 接口入参
     * @author liuqiuyi
     * @date 2022/8/1 15:44
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateSku(Long id, SaveOrUpdateSkuVO saveOrUpdateSkuVO) {


    }
}
