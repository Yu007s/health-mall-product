package com.drstrong.health.product.facade.product.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.drstrong.health.product.enums.MedicineAttributeEnum;
import com.drstrong.health.product.facade.product.ProductBussinessFacade;
import com.drstrong.health.product.facade.sku.SkuBusinessBaseFacade;
import com.drstrong.health.product.facade.sku.SkuBusinessFacadeHolder;
import com.drstrong.health.product.model.dto.medicine.MedicineImageDTO;
import com.drstrong.health.product.model.dto.medicine.ProductDetailInfoDTO;
import com.drstrong.health.product.model.dto.product.*;
import com.drstrong.health.product.model.dto.stock.SkuCanStockDTO;
import com.drstrong.health.product.model.entity.medication.AgreementPrescriptionMedicineEntity;
import com.drstrong.health.product.model.entity.medication.WesternMedicineEntity;
import com.drstrong.health.product.model.entity.medication.WesternMedicineInstructionsEntity;
import com.drstrong.health.product.model.entity.medication.WesternMedicineSpecificationsEntity;
import com.drstrong.health.product.model.entity.sku.StoreSkuInfoEntity;
import com.drstrong.health.product.model.entity.store.StoreEntity;
import com.drstrong.health.product.model.enums.ErrorEnums;
import com.drstrong.health.product.model.enums.MedicineClassificationEnum;
import com.drstrong.health.product.model.enums.ProductTypeEnum;
import com.drstrong.health.product.model.enums.UpOffEnum;
import com.drstrong.health.product.model.request.product.SearchWesternRequestParamBO;
import com.drstrong.health.product.model.request.product.v3.ProductManageQueryRequest;
import com.drstrong.health.product.model.response.result.BusinessException;
import com.drstrong.health.product.remote.pro.StockRemoteProService;
import com.drstrong.health.product.service.activty.ActivityPackageSkuInfoSevice;
import com.drstrong.health.product.service.activty.PackageService;
import com.drstrong.health.product.service.medicine.AgreementPrescriptionMedicineService;
import com.drstrong.health.product.service.medicine.WesternMedicineInstructionsService;
import com.drstrong.health.product.service.medicine.WesternMedicineService;
import com.drstrong.health.product.service.medicine.WesternMedicineSpecificationsService;
import com.drstrong.health.product.service.sku.StoreSkuInfoService;
import com.drstrong.health.product.service.store.StoreService;
import com.drstrong.health.product.util.BigDecimalUtil;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Lists;
import org.springframework.beans.BeanUtils;
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
    private WesternMedicineService westernMedicineService;

    @Autowired
    private WesternMedicineSpecificationsService westernMedicineSpecificationsService;

    @Autowired
    private WesternMedicineInstructionsService westernMedicineInstructionsService;

    @Autowired
    private AgreementPrescriptionMedicineService agreementPrescriptionMedicineService;

    @Autowired
    private PackageService packageService;

    @Autowired
    private ActivityPackageSkuInfoSevice activityPackageSkuInfoSevice;

    @Autowired
    private SkuBusinessFacadeHolder skuBusinessFacadeHolder;

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
        ProductManageQueryRequest queryParam = ProductManageQueryRequest.builder()
                .skuName(searchWesternRequestParamBO.getKey())
                .storeIds(storeIds)
                .productType(searchWesternRequestParamBO.getProductType())
                .categoryId(searchWesternRequestParamBO.getCategoryId())
                .cityId(searchWesternRequestParamBO.getCityId())
                .skuStatus(UpOffEnum.UP.getCode())
                .build();
        List<StoreSkuInfoEntity> storeSkuInfoEntityList = storeSkuInfoService.listQueryByParam(queryParam);
        if (CollectionUtils.isEmpty(storeSkuInfoEntityList)) {
            log.info("searchProductList,商品列表信息为空");
            return Lists.newArrayList();
        }

        //库存信息-限制库存高于阈值
        List<String> skuCodeList = storeSkuInfoEntityList.stream().map(StoreSkuInfoEntity::getSkuCode).collect(Collectors.toList());
        Map<String, List<SkuCanStockDTO>> stockToMap = stockRemoteProService.getStockToMap(skuCodeList);
        List<StoreSkuInfoEntity> storeSkuInfoEntities = storeSkuInfoEntityList.stream().filter(StoreSkuInfoEntity -> stockToMap.keySet().contains(StoreSkuInfoEntity.getSkuCode())).collect(toList());
        if (CollectionUtils.isEmpty(storeSkuInfoEntities)) {
            log.info("商品列表库存数据为空,skuCodeList={}", skuCodeList);
            return Lists.newArrayList();
        }

        //西药规格信息
        SkuBusinessBaseFacade skuWesternBusinessBaseFacade = skuBusinessFacadeHolder.getSkuBusinessBaseFacade(ProductTypeEnum.MEDICINE.getCode());
        Set<String> skuCodeWesternList = storeSkuInfoEntities.stream().filter(StoreSkuInfoEntity -> ProductTypeEnum.MEDICINE.getCode().equals(StoreSkuInfoEntity.getSkuType()))
                .map(StoreSkuInfoEntity::getSkuCode).collect(Collectors.toSet());
        List<ProductDetailInfoDTO> westernProductDetailInfoDTOS = skuWesternBusinessBaseFacade.queryProductDetailsBySkuCodes(skuCodeWesternList);
        Map<String, ProductDetailInfoDTO> skuCodeAndWesternProductDetailInfoDTOMap = westernProductDetailInfoDTOS.stream().collect(toMap(ProductDetailInfoDTO::getSkuCode, dto -> dto, (v1, v2) -> v1));


        //协定方规格信息
        SkuBusinessBaseFacade skuAgreementBusinessBaseFacade = skuBusinessFacadeHolder.getSkuBusinessBaseFacade(ProductTypeEnum.AGREEMENT.getCode());
        Set<String> skuAgreementCodeAgreementList = storeSkuInfoEntities.stream().filter(StoreSkuInfoEntity -> ProductTypeEnum.AGREEMENT.getCode().equals(StoreSkuInfoEntity.getSkuType()))
                .map(StoreSkuInfoEntity::getSkuCode).collect(Collectors.toSet());
        List<ProductDetailInfoDTO> agreementProductDetailInfoDTOS = skuAgreementBusinessBaseFacade.queryProductDetailsBySkuCodes(skuAgreementCodeAgreementList);
        Map<String, ProductDetailInfoDTO> skuCodeAndAgreementProductDetailInfoDTOMap = agreementProductDetailInfoDTOS.stream().collect(toMap(ProductDetailInfoDTO::getSkuCode, dto -> dto, (v1, v2) -> v1));

        //获取所有正在进行的套餐信息
        Map<String, List<PackageInfoVO>> activityPackageInfoListMap = packageService.getUpPackageInfo();

        //组装返回信息
        List<ProductListInfoVO> result = buildProductListResponse(storeIdNameMap, stockToMap, storeSkuInfoEntities, skuCodeAndWesternProductDetailInfoDTOMap, skuCodeAndAgreementProductDetailInfoDTOMap, activityPackageInfoListMap);
        return result;
    }

    /**
     * 组装医生端搜索商品列表返回值信息
     */
    private List<ProductListInfoVO> buildProductListResponse(Map<Long, String> storeIdNameMap,
                                                             Map<String, List<SkuCanStockDTO>> stockToMap,
                                                             List<StoreSkuInfoEntity> storeSkuInfoEntities,
                                                             Map<String, ProductDetailInfoDTO> skuCodeAndWesternProductDetailInfoDTOMap,
                                                             Map<String, ProductDetailInfoDTO> skuCodeAndAgreementProductDetailInfoDTOMap,
                                                             Map<String, List<PackageInfoVO>> activityPackageInfoListMap) {
        List<ProductListInfoVO> result = Lists.newArrayList();
        for (StoreSkuInfoEntity storeSkuInfoEntity : storeSkuInfoEntities) {
            List<MedicineImageDTO> imageInfo = Lists.newArrayList();
            String company = null;
            String spec = null;
            String usage = null;
            Integer rx = null;
            Integer medicineAttributeId = null;
            if (ObjectUtil.isNotNull(storeSkuInfoEntity.getLabelInfo())) {
                for (MedicineAttributeEnum value : MedicineAttributeEnum.values()) {
                    if (storeSkuInfoEntity.getLabelInfo().contains(value.getCode())) {
                        medicineAttributeId = value.getCode().intValue();
                        break;
                    }
                }
            }
            if (ProductTypeEnum.MEDICINE.getCode().equals(storeSkuInfoEntity.getSkuType()) && ObjectUtil.isNotNull(skuCodeAndWesternProductDetailInfoDTOMap.get(storeSkuInfoEntity.getSkuCode()))) {
                ProductDetailInfoDTO productDetailInfoDTO = skuCodeAndWesternProductDetailInfoDTOMap.get(storeSkuInfoEntity.getSkuCode());
                imageInfo = JSONObject.parseArray(productDetailInfoDTO.getWesternMedicineSpecificationsEntity().getSpecImageInfo(), MedicineImageDTO.class);
                company = productDetailInfoDTO.getWesternMedicineInstructionsEntity().getProductionEnterprise();
                spec = productDetailInfoDTO.getWesternMedicineSpecificationsEntity().getPackingSpec();
                usage = productDetailInfoDTO.getWesternMedicineInstructionsEntity().getUsageDosage();
                Map<String, Integer> map = (Map<String, Integer>) JSONObject.parse(productDetailInfoDTO.getWesternMedicineEntity().getMedicineClassificationInfo());
                rx = map.get(MedicineClassificationEnum.SECURITY_CLASSIFICATION.getName());
            } else if (ProductTypeEnum.AGREEMENT.getCode().equals(storeSkuInfoEntity.getSkuType()) && ObjectUtil.isNotNull(skuCodeAndAgreementProductDetailInfoDTOMap.get(storeSkuInfoEntity.getSkuCode()))) {
                ProductDetailInfoDTO productDetailInfoDTO = skuCodeAndAgreementProductDetailInfoDTOMap.get(storeSkuInfoEntity.getSkuCode());
                imageInfo = JSONObject.parseArray(productDetailInfoDTO.getAgreementPrescriptionMedicineEntity().getImageInfo(), MedicineImageDTO.class);
                spec = productDetailInfoDTO.getAgreementPrescriptionMedicineEntity().getPackingSpec();
                usage = productDetailInfoDTO.getAgreementPrescriptionMedicineEntity().getUsageMethod();
            }
            List<PackageInfoVO> packageInfoVOList = activityPackageInfoListMap.get(storeSkuInfoEntity.getSkuCode());
            Long quantity = stockToMap.get(storeSkuInfoEntity.getSkuCode()).stream().mapToLong(SkuCanStockDTO::getAvailableQuantity).sum();
            ProductListInfoVO productListInfoVO = ProductListInfoVO.builder()
                    .skuCode(storeSkuInfoEntity.getSkuCode())
                    .skuName(storeSkuInfoEntity.getSkuName())
                    .skuType(storeSkuInfoEntity.getSkuType())
                    .skuStatus(storeSkuInfoEntity.getSkuStatus())
                    .storeId(storeSkuInfoEntity.getStoreId())
                    .storeName(storeIdNameMap.get(storeSkuInfoEntity.getStoreId()))
                    .company(company)
                    .imageInfo(imageInfo)
                    .salePrice(BigDecimalUtil.F2Y(storeSkuInfoEntity.getPrice()))
                    .salePriceValue(storeSkuInfoEntity.getPrice().intValue())
                    .quantity(quantity)
                    .rx(rx)
                    .medicineAttributeId(medicineAttributeId)
                    .spec(spec)
                    .usage(usage)
                    .packageInfoVOList(CollectionUtil.isEmpty(packageInfoVOList) ? null : packageInfoVOList)
                    .build();
            result.add(productListInfoVO);
        }
        return result;
    }

    /**
     * 医生端查看药品详情(中西药+协定方)
     *
     * @param skuCode
     * @return
     */
    @Override
    public ProductDetailInfoVO queryProductDetail(String skuCode) {
        StoreSkuInfoEntity storeSkuInfoEntity = storeSkuInfoService.queryBySkuCode(skuCode, UpOffEnum.UP.getCode());
        if (Objects.isNull(storeSkuInfoEntity)) {
            throw new BusinessException(ErrorEnums.SKU_DETAIL_QUERY_ERROR);
        }
        //店铺信息
        StoreEntity storeEntity = storeService.getById(storeSkuInfoEntity.getStoreId());

        ProductDetailInfoVO detailInfoVO = ProductDetailInfoVO.builder()
                .skuCode(storeSkuInfoEntity.getSkuCode())
                .skuName(storeSkuInfoEntity.getSkuName())
                .medicineCode(storeSkuInfoEntity.getMedicineCode())
                .salePrice(BigDecimalUtil.F2Y(storeSkuInfoEntity.getPrice()))
                .skuType(storeSkuInfoEntity.getSkuType())
                .storeId(storeEntity.getId())
                .storeName(storeEntity.getStoreName())
                .build();
        if (storeSkuInfoEntity.getSkuType() == ProductTypeEnum.MEDICINE.getCode()) {
            //西药
            ProductWesternDetailVO productWesternDetailVO = new ProductWesternDetailVO();
            WesternMedicineEntity westernMedicineEntity = westernMedicineService.queryByMedicineCode(storeSkuInfoEntity.getMedicineCode());
            if (ObjectUtil.isNull(westernMedicineEntity)) {
                throw new BusinessException(ErrorEnums.SKU_DETAIL_QUERY_ERROR.getCode(), "查询的药品详情规格参数为空。");
            }
            WesternMedicineSpecificationsEntity westernMedicineSpecificationsEntity = westernMedicineSpecificationsService.queryByCode(storeSkuInfoEntity.getMedicineCode());
            WesternMedicineInstructionsEntity westernMedicineInstructionsEntity = westernMedicineInstructionsService.queryByMedicineId(westernMedicineEntity.getId());

            if (ObjectUtil.isNull(westernMedicineSpecificationsEntity) ||
                    ObjectUtil.isNull(westernMedicineInstructionsEntity)) {
                throw new BusinessException(ErrorEnums.SKU_DETAIL_QUERY_ERROR.getCode(), "查询的药品详情规格参数为空。");
            }
            BeanUtils.copyProperties(westernMedicineEntity, productWesternDetailVO);
            BeanUtils.copyProperties(westernMedicineSpecificationsEntity, productWesternDetailVO);
            BeanUtils.copyProperties(westernMedicineInstructionsEntity, productWesternDetailVO);
            String specImageInfo = westernMedicineSpecificationsEntity.getSpecImageInfo();
            List<MedicineImageDTO> medicineImageDTOS = JSONObject.parseArray(specImageInfo, MedicineImageDTO.class);
            productWesternDetailVO.setSpecImageInfo(medicineImageDTOS);
            detailInfoVO.setProductWesternDetailVO(productWesternDetailVO);
        } else if (storeSkuInfoEntity.getSkuType() == ProductTypeEnum.AGREEMENT.getCode()) {
            //协定方
            ProductAgreementDetailVO productAgreementDetailVO = new ProductAgreementDetailVO();
            AgreementPrescriptionMedicineEntity agreementPrescriptionMedicineEntity = agreementPrescriptionMedicineService.queryByCode(storeSkuInfoEntity.getMedicineCode());
            if (ObjectUtil.isNull(agreementPrescriptionMedicineEntity)) {
                throw new BusinessException(ErrorEnums.SKU_DETAIL_QUERY_ERROR.getCode(), "查询的药品详情规格参数为空。");
            }
            BeanUtils.copyProperties(agreementPrescriptionMedicineEntity, productAgreementDetailVO);
            String specImageInfo = agreementPrescriptionMedicineEntity.getImageInfo();
            List<MedicineImageDTO> medicineImageDTOS = JSONObject.parseArray(specImageInfo, MedicineImageDTO.class);
            productAgreementDetailVO.setSpecImageInfo(medicineImageDTOS);
            detailInfoVO.setProductAgreementDetailVO(productAgreementDetailVO);
        } else {
            log.error("查询的药品详情不存在,skuCode={}", skuCode);
            throw new BusinessException(ErrorEnums.SKU_DETAIL_QUERY_ERROR);
        }
        return detailInfoVO;
    }

    /**
     * 医生端的常用药列表查询
     *
     * @param skuCodes
     * @return
     */
    @Override
    public List<FrequentlyUsedProductInfoVO> getFrequentlyUsedProductList(Set<String> skuCodes) {
        //sku商品信息
        List<StoreSkuInfoEntity> storeSkuInfoEntities = storeSkuInfoService.querySkuCodes(skuCodes);

        //店铺信息
        Set<Long> storeIds = storeSkuInfoEntities.stream().map(StoreSkuInfoEntity::getStoreId).collect(Collectors.toSet());
        Map<Long, String> storeInfoMap = storeService.listByIds(storeIds).stream().collect(toMap(StoreEntity::getId, StoreEntity::getStoreName, (v1, v2) -> v1));

        //库存信息
        List<String> skuCodeList = storeSkuInfoEntities.stream().map(StoreSkuInfoEntity::getSkuCode).collect(Collectors.toList());
        Map<String, List<SkuCanStockDTO>> stockToMap = stockRemoteProService.getStockToMap(skuCodeList);

        //关联的套餐信息(正在进行状态)
        Map<String, List<PackageInfoVO>> activityPackageInfoListMap = packageService.getUpPackageInfo(skuCodeList);

        //规格信息
        List<String> skuMedicineCodeList = storeSkuInfoEntities.stream()
                .filter(StoreSkuInfoEntity -> ProductTypeEnum.MEDICINE.getCode().equals(StoreSkuInfoEntity.getSkuType()))
                .map(StoreSkuInfoEntity::getMedicineCode).collect(Collectors.toList());
        Map<String, String> medicineSpecificationsEntityListMap = new HashMap<>();
        if (CollectionUtil.isNotEmpty(skuMedicineCodeList)) {
            medicineSpecificationsEntityListMap = westernMedicineSpecificationsService.queryByCodeList(skuMedicineCodeList).stream()
                    .collect(Collectors.toMap(WesternMedicineSpecificationsEntity::getSpecCode, WesternMedicineSpecificationsEntity::getSpecImageInfo, (v1, v2) -> v1));
        }

        List<String> skuAgreementCodeList = storeSkuInfoEntities.stream()
                .filter(StoreSkuInfoEntity -> ProductTypeEnum.AGREEMENT.getCode().equals(StoreSkuInfoEntity.getSkuType()))
                .map(StoreSkuInfoEntity::getMedicineCode).collect(Collectors.toList());
        Map<String, String> agreementSpecificationsEntityListMap = new HashMap<>();
        if (CollectionUtil.isNotEmpty(skuAgreementCodeList)) {
            agreementSpecificationsEntityListMap = agreementPrescriptionMedicineService.queryByCodeList(skuAgreementCodeList)
                    .stream().collect(toMap(AgreementPrescriptionMedicineEntity::getMedicineCode, AgreementPrescriptionMedicineEntity::getImageInfo, (v1, v2) -> v1));
        }

        //组装信息
        List<FrequentlyUsedProductInfoVO> result = new ArrayList<>();
        for (StoreSkuInfoEntity storeSkuInfoEntity : storeSkuInfoEntities) {
            List<MedicineImageDTO> imageInfo = new ArrayList<>();
            if (ProductTypeEnum.MEDICINE.getCode().equals(storeSkuInfoEntity.getSkuType()) && MapUtil.isNotEmpty(medicineSpecificationsEntityListMap)) {
                imageInfo = JSONObject.parseArray(medicineSpecificationsEntityListMap.get(storeSkuInfoEntity.getMedicineCode()), MedicineImageDTO.class);
            } else if (ProductTypeEnum.AGREEMENT.getCode().equals(storeSkuInfoEntity.getSkuType()) && MapUtil.isNotEmpty(medicineSpecificationsEntityListMap)) {
                imageInfo = JSONObject.parseArray(agreementSpecificationsEntityListMap.get(storeSkuInfoEntity.getMedicineCode()), MedicineImageDTO.class);
            }
            List<PackageInfoVO> packageInfoVOList = activityPackageInfoListMap.get(storeSkuInfoEntity.getSkuCode());
            List<SkuCanStockDTO> skuCanStockDTOS = stockToMap.get(storeSkuInfoEntity.getSkuCode());
            Long quantity = CollectionUtils.isEmpty(skuCanStockDTOS) ? 0L : skuCanStockDTOS.stream().mapToLong(SkuCanStockDTO::getAvailableQuantity).sum();
            FrequentlyUsedProductInfoVO productInfoVO = FrequentlyUsedProductInfoVO.builder()
                    .skuName(storeSkuInfoEntity.getSkuName())
                    .skuCode(storeSkuInfoEntity.getSkuCode())
                    .skuStatus(storeSkuInfoEntity.getSkuStatus())
                    .storeId(storeSkuInfoEntity.getStoreId())
                    .storeName(storeInfoMap.get(storeSkuInfoEntity.getStoreId()))
                    .skuType(storeSkuInfoEntity.getSkuType())
                    .salePrice(BigDecimalUtil.F2Y(storeSkuInfoEntity.getPrice()))
                    .imageInfo(imageInfo)
                    .quantity(quantity)
                    .packageInfoVOList(CollectionUtil.isEmpty(packageInfoVOList) ? null : packageInfoVOList)
                    .build();
            result.add(productInfoVO);
        }
        return result;
    }
}
