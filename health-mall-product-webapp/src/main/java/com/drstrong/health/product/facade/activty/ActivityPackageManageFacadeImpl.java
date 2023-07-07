package com.drstrong.health.product.facade.activty;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.drstrong.health.common.enums.OperateTypeEnum;
import com.drstrong.health.product.constants.OperationLogConstant;
import com.drstrong.health.product.facade.incentive.SkuIncentivePolicyFacade;
import com.drstrong.health.product.facade.sku.SkuManageFacade;
import com.drstrong.health.product.model.OperationLog;
import com.drstrong.health.product.model.dto.product.ActivityPackageDetailDTO;
import com.drstrong.health.product.model.entity.activty.ActivityPackageInfoEntity;
import com.drstrong.health.product.model.entity.activty.ActivityPackageSkuInfoEntity;
import com.drstrong.health.product.model.entity.sku.SkuScheduledConfigEntity;
import com.drstrong.health.product.model.entity.sku.StoreSkuInfoEntity;
import com.drstrong.health.product.model.entity.store.StoreEntity;
import com.drstrong.health.product.model.enums.DelFlagEnum;
import com.drstrong.health.product.model.enums.ErrorEnums;
import com.drstrong.health.product.model.enums.ProductTypeEnum;
import com.drstrong.health.product.model.enums.UpOffEnum;
import com.drstrong.health.product.model.request.product.ActivityPackageManageQueryRequest;
import com.drstrong.health.product.model.request.product.ActivityPackageSkuRequest;
import com.drstrong.health.product.model.request.product.SaveOrUpdateActivityPackageRequest;
import com.drstrong.health.product.model.response.PageVO;
import com.drstrong.health.product.model.response.incentive.SkuIncentivePolicyDetailVO;
import com.drstrong.health.product.model.response.product.ActivityPackageInfoVO;
import com.drstrong.health.product.model.response.result.BusinessException;
import com.drstrong.health.product.service.activty.ActivityPackageInfoService;
import com.drstrong.health.product.service.activty.ActivityPackageSkuInfoSevice;
import com.drstrong.health.product.service.sku.SkuScheduledConfigService;
import com.drstrong.health.product.service.store.StoreService;
import com.drstrong.health.product.util.BigDecimalUtil;
import com.drstrong.health.product.util.RedisKeyUtils;
import com.drstrong.health.product.utils.OperationLogSendUtil;
import com.drstrong.health.product.utils.UniqueCodeUtils;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.klock.annotation.Dlock;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * huangpeng
 * 2023/7/5 17:51
 */
@Slf4j
@Service
public class ActivityPackageManageFacadeImpl implements ActivityPackageManageFacade {

    @Autowired
    private StoreService storeService;

    @Autowired
    private ActivityPackageInfoService activityPackageInfoService;

    @Autowired
    private ActivityPackageSkuInfoSevice activityPackageSkuInfoSevice;

    @Autowired
    private SkuIncentivePolicyFacade skuIncentivePolicyFacade;

    @Autowired
    private SkuScheduledConfigService skuScheduledConfigService;

    @Autowired
    private OperationLogSendUtil operationLogSendUtil;

    @Override
    public PageVO<ActivityPackageInfoVO> queryActivityPackageManageInfo(ActivityPackageManageQueryRequest productManageQueryRequest) {
        return null;
    }

    @Override
    public List<ActivityPackageInfoVO> listActivityPackageManageInfo(ActivityPackageManageQueryRequest activityPackageManageQueryRequest) {
        return null;
    }

    /**
     * 添加分布式锁，避免套餐重复添加和修改
     *
     * @param saveOrUpdateActivityPackageRequest
     */
    @Override
    @Dlock(prefix = RedisKeyUtils.SAVE_OR_UPDATE_PACKAGE, name = "#lockKey", waitTime = 3, leaseTime = 5)
    public void addLocksaveOrUpdateActivityPackage(SaveOrUpdateActivityPackageRequest saveOrUpdateActivityPackageRequest, String lockKey) {
        SpringUtil.getBean(ActivityPackageManageFacade.class).saveOrUpdateActivityPackage(saveOrUpdateActivityPackageRequest);
    }

