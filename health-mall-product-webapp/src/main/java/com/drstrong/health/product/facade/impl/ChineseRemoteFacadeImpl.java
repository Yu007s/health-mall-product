package com.drstrong.health.product.facade.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.drstrong.health.product.facade.ChineseAuthFacade;
import com.drstrong.health.product.facade.ChineseRemoteFacade;
import com.drstrong.health.product.model.dto.stock.SkuCanStockDTO;
import com.drstrong.health.product.model.entity.chinese.ChineseMedicineConflictEntity;
import com.drstrong.health.product.model.entity.chinese.ChineseMedicineEntity;
import com.drstrong.health.product.model.entity.chinese.ChineseSkuInfoEntity;
import com.drstrong.health.product.model.entity.product.SkuInfoEntity;
import com.drstrong.health.product.model.entity.store.StoreEntity;
import com.drstrong.health.product.model.enums.ErrorEnums;
import com.drstrong.health.product.model.enums.ProductStateEnum;
import com.drstrong.health.product.model.enums.ProductTypeEnum;
import com.drstrong.health.product.model.enums.UpOffEnum;
import com.drstrong.health.product.model.request.chinese.ChineseQueryDosageRequest;
import com.drstrong.health.product.model.request.chinese.QueryChineseSkuRequest;
import com.drstrong.health.product.model.request.store.AgencyStoreVO;
import com.drstrong.health.product.model.response.chinese.ChineseMedicineConflictVO;
import com.drstrong.health.product.model.response.chinese.ChineseMedicineInfoResponse;
import com.drstrong.health.product.model.response.chinese.ChineseSkuInfoExtendVO;
import com.drstrong.health.product.model.response.chinese.ChineseSkuInfoVO;
import com.drstrong.health.product.model.response.product.ProductInfoVO;
import com.drstrong.health.product.model.response.result.BusinessException;
import com.drstrong.health.product.remote.model.ChineseMedicineDTO;
import com.drstrong.health.product.remote.pro.StockRemoteProService;
import com.drstrong.health.product.service.chinese.ChineseMedicineConflictService;
import com.drstrong.health.product.service.chinese.ChineseMedicineService;
import com.drstrong.health.product.service.chinese.ChineseSkuInfoService;
import com.drstrong.health.product.service.chinese.ChineseSkuSupplierRelevanceService;
import com.drstrong.health.product.service.product.SkuInfoService;
import com.drstrong.health.product.service.store.StoreDeliveryPriorityService;
import com.drstrong.health.product.service.store.StoreService;
import com.drstrong.health.ware.model.response.SkuCanStockResponse;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.*;

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

	@Resource
	StockRemoteProService stockRemoteProService;

	@Resource
	SkuInfoService skuInfoService;

	@Resource
	ChineseSkuSupplierRelevanceService chineseSkuSupplierRelevanceService;

	@Resource
	ChineseAuthFacade chineseAuthFacade;

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
		Set<Long> medicineIds = Sets.newHashSetWithExpectedSize(chineseSkuInfoEntityList.size());
		List<String> skuCodes = Lists.newArrayListWithCapacity(chineseSkuInfoEntityList.size());
		chineseSkuInfoEntityList.forEach(chineseSkuInfoEntity -> {
			medicineIds.add(chineseSkuInfoEntity.getOldMedicineId());
			skuCodes.add(chineseSkuInfoEntity.getSkuCode());
		});
		// 3.获取药材名称
		Map<String, ChineseMedicineEntity> medicineCodeAndEntityMap = chineseMedicineService.listMedicineByIds(medicineIds)
				.stream().collect(Collectors.toMap(ChineseMedicineEntity::getMedicineCode, dto -> dto, (v1, v2) -> v1));
		// 4.获取 spu 信息
		Map<String, SkuInfoEntity> skuCodeSkuInfoMap = skuInfoService.getBySkuCodesToMap(skuCodes);
		// 5.判断是否需要查询库存信息
		Map<String, List<SkuCanStockDTO>> skuCodeStockMap = null;
		if (Objects.equals(Boolean.TRUE, chineseSkuRequest.getNeedQueryStock())) {
			skuCodeStockMap = stockRemoteProService.getStockToMap(skuCodes);
		}
		// 6.判断是否需要查询配送优先级
		if (Objects.equals(Boolean.TRUE, chineseSkuRequest.getNeedQueryPriority()) && Objects.nonNull(chineseSkuRequest.getAreaId())) {
			List<Long> storeChineseDeliveryInfoList = storeDeliveryPriorityService.queryByStoreIdAndArea(storeEntity.getId(), chineseSkuRequest.getAreaId());
			productInfoVO.setStoreChineseDeliveryInfoList(storeChineseDeliveryInfoList);
		}
		// 7.组装数据返回
		List<ChineseSkuInfoExtendVO> skuExtendVOList = buildChineseSkuExtendVOList(storeEntity.getStoreName(), chineseSkuInfoEntityList, medicineCodeAndEntityMap, skuCodeStockMap, skuCodeSkuInfoMap);
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
		Set<Long> deduplicationIds = Sets.newHashSetWithExpectedSize(conflictEntityList.size());
		conflictEntityList.forEach(chineseMedicineConflictEntity -> {
			Long id = medicineCodeAndIdMap.get(chineseMedicineConflictEntity.getMedicineCode());
			List<Long> conflictIdList = Stream.of(chineseMedicineConflictEntity.getMedicineConflictCodes().split(","))
					.map(medicineCodeAndIdMap::get).filter(Objects::nonNull).collect(Collectors.toList());

			if (Objects.nonNull(id) && !CollectionUtils.isEmpty(conflictIdList) && !deduplicationIds.contains(id)) {
				ChineseMedicineConflictVO chineseMedicineConflictVO = new ChineseMedicineConflictVO();
				chineseMedicineConflictVO.setId(id);
				chineseMedicineConflictVO.setConflictIdList(conflictIdList);
				medicineConflictVoList.add(chineseMedicineConflictVO);
				deduplicationIds.add(id);
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
		List<StoreEntity> storeEntityList = storeService.getStoreByAgencyIds(agencyIds);
		return buildAgencyStoreVoList(storeEntityList);
	}

	private List<AgencyStoreVO> buildAgencyStoreVoList(List<StoreEntity> storeEntityList) {
		List<AgencyStoreVO> agencyStoreVOList = Lists.newArrayListWithCapacity(storeEntityList.size());
		storeEntityList.forEach(storeEntity -> {
			AgencyStoreVO agencyStoreVO = new AgencyStoreVO();
			agencyStoreVO.setStoreId(storeEntity.getId());
			agencyStoreVO.setAgencyId(storeEntity.getAgencyId());
			agencyStoreVO.setStoreName(storeEntity.getStoreName());
			agencyStoreVO.setStoreType(storeEntity.getStoreType());
			agencyStoreVOList.add(agencyStoreVO);
		});
		return agencyStoreVOList;
	}

	/**
	 * 根据店铺 id 获取 互联网医院 id
	 *
	 * @param storeIds 店铺 id
	 * @return 互联网医院 id 和店铺 id 信息
	 * @author liuqiuyi
	 * @date 2022/8/10 16:05
	 */
	@Override
	public List<AgencyStoreVO> listAgencyByStoreIds(Set<Long> storeIds) {
		List<StoreEntity> storeEntityList = storeService.listByIds(storeIds);
		return buildAgencyStoreVoList(storeEntityList);
	}

	/**
	 * 校验是否有上架的 sku
	 * <p> 用于供应商那边删除中药材时进行校验,如果删除的中药材关联了上架的 sku,则不允许删除 </>
	 *
	 * @param medicineCodes 药材 code
	 * @author liuqiuyi
	 * @date 2022/8/11 17:35
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public List<ChineseMedicineInfoResponse> checkHasUpChineseByMedicineCodes(Set<String> medicineCodes, Long supplierId, Long operatorId) {
		// 1.先查询出所有的 sku 信息
		List<ChineseSkuInfoEntity> chineseSkuInfoEntityList = chineseSkuInfoService.listByMedicineCodes(medicineCodes);
		if (CollectionUtils.isEmpty(chineseSkuInfoEntityList)) {
			return Lists.newArrayList();
		}
		Set<String> skuCodes = chineseSkuInfoEntityList.stream().map(ChineseSkuInfoEntity::getSkuCode).collect(Collectors.toSet());
		// 2.根据 skuCode 信息获取供应商
		Map<String, Set<Long>> skuCodeAndSupplierIdsMap = chineseSkuSupplierRelevanceService.getSkuCodeAndSupplierIdsMap(skuCodes);
		// 3.判断供应商关联的店铺是否存在已上架的 sku 信息
		List<ChineseMedicineInfoResponse> infoResponseList = checkHasUpSku(chineseSkuInfoEntityList, skuCodeAndSupplierIdsMap, supplierId);
		if (CollectionUtil.isNotEmpty(infoResponseList)) {
			return infoResponseList;
		}
		// 4.如果校验通过,则删除这些药材关联供应商对应的店铺 sku 信息以及关联关系
		Set<String> deleteSkuCodes = chineseSkuInfoEntityList.stream().filter(chineseSkuInfoEntity ->
				Objects.equals(UpOffEnum.DOWN.getCode(), chineseSkuInfoEntity.getSkuStatus())
						&& skuCodeAndSupplierIdsMap.getOrDefault(chineseSkuInfoEntity.getSkuCode(), Sets.newHashSet()).contains(supplierId))
				.map(ChineseSkuInfoEntity::getSkuCode).collect(toSet());
		chineseSkuInfoService.deleteSkuInfoBySkuCodes(deleteSkuCodes, operatorId);
		chineseSkuSupplierRelevanceService.deleteRelevanceBySkuCodesAndSupplierId(deleteSkuCodes, supplierId, operatorId);
		return Lists.newArrayList();
	}

	/**
	 * 校验供应商关联的店铺中,是否有上线的sku 信息
	 *
	 * @author liuqiuyi
	 * @date 2022/10/31 14:13
	 */
	private List<ChineseMedicineInfoResponse> checkHasUpSku(List<ChineseSkuInfoEntity> chineseSkuInfoEntityList, Map<String, Set<Long>> skuCodeAndSupplierIdsMap, Long supplierId) {
		// 筛选出 sku 是上线状态,并且店铺关联了传入的供应商
		List<ChineseSkuInfoEntity> upSkuInfoList = chineseSkuInfoEntityList.stream().filter(chineseSkuInfoEntity ->
				Objects.equals(UpOffEnum.UP.getCode(), chineseSkuInfoEntity.getSkuStatus())
						&& skuCodeAndSupplierIdsMap.getOrDefault(chineseSkuInfoEntity.getSkuCode(), Sets.newHashSet()).contains(supplierId))
				.collect(Collectors.toList());
		if (CollectionUtils.isEmpty(upSkuInfoList)) {
			return Lists.newArrayList();
		}
		Set<String> upMedicineCodes = upSkuInfoList.stream().map(ChineseSkuInfoEntity::getMedicineCode).collect(Collectors.toSet());
		// 获取这部分药材的名称,返回
		List<ChineseMedicineEntity> chineseMedicineEntityList = chineseMedicineService.getByMedicineCode(upMedicineCodes);
		List<ChineseMedicineInfoResponse> infoResponseList = Lists.newArrayListWithCapacity(chineseMedicineEntityList.size());
		chineseMedicineEntityList.forEach(chineseMedicineEntity -> {
			ChineseMedicineInfoResponse medicineInfoResponse = new ChineseMedicineInfoResponse();
			medicineInfoResponse.setMedicineCode(chineseMedicineEntity.getMedicineCode());
			medicineInfoResponse.setMedicineName(chineseMedicineEntity.getMedicineName());
			infoResponseList.add(medicineInfoResponse);
		});
		return infoResponseList;
	}

	private List<ChineseSkuInfoExtendVO> buildChineseSkuExtendVOList(String storeName, List<ChineseSkuInfoEntity> skuInfoEntityList,
																	 Map<String, ChineseMedicineEntity> medicineCodeAndEntityMap, Map<String, List<SkuCanStockDTO>> skuCodeStockMap,
																	 Map<String, SkuInfoEntity> skuCodeSkuInfoMap) {
		List<ChineseSkuInfoExtendVO> chineseSkuInfoExtendVOList = Lists.newArrayListWithCapacity(skuInfoEntityList.size());
		skuInfoEntityList.forEach(chineseSkuInfoEntity -> {
			ChineseSkuInfoExtendVO chineseSkuInfoExtendVO = new ChineseSkuInfoExtendVO();
			BeanUtils.copyProperties(chineseSkuInfoEntity, chineseSkuInfoExtendVO);
			chineseSkuInfoExtendVO.setSkuId(chineseSkuInfoEntity.getId());
			String spuCode = Optional.ofNullable(skuCodeSkuInfoMap.get(chineseSkuInfoEntity.getSkuCode())).map(SkuInfoEntity::getSpuCode).orElse("");
			chineseSkuInfoExtendVO.setSpuCode(spuCode);
			chineseSkuInfoExtendVO.setPrice(BigDecimal.valueOf(chineseSkuInfoEntity.getPrice()));
			chineseSkuInfoExtendVO.setProductType(ProductTypeEnum.CHINESE.getCode());
			chineseSkuInfoExtendVO.setProductTypeName(ProductTypeEnum.CHINESE.getValue());
			chineseSkuInfoExtendVO.setMedicineId(chineseSkuInfoEntity.getOldMedicineId());
			chineseSkuInfoExtendVO.setStoreName(storeName);
			chineseSkuInfoExtendVO.setSkuState(chineseSkuInfoEntity.getSkuStatus());
			chineseSkuInfoExtendVO.setSkuStateName(ProductStateEnum.getValueByCode(chineseSkuInfoEntity.getSkuStatus()));
			ChineseMedicineEntity chineseMedicineEntity = Optional.ofNullable(medicineCodeAndEntityMap.get(chineseSkuInfoEntity.getMedicineCode())).orElse(new ChineseMedicineEntity());
			chineseSkuInfoExtendVO.setMedicineName(chineseMedicineEntity.getMedicineName());
			chineseSkuInfoExtendVO.setMaxDosage(chineseMedicineEntity.getMaxDosage());
			// 设置库存信息
			if (!CollectionUtils.isEmpty(skuCodeStockMap) && skuCodeStockMap.containsKey(chineseSkuInfoEntity.getSkuCode())) {
				List<SkuCanStockDTO> stockResponseList = skuCodeStockMap.get(chineseSkuInfoEntity.getSkuCode());
				List<ChineseSkuInfoExtendVO.StockInfoVO> stockInfoVOList = Lists.newArrayListWithCapacity(stockResponseList.size());
				stockResponseList.forEach(skuCanStockResponse -> {
					ChineseSkuInfoExtendVO.StockInfoVO stockInfoVO = new ChineseSkuInfoExtendVO.StockInfoVO();
					stockInfoVO.setSupplierId(skuCanStockResponse.getSupplierId());
					stockInfoVO.setSupplierName(skuCanStockResponse.getSupplierName());
					BigDecimal virtualQuantity = Optional.ofNullable(skuCanStockResponse.getAvailableQuantity()).map(BigDecimal::valueOf).orElse(null);
					stockInfoVO.setVirtualQuantity(virtualQuantity);
					stockInfoVOList.add(stockInfoVO);
				});
				chineseSkuInfoExtendVO.setStockInfoVOList(stockInfoVOList);
			}
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
					.dosageType(chineseSkuInfoEntity.getDosageType())
					.dosageValue(chineseSkuInfoEntity.getDosageValue())
					.build();
			chineseSkuInfoVOList.add(chineseSkuInfoVO);
		});
		return chineseSkuInfoVOList;
	}

	@Override
	public List<ChineseMedicineDTO> getChineseMedicineDTOListByIds(Set<Long> ids) {
		List<ChineseMedicineEntity>  list  = chineseMedicineService.listMedicineByIds(ids);
		return BeanUtil.copyToList(Optional.ofNullable(list).orElse(Collections.emptyList()),ChineseMedicineDTO.class);
	}

	/**
	 * 查询店铺下所有存在倍数限制的药材
	 *
	 * @param chineseQueryDosageRequest 入参
	 * @author liuqiuyi
	 * @date 2022/12/9 10:21
	 */
	@Override
	public List<ChineseSkuInfoVO> queryAllDosage(ChineseQueryDosageRequest chineseQueryDosageRequest) {
		log.info("invoke chineseAuthFacade.queryAllDosage param:{}", JSONUtil.toJsonStr(chineseQueryDosageRequest));
		return chineseAuthFacade.queryAllDosage(chineseQueryDosageRequest).getDosageChineseSkuInfoList();
	}
}
