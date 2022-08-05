package com.drstrong.health.product.facade.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.drstrong.health.product.facade.ChineseManagerFacade;
import com.drstrong.health.product.model.dto.SupplierChineseSkuDTO;
import com.drstrong.health.product.model.entity.chinese.ChineseMedicineEntity;
import com.drstrong.health.product.model.entity.chinese.ChineseSkuInfoEntity;
import com.drstrong.health.product.model.entity.chinese.ChineseSkuSupplierRelevanceEntity;
import com.drstrong.health.product.model.entity.store.StoreEntity;
import com.drstrong.health.product.model.enums.ErrorEnums;
import com.drstrong.health.product.model.enums.ProductStateEnum;
import com.drstrong.health.product.model.request.chinese.ChineseManagerSkuRequest;
import com.drstrong.health.product.model.request.chinese.UpdateSkuStateRequest;
import com.drstrong.health.product.model.response.PageVO;
import com.drstrong.health.product.model.response.chinese.ChineseManagerSkuVO;
import com.drstrong.health.product.model.response.chinese.SaveOrUpdateSkuVO;
import com.drstrong.health.product.model.response.chinese.SupplierChineseManagerSkuVO;
import com.drstrong.health.product.model.response.result.BusinessException;
import com.drstrong.health.product.service.chinese.ChineseMedicineService;
import com.drstrong.health.product.service.chinese.ChineseSkuInfoService;
import com.drstrong.health.product.service.chinese.ChineseSkuSupplierRelevanceService;
import com.drstrong.health.product.service.store.StoreService;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;

import static java.util.stream.Collectors.*;

/**
 * @author liuqiuyi
 * @date 2022/8/1 11:14
 */
@Slf4j
@Service
public class ChineseManagerFacadeImpl implements ChineseManagerFacade {
    @Resource
    ChineseSkuInfoService chineseSkuInfoService;

    @Resource
    ChineseSkuSupplierRelevanceService chineseSkuSupplierRelevanceService;

    @Resource
    StoreService storeService;

    @Resource
    ChineseMedicineService chineseMedicineService;

    /**
     * 中药管理页面，列表查询
     *
     * @param skuRequest 分页查询的入参
     * @return 中药管理列表
     * @author liuqiuyi
     * @date 2022/8/1 11:16
     */
    @Override
    public PageVO<ChineseManagerSkuVO> pageChineseManagerSku(ChineseManagerSkuRequest skuRequest) {
        log.info("invoke pageChineseManagerSku() param:{}", JSON.toJSONString(skuRequest));
        // 1。根据条件查询 sku 信息
        Page<ChineseSkuInfoEntity> infoEntityPage = chineseSkuInfoService.pageQuerySkuByRequest(skuRequest);
        List<ChineseSkuInfoEntity> skuInfoEntityList = infoEntityPage.getRecords();
        if (CollectionUtils.isEmpty(skuInfoEntityList)) {
            return PageVO.buildPageVO();
        }
        // 2.组装返回值
        List<ChineseManagerSkuVO> managerSkuVOList = buildChineseManagerSku(skuInfoEntityList);
        return PageVO.buildPageVO(skuRequest.getPageNo(), skuRequest.getPageSize(), infoEntityPage.getTotal(), managerSkuVOList);
    }

    /**
     * 中药管理页面，列表查询，导出数据使用
     *
     * @param skuRequest 分页查询的入参
     * @return 中药管理列表
     * @author liuqiuyi
     * @date 2022/8/1 11:16
     */
    @Override
    public List<ChineseManagerSkuVO> listChineseManagerSkuExport(ChineseManagerSkuRequest skuRequest) {
        log.info("invoke listChineseManagerSkuExport() param:{}", JSON.toJSONString(skuRequest));
        // 1。根据条件查询 sku 信息
        List<ChineseSkuInfoEntity> infoEntityList = chineseSkuInfoService.listQuerySkuByRequest(skuRequest);
        if (CollectionUtils.isEmpty(infoEntityList)) {
            return Lists.newArrayList();
        }
        // 2.组装返回值
        return buildChineseManagerSku(infoEntityList);
    }

