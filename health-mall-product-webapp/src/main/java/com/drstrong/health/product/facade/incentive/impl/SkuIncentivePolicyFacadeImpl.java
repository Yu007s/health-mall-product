package com.drstrong.health.product.facade.incentive.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.drstrong.health.common.enums.OperateTypeEnum;
import com.drstrong.health.product.constants.OperationLogConstant;
import com.drstrong.health.product.facade.incentive.SkuIncentivePolicyFacade;
import com.drstrong.health.product.model.OperationLog;
import com.drstrong.health.product.model.dto.label.LabelDTO;
import com.drstrong.health.product.model.entity.incentive.IncentivePolicyConfigEntity;
import com.drstrong.health.product.model.entity.incentive.SkuIncentivePolicyEntity;
import com.drstrong.health.product.model.entity.label.LabelInfoEntity;
import com.drstrong.health.product.model.entity.sku.StoreSkuInfoEntity;
import com.drstrong.health.product.model.entity.store.StoreEntity;
import com.drstrong.health.product.model.enums.EarningPolicyTypeEnum;
import com.drstrong.health.product.model.enums.ErrorEnums;
import com.drstrong.health.product.model.request.incentive.SaveEarningNameRequest;
import com.drstrong.health.product.model.request.incentive.SaveOrUpdateSkuPolicyRequest;
import com.drstrong.health.product.model.response.incentive.SkuIncentivePolicyDetailVO;
import com.drstrong.health.product.model.response.incentive.SkuIncentivePolicyVO;
import com.drstrong.health.product.model.response.result.BusinessException;
import com.drstrong.health.product.model.response.store.v3.SupplierInfoVO;
import com.drstrong.health.product.remote.pro.SupplierRemoteProService;
import com.drstrong.health.product.service.incentive.IncentivePolicyConfigService;
import com.drstrong.health.product.service.incentive.SkuIncentivePolicyService;
import com.drstrong.health.product.service.label.LabelInfoService;
import com.drstrong.health.product.service.sku.StoreSkuInfoService;
import com.drstrong.health.product.service.store.StoreService;
import com.drstrong.health.product.util.BigDecimalUtil;
import com.drstrong.health.product.utils.OperationLogSendUtil;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author liuqiuyi
 * @date 2023/6/7 14:41
 */
@Slf4j
@Service
public class SkuIncentivePolicyFacadeImpl implements SkuIncentivePolicyFacade {
	@Resource
	IncentivePolicyConfigService incentivePolicyConfigService;

	@Resource
	SkuIncentivePolicyService skuIncentivePolicyService;

	@Resource
	private OperationLogSendUtil operationLogSendUtil;

	@Resource
	StoreSkuInfoService storeSkuInfoService;

	@Resource
	StoreService storeService;

	@Resource
	SupplierRemoteProService supplierRemoteProService;

	@Resource
	LabelInfoService labelInfoService;

	/**
	 * 保存店铺下的收益名称
	 *
	 * @param saveEarningNameRequest
	 * @author liuqiuyi
	 * @date 2023/6/7 11:42
	 */
	@Override
	public void saveEarningName(SaveEarningNameRequest saveEarningNameRequest) {
		// 1.校验店铺下同类型的配置项,名称不能重复
		IncentivePolicyConfigEntity incentivePolicyConfigEntity = incentivePolicyConfigService.queryByStoreIdAndGoalType(saveEarningNameRequest.getStoreId(),
				saveEarningNameRequest.getConfigGoalType(), saveEarningNameRequest.getEarningName());
		if (Objects.nonNull(incentivePolicyConfigEntity)) {
			throw new BusinessException(ErrorEnums.INCENTIVE_POLICY_CONFIG_REPEAT);
		}
		// 2.保存
		IncentivePolicyConfigEntity saveParam = IncentivePolicyConfigEntity.buildDefaultEntity(saveEarningNameRequest.getOperatorId());
		saveParam.setStoreId(saveEarningNameRequest.getStoreId());
		saveParam.setConfigGoalType(saveEarningNameRequest.getConfigGoalType());
		saveParam.setEarningName(saveEarningNameRequest.getEarningName());
		incentivePolicyConfigService.save(saveParam);
	}

