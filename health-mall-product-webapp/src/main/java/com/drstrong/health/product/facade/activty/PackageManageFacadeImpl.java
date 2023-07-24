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
import com.drstrong.health.product.enums.ActivityStatusEnum;
import com.drstrong.health.product.facade.incentive.SkuIncentivePolicyFacade;
import com.drstrong.health.product.facade.sku.SkuBusinessBaseFacade;
import com.drstrong.health.product.facade.sku.SkuBusinessFacadeHolder;
import com.drstrong.health.product.facade.sku.SkuScheduledConfigFacade;
import com.drstrong.health.product.model.OperationLog;
import com.drstrong.health.product.model.dto.product.ActivityPackageDetailDTO;
import com.drstrong.health.product.model.dto.product.PackageSkuDetailDTO;
import com.drstrong.health.product.model.dto.sku.SkuBusinessListDTO;
import com.drstrong.health.product.model.dto.sku.SkuInfoSummaryDTO;
import com.drstrong.health.product.model.dto.stock.SkuCanStockDTO;
import com.drstrong.health.product.model.entity.activty.ActivityPackageInfoEntity;
import com.drstrong.health.product.model.entity.activty.ActivityPackageSkuInfoEntity;
import com.drstrong.health.product.model.entity.store.StoreEntity;
import com.drstrong.health.product.model.enums.DelFlagEnum;
import com.drstrong.health.product.model.enums.ErrorEnums;
import com.drstrong.health.product.model.enums.ProductTypeEnum;
import com.drstrong.health.product.model.request.chinese.UpdateSkuStateRequest;
import com.drstrong.health.product.model.request.product.ActivityPackageManageQueryRequest;
import com.drstrong.health.product.model.request.product.ActivityPackageSkuRequest;
import com.drstrong.health.product.model.request.product.SaveOrUpdateActivityPackageRequest;
import com.drstrong.health.product.model.request.sku.QuerySkuBusinessListRequest;
import com.drstrong.health.product.model.request.sku.SkuQueryRequest;
import com.drstrong.health.product.model.response.PageVO;
import com.drstrong.health.product.model.response.product.PackageManageListVO;
import com.drstrong.health.product.model.response.product.v3.AgreementSkuInfoVO;
import com.drstrong.health.product.model.response.result.BusinessException;
import com.drstrong.health.product.model.response.result.ResultStatus;
import com.drstrong.health.product.remote.pro.StockRemoteProService;
import com.drstrong.health.product.service.activty.ActivityPackageSkuInfoSevice;
import com.drstrong.health.product.service.activty.PackageService;
import com.drstrong.health.product.service.sku.SkuScheduledConfigService;
import com.drstrong.health.product.service.store.StoreService;
import com.drstrong.health.product.util.BigDecimalUtil;
import com.drstrong.health.product.util.RedisKeyUtils;
import com.drstrong.health.product.utils.OperationLogSendUtil;
import com.drstrong.health.product.utils.UniqueCodeUtils;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.klock.annotation.Dlock;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

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
public class PackageManageFacadeImpl implements PackageManageFacade {

    @Autowired
    private StoreService storeService;

    @Autowired
    private PackageService packageService;

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

    @Autowired
    private StockRemoteProService stockRemoteProService;

    @Autowired
    private SkuBusinessFacadeHolder skuBusinessFacadeHolder;

