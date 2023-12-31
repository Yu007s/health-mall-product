package com.drstrong.health.product.service.chinese.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.drstrong.health.common.enums.AgencyIdEnum;
import com.drstrong.health.product.dao.chinese.ChineseSkuInfoMapper;
import com.drstrong.health.product.facade.ChineseRemoteFacade;
import com.drstrong.health.product.model.dto.SupplierChineseSkuDTO;
import com.drstrong.health.product.model.entity.chinese.ChineseSkuInfoEntity;
import com.drstrong.health.product.model.entity.chinese.ChineseSkuSupplierRelevanceEntity;
import com.drstrong.health.product.model.entity.chinese.ChineseSpuInfoEntity;
import com.drstrong.health.product.model.entity.product.SkuInfoEntity;
import com.drstrong.health.product.model.enums.DelFlagEnum;
import com.drstrong.health.product.model.enums.DosageTypeEnum;
import com.drstrong.health.product.model.enums.ErrorEnums;
import com.drstrong.health.product.model.enums.ProductStateEnum;
import com.drstrong.health.product.model.enums.ProductTypeEnum;
import com.drstrong.health.product.model.request.chinese.ChineseManagerSkuRequest;
import com.drstrong.health.product.model.request.chinese.UpdateSkuStateRequest;
import com.drstrong.health.product.model.request.store.AgencyStoreVO;
import com.drstrong.health.product.model.response.chinese.SaveOrUpdateSkuVO;
import com.drstrong.health.product.model.response.result.BusinessException;
import com.drstrong.health.product.remote.api.chinese.ChineseRemoteApi;
import com.drstrong.health.product.remote.model.SkuChineseAgencyDTO;
import com.drstrong.health.product.remote.pro.StockRemoteProService;
import com.drstrong.health.product.service.chinese.ChineseSkuInfoService;
import com.drstrong.health.product.service.chinese.ChineseSkuSupplierRelevanceService;
import com.drstrong.health.product.service.chinese.ChineseSpuInfoService;
import com.drstrong.health.product.service.product.SkuInfoService;
import com.drstrong.health.product.util.BigDecimalUtil;
import com.drstrong.health.product.utils.UniqueCodeUtils;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toMap;

/**
 * <p>
 * 中药 sku 信息表 服务实现类
 * </p>
 *
 * @author mybatis plus generator
 * @since 2022-08-01
 */
@Slf4j
@Service
public class ChineseSkuInfoServiceImpl extends ServiceImpl<ChineseSkuInfoMapper, ChineseSkuInfoEntity> implements ChineseSkuInfoService {
    @Resource
    ChineseSkuInfoMapper chineseSkuInfoMapper;

    @Resource
    ChineseSpuInfoService chineseSpuInfoService;

    @Resource
    SkuInfoService skuInfoService;

    @Resource
    ChineseSkuSupplierRelevanceService chineseSkuSupplierRelevanceService;

    @Resource
    StockRemoteProService stockRemoteProService;

