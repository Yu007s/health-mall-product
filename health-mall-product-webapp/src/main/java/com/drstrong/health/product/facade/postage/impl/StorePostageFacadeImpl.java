package com.drstrong.health.product.facade.postage.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.json.JSONUtil;
import com.drstrong.health.common.enums.OperateTypeEnum;
import com.drstrong.health.product.constants.OperationLogConstant;
import com.drstrong.health.product.facade.postage.StorePostageFacade;
import com.drstrong.health.product.model.OperationLog;
import com.drstrong.health.product.model.entity.postage.StorePostageEntity;
import com.drstrong.health.product.model.entity.store.StoreEntity;
import com.drstrong.health.product.model.entity.store.StoreLinkSupplierEntity;
import com.drstrong.health.product.model.enums.ErrorEnums;
import com.drstrong.health.product.model.request.store.SaveStorePostageRequest;
import com.drstrong.health.product.model.request.store.SaveStoreSupplierPostageRequest;
import com.drstrong.health.product.model.response.area.AreaInfoResponse;
import com.drstrong.health.product.model.response.result.BusinessException;
import com.drstrong.health.product.model.response.store.v3.StorePostageVO;
import com.drstrong.health.product.remote.pro.SupplierRemoteProService;
import com.drstrong.health.product.service.area.AreaService;
import com.drstrong.health.product.service.postage.StorePostageService;
import com.drstrong.health.product.service.store.StoreLinkSupplierService;
import com.drstrong.health.product.service.store.StoreService;
import com.drstrong.health.product.util.BigDecimalUtil;
import com.drstrong.health.product.utils.OperationLogSendUtil;
import com.drstrong.health.ware.model.response.SupplierInfoDTO;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author liuqiuyi
 * @date 2023/6/6 14:04
 */
@Slf4j
@Service
public class StorePostageFacadeImpl implements StorePostageFacade {
	public static final String STORE_FREE_POSTAGE = "300";
	public static final String STORE_SUPPLIER_FREE_POSTAGE = "88";
	public static final String STORE_SUPPLIER_AREA_FREE_POSTAGE = "22";

	@Resource
	StorePostageService storePostageService;

	@Resource
	OperationLogSendUtil operationLogSendUtil;

	@Resource
	StoreLinkSupplierService storeLinkSupplierService;

	@Resource
	StoreService storeService;

	@Resource
	SupplierRemoteProService supplierRemoteProService;

	@Resource
	AreaService areaService;

	/**
	 * 保存店铺包邮金额,并记录日志
	 *
	 * @param saveStorePostageRequest
	 * @author liuqiuyi
	 * @date 2023/6/6 14:03
	 */
	@Override
	public void saveStorePostage(SaveStorePostageRequest saveStorePostageRequest) {
		// 查询店铺的配送费是否存在
		StorePostageEntity storePostageEntity = storePostageService.queryByStoreId(saveStorePostageRequest.getStoreId());
		// 记录操作日志
		OperationLog operationLog = OperationLog.buildOperationLog(saveStorePostageRequest.getStoreId().toString(), OperationLogConstant.MALL_STORE_CHANGE,
				OperationLogConstant.STORE_POSTAGE_CHANGE, saveStorePostageRequest.getOperatorId(), saveStorePostageRequest.getOperatorName(),
				OperateTypeEnum.CMS.getCode(), JSONUtil.toJsonStr(storePostageEntity));
		// 1.保存或更新店铺的配送费信息
		if (Objects.isNull(storePostageEntity)) {
			StorePostageEntity saveParam = StorePostageEntity.buildDefaultEntity(saveStorePostageRequest.getOperatorId());
			saveParam.setStoreId(saveStorePostageRequest.getStoreId());
			saveParam.setFreePostage(BigDecimalUtil.Y2F(saveStorePostageRequest.getFreePostage()));
			storePostageService.save(saveParam);
			operationLog.setChangeAfterData(JSONUtil.toJsonStr(saveParam));
		} else {
			storePostageEntity.setStoreId(saveStorePostageRequest.getStoreId());
			storePostageEntity.setFreePostage(BigDecimalUtil.Y2F(saveStorePostageRequest.getFreePostage()));
			storePostageEntity.setChangedBy(saveStorePostageRequest.getOperatorId());
			storePostageEntity.setChangedAt(LocalDateTime.now());
			storePostageService.updateById(storePostageEntity);
			operationLog.setChangeAfterData(JSONUtil.toJsonStr(storePostageEntity));
		}
		// 2.记录操作日志
		operationLogSendUtil.sendOperationLog(operationLog);
	}

