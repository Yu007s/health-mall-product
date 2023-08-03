package com.drstrong.health.product.facade.sku.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.drstrong.health.common.enums.OperateTypeEnum;
import com.drstrong.health.product.constants.OperationLogConstant;
import com.drstrong.health.product.enums.ScheduledStatusEnum;
import com.drstrong.health.product.facade.sku.SkuManageFacade;
import com.drstrong.health.product.facade.sku.SkuScheduledConfigFacade;
import com.drstrong.health.product.model.OperationLog;
import com.drstrong.health.product.model.entity.sku.SkuScheduledConfigEntity;
import com.drstrong.health.product.model.enums.DelFlagEnum;
import com.drstrong.health.product.model.enums.ErrorEnums;
import com.drstrong.health.product.model.enums.UpOffEnum;
import com.drstrong.health.product.model.request.chinese.UpdateSkuStateRequest;
import com.drstrong.health.product.model.request.product.v3.ScheduledSkuUpDownRequest;
import com.drstrong.health.product.model.response.result.BusinessException;
import com.drstrong.health.product.service.sku.SkuScheduledConfigService;
import com.drstrong.health.product.service.sku.StoreSkuInfoService;
import com.drstrong.health.product.utils.ChangeEventSendUtil;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toMap;

/**
 * @author liuqiuyi
 * @date 2023/6/14 16:32
 */
@Slf4j
@Service
public class SkuScheduledConfigFacadeImpl implements SkuScheduledConfigFacade {
	@Resource
	SkuScheduledConfigService skuScheduledConfigService;

	@Resource
	ChangeEventSendUtil changeEventSendUtil;

	@Resource
	StoreSkuInfoService storeSkuInfoService;

	@Resource
	SkuManageFacade skuManageFacade;

	/**
	 * 批量修改 skuCode 的定时配置
	 *
	 * @param updateSkuStateRequest
	 * @author liuqiuyi
	 * @date 2023/6/14 16:53
	 */
	@Override
	public void batchUpdateScheduledStatusByCodes(UpdateSkuStateRequest updateSkuStateRequest) {
		Set<String> skuCodeList = updateSkuStateRequest.getSkuCodeList();
		Long operatorId = updateSkuStateRequest.getOperatorId();
		String operatorName = updateSkuStateRequest.getOperatorName();

		log.info("invoke batchUpdateScheduledStatusToCancelByCodes param:{},{},{}", skuCodeList, operatorId, operatorName);
		// 1.根据 skuCode 查询配置
		List<SkuScheduledConfigEntity> skuScheduledConfigEntityList = skuScheduledConfigService.listBySkuCode(skuCodeList, Sets.newHashSet(ScheduledStatusEnum.UN_COMPLETE.getCode(), ScheduledStatusEnum.IN_PROCESS.getCode()));
		if (CollectionUtil.isEmpty(skuScheduledConfigEntityList)) {
			log.info("未找到需要修改的sku定时上下架配置,不处理!参数为:{},{},{}", skuCodeList, operatorId, operatorName);
			return;
		}
		// 2.批量处理 skuCode 的定时配置
		Set<String> needCancelSkuCodes = skuScheduledConfigEntityList.stream().map(SkuScheduledConfigEntity::getSkuCode).collect(Collectors.toSet());
		skuScheduledConfigService.batchUpdateScheduledStatusByCodes(needCancelSkuCodes, updateSkuStateRequest.getScheduledStatus(), operatorId);
		// 3.记录日志
		sendSkuStatusUpdateLog(skuScheduledConfigEntityList, needCancelSkuCodes, operatorId, operatorName);
	}

