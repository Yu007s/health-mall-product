package com.drstrong.health.product.facade.incentive.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.NumberUtil;
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
import com.drstrong.health.product.model.response.incentive.excel.SkuIncentivePolicyDetailExcelVO;
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
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
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
		List<SkuIncentivePolicyDetailVO> skuIncentivePolicyDetailVOList = buildSkuIncentivePolicyDetailVO(Lists.newArrayList(storeSkuInfoEntity), storeSkuInfoEntity.getSkuType(), null, null);
		return CollectionUtil.isEmpty(skuIncentivePolicyDetailVOList) ? null : skuIncentivePolicyDetailVOList.get(0);
	}

	/**
	 * 查询所有的 sku 政策信息
	 *
	 * @author liuqiuyi
	 * @date 2023/6/13 16:54
	 */
	@Override
	public SkuIncentivePolicyDetailExcelVO querySkuPolicyDetailToExcelVO(Integer productType) {
		// 1.获取所有的 sku 信息
		List<StoreSkuInfoEntity> storeSkuInfoEntityList = storeSkuInfoService.queryAllByProductType(null, productType);
		if (CollectionUtil.isEmpty(storeSkuInfoEntityList)) {
			log.info("没有查询到sku信息,不处理");
			return SkuIncentivePolicyDetailExcelVO.builder().skuIncentivePolicyDetailVOList(Lists.newArrayList()).storePolicyConfigIdsMap(Maps.newHashMap()).build();
		}
		Set<Long> storeIds = storeSkuInfoEntityList.stream().map(StoreSkuInfoEntity::getStoreId).collect(Collectors.toSet());
		// 2.获取店铺配置的收益单位
		Map<Long, Map<Long, String>> storePolicyConfigIdsMap = getStorePolicyConfigIdsMap(storeIds, productType);
		List<SkuIncentivePolicyDetailVO> skuIncentivePolicyDetailVOS = buildSkuIncentivePolicyDetailVO(storeSkuInfoEntityList, productType, storeIds, storePolicyConfigIdsMap);
		return SkuIncentivePolicyDetailExcelVO.builder().skuIncentivePolicyDetailVOList(skuIncentivePolicyDetailVOS).storePolicyConfigIdsMap(storePolicyConfigIdsMap).build();
	}

	private List<SkuIncentivePolicyDetailVO> buildSkuIncentivePolicyDetailVO(List<StoreSkuInfoEntity> storeSkuInfoEntityList, Integer productType, Set<Long> storeIds, Map<Long, Map<Long, String>> storePolicyConfigIdsMap) {
		if (CollectionUtil.isEmpty(storeIds)) {
			storeIds = storeSkuInfoEntityList.stream().map(StoreSkuInfoEntity::getStoreId).collect(Collectors.toSet());
		}
		// 1.获取店铺信息
		Map<Long, String> storeIdNameMap = storeService.listByIds(storeIds).stream().collect(Collectors.toMap(StoreEntity::getId, StoreEntity::getStoreName, (v1, v2) -> v1));
		// 2.获取供应商信息
		Set<Long> supplierIds = new HashSet<>();
		Set<Long> labelIds = new HashSet<>();
		Set<String> skuCodes = Sets.newHashSetWithExpectedSize(storeSkuInfoEntityList.size());
		storeSkuInfoEntityList.forEach(storeSkuInfoEntity -> {
			supplierIds.addAll(storeSkuInfoEntity.getSupplierInfo());
			labelIds.addAll(storeSkuInfoEntity.getLabelInfo());
			skuCodes.add(storeSkuInfoEntity.getSkuCode());
		});
		Map<Long, String> supplierNameToMap = supplierRemoteProService.getSupplierNameToMap(Lists.newArrayList(supplierIds));
		// 3.获取标签信息
		Map<Long, LabelInfoEntity> labelIdAndInfoEntityMap = labelInfoService.queryByIds(Lists.newArrayList(labelIds))
				.stream().collect(Collectors.toMap(LabelInfoEntity::getId, dto -> dto, (v1, v2) -> v1));
		// 4.根据店铺id和类型获取店铺下的自定义收益单元
		if (CollectionUtil.isEmpty(storePolicyConfigIdsMap)) {
			storePolicyConfigIdsMap = getStorePolicyConfigIdsMap(storeIds, productType);
		}
		// 5.获取sku的收益单元配置
		Map<String, SkuIncentivePolicyEntity> skuCodePolicyEntityMap = skuIncentivePolicyService.listBySkuCode(skuCodes)
				.stream().collect(Collectors.toMap(SkuIncentivePolicyEntity::getSkuCode, dto -> dto, (v1, v2) -> v1));
		// 6.组装参数
		List<SkuIncentivePolicyDetailVO> skuIncentivePolicyDetailVOList = Lists.newArrayListWithCapacity(storeSkuInfoEntityList.size());
		for (StoreSkuInfoEntity storeSkuInfoEntity : storeSkuInfoEntityList) {
			// sku 的供应商信息
			List<SupplierInfoVO> supplierInfoVOList = Lists.newArrayListWithCapacity(storeSkuInfoEntity.getSupplierInfo().size());
			storeSkuInfoEntity.getSupplierInfo().forEach(supplierId -> supplierInfoVOList.add(new SupplierInfoVO(supplierId, supplierNameToMap.get(supplierId))));
			// sku 的标签信息
			List<LabelDTO> skuLabelList = Lists.newArrayListWithCapacity(storeSkuInfoEntity.getLabelInfo().size());
			storeSkuInfoEntity.getLabelInfo().forEach(labelId -> skuLabelList.add(new LabelDTO(labelId, labelIdAndInfoEntityMap.getOrDefault(labelId, new LabelInfoEntity()).getLabelName(), null, null)));
			// sku 的政策信息
			SkuIncentivePolicyEntity skuIncentivePolicyEntity = skuCodePolicyEntityMap.get(storeSkuInfoEntity.getSkuCode());
			// 店铺下配置的收益单元
			Map<Long, String> policyConfigIdsMap = storePolicyConfigIdsMap.getOrDefault(storeSkuInfoEntity.getStoreId(), Maps.newHashMap());

			SkuIncentivePolicyDetailVO skuIncentivePolicyDetailVO = SkuIncentivePolicyDetailVO.builder()
					.skuCode(storeSkuInfoEntity.getSkuCode())
					.skuName(storeSkuInfoEntity.getSkuName())
					.storeId(storeSkuInfoEntity.getStoreId())
					.storeName(storeIdNameMap.get(storeSkuInfoEntity.getStoreId()))
					.supplierInfoList(supplierInfoVOList)
					.skuLabelList(skuLabelList)
					.price(BigDecimalUtil.F2Y(storeSkuInfoEntity.getPrice()))
					.costPrice(ObjectUtil.isNull(skuIncentivePolicyEntity) ? new BigDecimal("-1") : BigDecimalUtil.F2Y(skuIncentivePolicyEntity.getCostPrice()))
					.build();
			// 计算利润率(零售价-成本价)/成本价,只有当成本价大于 0 时才计算,避免除 0 异常
			if (NumberUtil.isGreater(skuIncentivePolicyDetailVO.getCostPrice(), BigDecimal.ZERO)) {
				BigDecimal profit = skuIncentivePolicyDetailVO.getPrice().subtract(skuIncentivePolicyDetailVO.getCostPrice())
						.divide(skuIncentivePolicyDetailVO.getCostPrice(), 2, RoundingMode.HALF_UP);
				skuIncentivePolicyDetailVO.setProfit(profit);
			}

			// 将配置的收益单元转成 map
			Map<Long, SkuIncentivePolicyEntity.IncentivePolicyInfo> configIdPolicyInfoMap = Optional.ofNullable(skuIncentivePolicyEntity).map(SkuIncentivePolicyEntity::getIncentivePolicyInfo).orElse(Lists.newArrayList())
					.stream().collect(Collectors.toMap(SkuIncentivePolicyEntity.IncentivePolicyInfo::getPolicyConfigId, dto -> dto, (v1, v2) -> v1));
			// 组装最终的参数
			List<SkuIncentivePolicyVO> skuIncentivePolicyList = Lists.newArrayListWithCapacity(policyConfigIdsMap.size());
			policyConfigIdsMap.forEach((policyConfigEntityId, policyConfigName) -> {
				SkuIncentivePolicyEntity.IncentivePolicyInfo policyInfo = configIdPolicyInfoMap.getOrDefault(policyConfigEntityId, new SkuIncentivePolicyEntity.IncentivePolicyInfo());
				SkuIncentivePolicyVO skuIncentivePolicyVO = SkuIncentivePolicyVO.builder()
						.policyConfigId(policyConfigEntityId)
						.policyConfigName(policyConfigName)
						.policyType(ObjectUtil.defaultIfNull(policyInfo.getPolicyType(), EarningPolicyTypeEnum.NONE.getCode()))
						.policyTypeName(EarningPolicyTypeEnum.getValueByCode(ObjectUtil.defaultIfNull(policyInfo.getPolicyType(), EarningPolicyTypeEnum.NONE.getCode())))
						.policyValue(policyInfo.getPolicyValue())
						.build();
				skuIncentivePolicyList.add(skuIncentivePolicyVO);
			});
			skuIncentivePolicyDetailVO.setSkuIncentivePolicyList(skuIncentivePolicyList);
			skuIncentivePolicyDetailVOList.add(skuIncentivePolicyDetailVO);
		}
		return skuIncentivePolicyDetailVOList;
	}

	/**
	 * 获取店铺的激励政策收益单位
	 */
	private Map<Long, Map<Long, String>> getStorePolicyConfigIdsMap(Set<Long> storeIds, Integer productType) {
		List<IncentivePolicyConfigEntity> incentivePolicyConfigEntityList = incentivePolicyConfigService.listByStoreIdsAndGoalType(storeIds, productType);
		if (CollectionUtil.isEmpty(incentivePolicyConfigEntityList)) {
			log.info("未查询到任何店铺的sku政策信息,参数为:{},{}", storeIds, productType);
			return Maps.newHashMap();
		}
		// 组装店铺的sku激励政策收益单位信息
		Map<Long, Map<Long, String>> storeIdPolicyConfigIdAndNameMap = Maps.newHashMapWithExpectedSize(storeIds.size());
		incentivePolicyConfigEntityList.forEach(incentivePolicyConfigEntity -> {
			storeIdPolicyConfigIdAndNameMap.putIfAbsent(incentivePolicyConfigEntity.getStoreId(), Maps.newHashMap());
			storeIdPolicyConfigIdAndNameMap.get(incentivePolicyConfigEntity.getStoreId()).putIfAbsent(incentivePolicyConfigEntity.getId(), incentivePolicyConfigEntity.getEarningName());
		});
		return storeIdPolicyConfigIdAndNameMap;
	}
}
