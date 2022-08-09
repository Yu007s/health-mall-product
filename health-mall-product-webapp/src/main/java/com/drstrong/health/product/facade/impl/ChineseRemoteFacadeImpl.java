package com.drstrong.health.product.facade.impl;

import com.alibaba.fastjson.JSON;
import com.drstrong.health.product.facade.ChineseRemoteFacade;
import com.drstrong.health.product.model.entity.chinese.ChineseMedicineConflictEntity;
import com.drstrong.health.product.model.entity.chinese.ChineseMedicineEntity;
import com.drstrong.health.product.model.entity.chinese.ChineseSkuInfoEntity;
import com.drstrong.health.product.model.entity.store.StoreEntity;
import com.drstrong.health.product.model.enums.ErrorEnums;
import com.drstrong.health.product.model.enums.ProductStateEnum;
import com.drstrong.health.product.model.enums.ProductTypeEnum;
import com.drstrong.health.product.model.request.chinese.QueryChineseSkuRequest;
import com.drstrong.health.product.model.request.store.AgencyStoreVO;
import com.drstrong.health.product.model.response.chinese.ChineseMedicineConflictVO;
import com.drstrong.health.product.model.response.chinese.ChineseSkuInfoExtendVO;
import com.drstrong.health.product.model.response.chinese.ChineseSkuInfoVO;
import com.drstrong.health.product.model.response.product.ProductInfoVO;
import com.drstrong.health.product.model.response.result.BusinessException;
import com.drstrong.health.product.service.chinese.ChineseMedicineConflictService;
import com.drstrong.health.product.service.chinese.ChineseMedicineService;
import com.drstrong.health.product.service.chinese.ChineseSkuInfoService;
import com.drstrong.health.product.service.store.StoreDeliveryPriorityService;
import com.drstrong.health.product.service.store.StoreService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 中药的远程接口门面层
 *
 * @author liuqiuyi
 * @date 2022/8/3 19:35
 */
@Slf4j
@Service
public class ChineseRemoteFacadeImpl implements ChineseRemoteFacade {
	@Resource
	ChineseSkuInfoService chineseSkuInfoService;

	@Resource
	StoreService storeService;

	@Resource
	ChineseMedicineService chineseMedicineService;

	@Resource
	ChineseMedicineConflictService chineseMedicineConflictService;

	@Resource
	StoreDeliveryPriorityService storeDeliveryPriorityService;

	/**
	 * 根据关键字和互联网医院 id 模糊搜索药材
	 *
	 * @param keyword  搜索关键字
	 * @param agencyId 互联网医院 id
	 * @param storeId  店铺 id
	 * @return 中药材基本信息
	 * @author liuqiuyi
	 * @date 2022/8/3 19:34
	 */
	@Override
	public List<ChineseSkuInfoVO> keywordSearch(String keyword, Long agencyId, Long storeId) {
		log.info("invoke keywordSearch() param:{},{},{}", keyword, agencyId, storeId);
		// 1.先根据互联网医院 id，获取店铺信息
		StoreEntity storeEntity = storeService.getStoreByAgencyIdOrStoreId(agencyId, storeId);
		if (Objects.isNull(storeEntity)) {
			throw new BusinessException(ErrorEnums.STORE_NOT_EXIST);
		}
		// 2.调用店铺的搜索接口，模糊查询获取所有的药材 code
		List<ChineseMedicineEntity> chineseMedicineEntityList = chineseMedicineService.likeQueryByKeyword(keyword);
		if (CollectionUtils.isEmpty(chineseMedicineEntityList)) {
			return Lists.newArrayList();
		}
		Map<String, String> medicineCodeAndNameMap = Maps.newHashMapWithExpectedSize(chineseMedicineEntityList.size());
		Map<String, BigDecimal> medicineCodeAndMaxDosageMap = Maps.newHashMapWithExpectedSize(chineseMedicineEntityList.size());
		chineseMedicineEntityList.forEach(chineseMedicineEntity -> {
			medicineCodeAndNameMap.put(chineseMedicineEntity.getMedicineCode(), chineseMedicineEntity.getMedicineName());
			medicineCodeAndMaxDosageMap.put(chineseMedicineEntity.getMedicineCode(), chineseMedicineEntity.getMaxDosage());
		});
		// 3.根据药材 code 和 店铺 id，获取 sku 信息
		List<ChineseSkuInfoEntity> skuInfoEntityList = chineseSkuInfoService.listByMedicineCodeAndStoreId(medicineCodeAndNameMap.keySet(), storeEntity.getId());
		if (CollectionUtils.isEmpty(skuInfoEntityList)) {
			return Lists.newArrayList();
		}
		// 4.组装数据返回
		return buildChineseSkuInfoVOList(storeEntity.getStoreName(), skuInfoEntityList, medicineCodeAndNameMap, medicineCodeAndMaxDosageMap);
	}

