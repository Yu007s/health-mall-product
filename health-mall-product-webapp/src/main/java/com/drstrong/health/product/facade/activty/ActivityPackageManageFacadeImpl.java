package com.drstrong.health.product.facade.activty;

import com.drstrong.health.product.facade.incentive.SkuIncentivePolicyFacade;
import com.drstrong.health.product.model.dto.product.ActivityPackageDetailDTO;
import com.drstrong.health.product.model.entity.activty.ActivityPackageInfoEntity;
import com.drstrong.health.product.model.entity.activty.ActivityPackageSkuInfoEntity;
import com.drstrong.health.product.model.entity.sku.SkuScheduledConfigEntity;
import com.drstrong.health.product.model.entity.sku.StoreSkuInfoEntity;
import com.drstrong.health.product.model.entity.store.StoreEntity;
import com.drstrong.health.product.model.request.product.ActivityPackageManageQueryRequest;
import com.drstrong.health.product.model.request.product.SaveOrUpdateActivityPackageRequest;
import com.drstrong.health.product.model.response.PageVO;
import com.drstrong.health.product.model.response.incentive.SkuIncentivePolicyDetailVO;
import com.drstrong.health.product.model.response.product.ActivityPackageInfoVO;
import com.drstrong.health.product.service.activty.ActivityPackageInfoService;
import com.drstrong.health.product.service.activty.ActivityPackageSkuInfoSevice;
import com.drstrong.health.product.service.sku.SkuScheduledConfigService;
import com.drstrong.health.product.service.store.StoreService;
import com.drstrong.health.product.util.BigDecimalUtil;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * huangpeng
 * 2023/7/5 17:51
 */
@Slf4j
@Service
public class ActivityPackageManageFacadeImpl implements ActivityPackageManageFacade {

    @Autowired
    private ActivityPackageInfoService activityPackageInfoService;

    @Autowired
    private StoreService storeService;

    @Autowired
    private ActivityPackageSkuInfoSevice activityPackageSkuInfoSevice;

    @Autowired
    private SkuIncentivePolicyFacade skuIncentivePolicyFacade;

    @Autowired
    private SkuScheduledConfigService skuScheduledConfigService;

    @Override
    public PageVO<ActivityPackageInfoVO> queryActivityPackageManageInfo(ActivityPackageManageQueryRequest productManageQueryRequest) {
        return null;
    }

    @Override
    public List<ActivityPackageInfoVO> listActivityPackageManageInfo(ActivityPackageManageQueryRequest activityPackageManageQueryRequest) {
        return null;
    }

    @Override
    public void addLocksaveOrUpdateActivityPackage(SaveOrUpdateActivityPackageRequest saveOrUpdateActivityPackageRequest, String skuCode) {

    }

    @Override
    public ActivityPackageDetailDTO queryDetailByCode(String activityPackageCode) {
        //根据activityPackageCode查询信息
        ActivityPackageInfoEntity activityPackageInfoEntity = activityPackageInfoService.findPackageByCode(activityPackageCode, null);
        //店铺信息
        StoreEntity storeEntity = storeService.getById(activityPackageInfoEntity.getStoreId());
        //套餐上下架时间
        List<SkuScheduledConfigEntity> skuScheduledConfigEntityList = skuScheduledConfigService.getByActivityPackageCode(activityPackageInfoEntity.getActivityPackageCode(), null);
        Optional<SkuScheduledConfigEntity> scheduledUp = skuScheduledConfigEntityList.stream().filter(SkuScheduledConfigEntity -> SkuScheduledConfigEntity.getScheduledType() == 1).findFirst();
        Optional<SkuScheduledConfigEntity> scheduledDown = skuScheduledConfigEntityList.stream().filter(SkuScheduledConfigEntity -> SkuScheduledConfigEntity.getScheduledType() == 2).findFirst();
        //套餐sku信息
        List<ActivityPackageSkuInfoEntity> packageSkuInfoEntityList = activityPackageSkuInfoSevice.findPackageByCode(activityPackageCode);
        //激励政策
        SkuIncentivePolicyDetailVO skuIncentivePolicyDetailVO = skuIncentivePolicyFacade.queryPolicyDetailBySkuCode(activityPackageInfoEntity.getActivityPackageCode());
        //组装数据
        ActivityPackageDetailDTO activityPackageDetailDTO = ActivityPackageDetailDTO.builder()
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
                .activityPackageStartAt(Date.from(scheduledUp.get().getScheduledDateTime().atZone(ZoneId.systemDefault()).toInstant()))
                .activityPackageEndAt(Date.from(scheduledDown.get().getScheduledDateTime().atZone(ZoneId.systemDefault()).toInstant()))
                .activityPackageSkuInfoEntityList(packageSkuInfoEntityList)
                .skuIncentivePolicyDetailVO(skuIncentivePolicyDetailVO)
                .build();
        return activityPackageDetailDTO;
    }
}
