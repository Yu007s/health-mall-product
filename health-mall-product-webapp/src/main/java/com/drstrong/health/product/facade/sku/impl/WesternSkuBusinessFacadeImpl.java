package com.drstrong.health.product.facade.sku.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.json.JSONUtil;
import com.drstrong.health.product.facade.sku.SkuBusinessBaseFacade;
import com.drstrong.health.product.facade.sku.SkuManageFacade;
import com.drstrong.health.product.model.dto.medicine.MedicineUsageDTO;
import com.drstrong.health.product.model.dto.medicine.ProductDetailInfoDTO;
import com.drstrong.health.product.model.dto.sku.SkuInfoSummaryDTO;
import com.drstrong.health.product.model.entity.medication.WesternMedicineEntity;
import com.drstrong.health.product.model.entity.medication.WesternMedicineInstructionsEntity;
import com.drstrong.health.product.model.entity.medication.WesternMedicineSpecificationsEntity;
import com.drstrong.health.product.model.entity.sku.StoreSkuInfoEntity;
import com.drstrong.health.product.model.enums.ProductTypeEnum;
import com.drstrong.health.product.model.request.product.v3.ProductManageQueryRequest;
import com.drstrong.health.product.model.request.sku.SkuQueryRequest;
import com.drstrong.health.product.model.response.product.v3.AgreementSkuInfoVO;
import com.drstrong.health.product.remote.pro.StockRemoteProService;
import com.drstrong.health.product.service.medicine.WesternMedicineInstructionsService;
import com.drstrong.health.product.service.medicine.WesternMedicineService;
import com.drstrong.health.product.service.medicine.WesternMedicineSpecificationsService;
import com.drstrong.health.product.service.sku.StoreSkuInfoService;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

/**
 * 西药 facade （具体到规格）
 *
 * @author liuqiuyi
 * @date 2023/6/20 11:27
 */
@Slf4j
@Service
public class WesternSkuBusinessFacadeImpl implements SkuBusinessBaseFacade {

    @Resource
    SkuManageFacade skuManageFacade;

    @Resource
    StockRemoteProService stockRemoteProService;

    @Resource
    StoreSkuInfoService storeSkuInfoService;

    @Resource
    WesternMedicineSpecificationsService westernMedicineSpecificationsService;

    @Autowired
    private WesternMedicineInstructionsService westernMedicineInstructionsService;

    @Autowired
    private WesternMedicineService westernMedicineService;

    /**
     * 返回每个处理类的商品类型
     *
     * @author liuqiuyi
     * @date 2023/6/20 10:41
     */
    @Override
    public ProductTypeEnum getProductType() {
        return ProductTypeEnum.MEDICINE;
    }

    @Override
    public SkuInfoSummaryDTO querySkuByParam(SkuQueryRequest skuQueryRequest) {
        SkuInfoSummaryDTO skuBaseVO = SkuInfoSummaryDTO.builder()
                .productType(getProductType().getCode())
                .productTypeName(getProductType().getValue()).build();
        // 1.查询数据
        ProductManageQueryRequest queryRequest = BeanUtil.copyProperties(skuQueryRequest, ProductManageQueryRequest.class);
        List<AgreementSkuInfoVO> westernSkuInfoVOList = skuManageFacade.listSkuManageInfo(queryRequest);
        if (CollectionUtil.isEmpty(westernSkuInfoVOList)) {
            log.info("根据条件查询西药列表，返回值为空，查询参数为：{}", JSONUtil.toJsonStr(skuQueryRequest));
            skuBaseVO.setWesternSkuInfoVoList(Lists.newArrayList());
            return skuBaseVO;
        }
        skuBaseVO.setWesternSkuInfoVoList(westernSkuInfoVOList);
        if (ObjectUtil.equal(Boolean.TRUE, skuQueryRequest.getNeedQueryInventory())) {
            // 2.调用库存接口
            List<String> skuCodes = westernSkuInfoVOList.stream().map(AgreementSkuInfoVO::getSkuCode).collect(Collectors.toList());
            skuBaseVO.setSkuCanStockList(stockRemoteProService.getStockToMap(skuCodes));
        }
        return skuBaseVO;
    }

    @Override
    public SkuInfoSummaryDTO queryBySkuCode(String skuCode) {
        SkuQueryRequest skuQueryRequest = SkuQueryRequest.builder().skuCode(skuCode).build();
        return SpringUtil.getBean(WesternSkuBusinessFacadeImpl.class).querySkuByParam(skuQueryRequest);
    }