	/**
	 * 根据条件查询 sku 信息
	 * <p> 仅支持查询同一店铺的 sku 信息 </>
	 *
	 * @param chineseSkuRequest 查询条件
	 * @return 查询结果
	 * @author liuqiuyi
	 * @date 2022/8/4 14:19
	 */
	@Override
	public ProductInfoVO queryStoreSku(QueryChineseSkuRequest chineseSkuRequest) {
		log.info("invoke querySku() param:{}", JSON.toJSONString(chineseSkuRequest));

		ProductInfoVO productInfoVO = new ProductInfoVO();
		productInfoVO.setProductType(ProductTypeEnum.CHINESE.getCode());
		productInfoVO.setProductTypeName(ProductTypeEnum.CHINESE.getValue());
		// 1.先根据互联网医院 id，获取店铺信息
		StoreEntity storeEntity = storeService.getStoreByAgencyIdOrStoreId(chineseSkuRequest.getAgencyId(), chineseSkuRequest.getStoreId());
		if (Objects.isNull(storeEntity)) {
			throw new BusinessException(ErrorEnums.STORE_NOT_EXIST);
		}
		// 2.根据 skuCode 或者 药材 id 获取 sku 信息
		List<ChineseSkuInfoEntity> chineseSkuInfoEntityList = chineseSkuInfoService.queryStoreSkuByCodesOrMedicineIds(chineseSkuRequest.getSkuCodeList(), chineseSkuRequest.getMedicineIdList(), storeEntity.getId());
		if (CollectionUtils.isEmpty(chineseSkuInfoEntityList)) {
			return productInfoVO;
		}
		Set<String> medicineCodes = chineseSkuInfoEntityList.stream().map(ChineseSkuInfoEntity::getMedicineCode).collect(Collectors.toSet());
		// 3.获取药材名称
		Map<String, String> medicineCodeAndNameMap = chineseMedicineService.getByMedicineCode(medicineCodes)
				.stream().collect(Collectors.toMap(ChineseMedicineEntity::getMedicineCode, ChineseMedicineEntity::getMedicineName, (v1, v2) -> v1));
		List<ChineseSkuInfoExtendVO> skuExtendVOList = buildChineseSkuExtendVOList(storeEntity.getStoreName(), chineseSkuInfoEntityList, medicineCodeAndNameMap);
		// 3.判断是否需要查询库存信息
		if (Objects.equals(Boolean.TRUE, chineseSkuRequest.getNeedQueryStock())) {
			// TODO liuqiuyi
		}
		// 4.判断是否需要查询配送优先级
		if (Objects.equals(Boolean.TRUE, chineseSkuRequest.getNeedQueryPriority()) && Objects.nonNull(chineseSkuRequest.getAreaId())) {
			List<Long> storeChineseDeliveryInfoList = storeDeliveryPriorityService.queryByStoreIdAndArea(storeEntity.getId(), chineseSkuRequest.getAreaId());
			productInfoVO.setStoreChineseDeliveryInfoList(storeChineseDeliveryInfoList);
		}
		// 5.组装数据返回
		productInfoVO.setChineseSkuInfoExtendVOList(skuExtendVOList);
		return productInfoVO;
	}

	/**
	 * 获取所有的中药相反药材,出参和之前的业务结构保持一致
	 *
	 * @author liuqiuyi
	 * @date 2022/8/8 11:44
	 */
	@Override
	public List<ChineseMedicineConflictVO> listAllConflict() {
		List<ChineseMedicineConflictEntity> conflictEntityList = chineseMedicineConflictService.listAllConflictEntity();
		if (CollectionUtils.isEmpty(conflictEntityList)) {
			return Lists.newArrayList();
		}
		// 1.获取所有的药材 code
		Set<String> medicineCodes = Sets.newHashSetWithExpectedSize(conflictEntityList.size());
		conflictEntityList.forEach(chineseMedicineConflictEntity -> {
			medicineCodes.add(chineseMedicineConflictEntity.getMedicineCode());
			medicineCodes.addAll(Sets.newHashSet(chineseMedicineConflictEntity.getMedicineConflictCodes().split(",")));
		});
		// 2.根据药材 code 换取 id
		List<ChineseMedicineEntity> chineseMedicineEntityList = chineseMedicineService.getByMedicineCode(medicineCodes);
		if (CollectionUtils.isEmpty(chineseMedicineEntityList)) {
			return Lists.newArrayList();
		}
		Map<String, Long> medicineCodeAndIdMap = chineseMedicineEntityList.stream()
				.collect(Collectors.toMap(ChineseMedicineEntity::getMedicineCode, ChineseMedicineEntity::getId, (v1, v2) -> v1));
		// 3.组装数据返回
		List<ChineseMedicineConflictVO> medicineConflictVoList = Lists.newArrayListWithCapacity(conflictEntityList.size());
		conflictEntityList.forEach(chineseMedicineConflictEntity -> {
			Long id = medicineCodeAndIdMap.get(chineseMedicineConflictEntity.getMedicineCode());
			List<Long> conflictIdList = Stream.of(chineseMedicineConflictEntity.getMedicineConflictCodes().split(","))
					.map(medicineCodeAndIdMap::get).filter(Objects::nonNull).collect(Collectors.toList());

			if (Objects.nonNull(id) && !CollectionUtils.isEmpty(conflictIdList)) {
				ChineseMedicineConflictVO chineseMedicineConflictVO = new ChineseMedicineConflictVO();
				chineseMedicineConflictVO.setId(id);
				chineseMedicineConflictVO.setConflictIdList(conflictIdList);
				medicineConflictVoList.add(chineseMedicineConflictVO);
			}
		});
		return medicineConflictVoList;
	}