    /**
     * 保存/修改套餐
     *
     * @param saveOrUpdateActivityPackageRequest
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdateActivityPackage(SaveOrUpdateActivityPackageRequest saveOrUpdateActivityPackageRequest) {
        log.info(" saveOrUpdateActivityPackage(),param:{}", JSONUtil.toJsonStr(saveOrUpdateActivityPackageRequest));
        boolean updateFlag = StrUtil.isNotBlank(saveOrUpdateActivityPackageRequest.getActivityPackageCode());
        List<ActivityPackageSkuRequest> activityPackageSkuList = saveOrUpdateActivityPackageRequest.getActivityPackageSkuList();
        if (activityPackageSkuList.size() != 1) {
            throw new BusinessException(ErrorEnums.ACTIVTY_PACKAGE_SKU_MORE_THAN_ONE);
        }
        ActivityPackageSkuRequest activityPackageSkuRequest = activityPackageSkuList.get(0);
        //套餐
        ActivityPackageInfoEntity packageInfoEntity = ActivityPackageInfoEntity.builder()
                .activityPackageName(saveOrUpdateActivityPackageRequest.getActivityPackageName())
                .activityPackageCode(saveOrUpdateActivityPackageRequest.getActivityPackageCode())
                .productType(saveOrUpdateActivityPackageRequest.getProductType())
                .storeId(saveOrUpdateActivityPackageRequest.getStoreId())
                .activityStatus(UpOffEnum.DOWN.getCode())
                .originalPrice(saveOrUpdateActivityPackageRequest.getOriginalPrice().longValue())
                .preferentialPrice(saveOrUpdateActivityPackageRequest.getPreferentialPrice().longValue())
                .originalAmountDisplay(saveOrUpdateActivityPackageRequest.getOriginalAmountDisplay())
                .activityPackageImageInfo(saveOrUpdateActivityPackageRequest.getActivityPackageImageInfo())
                .activityPackageIntroduce(saveOrUpdateActivityPackageRequest.getActivityPackageIntroduce())
                .activityPackageRemark(saveOrUpdateActivityPackageRequest.getActivityPackageRemark())
                .build();
        packageInfoEntity.setChangedAt(LocalDateTime.now());
        packageInfoEntity.setChangedBy(saveOrUpdateActivityPackageRequest.getOperatorId());
        //套餐sku信息
        ActivityPackageSkuInfoEntity activityPackageSkuInfoEntity = ActivityPackageSkuInfoEntity.builder()
                .activityPackageCode(saveOrUpdateActivityPackageRequest.getActivityPackageCode())
                .skuCode(activityPackageSkuRequest.getSkuCode())
                .skuName(activityPackageSkuRequest.getSkuName())
                .originalPrice(activityPackageSkuRequest.getOriginalPrice().longValue())
                .preferentialPrice(activityPackageSkuRequest.getPreferential_price().longValue())
                .amount(activityPackageSkuRequest.getAmount())
                .build();
        activityPackageSkuInfoEntity.setChangedAt(LocalDateTime.now());
        activityPackageSkuInfoEntity.setChangedBy(saveOrUpdateActivityPackageRequest.getOperatorId());
        if (updateFlag) {
            //套餐更新
            LambdaQueryWrapper<ActivityPackageInfoEntity> updateActivityPackageWrapper = new LambdaQueryWrapper<ActivityPackageInfoEntity>()
                    .eq(ActivityPackageInfoEntity::getActivityPackageCode, packageInfoEntity.getActivityPackageCode());
            activityPackageInfoService.update(packageInfoEntity, updateActivityPackageWrapper);
            //套餐sku更新
            activityPackageSkuInfoEntity.setChangedAt(LocalDateTime.now());
            activityPackageSkuInfoEntity.setChangedBy(saveOrUpdateActivityPackageRequest.getOperatorId());
            LambdaQueryWrapper<ActivityPackageSkuInfoEntity> updateActivityPackageSkuWrapper = new LambdaQueryWrapper<ActivityPackageSkuInfoEntity>()
                    .eq(ActivityPackageSkuInfoEntity::getActivityPackageCode, packageInfoEntity.getActivityPackageCode());
            activityPackageSkuInfoSevice.update(activityPackageSkuInfoEntity, updateActivityPackageSkuWrapper);
        } else {
            //新增套餐
            packageInfoEntity.setActivityPackageCode(createSkuCode(saveOrUpdateActivityPackageRequest.getStoreId()));
            packageInfoEntity.setCreatedAt(LocalDateTime.now());
            packageInfoEntity.setCreatedBy(saveOrUpdateActivityPackageRequest.getOperatorId());
            activityPackageInfoService.save(packageInfoEntity);
            //新增套餐sku
            activityPackageSkuInfoEntity.setActivityPackageCode(packageInfoEntity.getActivityPackageCode());
            activityPackageSkuInfoEntity.setCreatedAt(LocalDateTime.now());
            activityPackageSkuInfoEntity.setCreatedBy(saveOrUpdateActivityPackageRequest.getOperatorId());
            activityPackageSkuInfoSevice.save(activityPackageSkuInfoEntity);
        }
        //更新/新增政策信息
        skuIncentivePolicyFacade.saveOrUpdateSkuPolicy(saveOrUpdateActivityPackageRequest.getSaveOrUpdateSkuPolicyRequest());
        //组装操作日志
        OperationLog operationLog = OperationLog.buildOperationLog(null, OperationLogConstant.MALL_PRODUCT_PACKAGE_CHANGE,
                OperationLogConstant.SAVE_UPDATE_SKU, saveOrUpdateActivityPackageRequest.getOperatorId(), saveOrUpdateActivityPackageRequest.getOperatorName(),
                OperateTypeEnum.CMS.getCode(), JSONUtil.toJsonStr(packageInfoEntity));
        operationLog.setBusinessId(packageInfoEntity.getActivityPackageCode());
        ActivityPackageInfoEntity afterEntity = activityPackageInfoService.findPackageByCode(packageInfoEntity.getActivityPackageCode(), null);
        operationLog.setChangeAfterData(JSONUtil.toJsonStr(afterEntity));
        operationLogSendUtil.sendOperationLog(operationLog);
    }

    /**
     * 生成activityPackageCode
     *
     * @param storeId
     * @return
     */
    private String createSkuCode(Long storeId) {
        String nextSpuCode = UniqueCodeUtils.getNextSpuCode(ProductTypeEnum.PACKAGE);
        return UniqueCodeUtils.getNextSkuCode(nextSpuCode, storeId);
    }

    @Override
    public ActivityPackageDetailDTO queryDetailByCode(String activityPackageCode) {
        //根据activityPackageCode查询套餐
        ActivityPackageInfoEntity activityPackageInfoEntity = activityPackageInfoService.findPackageByCode(activityPackageCode, null);
        //套餐关联的店铺信息
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