    private List<ChineseManagerSkuVO> buildChineseManagerSku(List<ChineseSkuInfoEntity> skuInfoEntityList) {
        Set<String> skuCodes = Sets.newHashSetWithExpectedSize(skuInfoEntityList.size());
        Set<Long> storeIds = Sets.newHashSetWithExpectedSize(skuInfoEntityList.size());
        skuInfoEntityList.forEach(chineseSkuInfoEntity -> {
            skuCodes.add(chineseSkuInfoEntity.getSkuCode());
            storeIds.add(chineseSkuInfoEntity.getStoreId());
        });
        // 2.由于一个店铺可能关联多个供应商，但是入参中只传入了一个供应商，这里需要在回查一遍店铺的供应商
        List<ChineseSkuSupplierRelevanceEntity> supplierRelevanceEntityList = chineseSkuSupplierRelevanceService.listQueryBySkuCodeList(skuCodes);
        Map<String, Set<Long>> skuCodeSupplierIdsMap = supplierRelevanceEntityList.stream()
                .collect(groupingBy(ChineseSkuSupplierRelevanceEntity::getSkuCode, mapping(ChineseSkuSupplierRelevanceEntity::getSupplierId, toSet())));
        // 3.获取店铺名称
        List<StoreEntity> storeEntityList = storeService.listByIds(storeIds);
        Map<Long, String> storeIdNameMap = storeEntityList.stream().collect(toMap(StoreEntity::getId, StoreEntity::getStoreName, (v1, v2) -> v1));
        // 4.获取供应商名称
        // TODO liuqiuyi 调用供应商接口
        // 5.组装返回值
        return buildChineseManagerSkuResponse(skuInfoEntityList, skuCodeSupplierIdsMap, storeIdNameMap);
    }

    /**
     * 保存sku信息
     *
     * @param saveOrUpdateSkuVO 接口入参
     * @author liuqiuyi
     * @date 2022/8/1 15:44
     */
    @Override
    public void saveOrUpdateSku(SaveOrUpdateSkuVO saveOrUpdateSkuVO) {
        log.info("invoke saveOrUpdateSku() param：{}", JSON.toJSONString(saveOrUpdateSkuVO));
        boolean updateFlag = StringUtils.isNotBlank(saveOrUpdateSkuVO.getSkuCode());
        // 校验请求入参
        checkSaveOrUpdateSkuParam(saveOrUpdateSkuVO, updateFlag);
        // 保存或者更新
        if (updateFlag) {
            chineseSkuInfoService.updateSku(saveOrUpdateSkuVO);
        } else {
            chineseSkuInfoService.saveSku(saveOrUpdateSkuVO);
        }
    }

    /**
     * 根据 skuCode 获取详情
     *
     * @param skuCode sku 编码
     * @return sku 详细信息，包含供应商等信息
     * @author liuqiuyi
     * @date 2022/8/2 11:50
     */
    @Override
    public SaveOrUpdateSkuVO getSkuByCode(String skuCode) {
        ChineseSkuInfoEntity skuInfoEntity = chineseSkuInfoService.getBySkuCode(skuCode);
        if (Objects.isNull(skuInfoEntity)) {
            throw new BusinessException(ErrorEnums.SKU_IS_NULL);
        }
        SaveOrUpdateSkuVO response = new SaveOrUpdateSkuVO();
        response.setSkuCode(skuInfoEntity.getSkuCode());
        response.setMedicineId(skuInfoEntity.getOldMedicineId());
        response.setMedicineCode(skuInfoEntity.getMedicineCode());
        response.setSkuName(skuInfoEntity.getSkuName());
        response.setPrice(skuInfoEntity.getPrice());
        response.setStoreId(skuInfoEntity.getStoreId());
        // 1、根据店铺id获取店铺名称
        List<StoreEntity> storeEntityList = storeService.listByIds(Sets.newHashSet(skuInfoEntity.getStoreId()));
        if (!CollectionUtils.isEmpty(storeEntityList)) {
            String storeName = Optional.ofNullable(storeEntityList.get(0)).map(StoreEntity::getStoreName).orElse("");
            response.setStoreName(storeName);
        }
        // 2.根据药材code获取药材名称
        ChineseMedicineEntity chineseMedicineEntity = chineseMedicineService.getByMedicineCode(skuInfoEntity.getMedicineCode());
        if (Objects.nonNull(chineseMedicineEntity)) {
            response.setMedicineName(chineseMedicineEntity.getMedicineName());
        }

        // 3.调用供应商接口，获取供应商的信息

        // 4.组装数据

        return response;
    }

