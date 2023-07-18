package com.drstrong.health.product.facade.activty;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.drstrong.health.product.model.dto.product.ActivityPackageDetailDTO;
import com.drstrong.health.product.model.dto.product.PackageSkuDetailDTO;
import com.drstrong.health.product.model.entity.activty.ActivityPackageInfoEntity;
import com.drstrong.health.product.model.entity.activty.ActivityPackageSkuInfoEntity;
import com.drstrong.health.product.model.entity.store.StoreEntity;
import com.drstrong.health.product.model.enums.UpOffEnum;
import com.drstrong.health.product.model.request.product.ActivityPackageManageQueryRequest;
import com.drstrong.health.product.model.request.product.PackageBussinessQueryListRequest;
import com.drstrong.health.product.model.response.PageVO;
import com.drstrong.health.product.model.response.product.PackageBussinessListVO;
import com.drstrong.health.product.model.response.result.BusinessException;
import com.drstrong.health.product.model.response.result.ResultStatus;
import com.drstrong.health.product.service.activty.ActivityPackageSkuInfoSevice;
import com.drstrong.health.product.service.activty.PackageService;
import com.drstrong.health.product.service.store.StoreService;
import com.drstrong.health.product.util.BigDecimalUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * huangpeng
 * 2023/7/11 18:09
 */
@Slf4j
@Service
public class PackageBussinessFacadeImpl implements PackageBussinessFacade {

    @Autowired
    private StoreService storeService;

    @Autowired
    private PackageService packageService;

    @Autowired
    private ActivityPackageSkuInfoSevice activityPackageSkuInfoSevice;

    /**
     * 查询套餐详情
     *
     * @param activityPackageCode
     * @return
     */
    @Override
    public ActivityPackageDetailDTO queryDetailByCode(String activityPackageCode) {
/*        //根据activityPackageCode查询套餐
        ActivityPackageInfoEntity activityPackageInfoEntity = packageService.findPackageByCode(activityPackageCode, null);
        //套餐关联的店铺信息
        StoreEntity storeEntity = storeService.getById(activityPackageInfoEntity.getStoreId());
        if (storeEntity == null) {
            throw new BusinessException(ResultStatus.PARAM_ERROR.getCode(), "错误的店铺类型名称");
        }
        //套餐sku信息
        List<ActivityPackageSkuInfoEntity> packageSkuInfoEntityList = activityPackageSkuInfoSevice.findPackageByCode(activityPackageCode);
        List<PackageSkuDetailDTO> packageSkuDetailDTOS = new ArrayList<>();
        for (ActivityPackageSkuInfoEntity activityPackageSkuInfoEntity : packageSkuInfoEntityList) {
            PackageSkuDetailDTO detailDTO = PackageSkuDetailDTO.builder()
                    .skuCode(activityPackageSkuInfoEntity.getSkuCode())
                    .skuName(activityPackageSkuInfoEntity.getSkuName())
                    .originalPrice(BigDecimalUtil.F2Y(activityPackageSkuInfoEntity.getOriginalPrice()))
                    .preferentialPrice(BigDecimalUtil.F2Y(activityPackageSkuInfoEntity.getPreferentialPrice()))
                    .amount(activityPackageSkuInfoEntity.getAmount())
                    .build();
            packageSkuDetailDTOS.add(detailDTO);
        }

        //组装数据
        ActivityPackageDetailDTO activityPackageDetailDTO = ActivityPackageDetailDTO.builder()
                .id(activityPackageInfoEntity.getId())
                .activityPackageName(activityPackageInfoEntity.getActivityPackageName())
                .activityPackageCode(activityPackageInfoEntity.getActivityPackageCode())
                .productType(activityPackageInfoEntity.getProductType())
                .storeId(storeEntity.getId())
                .storeName(storeEntity.getStoreName())
                .activityStatus(activityPackageInfoEntity.getActivityStatus())
                .originalPrice(BigDecimalUtil.F2Y(activityPackageInfoEntity.getOriginalPrice()))
                .preferentialPrice(BigDecimalUtil.F2Y(activityPackageInfoEntity.getPreferentialPrice()))
                .originalAmountDisplay(activityPackageInfoEntity.getOriginalAmountDisplay())
                .activityPackageImageInfo(activityPackageInfoEntity.getActivityPackageImageInfo())
                .activityPackageIntroduce(activityPackageInfoEntity.getActivityPackageIntroduce())
                .activityPackageRemark(activityPackageInfoEntity.getActivityPackageRemark())
                .activityPackageSkuInfoEntityList(packageSkuDetailDTOS)
                .build();
        return activityPackageDetailDTO;*/
        return null;
    }

