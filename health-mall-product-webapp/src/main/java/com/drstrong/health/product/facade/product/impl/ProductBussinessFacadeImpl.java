package com.drstrong.health.product.facade.product.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.map.MapUtil;
import com.drstrong.health.product.facade.product.ProductBussinessFacade;
import com.drstrong.health.product.model.dto.product.ProductDetailInfoVO;
import com.drstrong.health.product.model.dto.product.ProductListInfoVO;
import com.drstrong.health.product.model.entity.medication.AgreementPrescriptionMedicineEntity;
import com.drstrong.health.product.model.entity.medication.WesternMedicineSpecificationsEntity;
import com.drstrong.health.product.model.entity.sku.StoreSkuInfoEntity;
import com.drstrong.health.product.model.entity.store.StoreEntity;
import com.drstrong.health.product.model.enums.ErrorEnums;
import com.drstrong.health.product.model.enums.ProductTypeEnum;
import com.drstrong.health.product.model.enums.UpOffEnum;
import com.drstrong.health.product.model.request.product.SearchWesternRequestParamBO;
import com.drstrong.health.product.model.response.result.BusinessException;
import com.drstrong.health.product.remote.pro.StockRemoteProService;
import com.drstrong.health.product.service.medicine.AgreementPrescriptionMedicineService;
import com.drstrong.health.product.service.medicine.WesternMedicineSpecificationsService;
import com.drstrong.health.product.service.sku.StoreSkuInfoService;
import com.drstrong.health.product.service.store.StoreService;
import com.drstrong.health.product.util.BigDecimalUtil;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

/**
 * huangpeng
 * 2023/7/11 21:04
 */
@Slf4j
@Service
public class ProductBussinessFacadeImpl implements ProductBussinessFacade {

    @Autowired
    private StoreService storeService;

    @Resource
    private StoreSkuInfoService storeSkuInfoService;

    @Autowired
    private StockRemoteProService stockRemoteProService;

    @Autowired
    private WesternMedicineSpecificationsService westernMedicineSpecificationsService;

    @Autowired
    private AgreementPrescriptionMedicineService agreementPrescriptionMedicineService;