    /**
     * 批量更新sku上下架状态
     *
     * @param updateSkuStateRequest 入参
     * @author liuqiuyi
     * @date 2022/8/2 14:21
     */
    @Override
    public void listUpdateSkuState(UpdateSkuStateRequest updateSkuStateRequest) {
        Set<String> skuCodeList = updateSkuStateRequest.getSkuCodeList();
        // 1.校验sku是否存在
        List<ChineseSkuInfoEntity> chineseSkuInfoEntityList = chineseSkuInfoService.listBySkuCode(skuCodeList);
        if (CollectionUtils.isEmpty(chineseSkuInfoEntityList) || !Objects.equals(chineseSkuInfoEntityList.size(), skuCodeList.size())) {
            throw new BusinessException(ErrorEnums.SKU_IS_NULL);
        }
        // 2.校验所属的店铺是否存在
        Set<Long> storeIds = chineseSkuInfoEntityList.stream().map(ChineseSkuInfoEntity::getStoreId).collect(toSet());
        List<StoreEntity> storeEntities = storeService.listByIds(storeIds);
        if (CollectionUtils.isEmpty(storeEntities) || !Objects.equals(storeEntities.size(), storeIds.size())) {
            throw new BusinessException(ErrorEnums.STORE_NOT_EXIST);
        }
        // 3.更新
        chineseSkuInfoService.updateSkuStatue(updateSkuStateRequest);
    }

    /**
     * 供应商中药库存页面，列表查询接口,提供给供应商远程调用
     *
     * @param skuRequest
     * @author liuqiuyi
     * @date 2022/8/5 10:17
     */
    @Override
    public PageVO<SupplierChineseManagerSkuVO> pageSupplierChineseManagerSku(ChineseManagerSkuRequest skuRequest) {
        log.info("invoke pageSupplierChineseManagerSku() param:{}", JSON.toJSONString(skuRequest));
        // 1。根据条件查询 sku 信息,不进行去重
        Page<SupplierChineseSkuDTO> infoEntityPage = chineseSkuInfoService.pageSupplierChineseManagerSku(skuRequest);
        List<SupplierChineseSkuDTO> skuInfoEntityList = infoEntityPage.getRecords();
        if (CollectionUtils.isEmpty(skuInfoEntityList)) {
            return PageVO.buildPageVO();
        }
        // 2.组装返回值
        List<SupplierChineseManagerSkuVO> managerSkuVOList = buildSupplierChineseManagerSku(skuInfoEntityList);
        return PageVO.buildPageVO(skuRequest.getPageNo(), skuRequest.getPageSize(), infoEntityPage.getTotal(), managerSkuVOList);
    }

    /**
     * 供应商中药库存页面，列表查询导出,提供给供应商远程调用
     *
     * @param skuRequest
     * @author liuqiuyi
     * @date 2022/8/5 10:17
     */
    @Override
    public List<SupplierChineseManagerSkuVO> listSupplierChineseManagerSkuExport(ChineseManagerSkuRequest skuRequest) {
        log.info("invoke listSupplierChineseManagerSkuExport() param:{}", JSON.toJSONString(skuRequest));
        // 1。根据条件查询 sku 信息
        List<SupplierChineseSkuDTO> infoEntityList = chineseSkuInfoService.listSupplierChineseManagerSkuExport(skuRequest);
        if (CollectionUtils.isEmpty(infoEntityList)) {
            return Lists.newArrayList();
        }
        // 2.组装返回值
        return buildSupplierChineseManagerSku(infoEntityList);
    }