	/**
	 * 保存店铺下供应商的邮费信息,并记录日志
	 *
	 * @param saveStoreSupplierPostageRequest
	 * @author liuqiuyi
	 * @date 2023/6/6 14:49
	 */
	@Override
	public void saveStoreSupplierPostage(SaveStoreSupplierPostageRequest saveStoreSupplierPostageRequest) {
		// 1.查询店铺的配送费数据库记录是否存在,不存在则新建
		StorePostageEntity storePostageEntity = storePostageService.queryByStoreId(saveStoreSupplierPostageRequest.getStoreId());
		if (Objects.isNull(storePostageEntity)) {
			SaveStorePostageRequest saveStorePostageRequest = SaveStorePostageRequest.builder()
					.storeId(saveStoreSupplierPostageRequest.getStoreId())
					.freePostage(new BigDecimal(STORE_FREE_POSTAGE))
					.build();
			saveStorePostageRequest.setOperatorId(saveStoreSupplierPostageRequest.getOperatorId());
			saveStorePostageRequest.setOperatorName(saveStoreSupplierPostageRequest.getOperatorName());
			SpringUtil.getBean(StorePostageFacade.class).saveStorePostage(saveStorePostageRequest);
			// 重查询一次,记录日志需要
			storePostageEntity = storePostageService.queryByStoreId(saveStoreSupplierPostageRequest.getStoreId());
		}

		OperationLog operationLog = OperationLog.buildOperationLog(saveStoreSupplierPostageRequest.getStoreId().toString(), OperationLogConstant.MALL_STORE_CHANGE,
				OperationLogConstant.STORE_POSTAGE_CHANGE, saveStoreSupplierPostageRequest.getOperatorId(), saveStoreSupplierPostageRequest.getOperatorName(),
				OperateTypeEnum.CMS.getCode(), JSONUtil.toJsonStr(storePostageEntity));
		// 2.保存或者更新店铺供应商的配送费信息
		storePostageService.saveOrUpdateSupplierPostageInfo(saveStoreSupplierPostageRequest);
		// 3.查询操作后的日志并记录
		StorePostageEntity afterStorePostageEntity = storePostageService.queryByStoreId(saveStoreSupplierPostageRequest.getStoreId());
		operationLog.setChangeAfterData(JSONUtil.toJsonStr(afterStorePostageEntity));
		// 4.记录操作日志
		operationLogSendUtil.sendOperationLog(operationLog);
	}

	/**
	 * 查询店铺的配送费信息和店铺下所有供应商的配送费信息
	 *
	 * @param storeId
	 * @author liuqiuyi
	 * @date 2023/6/6 15:55
	 */
	@Override
	public StorePostageVO queryStorePostage(Long storeId) {
		StoreEntity storeEntity = storeService.getById(storeId);
		if (Objects.isNull(storeEntity)) {
			throw new BusinessException(ErrorEnums.STORE_NOT_EXIST);
		}
		StorePostageVO storePostageVO = StorePostageVO.builder().storeId(storeId).storeName(storeEntity.getStoreName()).build();
		// 1.查询店铺关联的供应商
		List<StoreLinkSupplierEntity> storeLinkSupplierEntityList = storeLinkSupplierService.queryByStoreId(storeId);
		// 2.查询店铺的配送费
		StorePostageEntity storePostageEntity = storePostageService.queryByStoreId(storeId);
		// 3.设置店铺的总邮费
		if (Objects.nonNull(storePostageEntity)) {
			storePostageVO.setFreePostage(BigDecimalUtil.F2Y(storePostageEntity.getFreePostage()));
		}
		// 4.设置店铺下供应商邮费信息
		List<StorePostageVO.StoreSupplierPostageVO> storeSupplierPostageVOList = buildStoreSupplierPostageList(storeLinkSupplierEntityList, storePostageEntity);
		storePostageVO.setStoreSupplierPostageList(storeSupplierPostageVOList);
		return storePostageVO;
	}