    /**
     * 医生端的列表套餐搜索
     *
     * @param packageBussinessQueryListRequest
     * @return
     */
    @Override
    public PageVO<PackageBussinessListVO> queryActivityPackageList(PackageBussinessQueryListRequest packageBussinessQueryListRequest) {
/*        log.info("invoke queryActivityPackageList(),param:{}", JSONUtil.toJsonStr(packageBussinessQueryListRequest));
        //店铺信息
        List<StoreEntity> storeEntityList = storeService.getStoreByAgencyIds(Sets.newHashSet(Long.valueOf(packageBussinessQueryListRequest.getAgencyId())));
        List<Long> storeIds = storeEntityList.stream().map(StoreEntity::getId).collect(Collectors.toList());
        Map<Long, String> storeIdNameMap = storeEntityList.stream().collect(Collectors.toMap(StoreEntity::getId, StoreEntity::getStoreName, (v1, v2) -> v1));
        packageBussinessQueryListRequest.setStoreIds(storeIds);
        packageBussinessQueryListRequest.setActivityStatus(UpOffEnum.UP.getCode());
        Page<ActivityPackageInfoEntity> activityPackageInfoEntityPage = packageService.pageQueryList(packageBussinessQueryListRequest);
        if (activityPackageInfoEntityPage == null || CollectionUtil.isEmpty(activityPackageInfoEntityPage.getRecords())) {
            log.info("未查询到任何套餐数据,参数为:{}", JSONUtil.toJsonStr(packageBussinessQueryListRequest));
            return PageVO.newBuilder().result(Lists.newArrayList()).totalCount(0).pageNo(packageBussinessQueryListRequest.getPageNo()).pageSize(packageBussinessQueryListRequest.getPageSize()).build();
        }
        List<PackageBussinessListVO> activityPackageInfoVOList = new ArrayList<>();
        for (ActivityPackageInfoEntity record : activityPackageInfoEntityPage.getRecords()) {
            PackageBussinessListVO activityPackageInfoVO = PackageBussinessListVO.builder()
                    .id(record.getId())
                    .activityPackageName(record.getActivityPackageName())
                    .activityPackageCode(record.getActivityPackageCode())
                    .productType(record.getProductType())
                    .storeId(record.getStoreId())
                    .storeName(storeIdNameMap.get(record.getStoreId()))
                    .activityStatus(record.getActivityStatus())
                    .originalPrice(BigDecimalUtil.F2Y(record.getOriginalPrice()))
                    .preferentialPrice(BigDecimalUtil.F2Y(record.getPreferentialPrice()))
                    .originalAmountDisplay(record.getOriginalAmountDisplay())
                    .createdAt(Date.from(record.getCreatedAt().atZone(ZoneId.systemDefault()).toInstant()))
                    .build();
            activityPackageInfoVOList.add(activityPackageInfoVO);
        }
        return PageVO.newBuilder()
                .result(activityPackageInfoVOList)
                .totalCount((int) activityPackageInfoEntityPage.getTotal())
                .pageNo(packageBussinessQueryListRequest.getPageNo())
                .pageSize(packageBussinessQueryListRequest.getPageSize())
                .build();*/
        return null;
    }
}
