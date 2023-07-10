package com.drstrong.health.product.facade.activty;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.drstrong.health.common.enums.OperateTypeEnum;
import com.drstrong.health.product.constants.OperationLogConstant;
import com.drstrong.health.product.facade.incentive.SkuIncentivePolicyFacade;
import com.drstrong.health.product.facade.sku.SkuManageFacade;
import com.drstrong.health.product.facade.sku.SkuScheduledConfigFacade;
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
import com.drstrong.health.product.model.request.chinese.UpdateSkuStateRequest;
import com.drstrong.health.product.model.request.product.ActivityPackageManageQueryRequest;
import com.drstrong.health.product.model.request.product.ActivityPackageSkuRequest;
import com.drstrong.health.product.model.request.product.SaveOrUpdateActivityPackageRequest;
import com.drstrong.health.product.model.request.product.v3.ScheduledSkuUpDownRequest;
import com.drstrong.health.product.model.response.PageVO;
import com.drstrong.health.product.model.response.incentive.SkuIncentivePolicyDetailVO;
import com.drstrong.health.product.model.response.product.ActivityPackageInfoVO;
import com.drstrong.health.product.model.response.result.BusinessException;
import com.drstrong.health.product.model.response.result.ResultStatus;
import com.drstrong.health.product.service.activty.ActivityPackageInfoService;
import com.drstrong.health.product.service.activty.ActivityPackageSkuInfoSevice;
import com.drstrong.health.product.service.activty.impl.ActivityPackageSkuInfoSeviceImpl;
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