    private void checkSaveOrUpdateSkuParam(SaveOrUpdateSkuVO saveOrUpdateSkuVO, boolean updateFlag) {
        // 1.根据药材code校验编码是否存在
        ChineseMedicineEntity chineseMedicineEntity = chineseMedicineService.getByMedicineCode(saveOrUpdateSkuVO.getMedicineCode());
        if (Objects.isNull(chineseMedicineEntity)) {
            throw new BusinessException(ErrorEnums.CHINESE_MEDICINE_IS_NULL);
        }
        saveOrUpdateSkuVO.setMedicineId(chineseMedicineEntity.getId());
        saveOrUpdateSkuVO.setMedicineName(chineseMedicineEntity.getMedicineName());

        // 2.校验店铺是否存在
        List<StoreEntity> storeEntityList = storeService.listByIds(Sets.newHashSet(saveOrUpdateSkuVO.getStoreId()));
        if (CollectionUtils.isEmpty(storeEntityList)) {
            throw new BusinessException(ErrorEnums.STORE_NOT_EXIST);
        }
        // 3.校验供应商是否存在
        // TODO liuqiuyi 调用供应商接口


        // 4.如果是更新sku，校验skuCode是否存在，否则校验重复添加
        if (updateFlag) {
            ChineseSkuInfoEntity skuInfoEntity = chineseSkuInfoService.getBySkuCode(saveOrUpdateSkuVO.getSkuCode());
            if (Objects.isNull(skuInfoEntity)) {
                throw new BusinessException(ErrorEnums.SKU_IS_NULL);
            }
        } else {
            // 校验是否重复添加
            ChineseSkuInfoEntity chineseSkuInfoEntity = chineseSkuInfoService.getByMedicineCodeAndStoreId(saveOrUpdateSkuVO.getMedicineCode(), saveOrUpdateSkuVO.getStoreId());
            if (Objects.nonNull(chineseSkuInfoEntity)) {
                throw new BusinessException(ErrorEnums.CHINESE_IS_REPEAT);
            }
        }
    }

    private List<ChineseManagerSkuVO> buildChineseManagerSkuResponse(List<ChineseSkuInfoEntity> skuInfoEntityList,
                                                                     Map<String, Set<Long>> skuCodeSupplierIdsMap,
                                                                     Map<Long, String> storeIdNameMap) {
        List<ChineseManagerSkuVO> managerSkuVOList = Lists.newArrayListWithCapacity(skuInfoEntityList.size());
        for (ChineseSkuInfoEntity chineseSkuInfoEntity : skuInfoEntityList) {
            Set<Long> supplierIds = skuCodeSupplierIdsMap.get(chineseSkuInfoEntity.getSkuCode());

            ChineseManagerSkuVO chineseManagerSkuVO = ChineseManagerSkuVO.builder()
                    .medicineCode(chineseSkuInfoEntity.getMedicineCode())
                    .skuCode(chineseSkuInfoEntity.getSkuCode())
                    .skuName(chineseSkuInfoEntity.getSkuName())
                    .storeId(chineseSkuInfoEntity.getStoreId())
                    .storeName(storeIdNameMap.get(chineseSkuInfoEntity.getStoreId()))
                    .supplierIdList(supplierIds)
                    // TODO
//                    .supplierName()
                    .price(chineseSkuInfoEntity.getPrice())
                    .skuState(chineseSkuInfoEntity.getSkuStatus())
                    .skuStateName(ProductStateEnum.getValueByCode(chineseSkuInfoEntity.getSkuStatus()))
                    .build();

            managerSkuVOList.add(chineseManagerSkuVO);
        }
        return managerSkuVOList;
    }


    private List<SupplierChineseManagerSkuVO> buildSupplierChineseManagerSku(List<SupplierChineseSkuDTO> skuInfoEntityList) {
        // 1.获取店铺名称
        Set<Long> storeIds = skuInfoEntityList.stream().map(SupplierChineseSkuDTO::getStoreId).collect(toSet());
        List<StoreEntity> storeEntityList = storeService.listByIds(storeIds);
        Map<Long, String> storeIdNameMap = storeEntityList.stream().collect(toMap(StoreEntity::getId, StoreEntity::getStoreName, (v1, v2) -> v1));
        // 2.组装返回值
        List<SupplierChineseManagerSkuVO> chineseManagerSkuVOList = Lists.newArrayListWithCapacity(skuInfoEntityList.size());
        skuInfoEntityList.forEach(supplierChineseSkuDTO -> {
            SupplierChineseManagerSkuVO chineseManagerSkuVO = new SupplierChineseManagerSkuVO();
            BeanUtils.copyProperties(supplierChineseSkuDTO, chineseManagerSkuVO);
            chineseManagerSkuVO.setSkuState(supplierChineseSkuDTO.getSkuStatus());
            chineseManagerSkuVO.setSkuStateName(ProductStateEnum.getValueByCode(supplierChineseSkuDTO.getSkuStatus()));
            chineseManagerSkuVO.setStoreName(storeIdNameMap.get(supplierChineseSkuDTO.getStoreId()));
            chineseManagerSkuVOList.add(chineseManagerSkuVO);
        });
        return chineseManagerSkuVOList;
    }
}
