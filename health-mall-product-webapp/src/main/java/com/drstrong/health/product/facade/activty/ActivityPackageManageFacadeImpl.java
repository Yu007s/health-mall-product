package com.drstrong.health.product.facade.activty;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
import com.google.common.collect.Lists;
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
import java.util.*;
import java.util.stream.Collectors;

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

    /**
     * 条件分页列表查询
     *
     * @param activityPackageManageQueryRequest
     * @return
     */
    @Override
    public PageVO<ActivityPackageInfoVO> queryActivityPackageManageInfo(ActivityPackageManageQueryRequest activityPackageManageQueryRequest) {
        log.info("queryActivityPackageManageInfo(),param:{}", JSONUtil.toJsonStr(activityPackageManageQueryRequest));
        Page<ActivityPackageInfoEntity> activityPackageInfoEntityPage = activityPackageInfoService.pageQueryByParam(activityPackageManageQueryRequest);
        List<ActivityPackageInfoEntity> pageListRecords = activityPackageInfoEntityPage.getRecords();
        if (CollectionUtil.isEmpty(pageListRecords)) {
            log.info("未查询到任何套餐数据,参数为:{}", JSONUtil.toJsonStr(activityPackageManageQueryRequest));
            return PageVO.newBuilder().result(Lists.newArrayList()).totalCount(0).pageNo(activityPackageManageQueryRequest.getPageNo()).pageSize(activityPackageManageQueryRequest.getPageSize()).build();
        }
        List<ActivityPackageInfoVO> activityPackageInfoVOList = buildAgreementActivityPackageInfoVo(pageListRecords);
        return PageVO.newBuilder().result(activityPackageInfoVOList).totalCount((int) activityPackageInfoEntityPage.getTotal()).pageNo(activityPackageManageQueryRequest.getPageNo()).pageSize(activityPackageManageQueryRequest.getPageSize()).build();
    }

    /**
     * 组装列表信息
     *
     * @param pageListRecords
     * @return
     */
    private List<ActivityPackageInfoVO> buildAgreementActivityPackageInfoVo(List<ActivityPackageInfoEntity> pageListRecords) {
        List<ActivityPackageInfoVO> activityPackageInfoVOList = new ArrayList<>();
        Set<Long> storeIds = pageListRecords.stream().map(ActivityPackageInfoEntity::getStoreId).collect(Collectors.toSet());
        Map<Long, String> storeIdNameMap = storeService.listByIds(storeIds).stream().collect(Collectors.toMap(StoreEntity::getId, StoreEntity::getStoreName, (v1, v2) -> v1));
        for (ActivityPackageInfoEntity record : pageListRecords) {
            ActivityPackageInfoVO activityPackageInfoVO = ActivityPackageInfoVO.builder()
                    .activityPackageName(record.getActivityPackageName())
                    .activityPackageCode(record.getActivityPackageCode())
                    .productType(record.getProductType())
                    .storeId(record.getStoreId())
                    .storeName(storeIdNameMap.get(record.getStoreId()))
                    .activityStatus(record.getActivityStatus())
                    .originalPrice(BigDecimalUtil.F2Y(record.getOriginalPrice()))
                    .preferentialPrice(BigDecimalUtil.F2Y(record.getPreferentialPrice()))
                    .originalAmountDisplay(record.getOriginalAmountDisplay()).build();
            activityPackageInfoVOList.add(activityPackageInfoVO);
            //活动时间是否展示 待确定 todo
        }
        return activityPackageInfoVOList;
    }

    /**
     * 列表数据导出
     *
     * @param activityPackageManageQueryRequest
     * @return
     */
    @Override
    public List<ActivityPackageInfoVO> listActivityPackageManageInfo(ActivityPackageManageQueryRequest activityPackageManageQueryRequest) {
        log.info("queryActivityPackageManageInfo(),param:{}", JSONUtil.toJsonStr(activityPackageManageQueryRequest));
        List<ActivityPackageInfoEntity> activityPackageInfoEntityList = activityPackageInfoService.listQueryByParam(activityPackageManageQueryRequest);

        if (CollectionUtil.isEmpty(activityPackageInfoEntityList)) {
            log.info("未查询到任何套餐数据,参数为:{}", JSONUtil.toJsonStr(activityPackageInfoEntityList));
            return Lists.newArrayList();
        }
        List<ActivityPackageInfoVO> activityPackageInfoVOList = buildAgreementActivityPackageInfoVo(activityPackageInfoEntityList);
        return activityPackageInfoVOList;
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
        //更新/新增政策信息 可能和西药保持一致，只通过按钮来编辑政策  todo
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