import static java.util.stream.Collectors.toMap;

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

    @Autowired
    private SkuScheduledConfigFacade skuScheduledConfigFacade;

    /**
     * 条件分页列表查询
     *
     * @param activityPackageManageQueryRequest
     * @return
     */
    @Override
    public PageVO<ActivityPackageInfoVO> queryActivityPackageManageInfo(ActivityPackageManageQueryRequest activityPackageManageQueryRequest) {
        log.info("invoke queryActivityPackageManageInfo(),param:{}", JSONUtil.toJsonStr(activityPackageManageQueryRequest));
        Page<ActivityPackageInfoEntity> activityPackageInfoEntityPage = activityPackageInfoService.pageQueryByParam(activityPackageManageQueryRequest);
        if (activityPackageInfoEntityPage == null || CollectionUtil.isEmpty(activityPackageInfoEntityPage.getRecords())) {
            log.info("未查询到任何套餐数据,参数为:{}", JSONUtil.toJsonStr(activityPackageManageQueryRequest));
            return PageVO.newBuilder().result(Lists.newArrayList()).totalCount(0).pageNo(activityPackageManageQueryRequest.getPageNo()).pageSize(activityPackageManageQueryRequest.getPageSize()).build();
        }
        List<ActivityPackageInfoVO> activityPackageInfoVOList = buildAgreementActivityPackageInfoVo(activityPackageInfoEntityPage.getRecords());
        return PageVO.newBuilder()
                .result(activityPackageInfoVOList)
                .totalCount((int) activityPackageInfoEntityPage.getTotal())
                .pageNo(activityPackageManageQueryRequest.getPageNo())
                .pageSize(activityPackageManageQueryRequest.getPageSize())
                .build();
    }

    /**
     * 组装列表信息
     *
     * @param pageListRecords
     * @return
     */
    private List<ActivityPackageInfoVO> buildAgreementActivityPackageInfoVo(List<ActivityPackageInfoEntity> pageListRecords) {
        //店铺信息
        Set<Long> storeIds = pageListRecords.stream().map(ActivityPackageInfoEntity::getStoreId).collect(Collectors.toSet());
        Map<Long, String> storeIdNameMap = storeService.listByIds(storeIds).stream().collect(Collectors.toMap(StoreEntity::getId, StoreEntity::getStoreName, (v1, v2) -> v1));
        List<ActivityPackageInfoVO> activityPackageInfoVOList = new ArrayList<>();
        for (ActivityPackageInfoEntity record : pageListRecords) {
            ActivityPackageInfoVO activityPackageInfoVO = ActivityPackageInfoVO.builder()
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
        log.info("invoke queryActivityPackageManageInfo(),param:{}", JSONUtil.toJsonStr(activityPackageManageQueryRequest));
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
        log.info("invoke saveOrUpdateActivityPackage(),param:{}", JSONUtil.toJsonStr(saveOrUpdateActivityPackageRequest));
        boolean updateFlag = StrUtil.isNotBlank(saveOrUpdateActivityPackageRequest.getActivityPackageCode());
        List<ActivityPackageSkuRequest> activityPackageSkuList = saveOrUpdateActivityPackageRequest.getActivityPackageSkuList();
        if (CollectionUtils.isEmpty(activityPackageSkuList)) {
            log.error("套餐至少包含一种药品种类。");
            throw new BusinessException(ErrorEnums.ACTIVTY_PACKAGE_SKU_AT_LEAST_ONE);
        }
        if (activityPackageSkuList.size() != ActivityPackageSkuInfoEntity.LIMITED_NUMBER_OF_PACHAGES_SKUS) {
            log.error("目前套餐药品种类大于支持的药品种类数量。");
            throw new BusinessException(ErrorEnums.ACTIVTY_PACKAGE_SKU_MORE_THAN_ONE);
        }
        ActivityPackageSkuRequest activityPackageSkuRequest = activityPackageSkuList.get(0);
        LocalDateTime dateTime = LocalDateTime.now();
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
        packageInfoEntity.setChangedAt(dateTime);
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
        activityPackageSkuInfoEntity.setChangedAt(dateTime);
        activityPackageSkuInfoEntity.setChangedBy(saveOrUpdateActivityPackageRequest.getOperatorId());
        if (updateFlag) {
            //套餐更新
            LambdaQueryWrapper<ActivityPackageInfoEntity> updateActivityPackageWrapper = new LambdaQueryWrapper<ActivityPackageInfoEntity>()
                    .eq(ActivityPackageInfoEntity::getDelFlag, DelFlagEnum.UN_DELETED.getCode())
                    .eq(ActivityPackageInfoEntity::getActivityPackageCode, packageInfoEntity.getActivityPackageCode());
            activityPackageInfoService.update(packageInfoEntity, updateActivityPackageWrapper);
            //套餐sku更新
            activityPackageSkuInfoEntity.setChangedAt(dateTime);
            activityPackageSkuInfoEntity.setChangedBy(saveOrUpdateActivityPackageRequest.getOperatorId());
            LambdaQueryWrapper<ActivityPackageSkuInfoEntity> updateActivityPackageSkuWrapper = new LambdaQueryWrapper<ActivityPackageSkuInfoEntity>()
                    .eq(ActivityPackageSkuInfoEntity::getDelFlag, DelFlagEnum.UN_DELETED.getCode())
                    .eq(ActivityPackageSkuInfoEntity::getActivityPackageCode, packageInfoEntity.getActivityPackageCode());
            activityPackageSkuInfoSevice.update(activityPackageSkuInfoEntity, updateActivityPackageSkuWrapper);
        } else {
            //新增套餐
            packageInfoEntity.setActivityPackageCode(createSkuCode(saveOrUpdateActivityPackageRequest.getStoreId()));
            packageInfoEntity.setCreatedAt(dateTime);
            packageInfoEntity.setCreatedBy(saveOrUpdateActivityPackageRequest.getOperatorId());
            activityPackageInfoService.save(packageInfoEntity);
            //新增套餐sku
            activityPackageSkuInfoEntity.setActivityPackageCode(packageInfoEntity.getActivityPackageCode());
            activityPackageSkuInfoEntity.setCreatedAt(dateTime);
            activityPackageSkuInfoEntity.setCreatedBy(saveOrUpdateActivityPackageRequest.getOperatorId());
            activityPackageSkuInfoSevice.save(activityPackageSkuInfoEntity);
        }
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

    /**
     * 查询套餐详情
     *
     * @param activityPackageCode
     * @return
     */
    @Override
    public ActivityPackageDetailDTO queryDetailByCode(String activityPackageCode) {
        //根据activityPackageCode查询套餐
        ActivityPackageInfoEntity activityPackageInfoEntity = activityPackageInfoService.findPackageByCode(activityPackageCode, null);
        //套餐关联的店铺信息
        StoreEntity storeEntity = storeService.getById(activityPackageInfoEntity.getStoreId());
        if (storeEntity == null) {
            throw new BusinessException(ResultStatus.PARAM_ERROR.getCode(), "错误的店铺类型名称");
        }
        //套餐sku信息
        List<ActivityPackageSkuInfoEntity> packageSkuInfoEntityList = activityPackageSkuInfoSevice.findPackageByCode(activityPackageCode);
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
                .activityPackageSkuInfoEntityList(packageSkuInfoEntityList)
                .build();
        return activityPackageDetailDTO;
    }

    /**
     * 上下架处理
     *
     * @param updateSkuStateRequest
     */
    @Override
    public void updateActivityPackageStatus(UpdateSkuStateRequest updateSkuStateRequest) {
        log.info("invoke updateActivityPackageStatus param:{}", JSONUtil.toJsonStr(updateSkuStateRequest));
        List<ActivityPackageInfoEntity> activityPackageInfoEntityList = activityPackageInfoService.queryByActivityPackageCode(updateSkuStateRequest.getSkuCodeList());
        //信息校验
        checkActivityPackageStatus(updateSkuStateRequest, activityPackageInfoEntityList);
        //修改状态
        activityPackageInfoService.batchUpdateActivityStatusByCodes(updateSkuStateRequest.getSkuCodeList(), updateSkuStateRequest.getSkuState(), updateSkuStateRequest.getOperatorId());
        // 操作日志
        sendActivityStatusUpdateLog(activityPackageInfoEntityList, updateSkuStateRequest.getSkuCodeList(), updateSkuStateRequest.getOperatorId(), updateSkuStateRequest.getOperatorName());
        //定时上下架处理
        skuScheduledConfigFacade.batchUpdateScheduledStatusByCodes(updateSkuStateRequest);
    }

    /**
     * 预约上下架处理
     *
     * @param scheduledSkuUpDownRequest
     */
    @Override
    public void scheduledActivityPackageUpDown(ScheduledSkuUpDownRequest scheduledSkuUpDownRequest) {
        //校验套餐是否存在
        ActivityPackageInfoEntity packageInfoEntity = activityPackageInfoService.findPackageByCode(scheduledSkuUpDownRequest.getSkuCode(), null);
        //状态校验
        if (Objects.equals(UpOffEnum.UP.getCode(), packageInfoEntity.getActivityStatus()) && Objects.equals(ScheduledSkuUpDownRequest.SCHEDULED_UP, scheduledSkuUpDownRequest.getScheduledType())) {
            throw new BusinessException(ErrorEnums.ACTIVTY_PACKAGE_IS_UP_ERROR);
        }
        if (Objects.equals(UpOffEnum.DOWN.getCode(), packageInfoEntity.getActivityStatus()) && Objects.equals(ScheduledSkuUpDownRequest.SCHEDULED_DOWN, scheduledSkuUpDownRequest.getScheduledType())) {
            throw new BusinessException(ErrorEnums.ACTIVTY_PACKAGE_IS_DOWN_ERROR);
        }
        // 操作日志
        sendActivityStatusUpdateLog(Lists.newArrayList(packageInfoEntity), Sets.newHashSet(scheduledSkuUpDownRequest.getSkuCode()), scheduledSkuUpDownRequest.getOperatorId(), scheduledSkuUpDownRequest.getOperatorName());
        //定时上下架处理
        skuScheduledConfigFacade.saveOrUpdateSkuConfig(scheduledSkuUpDownRequest);
    }

    /**
     * 发送套餐状态更新日志
     *
     * @param beforeDataList
     * @param skuCodeList
     * @param operatorId
     * @param operatorName
     */
    private void sendActivityStatusUpdateLog(List<ActivityPackageInfoEntity> beforeDataList, Set<String> skuCodeList, Long operatorId, String operatorName) {
        Map<String, ActivityPackageInfoEntity> skuCodeInfoMap = activityPackageInfoService.queryByActivityPackageCode(skuCodeList)
                .stream().collect(toMap(ActivityPackageInfoEntity::getActivityPackageCode, dto -> dto, (v1, v2) -> v1));
        //循环发送操作日志
        beforeDataList.forEach(activityPackageInfoEntity -> {
            OperationLog operationLog = OperationLog.buildOperationLog(activityPackageInfoEntity.getActivityPackageCode(), OperationLogConstant.MALL_PRODUCT_PACKAGE_CHANGE,
                    OperationLogConstant.SKU_STATUS_CHANGE, operatorId, operatorName,
                    OperateTypeEnum.CMS.getCode(), JSONUtil.toJsonStr(activityPackageInfoEntity));
            operationLog.setChangeAfterData(JSONUtil.toJsonStr(skuCodeInfoMap.get(activityPackageInfoEntity.getActivityPackageCode())));
            operationLogSendUtil.sendOperationLog(operationLog);
        });
    }

    /**
     * 校验ActivityPackage状态,是否能进行更新
     */
    private void checkActivityPackageStatus(UpdateSkuStateRequest updateSkuStateRequest, List<ActivityPackageInfoEntity> activityPackageInfoEntityList) {
        if (CollectionUtil.isEmpty(activityPackageInfoEntityList) || ObjectUtil.notEqual(activityPackageInfoEntityList.size(), updateSkuStateRequest.getSkuCodeList().size())) {
            log.error("根据activityPackageCode未找到数据,可能传入的参数为空,或者存在非法的activityPackageCode,参数为:{}", JSONUtil.toJsonStr(updateSkuStateRequest));
            throw new BusinessException(ErrorEnums.ACTIVTY_PACKAGE_IS_NULL);
        }
    }
}