	/**
	 * 根据互联网医院 id 获取店铺信息
	 *
	 * @param agencyIds 互联网医院 id
	 * @return 互联网医院 id 和店铺 id 信息
	 * @author liuqiuyi
	 * @date 2022/8/8 19:53
	 */
	@Override
	public List<AgencyStoreVO> listStoreByAgencyIds(Set<Long> agencyIds) {
		List<StoreEntity> storeEntityList = storeService.listByIds(agencyIds);
		List<AgencyStoreVO> agencyStoreVOList = Lists.newArrayListWithCapacity(storeEntityList.size());
		storeEntityList.forEach(storeEntity -> {
			AgencyStoreVO agencyStoreVO = new AgencyStoreVO();
			agencyStoreVO.setStoreId(storeEntity.getId());
			agencyStoreVO.setAgencyId(storeEntity.getAgencyId());
			agencyStoreVO.setStoreName(storeEntity.getStoreName());
			agencyStoreVOList.add(agencyStoreVO);
		});
		return agencyStoreVOList;
	}

	private List<ChineseSkuInfoExtendVO> buildChineseSkuExtendVOList(String storeName, List<ChineseSkuInfoEntity> skuInfoEntityList, Map<String, String> medicineCodeAndNameMap) {
		List<ChineseSkuInfoExtendVO> chineseSkuInfoExtendVOList = Lists.newArrayListWithCapacity(skuInfoEntityList.size());
		skuInfoEntityList.forEach(chineseSkuInfoEntity -> {
			ChineseSkuInfoExtendVO chineseSkuInfoExtendVO = new ChineseSkuInfoExtendVO();
			BeanUtils.copyProperties(chineseSkuInfoEntity, chineseSkuInfoExtendVO);
			chineseSkuInfoExtendVO.setPrice(BigDecimal.valueOf(chineseSkuInfoEntity.getPrice()));
			chineseSkuInfoExtendVO.setProductType(ProductTypeEnum.CHINESE.getCode());
			chineseSkuInfoExtendVO.setProductTypeName(ProductTypeEnum.CHINESE.getValue());
			chineseSkuInfoExtendVO.setMedicineId(chineseSkuInfoEntity.getOldMedicineId());
			chineseSkuInfoExtendVO.setMedicineName(medicineCodeAndNameMap.get(chineseSkuInfoEntity.getMedicineCode()));
			chineseSkuInfoExtendVO.setStoreName(storeName);
			chineseSkuInfoExtendVO.setSkuState(chineseSkuInfoEntity.getSkuStatus());
			chineseSkuInfoExtendVO.setSkuStateName(ProductStateEnum.getValueByCode(chineseSkuInfoEntity.getSkuStatus()));
			chineseSkuInfoExtendVOList.add(chineseSkuInfoExtendVO);
		});
		return chineseSkuInfoExtendVOList;
	}

	private List<ChineseSkuInfoVO> buildChineseSkuInfoVOList(String storeName,
															 List<ChineseSkuInfoEntity> skuInfoEntityList,
															 Map<String, String> medicineCodeAndNameMap,
															 Map<String, BigDecimal> medicineCodeAndMaxDosageMap) {
		List<ChineseSkuInfoVO> chineseSkuInfoVOList = Lists.newArrayListWithCapacity(skuInfoEntityList.size());
		skuInfoEntityList.forEach(chineseSkuInfoEntity -> {
			ChineseSkuInfoVO chineseSkuInfoVO = ChineseSkuInfoVO.builder()
					.productType(ProductTypeEnum.CHINESE.getCode())
					.productTypeName(ProductTypeEnum.CHINESE.getValue())
					.skuCode(chineseSkuInfoEntity.getSkuCode())
					.skuName(chineseSkuInfoEntity.getSkuName())
					.medicineId(chineseSkuInfoEntity.getOldMedicineId())
					.medicineCode(chineseSkuInfoEntity.getMedicineCode())
					.medicineName(medicineCodeAndNameMap.get(chineseSkuInfoEntity.getMedicineCode()))
					.maxDosage(medicineCodeAndMaxDosageMap.get(chineseSkuInfoEntity.getMedicineCode()))
					.storeId(chineseSkuInfoEntity.getStoreId())
					.storeName(storeName)
					.price(BigDecimal.valueOf(chineseSkuInfoEntity.getPrice()))
					.skuState(chineseSkuInfoEntity.getSkuStatus())
					.skuStateName(ProductStateEnum.getValueByCode(chineseSkuInfoEntity.getSkuStatus()))
					.build();
			chineseSkuInfoVOList.add(chineseSkuInfoVO);
		});
		return chineseSkuInfoVOList;
	}
}