    /**
     * 医生端搜索商品列表(不包含健康药品)
     *
     * @param searchWesternRequestParamBO
     * @return
     */
    @Override
    public List<ProductListInfoVO> searchProductList(SearchWesternRequestParamBO searchWesternRequestParamBO) {
        //店铺信息sku信息
        List<StoreEntity> storeEntityList = storeService.getStoreByAgencyIds(Sets.newHashSet(Long.valueOf(searchWesternRequestParamBO.getAgencyId())));
        List<Long> storeIds = storeEntityList.stream().map(StoreEntity::getId).collect(Collectors.toList());
        Map<Long, String> storeIdNameMap = storeEntityList.stream().collect(Collectors.toMap(StoreEntity::getId, StoreEntity::getStoreName, (v1, v2) -> v1));
        List<StoreSkuInfoEntity> storeSkuInfoEntityList = storeSkuInfoService.queryStoreSkuInfoByCategoryAndCityId(searchWesternRequestParamBO.getKey(), searchWesternRequestParamBO.getCategoryId(), searchWesternRequestParamBO.getCityId(), storeIds);
        if (CollectionUtils.isEmpty(storeSkuInfoEntityList)) {
            return null;
        }

        //限制库存高于阈值
        List<String> skuCodeList = storeSkuInfoEntityList.stream().map(StoreSkuInfoEntity::getSkuCode).collect(Collectors.toList());
        Set<String> skuCodeSet = stockRemoteProService.getStockToMap(skuCodeList).keySet();
        List<StoreSkuInfoEntity> storeSkuInfoEntities = storeSkuInfoEntityList.stream().filter(StoreSkuInfoEntity -> skuCodeSet.contains(StoreSkuInfoEntity.getSkuCode())).collect(toList());
        if (CollectionUtils.isEmpty(storeSkuInfoEntities)) {
            return null;
        }

        //西药规格信息
        List<String> skuMedicineCodeList = storeSkuInfoEntities.stream()
                .filter(StoreSkuInfoEntity -> ProductTypeEnum.MEDICINE.equals(StoreSkuInfoEntity.getSkuType()))
                .map(StoreSkuInfoEntity::getMedicineCode).collect(Collectors.toList());
        Map<String, String> medicineSpecificationsEntityListMap = new HashMap<>();
        if (CollectionUtil.isNotEmpty(skuMedicineCodeList)) {
            medicineSpecificationsEntityListMap = westernMedicineSpecificationsService.queryByCodeList(skuMedicineCodeList).stream()
                    .collect(Collectors.toMap(WesternMedicineSpecificationsEntity::getSpecCode, WesternMedicineSpecificationsEntity::getSpecImageInfo, (v1, v2) -> v1));
        }

        //协定方规格信息
        List<String> skuAgreementCodeList = storeSkuInfoEntities.stream()
                .filter(StoreSkuInfoEntity -> ProductTypeEnum.AGREEMENT.equals(StoreSkuInfoEntity.getSkuType()))
                .map(StoreSkuInfoEntity::getMedicineCode).collect(Collectors.toList());
        Map<String, String> agreementSpecificationsEntityListMap = new HashMap<>();
        if (CollectionUtil.isNotEmpty(skuAgreementCodeList)) {
            agreementSpecificationsEntityListMap = agreementPrescriptionMedicineService.queryByCodeList(skuAgreementCodeList).stream().collect(toMap(AgreementPrescriptionMedicineEntity::getMedicineCode, AgreementPrescriptionMedicineEntity::getImageInfo, (v1, v2) -> v1));
        }

        //组装返回信息
        List<ProductListInfoVO> result = new ArrayList<>();
        for (StoreSkuInfoEntity storeSkuInfoEntity : storeSkuInfoEntities) {
            String imageInfo = null;
            if (ProductTypeEnum.MEDICINE.getCode().equals(storeSkuInfoEntity.getSkuType()) && MapUtil.isNotEmpty(medicineSpecificationsEntityListMap)) {
                imageInfo = medicineSpecificationsEntityListMap.get(storeSkuInfoEntity.getMedicineCode());
            } else if (ProductTypeEnum.AGREEMENT.getCode().equals(storeSkuInfoEntity.getSkuType()) && MapUtil.isNotEmpty(medicineSpecificationsEntityListMap)) {
                imageInfo = agreementSpecificationsEntityListMap.get(storeSkuInfoEntity.getMedicineCode());
            }
            ProductListInfoVO productListInfoVO = ProductListInfoVO.builder()
                    .skuName(storeSkuInfoEntity.getSkuName())
                    .skuCode(storeSkuInfoEntity.getSkuCode())
                    .skuType(storeSkuInfoEntity.getSkuType())
                    .storeId(storeSkuInfoEntity.getStoreId())
                    .storeName(storeIdNameMap.get(storeSkuInfoEntity.getStoreId()))
                    .salePrice(storeSkuInfoEntity.getPrice().toString())
                    .imageInfo(imageInfo)
                    .build();
            result.add(productListInfoVO);
        }
        return result;
    }

    /**
     * 医生端查看药品详情(不包含健康药品)
     *
     * @param skuCode
     * @return
     */
    @Override
    public ProductDetailInfoVO queryProductDetail(String skuCode) {
        StoreSkuInfoEntity storeSkuInfoEntity = storeSkuInfoService.queryBySkuCode(skuCode, UpOffEnum.DOWN.getCode());
        //店铺信息
        StoreEntity storeEntity = storeService.getById(storeSkuInfoEntity.getStoreId());
        if(Objects.isNull(storeSkuInfoEntity)||Objects.isNull(storeEntity)){
            throw new BusinessException(ErrorEnums.SKU_DETAIL_QUERY_ERROR);
        }

        ProductDetailInfoVO detailInfoVO = ProductDetailInfoVO.builder()
                .skuCode(storeSkuInfoEntity.getSkuCode())
                .skuName(storeSkuInfoEntity.getSkuName())
                .salePrice(BigDecimalUtil.F2Y(storeSkuInfoEntity.getPrice()))
                .skuType(storeSkuInfoEntity.getSkuType())
                .storeId(storeEntity.getId())
                .storeName(storeEntity.getStoreName())
                .build();
        if (storeSkuInfoEntity.getSkuType() == ProductTypeEnum.MEDICINE.getCode()) {
            //西药

        }
        if (storeSkuInfoEntity.getSkuType() == ProductTypeEnum.MEDICINE.getCode()) {
            //中药

        }
        if (storeSkuInfoEntity.getSkuType() == ProductTypeEnum.MEDICINE.getCode()) {
            //协定方

        }

        return null;
    }
}