	/**
	 * 保存或者更新 sku 的激励政策
	 *
	 * @param saveOrUpdateSkuPolicyRequest
	 * @author liuqiuyi
	 * @date 2023/6/7 16:52
	 */
	@Override
	public void saveOrUpdateSkuPolicy(SaveOrUpdateSkuPolicyRequest saveOrUpdateSkuPolicyRequest) {
		// 校验 skuCode 是否存在
		storeSkuInfoService.checkSkuExistByCode(saveOrUpdateSkuPolicyRequest.getSkuCode(), null);
		// 1.判断是保存还是更新激励政策
		SkuIncentivePolicyEntity skuIncentivePolicyEntity = skuIncentivePolicyService.queryBySkuCode(saveOrUpdateSkuPolicyRequest.getSkuCode());
		// 记录日志
		OperationLog operationLog = OperationLog.buildOperationLog(saveOrUpdateSkuPolicyRequest.getSkuCode(), OperationLogConstant.MALL_PRODUCT_SKU_CHANGE,
				OperationLogConstant.SKU_INCENTIVE_POLICY_CHANGE, saveOrUpdateSkuPolicyRequest.getOperatorId(), saveOrUpdateSkuPolicyRequest.getOperatorName(),
				OperateTypeEnum.CMS.getCode(), JSONUtil.toJsonStr(skuIncentivePolicyEntity));
		if (Objects.isNull(skuIncentivePolicyEntity)) {
			skuIncentivePolicyEntity = SkuIncentivePolicyEntity.buildDefaultEntity(saveOrUpdateSkuPolicyRequest.getOperatorId());
		}
		skuIncentivePolicyEntity.setStoreId(saveOrUpdateSkuPolicyRequest.getStoreId());
		skuIncentivePolicyEntity.setSkuCode(saveOrUpdateSkuPolicyRequest.getSkuCode());
		skuIncentivePolicyEntity.setCostPrice(BigDecimalUtil.Y2F(saveOrUpdateSkuPolicyRequest.getCostPrice()));
		skuIncentivePolicyEntity.setIncentivePolicyInfo(BeanUtil.copyToList(saveOrUpdateSkuPolicyRequest.getSkuIncentivePolicyList(), SkuIncentivePolicyEntity.IncentivePolicyInfo.class));
		skuIncentivePolicyEntity.setChangedBy(saveOrUpdateSkuPolicyRequest.getOperatorId());
		skuIncentivePolicyEntity.setChangedAt(LocalDateTime.now());
		skuIncentivePolicyService.saveOrUpdate(skuIncentivePolicyEntity);
		// 2.保存操作日志
		operationLogSendUtil.sendOperationLog(operationLog);
	}

	/**
	 * 根据 skuCode 查询激励政策信息
	 *
	 * @param skuCode
	 * @author liuqiuyi
	 * @date 2023/6/8 14:30
	 */
	@Override
	public SkuIncentivePolicyDetailVO queryPolicyDetailBySkuCode(String skuCode) {
		// 1.查询 sku 是否存在
		StoreSkuInfoEntity storeSkuInfoEntity = storeSkuInfoService.checkSkuExistByCode(skuCode, null);
		// 2.组装返回值
		return buildSkuIncentivePolicyDetailVO(storeSkuInfoEntity);
	}