    /**
     * 根据 productType 过滤skuCode
     *
     * @param skuCodes
     * @author liuqiuyi
     * @date 2023/7/17 14:41
     */
    @Override
    public Set<String> filterSkuCodesByProductType(Set<String> skuCodes) {
        if (CollectionUtil.isEmpty(skuCodes)) {
            return Sets.newHashSet();
        }
        return skuCodes.stream().filter(skuCode -> StrUtil.isNotBlank(skuCode) && skuCode.startsWith(getProductType().getMark())).collect(Collectors.toSet());
    }

    @Override
    public List<MedicineUsageDTO> queryMedicineUsageBySkuCode(Set<String> skuCodes) {
        // 1.根据skuCode查询药材信息
        List<StoreSkuInfoEntity> storeSkuInfoEntityList = storeSkuInfoService.querySkuCodes(skuCodes);
        Set<String> medicineCodes = storeSkuInfoEntityList.stream().map(StoreSkuInfoEntity::getMedicineCode).collect(Collectors.toSet());
        // 3.根据规格编码查询西成药规格信息
        Map<String, MedicineUsageDTO> medicineCodeUsageDtoMap = westernMedicineSpecificationsService.queryMedicineUsageBySpecCodes(medicineCodes)
                .stream().collect(Collectors.toMap(MedicineUsageDTO::getMedicineCode, dto -> dto, (v1, v2) -> v1));
        // 4.组装返回值
        return buildMedicineCodeUsageDto(storeSkuInfoEntityList, medicineCodeUsageDtoMap);
    }

    /**
     * 根据sku编码列表查询药品详情
     *
     * @param skuCodes
     * @return
     */
    @Override
    public List<ProductDetailInfoDTO> queryProductDetailsBySkuCodes(Set<String> skuCodes) {
        if (CollectionUtil.isEmpty(skuCodes)) {
            return Lists.newArrayList();
        }
        //西药基本信息
        List<StoreSkuInfoEntity> storeSkuInfoEntities = storeSkuInfoService.querySkuCodes(skuCodes);
        Map<String, StoreSkuInfoEntity> skuCodeAndStoreSkuInfoEntityMap = storeSkuInfoEntities.stream().collect(toMap(StoreSkuInfoEntity::getSkuCode, dto -> dto, (v1, v2) -> v1));

        //西药规格信息
        List<String> skuMedicineCodeList = storeSkuInfoEntities.stream().map(StoreSkuInfoEntity::getMedicineCode).collect(Collectors.toList());
        Map<String, WesternMedicineSpecificationsEntity> medicineCodeAndWesternMedicineSpecificationsEntityMap = westernMedicineSpecificationsService.queryByCodeList(skuMedicineCodeList).stream()
                .collect(toMap(WesternMedicineSpecificationsEntity::getSpecCode, dto -> dto, (v1, v2) -> v1));

        //西药商品信息
        List<WesternMedicineEntity> westernMedicineEntityList = westernMedicineService.queryByMedicineCodeList(skuMedicineCodeList);
        Map<String, WesternMedicineEntity> medicineCodeAndWesternMedicineEntityMap = westernMedicineEntityList.stream().collect(toMap(WesternMedicineEntity::getMedicineCode, dto -> dto, (v1, v2) -> v1));

        //西药说明信息
        List<WesternMedicineInstructionsEntity> westernMedicineInstructionsEntities = westernMedicineInstructionsService.queryByMedicineIdList(westernMedicineEntityList.stream().map(WesternMedicineEntity::getId).collect(toList()));
        Map<Long, WesternMedicineInstructionsEntity> medicineIdAndWesternMedicineInstructionsEntityMap = westernMedicineInstructionsEntities.stream().collect(toMap(WesternMedicineInstructionsEntity::getMedicineId, dto -> dto, (v1, v2) -> v1));

        //组装返回值
        List<ProductDetailInfoDTO> result = Lists.newArrayListWithCapacity(skuCodes.size());
        for (String skuCode : skuCodes) {
            StoreSkuInfoEntity storeSkuInfoEntity = skuCodeAndStoreSkuInfoEntityMap.get(skuCode);
            WesternMedicineEntity westernMedicineEntity = medicineCodeAndWesternMedicineEntityMap.get(storeSkuInfoEntity.getMedicineCode());
            ProductDetailInfoDTO detailInfoDTO = ProductDetailInfoDTO.builder()
                    .skuCode(storeSkuInfoEntity.getSkuCode())
                    .productType(storeSkuInfoEntity.getSkuType())
                    .storeSkuInfoEntity(storeSkuInfoEntity)
                    .westernMedicineEntity(westernMedicineEntity)
                    .westernMedicineInstructionsEntity(medicineIdAndWesternMedicineInstructionsEntityMap.get(westernMedicineEntity.getId()))
                    .westernMedicineSpecificationsEntity(medicineCodeAndWesternMedicineSpecificationsEntityMap.get(storeSkuInfoEntity.getMedicineCode()))
                    .build();
            result.add(detailInfoDTO);
        }
        return result;
    }
}