	private void sendSkuStatusUpdateLog(List<SkuScheduledConfigEntity> beforeDataList, Set<String> skuCodeList, Long operatorId, String operatorName) {
		Map<String, SkuScheduledConfigEntity> skuCodeInfoMap = skuScheduledConfigService.listBySkuCode(skuCodeList, null)
				.stream().collect(toMap(SkuScheduledConfigEntity::getSkuCode, dto -> dto, (v1, v2) -> v1));
		// 循环发送操作日志
		beforeDataList.forEach(skuScheduledConfigEntity -> {
			OperationLog operationLog = OperationLog.buildOperationLog(skuScheduledConfigEntity.getSkuCode(), OperationLogConstant.MALL_PRODUCT_SKU_CHANGE,
					OperationLogConstant.SKU_SCHEDULED_CONFIG_CHANGE, operatorId, operatorName,
					OperateTypeEnum.CMS.getCode(), JSONUtil.toJsonStr(skuScheduledConfigEntity));
			operationLog.setChangeAfterData(JSONUtil.toJsonStr(skuCodeInfoMap.get(skuScheduledConfigEntity.getSkuCode())));
			changeEventSendUtil.sendOperationLog(operationLog);
		});
	}

	/**
	 * 保存或者更新 sku 定时上下架配置
	 *
	 * @param scheduledSkuUpDownRequest
	 * @author liuqiuyi
	 * @date 2023/6/15 10:15
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveOrUpdateSkuConfig(ScheduledSkuUpDownRequest scheduledSkuUpDownRequest) {
		// 1.查询 skuCode 配置是否存在
		SkuScheduledConfigEntity skuScheduledConfigEntity = skuScheduledConfigService.getBySkuCode(scheduledSkuUpDownRequest.getSkuCode(), null);
		// 判断是否在处理中,如果在处理中,提示稍后再试
		if (Objects.nonNull(skuScheduledConfigEntity) && Objects.equals(skuScheduledConfigEntity.getScheduledStatus(), ScheduledStatusEnum.IN_PROCESS.getCode())) {
			throw new BusinessException(ErrorEnums.SKU_SCHEDULED_IN_PROCESS);
		}
		// 2.组装操作日志
		OperationLog operationLog = OperationLog.buildOperationLog(scheduledSkuUpDownRequest.getSkuCode(), OperationLogConstant.MALL_PRODUCT_SKU_CHANGE,
				OperationLogConstant.SKU_SCHEDULED_CONFIG_CHANGE, scheduledSkuUpDownRequest.getOperatorId(), scheduledSkuUpDownRequest.getOperatorName(),
				OperateTypeEnum.CMS.getCode(), null);
		// 3.修改或者保存
		if (Objects.isNull(skuScheduledConfigEntity)) {
			operationLog.setChangeBeforeData(null);
			skuScheduledConfigEntity = SkuScheduledConfigEntity.buildDefaultEntity(scheduledSkuUpDownRequest.getOperatorId());
		} else {
			operationLog.setChangeBeforeData(JSONUtil.toJsonStr(skuScheduledConfigEntity));
			skuScheduledConfigEntity.setChangedAt(LocalDateTime.now());
			skuScheduledConfigEntity.setChangedBy(scheduledSkuUpDownRequest.getOperatorId());
		}
		skuScheduledConfigEntity.setSkuCode(scheduledSkuUpDownRequest.getSkuCode());
		skuScheduledConfigEntity.setScheduledType(scheduledSkuUpDownRequest.getScheduledType());
		skuScheduledConfigEntity.setScheduledDateTime(scheduledSkuUpDownRequest.getScheduledDateTime());
		skuScheduledConfigEntity.setScheduledStatus(ScheduledStatusEnum.UN_COMPLETE.getCode());
		skuScheduledConfigService.saveOrUpdate(skuScheduledConfigEntity);
		// 4.发送操作日志
		SkuScheduledConfigEntity changeAfterDataEntity = skuScheduledConfigService.getBySkuCode(scheduledSkuUpDownRequest.getSkuCode(), null);
		operationLog.setChangeAfterData(JSONUtil.toJsonStr(changeAfterDataEntity));
		changeEventSendUtil.sendOperationLog(operationLog);
		// 5.将 sku 的状态更新为 预约上架或预约下架
		Integer skuStatus = Objects.equals(ScheduledSkuUpDownRequest.SCHEDULED_UP, scheduledSkuUpDownRequest.getScheduledType()) ? UpOffEnum.SCHEDULED_UP.getCode() : UpOffEnum.SCHEDULED_DOWN.getCode();
		storeSkuInfoService.batchUpdateSkuStatusByCodes(Sets.newHashSet(scheduledSkuUpDownRequest.getSkuCode()), skuStatus, scheduledSkuUpDownRequest.getOperatorId());
	}

	/**
	 * 定时任务 - 执行预约上下架
	 *
	 * @author liuqiuyi
	 * @date 2023/6/15 17:44
	 */
	@Override
	public void doScheduledSkuUpDown() {
		log.info("sku预约上下架定时任务,开始处理!");
		// 1.查询需要定时上下架的 sku (状态为待处理,且配置的时间小于当前时间)
		LambdaQueryWrapper<SkuScheduledConfigEntity> queryWrapper = new LambdaQueryWrapper<SkuScheduledConfigEntity>()
				.eq(SkuScheduledConfigEntity::getDelFlag, DelFlagEnum.UN_DELETED.getCode())
				.eq(SkuScheduledConfigEntity::getScheduledStatus, ScheduledStatusEnum.UN_COMPLETE.getCode())
				.lt(SkuScheduledConfigEntity::getScheduledDateTime, LocalDateTime.now());
		List<SkuScheduledConfigEntity> skuScheduledConfigEntityList = skuScheduledConfigService.getBaseMapper().selectList(queryWrapper);
		if (CollectionUtil.isEmpty(skuScheduledConfigEntityList)) {
			log.info("sku预约上下架定时任务,未找到满足条件的数据,不处理");
			return;
		}
		// 2.将状态修改为执行中
		skuScheduledConfigEntityList.forEach(skuScheduledConfigEntity -> {
			skuScheduledConfigEntity.setScheduledStatus(ScheduledStatusEnum.IN_PROCESS.getCode());
			skuScheduledConfigEntity.setChangedBy(-1L);
			skuScheduledConfigEntity.setChangedAt(LocalDateTime.now());
		});
		skuScheduledConfigService.saveOrUpdateBatch(skuScheduledConfigEntityList);
		// 3.变更 sku 状态,变更定时任务执行状态,发送变更日志
		Map<Integer, List<SkuScheduledConfigEntity>> scheduledTypeEntityListMap = skuScheduledConfigEntityList.stream().collect(Collectors.groupingBy(SkuScheduledConfigEntity::getScheduledType));
		scheduledTypeEntityListMap.forEach((scheduledType, configEntityList) -> {
			Set<String> skuCodes = configEntityList.stream().map(SkuScheduledConfigEntity::getSkuCode).collect(Collectors.toSet());
			UpdateSkuStateRequest updateSkuStateRequest = new UpdateSkuStateRequest();
			updateSkuStateRequest.setSkuCodeList(skuCodes);
			updateSkuStateRequest.setSkuState(Objects.equals(scheduledType, ScheduledSkuUpDownRequest.SCHEDULED_UP) ? UpOffEnum.UP.getCode() : UpOffEnum.DOWN.getCode());
			updateSkuStateRequest.setScheduledStatus(ScheduledStatusEnum.SUCCESS.getCode());
			updateSkuStateRequest.setOperatorId(-1L);
			updateSkuStateRequest.setOperatorName("system");
			try {
				skuManageFacade.updateSkuStatus(updateSkuStateRequest);
			} catch (Exception e) {
				log.error("执行sku定时上下架出现异常,参数为:{},异常信息为:{}", JSONUtil.toJsonStr(updateSkuStateRequest), e);
				configEntityList.forEach(skuScheduledConfigEntity -> {
					skuScheduledConfigEntity.setScheduledStatus(ScheduledStatusEnum.UN_COMPLETE.getCode());
					skuScheduledConfigEntity.setChangedBy(-1L);
					skuScheduledConfigEntity.setChangedAt(LocalDateTime.now());
				});
				skuScheduledConfigService.saveOrUpdateBatch(configEntityList);
				log.info("将sku定时配置的状态修改为待处理,等待下一次定时任务执行,参数为:{}", JSONUtil.toJsonStr(skuCodes));
			}
		});
		log.info("sku预约上下架定时任务,处理完成,共处理了 {} 条数据", skuScheduledConfigEntityList.size());
	}
}