	private List<StorePostageVO.StoreSupplierPostageVO> buildStoreSupplierPostageList(List<StoreLinkSupplierEntity> storeLinkSupplierEntityList, StorePostageEntity storePostageEntity) {
		if (CollectionUtil.isEmpty(storeLinkSupplierEntityList)) {
			return Lists.newArrayList();
		}
		List<Long> supplierIds = storeLinkSupplierEntityList.stream().map(StoreLinkSupplierEntity::getSupplierId).collect(Collectors.toList());
		// 1.调用远程接口,获取供应商信息
		Map<Long, SupplierInfoDTO> supplierInfoToMap = supplierRemoteProService.getSupplierInfoToMap(supplierIds);
		// 2.获取供应商包邮配置
		List<StorePostageEntity.SupplierFreePostageInfo> supplierFreePostageInfoList = Objects.isNull(storePostageEntity) ? new ArrayList<>() : storePostageEntity.getSupplierFreePostageInfo();
		Map<Long, StorePostageEntity.SupplierFreePostageInfo> supplierIdFreePostageMap = supplierFreePostageInfoList.stream().collect(Collectors.toMap(StorePostageEntity.SupplierFreePostageInfo::getSupplierId, dto -> dto, (v1, v2) -> v1));
		// 3.获取供应商区域邮费设置
		List<StorePostageEntity.SupplierAreaPostageInfo> supplierAreaPostageInfoList = Objects.isNull(storePostageEntity) ? new ArrayList<>() : storePostageEntity.getSupplierAreaPostageInfo();
		Map<Long, List<StorePostageEntity.SupplierAreaPostageInfo>> supplierIdAreaPostageInfoListMap = supplierAreaPostageInfoList.stream().collect(Collectors.groupingBy(StorePostageEntity.SupplierAreaPostageInfo::getSupplierId));
		// 获取区域map
		Map<Long, String> areaIdNameMap = areaService.queryAllProvince().stream().collect(Collectors.toMap(AreaInfoResponse::getAreaId, AreaInfoResponse::getAreaName, (v1, v2) -> v1));
		// 4.组装参数
		List<StorePostageVO.StoreSupplierPostageVO> storeSupplierPostageVOList = Lists.newArrayListWithCapacity(storeLinkSupplierEntityList.size());
		for (StoreLinkSupplierEntity storeLinkSupplierEntity : storeLinkSupplierEntityList) {
			Long supplierId = storeLinkSupplierEntity.getSupplierId();
			// 获取供应商的区域邮费信息
			List<StorePostageVO.StoreSupplierAreaPostageVO> areaPostageVOList = Lists.newArrayList();
			supplierIdAreaPostageInfoListMap.getOrDefault(supplierId, Lists.newArrayList()).forEach(supplierAreaPostageInfo -> {
				StorePostageVO.StoreSupplierAreaPostageVO areaPostageVO = StorePostageVO.StoreSupplierAreaPostageVO.builder()
						.areaId(supplierAreaPostageInfo.getAreaId())
						.areaName(areaIdNameMap.get(supplierAreaPostageInfo.getAreaId()))
						.postage(BigDecimalUtil.F2Y(supplierAreaPostageInfo.getPostage()))
						.build();
				areaPostageVOList.add(areaPostageVO);
			});
			// 获取供应商的包邮邮费设置
			StorePostageEntity.SupplierFreePostageInfo supplierFreePostageInfo = supplierIdFreePostageMap.get(supplierId);
			SupplierInfoDTO supplierInfoToMapOrDefault = supplierInfoToMap.getOrDefault(supplierId, new SupplierInfoDTO());
			// 组装供应商的邮费返回值
			StorePostageVO.StoreSupplierPostageVO storeSupplierPostageVO = StorePostageVO.StoreSupplierPostageVO.builder()
					.supplierId(supplierId)
					.supplierName(supplierInfoToMapOrDefault.getSupplierName())
					.supplierTypeName(supplierInfoToMapOrDefault.getSupplierName())
					.freePostage(Objects.isNull(supplierFreePostageInfo) ? new BigDecimal("-1") : BigDecimalUtil.F2Y(supplierFreePostageInfo.getFreePostage()))
					.storeSupplierAreaPostageList(areaPostageVOList)
					.build();
			storeSupplierPostageVOList.add(storeSupplierPostageVO);
		}
		return storeSupplierPostageVOList;
	}
}