    @Resource
    ChineseRemoteFacade chineseRemoteFacade;

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
     * 根据条件查询 sku 信息，供导出使用
     *
     * @param queryParam 查询的条件
     * @return sku 信息
     * @author liuqiuyi
     * @date 2022/8/1 14:20
     */
    @Override
    public List<ChineseSkuInfoEntity> listQuerySkuByRequest(ChineseManagerSkuRequest queryParam) {
        return chineseSkuInfoMapper.listQuerySkuByRequest(queryParam);
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
        if (StringUtils.isBlank(medicineCode) || Objects.isNull(storeId)) {
            return null;
        }
        LambdaQueryWrapper<ChineseSkuInfoEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ChineseSkuInfoEntity::getDelFlag, DelFlagEnum.UN_DELETED.getCode())
                .eq(ChineseSkuInfoEntity::getMedicineCode, medicineCode)
                .eq(ChineseSkuInfoEntity::getStoreId, storeId)
                .last("limit 1");
        return getOne(queryWrapper);
    }

    /**
     * 根据 skuName 和店铺 id 获取 sku 信息 (忽略自己本身)
     * <p> skuName 完全匹配,用于判断店铺中 skuName 是否重复,重复抛出异常 </>
     *
     * @param skuName sku 名称
     * @param storeId 店铺 id
     * @param skuCode sku编码，如果名称和自己重复，忽略
     * @author liuqiuyi
     * @date 2022/8/18 10:33
     */
    @Override
    public void checkSkuNameIsRepeat(String skuName, Long storeId, String skuCode) {
        if (StringUtils.isBlank(skuName) || Objects.isNull(storeId)) {
            throw new BusinessException(ErrorEnums.PARAM_IS_NOT_NULL);
        }
        LambdaQueryWrapper<ChineseSkuInfoEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ChineseSkuInfoEntity::getDelFlag, DelFlagEnum.UN_DELETED.getCode())
                .eq(ChineseSkuInfoEntity::getSkuName, skuName)
                .eq(ChineseSkuInfoEntity::getStoreId, storeId)
                .last("limit 1");
        ChineseSkuInfoEntity skuInfoEntity = getOne(queryWrapper);
        if (Objects.nonNull(skuInfoEntity) && ObjectUtil.notEqual(skuInfoEntity.getSkuCode(), skuCode)) {
            throw new BusinessException(ErrorEnums.SKU_NAME_IS_REPEAT);
        }
    }

    /**
     * 根据药材 code 集合和店铺id，获取中药信息
     * <p> 获取的是已经上架的 </>
     *
     * @param medicineCodes 中药编码集合
     * @param storeId       店铺 id
     * @return 中药材信息集合
     * @author liuqiuyi
     * @date 2022/8/3 19:39
     */
    @Override
    public List<ChineseSkuInfoEntity> listByMedicineCodeAndStoreId(Set<String> medicineCodes, Long storeId) {
        if (CollectionUtils.isEmpty(medicineCodes) || Objects.isNull(storeId)) {
            return Lists.newArrayList();
        }
        LambdaQueryWrapper<ChineseSkuInfoEntity> queryWrapper = Wrappers.<ChineseSkuInfoEntity>lambdaQuery()
                .eq(ChineseSkuInfoEntity::getDelFlag, DelFlagEnum.UN_DELETED.getCode())
                .in(ChineseSkuInfoEntity::getMedicineCode, medicineCodes)
                .eq(ChineseSkuInfoEntity::getStoreId, storeId)
                .eq(ChineseSkuInfoEntity::getSkuStatus, ProductStateEnum.HAS_PUT.getCode());
        return list(queryWrapper);
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
        if (StringUtils.isBlank(skuCode)) {
            throw new BusinessException(ErrorEnums.CHINESE_MEDICINE_IS_NULL);
        }
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
     * 根据 medicineCodes 集合查询 sku 信息
     *
     * @param medicineCodes 药材编号
     * @return sku 信息
     * @author liuqiuyi
     * @date 2022/8/1 16:15
     */
    @Override
    public List<ChineseSkuInfoEntity> listByMedicineCodes(Set<String> medicineCodes) {
        if (CollectionUtils.isEmpty(medicineCodes)) {
            return Lists.newArrayList();
        }
        LambdaQueryWrapper<ChineseSkuInfoEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ChineseSkuInfoEntity::getDelFlag, DelFlagEnum.UN_DELETED.getCode())
                .in(ChineseSkuInfoEntity::getMedicineCode, medicineCodes);
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
    public String saveSku(SaveOrUpdateSkuVO saveOrUpdateSkuVO){
        // 1.校验 spu 是否存在，如果不存在，生成 spu 信息
        ChineseSpuInfoEntity spuInfoEntity = chineseSpuInfoService.getByMedicineCode(saveOrUpdateSkuVO.getMedicineCode(), saveOrUpdateSkuVO.getStoreId());
        String spuCode;
        if (Objects.isNull(spuInfoEntity)) {
            spuCode = chineseSpuInfoService.saveChineseSpu(saveOrUpdateSkuVO);
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
                .price(BigDecimalUtil.Y2F(saveOrUpdateSkuVO.getPrice()))
                .skuStatus(ProductStateEnum.UN_PUT.getCode())
                .dosageType(saveOrUpdateSkuVO.getDosageType())
                .dosageValue(Objects.equals(DosageTypeEnum.MULTIPLE.getCode(), saveOrUpdateSkuVO.getDosageType()) ? saveOrUpdateSkuVO.getDosageValue() : 0)
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
        stockRemoteProService.saveOrUpdateStockInfo(skuCode, saveOrUpdateSkuVO);
        return skuCode;
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
    public String updateSku(SaveOrUpdateSkuVO saveOrUpdateSkuVO) {
        String skuCode = saveOrUpdateSkuVO.getSkuCode();
        // 1.根据 skuCode 更新中药 sku 表
        ChineseSkuInfoEntity chineseSkuInfoEntity = ChineseSkuInfoEntity.builder()
                .skuName(saveOrUpdateSkuVO.getSkuName())
                .oldMedicineId(saveOrUpdateSkuVO.getMedicineId())
                .medicineCode(saveOrUpdateSkuVO.getMedicineCode())
                .storeId(saveOrUpdateSkuVO.getStoreId())
                .price(BigDecimalUtil.Y2F(saveOrUpdateSkuVO.getPrice()))
                .dosageType(saveOrUpdateSkuVO.getDosageType())
                .dosageValue(Objects.equals(DosageTypeEnum.MULTIPLE.getCode(), saveOrUpdateSkuVO.getDosageType()) ? saveOrUpdateSkuVO.getDosageValue() : 0)
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
        chineseSpuInfoService.updateMedicineCodeBySpuCode(skuInfoEntity.getSpuCode(), saveOrUpdateSkuVO);
        stockRemoteProService.saveOrUpdateStockInfo(skuCode, saveOrUpdateSkuVO);
        return skuCode;
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
                .set(ChineseSkuInfoEntity::getSkuStatus, updateSkuStateRequest.getSkuState())
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
        LambdaQueryWrapper<ChineseSkuInfoEntity> queryWrapper = Wrappers.<ChineseSkuInfoEntity>lambdaQuery()
                .eq(ChineseSkuInfoEntity::getDelFlag, DelFlagEnum.UN_DELETED.getCode())
                .eq(ChineseSkuInfoEntity::getMedicineCode, medicineCode)
                .last("limit 1");

        return Objects.nonNull(getOne(queryWrapper));
    }

    /**
     * 根据 skuCode 或者 药材 id,查询中药 sku 信息
     * <p> 仅支持查询同一店铺的 sku 信息 </>
     *
     * @param skuCodeList    sku编码信息
     * @param medicineIdList 药材 id
     * @param storeId        店铺 id
     * @return sku 信息
     * @author liuqiuyi
     * @date 2022/8/4 14:42
     */
    @Override
    public List<ChineseSkuInfoEntity> queryStoreSkuByCodesOrMedicineIds(Set<String> skuCodeList, Set<Long> medicineIdList, Long storeId) {
        if ((CollectionUtils.isEmpty(skuCodeList) && (CollectionUtils.isEmpty(medicineIdList)) || Objects.isNull(storeId))) {
            return Lists.newArrayList();
        }
        LambdaQueryWrapper<ChineseSkuInfoEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ChineseSkuInfoEntity::getDelFlag, DelFlagEnum.UN_DELETED.getCode())
                .eq(ChineseSkuInfoEntity::getStoreId, storeId);
        if (!CollectionUtils.isEmpty(skuCodeList)) {
            queryWrapper.in(ChineseSkuInfoEntity::getSkuCode, skuCodeList);
        } else {
            queryWrapper.in(ChineseSkuInfoEntity::getOldMedicineId, medicineIdList);
        }
        return list(queryWrapper);
    }

    /**
     * 供应商中药库存页面，列表查询接口,提供给供应商远程调用
     *
     * @param queryParam 参数
     * @author liuqiuyi
     * @date 2022/8/5 10:25
     */
    @Override
    public Page<SupplierChineseSkuDTO> pageSupplierChineseManagerSku(ChineseManagerSkuRequest queryParam) {
        Page<SupplierChineseSkuDTO> entityPage = new Page<>(queryParam.getPageNo(), queryParam.getPageSize());
        return chineseSkuInfoMapper.pageSupplierChineseManagerSku(entityPage, queryParam);
    }

    /**
     * 供应商中药库存页面，列表导出接口,提供给供应商远程调用
     *
     * @param queryParam
     * @author liuqiuyi
     * @date 2022/8/5 10:40
     */
    @Override
    public List<SupplierChineseSkuDTO> listSupplierChineseManagerSkuExport(ChineseManagerSkuRequest queryParam) {
        return chineseSkuInfoMapper.listSupplierChineseManagerSkuExport(queryParam);
    }

    /**
     * 根据skuCodes逻辑删除
     *
     * @param skuCodes sku编号
     * @author liuqiuyi
     * @date 2022/10/31 14:32
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteSkuInfoBySkuCodes(Set<String> skuCodes, Long operatorId) {
        if (CollectionUtils.isEmpty(skuCodes) || Objects.isNull(operatorId)) {
            log.info("invoke ChineseSkuInfoServiceImpl.deleteSkuInfoBySkuCodes() param is null,param:{},{}", JSONUtil.toJsonStr(skuCodes), operatorId);
            return;
        }
        lambdaUpdate()
                .in(ChineseSkuInfoEntity::getSkuCode, skuCodes)
                .eq(ChineseSkuInfoEntity::getDelFlag, DelFlagEnum.UN_DELETED.getCode())
                .set(ChineseSkuInfoEntity::getDelFlag, DelFlagEnum.IS_DELETED.getCode())
                .set(ChineseSkuInfoEntity::getChangedBy, operatorId)
                .update();
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


    @Override
    public List<SkuChineseAgencyDTO> listSkuChineseAgencyDTO(Integer skuStatus, Collection<Long> medicineIds) {
        List<SkuChineseAgencyDTO> chineseAgencyDTOS = chineseSkuInfoMapper.listSkuChineseAgencyDTO(skuStatus,medicineIds);
        if (CollectionUtils.isEmpty(chineseAgencyDTOS))return Collections.emptyList();
        List<AgencyStoreVO>  agencyStoreVOList = chineseRemoteFacade.listStoreByAgencyIds(Arrays.stream(AgencyIdEnum.values()).map(i->Long.valueOf(i.getCode())).collect(Collectors.toSet()));
        Map<Long,Long> storeAgencyMap =  Optional.ofNullable(agencyStoreVOList).orElse(Collections.emptyList()).stream().collect(toMap(AgencyStoreVO::getStoreId, AgencyStoreVO::getAgencyId, (v1, v2) -> v1));
        chineseAgencyDTOS.forEach(i->{
            i.setAgencyId(storeAgencyMap.get(i.getStoreId()));
        });
        return chineseAgencyDTOS;
    }
}
