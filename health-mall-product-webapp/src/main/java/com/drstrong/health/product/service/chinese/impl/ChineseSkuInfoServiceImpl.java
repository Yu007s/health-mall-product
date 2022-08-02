package com.drstrong.health.product.service.chinese.impl;

import cn.strong.mybatis.plus.extend.CustomServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.drstrong.health.product.dao.chinese.ChineseSkuInfoMapper;
import com.drstrong.health.product.model.entity.chinese.ChineseSkuInfoEntity;
import com.drstrong.health.product.model.entity.chinese.ChineseSkuSupplierRelevanceEntity;
import com.drstrong.health.product.model.entity.chinese.ChineseSpuInfoEntity;
import com.drstrong.health.product.model.entity.product.SkuInfoEntity;
import com.drstrong.health.product.model.enums.DelFlagEnum;
import com.drstrong.health.product.model.enums.ErrorEnums;
import com.drstrong.health.product.model.enums.ProductStateEnum;
import com.drstrong.health.product.model.enums.ProductTypeEnum;
import com.drstrong.health.product.model.request.chinese.ChineseManagerSkuRequest;
import com.drstrong.health.product.model.request.chinese.UpdateSkuStateRequest;
import com.drstrong.health.product.model.response.chinese.SaveOrUpdateSkuVO;
import com.drstrong.health.product.model.response.result.BusinessException;
import com.drstrong.health.product.service.chinese.ChineseSkuInfoService;
import com.drstrong.health.product.service.chinese.ChineseSkuSupplierRelevanceService;
import com.drstrong.health.product.service.chinese.ChineseSpuInfoService;
import com.drstrong.health.product.service.product.SkuInfoService;
import com.drstrong.health.product.utils.UniqueCodeUtils;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.Set;

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

    @Resource
    SkuInfoService skuInfoService;

    @Resource
    ChineseSkuSupplierRelevanceService chineseSkuSupplierRelevanceService;

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
     * 根据 skuCode 集合查询 sku 信息
     *
     * @param skuCodes sku 编码
     * @return sku 信息
     * @author liuqiuyi
     * @date 2022/8/1 16:15
     */
    @Override
    public List<ChineseSkuInfoEntity> listBySkuCode(Set<String> skuCodes) {
        if (CollectionUtils.isEmpty(skuCodes)) {
            return Lists.newArrayList();
        }
        LambdaQueryWrapper<ChineseSkuInfoEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ChineseSkuInfoEntity::getDelFlag, DelFlagEnum.UN_DELETED.getCode())
                .in(ChineseSkuInfoEntity::getSkuCode, skuCodes);
        return list(queryWrapper);
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
            spuCode = chineseSpuInfoService.saveChineseSpu(saveOrUpdateSkuVO.getMedicineCode(), saveOrUpdateSkuVO.getMedicineName(), saveOrUpdateSkuVO.getOperatorId());
        } else {
            spuCode = UniqueCodeUtils.getNextSpuCode(ProductTypeEnum.CHINESE);
        }
        String skuCode = UniqueCodeUtils.getNextSkuCode(spuCode, saveOrUpdateSkuVO.getMedicineId());
        // 2.保存中药sku信息
        ChineseSkuInfoEntity chineseSkuInfoEntity = ChineseSkuInfoEntity.builder()
                .skuCode(skuCode)
                .skuName(saveOrUpdateSkuVO.getSkuName())
                .oldMedicineId(saveOrUpdateSkuVO.getMedicineId())
                .medicineCode(saveOrUpdateSkuVO.getMedicineCode())
                .storeId(saveOrUpdateSkuVO.getStoreId())
                .price(saveOrUpdateSkuVO.getPrice())
                .skuState(ProductStateEnum.UN_PUT.getCode())
                .build();
        chineseSkuInfoEntity.setCreatedBy(saveOrUpdateSkuVO.getOperatorId());
        chineseSkuInfoEntity.setChangedBy(saveOrUpdateSkuVO.getOperatorId());
        // 3.保存sku主表
        SkuInfoEntity infoEntity = SkuInfoEntity.builder()
                .skuCode(skuCode)
                .skuType(ProductTypeEnum.CHINESE.getCode())
                .spuCode(spuCode)
                .build();
        infoEntity.setCreatedBy(saveOrUpdateSkuVO.getOperatorId());
        infoEntity.setChangedBy(saveOrUpdateSkuVO.getOperatorId());
        // 4.保存中药sku和供应商的关联关系
        List<ChineseSkuSupplierRelevanceEntity> relevanceEntityList = buildChineseSkuSupplierRelevanceList(saveOrUpdateSkuVO, skuCode);
        // 5.保存数据
        save(chineseSkuInfoEntity);
        skuInfoService.save(infoEntity);
        chineseSkuSupplierRelevanceService.saveBatch(relevanceEntityList);
        // TODO 调用世轩的供应商接口，如果远程接口报错，抛出异常
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
    public void updateSku(SaveOrUpdateSkuVO saveOrUpdateSkuVO) {
        String skuCode = saveOrUpdateSkuVO.getSkuCode();
        // 1.根据 skuCode 更新中药 sku 表
        ChineseSkuInfoEntity chineseSkuInfoEntity = ChineseSkuInfoEntity.builder()
                .skuName(saveOrUpdateSkuVO.getSkuName())
                .oldMedicineId(saveOrUpdateSkuVO.getMedicineId())
                .medicineCode(saveOrUpdateSkuVO.getMedicineCode())
                .storeId(saveOrUpdateSkuVO.getStoreId())
                .price(saveOrUpdateSkuVO.getPrice())
                .build();
        chineseSkuInfoEntity.setChangedBy(saveOrUpdateSkuVO.getOperatorId());

        LambdaQueryWrapper<ChineseSkuInfoEntity> update = new LambdaQueryWrapper<>();
        update.eq(ChineseSkuInfoEntity::getSkuCode, skuCode).eq(ChineseSkuInfoEntity::getDelFlag, DelFlagEnum.UN_DELETED.getCode());
        boolean updateFlag = update(chineseSkuInfoEntity, update);
        if (!updateFlag) {
            throw new BusinessException(ErrorEnums.SKU_IS_NULL);
        }
        // 2.更新供应商关系表，先删除，后新增
        chineseSkuSupplierRelevanceService.deleteBySkuCode(skuCode, saveOrUpdateSkuVO.getOperatorId());
        List<ChineseSkuSupplierRelevanceEntity> relevanceEntityList = buildChineseSkuSupplierRelevanceList(saveOrUpdateSkuVO, skuCode);
        chineseSkuSupplierRelevanceService.saveBatch(relevanceEntityList);
        // 3.将中药的 spu 中的药材 code 进行更新
        SkuInfoEntity skuInfoEntity = skuInfoService.getBySkuCode(skuCode);
        chineseSpuInfoService.updateMedicineCodeBySpuCode(skuInfoEntity.getSpuCode(), saveOrUpdateSkuVO.getMedicineCode(), saveOrUpdateSkuVO.getOperatorId());
        // TODO 调用世轩的供应商接口，如果远程接口报错，抛出异常
    }

    /**
     * 根据 skuCode 批量更新sku状态
     *
     * @param updateSkuStateRequest 参数
     * @author liuqiuyi
     * @date 2022/8/2 14:41
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateSkuStatue(UpdateSkuStateRequest updateSkuStateRequest) {
        lambdaUpdate()
                .in(ChineseSkuInfoEntity::getSkuCode, updateSkuStateRequest.getSkuCodeList())
                .eq(ChineseSkuInfoEntity::getDelFlag, DelFlagEnum.UN_DELETED.getCode())
                .set(ChineseSkuInfoEntity::getSkuState, updateSkuStateRequest.getSkuState())
                .set(ChineseSkuInfoEntity::getChangedBy, updateSkuStateRequest.getOperatorId())
                .update();
    }

    /**
     * 根据药材code判断是否有关联某个sku
     *
     * @param medicineCode 药材编码
     * @return boolean 类型，true-存在，false-不存在
     * @author liuqiuyi
     * @date 2022/8/2 14:48
     */
    @Override
    public Boolean checkHasChineseByMedicineCode(String medicineCode) {
        if (StringUtils.isBlank(medicineCode)) {
            throw new BusinessException(ErrorEnums.PARAM_IS_NOT_NULL);
        }
        LambdaQueryChainWrapper<ChineseSkuInfoEntity> chainWrapper = lambdaQuery()
                .eq(ChineseSkuInfoEntity::getDelFlag, DelFlagEnum.UN_DELETED.getCode())
                .eq(ChineseSkuInfoEntity::getMedicineCode, medicineCode)
                .last("limit 1");

        return Objects.nonNull(getOne(chainWrapper));
    }

    private List<ChineseSkuSupplierRelevanceEntity> buildChineseSkuSupplierRelevanceList(SaveOrUpdateSkuVO saveOrUpdateSkuVO, String skuCode) {
        List<ChineseSkuSupplierRelevanceEntity> relevanceEntityList = Lists.newArrayListWithCapacity(saveOrUpdateSkuVO.getSupplierInfoList().size());
        saveOrUpdateSkuVO.getSupplierInfoList().forEach(supplierInfo -> {
            ChineseSkuSupplierRelevanceEntity supplierRelevanceEntity = ChineseSkuSupplierRelevanceEntity.builder()
                    .skuCode(skuCode)
                    .storeId(saveOrUpdateSkuVO.getStoreId())
                    .supplierId(supplierInfo.getSupplierId())
                    .build();
            supplierRelevanceEntity.setCreatedBy(saveOrUpdateSkuVO.getOperatorId());
            supplierRelevanceEntity.setChangedBy(saveOrUpdateSkuVO.getOperatorId());
            relevanceEntityList.add(supplierRelevanceEntity);
        });
        return relevanceEntityList;
    }
}