	private SkuIncentivePolicyDetailVO buildSkuIncentivePolicyDetailVO(StoreSkuInfoEntity storeSkuInfoEntity) {
		// 1.获取店铺信息
		StoreEntity storeEntity = storeService.getById(storeSkuInfoEntity.getStoreId());
		// 2.获取供应商信息
		Map<Long, String> supplierNameToMap = supplierRemoteProService.getSupplierNameToMap(storeSkuInfoEntity.getSupplierInfo());
		List<SupplierInfoVO> supplierInfoVOList = Lists.newArrayListWithCapacity(storeSkuInfoEntity.getSupplierInfo().size());
		storeSkuInfoEntity.getSupplierInfo().forEach(supplierId -> supplierInfoVOList.add(new SupplierInfoVO(supplierId, supplierNameToMap.get(supplierId))));
		// 3.获取标签信息
		Map<Long, LabelInfoEntity> labelIdAndInfoEntityMap = labelInfoService.queryByIds(storeSkuInfoEntity.getLabelInfo()).stream().collect(Collectors.toMap(LabelInfoEntity::getId, dto -> dto, (v1, v2) -> v1));
		List<LabelDTO> skuLabelList = Lists.newArrayListWithCapacity(storeSkuInfoEntity.getLabelInfo().size());
		storeSkuInfoEntity.getLabelInfo().forEach(labelId -> skuLabelList.add(new LabelDTO(labelId, labelIdAndInfoEntityMap.getOrDefault(labelId, new LabelInfoEntity()).getLabelName(), null, null)));
		// 4.获取sku的收益单元配置
		SkuIncentivePolicyEntity skuIncentivePolicyEntity = skuIncentivePolicyService.queryBySkuCode(storeSkuInfoEntity.getSkuCode());
		// 组装
		SkuIncentivePolicyDetailVO skuIncentivePolicyDetailVO = SkuIncentivePolicyDetailVO.builder()
				.skuCode(storeSkuInfoEntity.getSkuCode())
				.skuName(storeSkuInfoEntity.getSkuName())
				.storeId(storeSkuInfoEntity.getStoreId())
				.storeName(storeEntity.getStoreName())
				.supplierInfoList(supplierInfoVOList)
				.skuLabelList(skuLabelList)
				.price(BigDecimalUtil.F2Y(storeSkuInfoEntity.getPrice()))
				.costPrice(ObjectUtil.isNull(skuIncentivePolicyEntity) ? new BigDecimal("-1") : BigDecimalUtil.F2Y(skuIncentivePolicyEntity.getCostPrice()))
				.build();
		// 4.根据店铺id和类型获取店铺下的自定义收益单元
		Map<Long, IncentivePolicyConfigEntity> idPolicyConfigEntityMap = incentivePolicyConfigService.listByStoreIdAndGoalType(storeSkuInfoEntity.getStoreId(), storeSkuInfoEntity.getSkuType())
				.stream().collect(Collectors.toMap(IncentivePolicyConfigEntity::getId, dto -> dto, (v1, v2) -> v1));
		if (CollectionUtil.isEmpty(idPolicyConfigEntityMap)) {
			log.info("店铺下未配置收益单元,不设置sku收益配置信息,店铺id为:{},skuCode:{}", storeSkuInfoEntity.getStoreId(), storeSkuInfoEntity.getSkuCode());
			return skuIncentivePolicyDetailVO;
		}
		// 将配置的收益单元转成 map
		Map<Long, SkuIncentivePolicyEntity.IncentivePolicyInfo> configIdPolicyInfoMap = Optional.ofNullable(skuIncentivePolicyEntity).map(SkuIncentivePolicyEntity::getIncentivePolicyInfo).orElse(Lists.newArrayList())
				.stream().collect(Collectors.toMap(SkuIncentivePolicyEntity.IncentivePolicyInfo::getPolicyConfigId, dto -> dto, (v1, v2) -> v1));
		// 组装最终的参数
		List<SkuIncentivePolicyVO> skuIncentivePolicyList = Lists.newArrayListWithCapacity(idPolicyConfigEntityMap.size());
		idPolicyConfigEntityMap.forEach((policyConfigEntityId, policyConfigEntity) -> {
			SkuIncentivePolicyEntity.IncentivePolicyInfo policyInfo = configIdPolicyInfoMap.getOrDefault(policyConfigEntityId, new SkuIncentivePolicyEntity.IncentivePolicyInfo());
			SkuIncentivePolicyVO skuIncentivePolicyVO = SkuIncentivePolicyVO.builder()
					.policyConfigId(policyConfigEntityId)
					.policyConfigName(policyConfigEntity.getEarningName())
					.policyType(policyInfo.getPolicyType())
					.policyTypeName(EarningPolicyTypeEnum.getValueByCode(policyInfo.getPolicyType()))
					.policyValue(policyInfo.getPolicyValue())
					.build();
			skuIncentivePolicyList.add(skuIncentivePolicyVO);
		});
		skuIncentivePolicyDetailVO.setSkuIncentivePolicyList(skuIncentivePolicyList);
		return skuIncentivePolicyDetailVO;
	}
}