    /**
     * 条件分页列表查询
     *
     * @param activityPackageManageQueryRequest
     * @return
     */
    @Override
    public PageVO<PackageManageListVO> queryActivityPackageManageInfo(ActivityPackageManageQueryRequest activityPackageManageQueryRequest) {
        log.info("invoke queryActivityPackageManageInfo(),param:{}", JSONUtil.toJsonStr(activityPackageManageQueryRequest));
        Page<ActivityPackageInfoEntity> activityPackageInfoEntityPage = packageService.pageQueryByParam(activityPackageManageQueryRequest);
        if (activityPackageInfoEntityPage == null || CollectionUtil.isEmpty(activityPackageInfoEntityPage.getRecords())) {
            log.info("未查询到任何套餐数据,参数为:{}", JSONUtil.toJsonStr(activityPackageManageQueryRequest));
            return PageVO.newBuilder().result(Lists.newArrayList()).totalCount(0).pageNo(activityPackageManageQueryRequest.getPageNo()).pageSize(activityPackageManageQueryRequest.getPageSize()).build();
        }
        List<PackageManageListVO> activityPackageInfoVOList = buildActivityPackageInfoVo(activityPackageInfoEntityPage.getRecords());
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
    private List<PackageManageListVO> buildActivityPackageInfoVo(List<ActivityPackageInfoEntity> pageListRecords) {
        //店铺信息
        Set<Long> storeIds = pageListRecords.stream().map(ActivityPackageInfoEntity::getStoreId).collect(Collectors.toSet());
        Map<Long, String> storeIdNameMap = storeService.listByIds(storeIds).stream().collect(Collectors.toMap(StoreEntity::getId, StoreEntity::getStoreName, (v1, v2) -> v1));
        List<PackageManageListVO> activityPackageInfoVOList = new ArrayList<>();
        for (ActivityPackageInfoEntity record : pageListRecords) {
            PackageManageListVO activityPackageInfoVO = PackageManageListVO.builder()
                    .id(record.getId())
                    .activityPackageName(record.getActivityPackageName())
                    .activityPackageCode(record.getActivityPackageCode())
                    .productType(record.getProductType())
                    .storeId(record.getStoreId())
                    .storeName(storeIdNameMap.get(record.getStoreId()))
                    .activityStatus(record.getActivityStatus())
                    .activityStatusName(ActivityStatusEnum.getValueByCode(record.getActivityStatus()).getValue())
                    .price(BigDecimalUtil.F2Y(record.getPrice()))
                    .activityStartTime(Date.from(record.getActivityStartTime().atZone(ZoneId.systemDefault()).toInstant()))
                    .activityEndTime(Date.from(record.getActivityEndTime().atZone(ZoneId.systemDefault()).toInstant()))
                    .createdAt(Date.from(record.getCreatedAt().atZone(ZoneId.systemDefault()).toInstant()))
                    .activityPackageRemark(record.getActivityPackageRemark())
                    .activitySkuName(record.getActivityPackageSkuName())
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
    public List<PackageManageListVO> listActivityPackageManageInfo(ActivityPackageManageQueryRequest activityPackageManageQueryRequest) {
        log.info("invoke queryActivityPackageManageInfo(),param:{}", JSONUtil.toJsonStr(activityPackageManageQueryRequest));
        List<ActivityPackageInfoEntity> activityPackageInfoEntityList = packageService.listQueryByParam(activityPackageManageQueryRequest);
        if (CollectionUtil.isEmpty(activityPackageInfoEntityList)) {
            log.info("未查询到任何套餐数据,参数为:{}", JSONUtil.toJsonStr(activityPackageInfoEntityList));
            return Lists.newArrayList();
        }
        List<PackageManageListVO> activityPackageInfoVOList = buildActivityPackageInfoVo(activityPackageInfoEntityList);
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
        SpringUtil.getBean(PackageManageFacade.class).saveOrUpdateActivityPackage(saveOrUpdateActivityPackageRequest);
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
        //参数校验
        checkPackagePrams(saveOrUpdateActivityPackageRequest, updateFlag);

        //套餐信息
        ActivityPackageSkuRequest activityPackageSkuRequest = saveOrUpdateActivityPackageRequest.getActivityPackageSkuList().get(0);
        LocalDateTime dateTime = LocalDateTime.now();
        ActivityPackageInfoEntity packageInfoEntity = ActivityPackageInfoEntity.builder()
                .activityPackageName(saveOrUpdateActivityPackageRequest.getActivityPackageName())
                .activityPackageCode(saveOrUpdateActivityPackageRequest.getActivityPackageCode())
                .productType(saveOrUpdateActivityPackageRequest.getProductType())
                .storeId(saveOrUpdateActivityPackageRequest.getStoreId())
                .activityStatus(ActivityStatusEnum.TO_BE_STARTED.getCode())
                .price(BigDecimalUtil.Y2F(saveOrUpdateActivityPackageRequest.getPrice()))
                .activityStartTime(saveOrUpdateActivityPackageRequest.getActivityStartTime())
                .activityEndTime(saveOrUpdateActivityPackageRequest.getActivityEndTime())
                .activityPackageRemark(saveOrUpdateActivityPackageRequest.getActivityPackageRemark())
                .build();
        packageInfoEntity.setChangedAt(dateTime);
        packageInfoEntity.setChangedBy(saveOrUpdateActivityPackageRequest.getOperatorId());

        //套餐sku信息
        ActivityPackageSkuInfoEntity activityPackageSkuInfoEntity = ActivityPackageSkuInfoEntity.builder()
                .activityPackageCode(saveOrUpdateActivityPackageRequest.getActivityPackageCode())
                .skuCode(activityPackageSkuRequest.getSkuCode())
                .skuName(activityPackageSkuRequest.getSkuName())
                .originalPrice(BigDecimalUtil.Y2F(activityPackageSkuRequest.getOriginalPrice()))
                .preferentialPrice(BigDecimalUtil.Y2F(activityPackageSkuRequest.getPreferential_price()))
                .amount(activityPackageSkuRequest.getAmount())
                .build();
        activityPackageSkuInfoEntity.setChangedAt(dateTime);
        activityPackageSkuInfoEntity.setChangedBy(saveOrUpdateActivityPackageRequest.getOperatorId());
        if (updateFlag) {
            //套餐更新
            LambdaQueryWrapper<ActivityPackageInfoEntity> updateActivityPackageWrapper = new LambdaQueryWrapper<ActivityPackageInfoEntity>()
                    .eq(ActivityPackageInfoEntity::getDelFlag, DelFlagEnum.UN_DELETED.getCode())
                    .eq(ActivityPackageInfoEntity::getActivityPackageCode, packageInfoEntity.getActivityPackageCode());
            packageService.update(packageInfoEntity, updateActivityPackageWrapper);
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
            packageService.save(packageInfoEntity);
            //新增套餐sku
            activityPackageSkuInfoEntity.setActivityPackageCode(packageInfoEntity.getActivityPackageCode());
            activityPackageSkuInfoEntity.setCreatedAt(dateTime);
            activityPackageSkuInfoEntity.setCreatedBy(saveOrUpdateActivityPackageRequest.getOperatorId());
            activityPackageSkuInfoSevice.save(activityPackageSkuInfoEntity);
        }

        //组装操作日志
        OperationLog operationLog = OperationLog.buildOperationLog(packageInfoEntity.getActivityPackageCode(),
                OperationLogConstant.MALL_PRODUCT_PACKAGE_CHANGE,
                OperationLogConstant.SAVE_UPDATE_SKU,
                saveOrUpdateActivityPackageRequest.getOperatorId(),
                saveOrUpdateActivityPackageRequest.getOperatorName(),
                OperateTypeEnum.CMS.getCode(),
                JSONUtil.toJsonStr(packageInfoEntity));
        ActivityPackageInfoEntity afterEntity = packageService.findPackageByCode(packageInfoEntity.getActivityPackageCode(), null);
        operationLog.setChangeAfterData(JSONUtil.toJsonStr(afterEntity));
        operationLogSendUtil.sendOperationLog(operationLog);
    }

    /**
     * 校验参数(保存/修改套餐)
     *
     * @param saveOrUpdateActivityPackageRequest
     * @param updateFlag
     */
    private void checkPackagePrams(SaveOrUpdateActivityPackageRequest saveOrUpdateActivityPackageRequest, boolean updateFlag) {
        List<ActivityPackageSkuRequest> activityPackageSkuList = saveOrUpdateActivityPackageRequest.getActivityPackageSkuList();
        if (CollectionUtils.isEmpty(activityPackageSkuList)) {
            log.error("套餐至少包含一种药品种类,当前套餐药品种类=0");
            throw new BusinessException(ErrorEnums.ACTIVTY_PACKAGE_SKU_AT_LEAST_ONE);
        }
        if (activityPackageSkuList.size() != ActivityPackageSkuInfoEntity.LIMITED_NUMBER_OF_PACHAGES_SKUS) {
            log.error("目前套餐药品种类大于支持的药品种类数量,当前套餐药品种类={}", activityPackageSkuList.size());
            throw new BusinessException(ErrorEnums.ACTIVTY_PACKAGE_SKU_MORE_THAN_ONE);
        }
        //套餐活动时间校验
        Date startTime = Date.from(saveOrUpdateActivityPackageRequest.getActivityStartTime().atZone(ZoneId.systemDefault()).toInstant());
        Date endTime = Date.from(saveOrUpdateActivityPackageRequest.getActivityEndTime().atZone(ZoneId.systemDefault()).toInstant());
        if (startTime.getTime() >= endTime.getTime()) {
            log.error("套餐活动开始时间必须小于套餐活动结束时间,开始时间={},结束时间={}", startTime.getTime(), endTime.getTime());
            throw new BusinessException(ErrorEnums.ACTIVTY_PACKAGE_TIME_ERROR);
        }
        if (startTime.getTime() < System.currentTimeMillis() && !updateFlag) {
            //更新的时候无需判断开始时间
            log.error("套餐活动开始时间必须大于等于当前时间，开始时间={}", startTime.getTime());
            throw new BusinessException(ErrorEnums.ACTIVTY_PACKAGE_TIME_MORE_THAN_NOW);
        }
        ActivityPackageSkuRequest activityPackageSkuRequest = activityPackageSkuList.get(0);
        if (updateFlag) {
            //进行中的套餐活动，不能修改skuid、价格和数量，结束时间只能往后延迟
            ActivityPackageInfoEntity activityPackageInfoEntity = packageService.findPackageByCode(saveOrUpdateActivityPackageRequest.getActivityPackageCode(), null);
            if (!ObjectUtil.isNull(activityPackageInfoEntity) && ActivityStatusEnum.UNDER_WAY.getCode().equals(activityPackageInfoEntity.getActivityStatus())) {
                Date endTime0 = Date.from(activityPackageInfoEntity.getActivityEndTime().atZone(ZoneId.systemDefault()).toInstant());
                Date startTime0 = Date.from(activityPackageInfoEntity.getActivityStartTime().atZone(ZoneId.systemDefault()).toInstant());
                if (endTime.getTime() > endTime0.getTime()) {
                    log.error("正在进行中的套餐活动结束时间只能向后延长。");
                    throw new BusinessException(ErrorEnums.ACTIVTY_PACKAGE_UPDATE_TIME_ERROR);
                }
            }
        }

        //套餐校验：根据skuid和数量检索套餐信息，只能存在一个待开始和进行中的套餐 是否存在时间交集的
        List<ActivityPackageSkuInfoEntity> activityPackageSkuInfoEntityList = activityPackageSkuInfoSevice.queryBySkuCodeAndAmount(activityPackageSkuRequest.getSkuCode(), activityPackageSkuRequest.getAmount());
        if (CollectionUtil.isNotEmpty(activityPackageSkuInfoEntityList) && updateFlag) {
            //更新 -- 过滤掉原先的activityPackageCode
            List<ActivityPackageSkuInfoEntity> activityPackageSkuInfoEntities = activityPackageSkuInfoEntityList.stream().filter(x -> !x.getActivityPackageCode().equals(saveOrUpdateActivityPackageRequest.getActivityPackageCode())).collect(Collectors.toList());
            checkActivity(startTime, endTime, activityPackageSkuInfoEntities);
        } else if (CollectionUtil.isNotEmpty(activityPackageSkuInfoEntityList) && !updateFlag) {
            //新增
            checkActivity(startTime, endTime, activityPackageSkuInfoEntityList);
        }
    }

    /**
     * 套餐校验
     *
     * @param startTime
     * @param endTime
     * @param activityPackageSkuInfoEntityList
     */
    private void checkActivity(Date startTime, Date endTime, List<ActivityPackageSkuInfoEntity> activityPackageSkuInfoEntityList) {
        Set<String> activityPackageCodeList = activityPackageSkuInfoEntityList.stream().map(ActivityPackageSkuInfoEntity::getActivityPackageCode).collect(Collectors.toSet());
        List<ActivityPackageInfoEntity> packageInfoEntityList = packageService.queryByActivityPackageCode(activityPackageCodeList);
        if (CollectionUtil.isNotEmpty(packageInfoEntityList)) {
            List<ActivityPackageInfoEntity> packageInfoEntities = packageInfoEntityList.stream().filter(x -> ActivityStatusEnum.TO_BE_STARTED.getCode().equals(x.getActivityStatus())).collect(Collectors.toList());
            if (CollectionUtil.isNotEmpty(packageInfoEntities)) {
                log.error("已存在相同状态的活动，请勿重复创建。");
                throw new BusinessException(ErrorEnums.ACTIVTY_PACKAGE_SAVE_THE_SAME);
            }
            for (ActivityPackageInfoEntity packageInfoEntity : packageInfoEntityList) {
                //时间交集判断
                Date startTime0 = Date.from(packageInfoEntity.getActivityStartTime().atZone(ZoneId.systemDefault()).toInstant());
                Date endTime0 = Date.from(packageInfoEntity.getActivityEndTime().atZone(ZoneId.systemDefault()).toInstant());
                if (!(endTime0.getTime() < startTime.getTime() || startTime0.getTime() > endTime.getTime())) {
                    log.error("已存在相同活动且活动时间存在冲突，请勿重复创建。");
                    throw new BusinessException(ErrorEnums.ACTIVTY_PACKAGE_SAVE_TIME_CONFLICT);
                }
            }
        }
    }

    /**
     * 套餐校验
     *
     * @param startTime
     * @param endTime
     * @param activityPackageCode
     */
    private void checkActivity(Date startTime, Date endTime, String activityPackageCode) {
        ActivityPackageInfoEntity packageInfoEntity = packageService.findPackageByCode(activityPackageCode, null);
        if (!Objects.isNull(packageInfoEntity) && ActivityStatusEnum.TO_BE_STARTED.equals(packageInfoEntity.getActivityStatus())) {
            log.error("已存在相同状态的活动，请勿重复创建。");
            throw new BusinessException(ErrorEnums.ACTIVTY_PACKAGE_SAVE_THE_SAME);
        }
        //时间交集判断
        Date startTime0 = Date.from(packageInfoEntity.getActivityStartTime().atZone(ZoneId.systemDefault()).toInstant());
        Date endTime0 = Date.from(packageInfoEntity.getActivityEndTime().atZone(ZoneId.systemDefault()).toInstant());
        if (!(endTime0.getTime() < startTime.getTime() || startTime0.getTime() > endTime.getTime())) {
            log.error("已存在相同活动且活动时间存在冲突，请勿重复创建。");
            throw new BusinessException(ErrorEnums.ACTIVTY_PACKAGE_SAVE_TIME_CONFLICT);
        }
    }

    /**
     * 生成套餐编码
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
        ActivityPackageInfoEntity activityPackageInfoEntity = packageService.findPackageByCode(activityPackageCode, null);
        //套餐关联的店铺信息
        StoreEntity storeEntity = storeService.getById(activityPackageInfoEntity.getStoreId());
        if (ObjectUtil.isNull(storeEntity)) {
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
                .activityStatusName(ActivityStatusEnum.getValueByCode(activityPackageInfoEntity.getActivityStatus()).getValue())
                .price(BigDecimalUtil.F2Y(activityPackageInfoEntity.getPrice()))
                .activityStartTime(Date.from(activityPackageInfoEntity.getActivityStartTime().atZone(ZoneId.systemDefault()).toInstant()))
                .activityEndTime(Date.from(activityPackageInfoEntity.getActivityEndTime().atZone(ZoneId.systemDefault()).toInstant()))
                .activityPackageRemark(activityPackageInfoEntity.getActivityPackageRemark())
                .activityPackageSkuInfoEntityList(packageSkuDetailDTOS)
                .build();
        return activityPackageDetailDTO;
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
        Map<String, ActivityPackageInfoEntity> skuCodeInfoMap = packageService.queryByActivityPackageCode(skuCodeList)
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

    /**
     * 条件检索sku列表
     *
     * @param querySkuBusinessListRequest
     * @return
     */
    @Override
    public PageVO<SkuBusinessListDTO> getPackageSkuBusinessList(QuerySkuBusinessListRequest querySkuBusinessListRequest) {
        SkuQueryRequest skuQueryRequest = SkuQueryRequest.builder()
                .productType(querySkuBusinessListRequest.getProductType())
                .key(querySkuBusinessListRequest.getKey())
                .storeId(querySkuBusinessListRequest.getStoreId())
                .needQueryInventory(ObjectUtil.equal(1, querySkuBusinessListRequest.getNeedQueryInventory()))
                .build();
        SkuBusinessBaseFacade skuBusinessBaseFacade = skuBusinessFacadeHolder.getSkuBusinessBaseFacade(querySkuBusinessListRequest.getProductType());
        SkuInfoSummaryDTO skuInfoSummaryDTO = skuBusinessBaseFacade.querySkuByParam(skuQueryRequest);
        // 组装参数
        List<SkuBusinessListDTO> skuBusinessListDTOList = buildSkuBusinessListDTOList(skuInfoSummaryDTO, querySkuBusinessListRequest);
        // 拼接返回值
        Integer pageNo = querySkuBusinessListRequest.getPageNo();
        Integer pageSize = querySkuBusinessListRequest.getPageSize();
        int start = (pageNo - 1) * pageSize > skuBusinessListDTOList.size() ? skuBusinessListDTOList.size() : (pageNo - 1) * pageSize;
        int end = pageNo * pageSize > skuBusinessListDTOList.size() ? skuBusinessListDTOList.size() : pageNo * pageSize;
        List<SkuBusinessListDTO> result = skuBusinessListDTOList.subList(start, end);
        PageVO<SkuBusinessListDTO> pageVO = PageVO.newBuilder()
                .result(result)
                .totalCount(ObjectUtil.isNull(result) ? 0 : (int) result.size())
                .pageNo(querySkuBusinessListRequest.getPageNo())
                .pageSize(querySkuBusinessListRequest.getPageSize()).build();
        return pageVO;
    }

    /**
     * 定时任务：套餐定时上下架
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void doScheduledUpDown() {
        log.info("invoke doScheduledUpDown start");
        List<ActivityPackageInfoEntity> activityPackageInfoEntityList = packageService.findScheduledPackage();
        if (CollectionUtil.isEmpty(activityPackageInfoEntityList)) {
            log.info("invoke doScheduledUpDown end,activityPackageInfoEntityList is null");
            return;
        }
        long nowTime = System.currentTimeMillis();
        List<ActivityPackageInfoEntity> activityListNotStart = activityPackageInfoEntityList.stream().filter(x -> ActivityStatusEnum.TO_BE_STARTED.getCode().equals(x.getActivityStatus())).collect(Collectors.toList());
        List<ActivityPackageInfoEntity> activityListNotEnd = activityPackageInfoEntityList.stream().filter(x -> ActivityStatusEnum.UNDER_WAY.getCode().equals(x.getActivityStatus())).collect(Collectors.toList());
        if (CollectionUtil.isNotEmpty(activityListNotStart)) {
            Set<String> updateActivityCode = new HashSet<>();
            for (ActivityPackageInfoEntity activityPackageInfoEntity : activityListNotStart) {
                Date startTime = Date.from(activityPackageInfoEntity.getActivityStartTime().atZone(ZoneId.systemDefault()).toInstant());
                Date endTime = Date.from(activityPackageInfoEntity.getActivityEndTime().atZone(ZoneId.systemDefault()).toInstant());
                if (nowTime >= startTime.getTime() && nowTime < endTime.getTime()) {
                    updateActivityCode.add(activityPackageInfoEntity.getActivityPackageCode());
                }
            }
            packageService.updateActivityStatus(updateActivityCode, ActivityStatusEnum.UNDER_WAY.getCode());
        }
        if (CollectionUtil.isNotEmpty(activityListNotEnd)) {
            Set<String> updateActivityCode = new HashSet<>();
            for (ActivityPackageInfoEntity activityPackageInfoEntity : activityListNotEnd) {
                Date startTime = Date.from(activityPackageInfoEntity.getActivityStartTime().atZone(ZoneId.systemDefault()).toInstant());
                Date endTime = Date.from(activityPackageInfoEntity.getActivityEndTime().atZone(ZoneId.systemDefault()).toInstant());
                if (nowTime >= endTime.getTime()) {
                    updateActivityCode.add(activityPackageInfoEntity.getActivityPackageCode());
                }
            }
            packageService.updateActivityStatus(updateActivityCode, ActivityStatusEnum.ALREADY_ENDED.getCode());
        }
        log.info("invoke doScheduledUpDown end");
    }

    /**
     * 构建SkuBusinessList列表返回值信息
     *
     * @param skuInfoSummaryDTO
     * @param querySkuBusinessListRequest
     * @return
     */
    private List<SkuBusinessListDTO> buildSkuBusinessListDTOList(SkuInfoSummaryDTO skuInfoSummaryDTO, QuerySkuBusinessListRequest querySkuBusinessListRequest) {
        List<AgreementSkuInfoVO> skuInfoVoList = null;
        if (ProductTypeEnum.MEDICINE.getCode().equals(querySkuBusinessListRequest.getProductType())) {
            skuInfoVoList = skuInfoSummaryDTO.getWesternSkuInfoVoList();
        } else if (ProductTypeEnum.AGREEMENT.getCode().equals(querySkuBusinessListRequest.getProductType())) {
            skuInfoVoList = skuInfoSummaryDTO.getAgreementSkuInfoVoList();
        } else {
            log.error("套餐目前只支持中西药和协定方");
        }
        if (CollectionUtil.isEmpty(skuInfoVoList)) {
            return Lists.newArrayList();
        }
        List<SkuBusinessListDTO> skuBusinessListDTOList = new ArrayList<>();
        Map<String, List<SkuCanStockDTO>> skuCanStockListMap = skuInfoSummaryDTO.getSkuCanStockList();
        for (AgreementSkuInfoVO skuInfoVO : skuInfoVoList) {
            if (Objects.equals(querySkuBusinessListRequest.getNeedQueryInventory(), 1)) {
                if (CollectionUtil.isEmpty(skuCanStockListMap) || CollectionUtil.isEmpty(skuCanStockListMap.get(skuInfoVO.getSkuCode()))) {
                    log.info("当前{}库存不足，不返回", skuInfoVO.getSkuCode());
                    continue;
                }
            }
            SkuBusinessListDTO skuBusinessListDTO = SkuBusinessListDTO.builder()
                    .skuCode(skuInfoVO.getSkuCode())
                    .productType(skuInfoVO.getProductType())
                    .skuName(skuInfoVO.getSkuName())
                    .storeId(skuInfoVO.getStoreId())
                    .storeName(skuInfoVO.getStoreName())
                    .salePrice(skuInfoVO.getSalePrice())
                    .skuStatus(skuInfoVO.getSkuStatus())
                    .build();
            skuBusinessListDTOList.add(skuBusinessListDTO);
        }
        return skuBusinessListDTOList;
    }
}
