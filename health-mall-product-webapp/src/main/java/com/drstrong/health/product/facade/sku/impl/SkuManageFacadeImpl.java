package com.drstrong.health.product.facade.sku.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.drstrong.health.common.enums.OperateTypeEnum;
import com.drstrong.health.product.constants.OperationLogConstant;
import com.drstrong.health.product.facade.medicine.MedicineWarehouseFacadeHolder;
import com.drstrong.health.product.facade.sku.SkuManageFacade;
import com.drstrong.health.product.facade.sku.SkuScheduledConfigFacade;
import com.drstrong.health.product.model.OperationLog;
import com.drstrong.health.product.model.dto.area.AreaDTO;
import com.drstrong.health.product.model.dto.category.CategoryDTO;
import com.drstrong.health.product.model.dto.label.LabelDTO;
import com.drstrong.health.product.model.dto.medicine.MedicineWarehouseBaseDTO;
import com.drstrong.health.product.model.dto.product.StoreSkuDetailDTO;
import com.drstrong.health.product.model.dto.product.SupplierInfoDTO;
import com.drstrong.health.product.model.entity.label.LabelInfoEntity;
import com.drstrong.health.product.model.entity.sku.StoreSkuInfoEntity;
import com.drstrong.health.product.model.entity.store.StoreEntity;
import com.drstrong.health.product.model.enums.ErrorEnums;
import com.drstrong.health.product.model.enums.ProductTypeEnum;
import com.drstrong.health.product.model.enums.UpOffEnum;
import com.drstrong.health.product.model.request.chinese.UpdateSkuStateRequest;
import com.drstrong.health.product.model.request.product.v3.ProductManageQueryRequest;
import com.drstrong.health.product.model.request.product.v3.SaveOrUpdateStoreSkuRequest;
import com.drstrong.health.product.model.request.product.v3.ScheduledSkuUpDownRequest;
import com.drstrong.health.product.model.response.PageVO;
import com.drstrong.health.product.model.response.chinese.SaveOrUpdateSkuVO;
import com.drstrong.health.product.model.response.product.v3.AgreementSkuInfoVO;
import com.drstrong.health.product.model.response.result.BusinessException;
import com.drstrong.health.product.remote.cms.CmsRemoteProService;
import com.drstrong.health.product.remote.pro.StockRemoteProService;
import com.drstrong.health.product.remote.pro.SupplierRemoteProService;
import com.drstrong.health.product.service.category.v3.CategoryService;
import com.drstrong.health.product.service.label.LabelInfoService;
import com.drstrong.health.product.service.sku.StoreSkuInfoService;
import com.drstrong.health.product.service.store.StoreService;
import com.drstrong.health.product.util.BigDecimalUtil;
import com.drstrong.health.product.utils.OperationLogSendUtil;
import com.drstrong.health.product.utils.UniqueCodeUtils;
import com.drstrong.health.ware.model.response.SkuStockResponse;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

/**
 * @author liuqiuyi
 * @date 2023/6/10 09:25
 */
@Slf4j
@Service
public class SkuManageFacadeImpl implements SkuManageFacade {
	@Resource
	StoreService storeService;

	@Resource
	SupplierRemoteProService supplierRemoteProService;

	@Resource
	StoreSkuInfoService storeSkuInfoService;

	@Resource
	OperationLogSendUtil operationLogSendUtil;

	@Resource
	StockRemoteProService stockRemoteProService;

	@Resource
	LabelInfoService labelInfoService;

	@Resource
	CmsRemoteProService cmsRemoteProService;

	@Resource
	CategoryService categoryService;

	@Resource
	SkuScheduledConfigFacade skuScheduledConfigFacade;

	@Resource
	MedicineWarehouseFacadeHolder medicineWarehouseFacadeHolder;

	/**
	 * 保存或者更新 sku 信息(目前不包括中药)
	 *
	 * @param saveOrUpdateStoreProductRequest
	 * @author liuqiuyi
	 * @date 2023/6/10 09:32
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveOrUpdateStoreProduct(SaveOrUpdateStoreSkuRequest saveOrUpdateStoreProductRequest) {
		log.info("invoke saveOrUpdateStoreProduct(),param:{}", JSONUtil.toJsonStr(saveOrUpdateStoreProductRequest));
		boolean updateFlag = StrUtil.isNotBlank(saveOrUpdateStoreProductRequest.getSkuCode());
		// 1.入参校验
		StoreSkuInfoEntity storeSkuInfoEntity = checkSaveOrUpdateStoreSkuParam(saveOrUpdateStoreProductRequest, updateFlag);
		// 组装操作日志
		OperationLog operationLog = OperationLog.buildOperationLog(null, OperationLogConstant.MALL_PRODUCT_SKU_CHANGE,
				OperationLogConstant.SAVE_UPDATE_SKU, saveOrUpdateStoreProductRequest.getOperatorId(), saveOrUpdateStoreProductRequest.getOperatorName(),
				OperateTypeEnum.CMS.getCode(), JSONUtil.toJsonStr(storeSkuInfoEntity));
		// 2.保存或者更新
		if (Objects.isNull(storeSkuInfoEntity)) {
			storeSkuInfoEntity = StoreSkuInfoEntity.buildDefault(saveOrUpdateStoreProductRequest.getOperatorId());
			storeSkuInfoEntity.setSkuCode(createSkuCode(saveOrUpdateStoreProductRequest.getProductType(), saveOrUpdateStoreProductRequest.getStoreId()));
		} else {
			storeSkuInfoEntity.setChangedBy(saveOrUpdateStoreProductRequest.getOperatorId());
			storeSkuInfoEntity.setChangedAt(LocalDateTime.now());
		}
		List<Long> supplierInfos = saveOrUpdateStoreProductRequest.getSupplierInfoList().stream().map(SupplierInfoDTO::getSupplierId).distinct().collect(toList());
		storeSkuInfoEntity.setSkuType(saveOrUpdateStoreProductRequest.getProductType());
		storeSkuInfoEntity.setSkuName(saveOrUpdateStoreProductRequest.getSkuName());
		storeSkuInfoEntity.setMedicineCode(saveOrUpdateStoreProductRequest.getMedicineCode());
		storeSkuInfoEntity.setStoreId(saveOrUpdateStoreProductRequest.getStoreId());
		storeSkuInfoEntity.setPrice(BigDecimalUtil.Y2F(saveOrUpdateStoreProductRequest.getSalePrice()));
		storeSkuInfoEntity.setSkuStatus(UpOffEnum.DOWN.getCode());
		storeSkuInfoEntity.setSupplierInfo(supplierInfos);
		storeSkuInfoEntity.setLabelInfo(CollectionUtil.isEmpty(saveOrUpdateStoreProductRequest.getLabelIdList()) ? Lists.newArrayList() : Lists.newArrayList(saveOrUpdateStoreProductRequest.getLabelIdList()));
		storeSkuInfoEntity.setProhibitAreaInfo(CollectionUtil.isEmpty(saveOrUpdateStoreProductRequest.getProhibitAreaIdList()) ? Lists.newArrayList() : Lists.newArrayList(saveOrUpdateStoreProductRequest.getProhibitAreaIdList()));
		storeSkuInfoEntity.setCategoryInfo(CollectionUtil.isEmpty(saveOrUpdateStoreProductRequest.getCategoryIdList()) ? Lists.newArrayList() : Lists.newArrayList(saveOrUpdateStoreProductRequest.getCategoryIdList()));
		storeSkuInfoService.saveOrUpdate(storeSkuInfoEntity);
		// 2.调用远程接口保存sku信息
		doSaveStockInfo(storeSkuInfoEntity.getSkuCode(), storeSkuInfoEntity.getStoreId(), saveOrUpdateStoreProductRequest);
		// 3.发送操作日志
		operationLog.setBusinessId(storeSkuInfoEntity.getSkuCode());
		StoreSkuInfoEntity afterEntity = storeSkuInfoService.checkSkuExistByCode(storeSkuInfoEntity.getSkuCode(), null);
		operationLog.setChangeAfterData(JSONUtil.toJsonStr(afterEntity));
		operationLogSendUtil.sendOperationLog(operationLog);
	}

	/**
	 * 保存库存设置到 ware 服务
	 */
	private void doSaveStockInfo(String skuCode, Long storeId, SaveOrUpdateStoreSkuRequest saveOrUpdateStoreProductRequest) {
		List<SaveOrUpdateSkuVO.SupplierInfo> supplierInfoList = BeanUtil.copyToList(saveOrUpdateStoreProductRequest.getSupplierInfoList(), SaveOrUpdateSkuVO.SupplierInfo.class);
		SaveOrUpdateSkuVO saveOrUpdateSkuVO = new SaveOrUpdateSkuVO();
		saveOrUpdateSkuVO.setStoreId(storeId);
		saveOrUpdateSkuVO.setMedicineCode(saveOrUpdateStoreProductRequest.getMedicineCode());
		saveOrUpdateSkuVO.setSupplierInfoList(supplierInfoList);
		saveOrUpdateSkuVO.setOperatorId(saveOrUpdateStoreProductRequest.getOperatorId());
		stockRemoteProService.saveOrUpdateStockInfo(skuCode, saveOrUpdateSkuVO);
	}

	private StoreSkuInfoEntity checkSaveOrUpdateStoreSkuParam(SaveOrUpdateStoreSkuRequest saveOrUpdateStoreProductRequest, Boolean updateFlag) {
		// 1.根据药材code校验编码是否存在
		MedicineWarehouseBaseDTO medicineWarehouseBaseDTO = medicineWarehouseFacadeHolder.getMedicineWarehouseFacade(saveOrUpdateStoreProductRequest.getProductType()).queryByCode(saveOrUpdateStoreProductRequest.getMedicineCode());
		if (Objects.isNull(medicineWarehouseBaseDTO)) {
			throw new BusinessException(ErrorEnums.MEDICINE_IS_NULL);
		}
		// 2.校验店铺是否存在
		List<StoreEntity> storeEntityList = storeService.listByIds(Sets.newHashSet(saveOrUpdateStoreProductRequest.getStoreId()));
		if (CollectionUtils.isEmpty(storeEntityList)) {
			throw new BusinessException(ErrorEnums.STORE_NOT_EXIST);
		}
		// 3.校验药材是否关联了供应商
		List<com.drstrong.health.ware.model.response.SupplierInfoDTO> supplierInfoDTOList = supplierRemoteProService.searchSupplierByCode(saveOrUpdateStoreProductRequest.getMedicineCode());
		if (CollectionUtils.isEmpty(supplierInfoDTOList)) {
			throw new BusinessException(ErrorEnums.MEDICINE_CODE_NOT_ASSOCIATED);
		}
		List<Long> supplierIds = supplierInfoDTOList.stream().map(com.drstrong.health.ware.model.response.SupplierInfoDTO::getSupplierId).collect(toList());
		boolean supplierFlag = saveOrUpdateStoreProductRequest.getSupplierInfoList().stream().anyMatch(supplierInfo -> !supplierIds.contains(supplierInfo.getSupplierId()));
		if (supplierFlag) {
			throw new BusinessException(ErrorEnums.SUPPLIER_IS_NULL);
		}
		// 4.如果是更新sku，校验skuCode是否存在，否则校验重复添加
		if (updateFlag) {
			StoreSkuInfoEntity storeSkuInfoEntity = storeSkuInfoService.checkSkuExistByCode(saveOrUpdateStoreProductRequest.getSkuCode(), null);
			// 如果是修改,且修改前后 skuName 不一样,也需要校验 skuName 是否重复
			if (!Objects.equals(storeSkuInfoEntity.getSkuName(), saveOrUpdateStoreProductRequest.getSkuName())) {
				storeSkuInfoService.checkSkuNameIsRepeat(saveOrUpdateStoreProductRequest.getSkuName(), saveOrUpdateStoreProductRequest.getStoreId());
			}
			return storeSkuInfoEntity;
		} else {
			// 校验是否重复添加
			storeSkuInfoService.checkMedicineCodeAndStoreId(saveOrUpdateStoreProductRequest.getMedicineCode(), saveOrUpdateStoreProductRequest.getStoreId());
			// 校验相同店铺下,sku名称是否重复添加
			storeSkuInfoService.checkSkuNameIsRepeat(saveOrUpdateStoreProductRequest.getSkuName(), saveOrUpdateStoreProductRequest.getStoreId());
			return null;
		}
	}

	/**
	 * 生成 skuCode,参照之前的工具类写法
	 *
	 * @author liuqiuyi
	 * @date 2023/6/10 15:01
	 */
	private String createSkuCode(Integer productType, Long storeId) {
		String nextSpuCode = UniqueCodeUtils.getNextSpuCode(ProductTypeEnum.getEnumByCode(productType));
		return UniqueCodeUtils.getNextSkuCode(nextSpuCode, storeId);
	}

	/**
	 * 根据skuCode 查询 sku 信息
	 *
	 * @param skuCode
	 * @author liuqiuyi
	 * @date 2023/6/10 15:20
	 */
	@Override
	public StoreSkuDetailDTO queryDetailByCode(String skuCode) {
		// 1.根据 skuCode 查询信息
		StoreSkuInfoEntity skuInfoEntity = storeSkuInfoService.checkSkuExistByCode(skuCode, null);
		// 2.获取店铺信息
		StoreEntity storeEntity = storeService.getById(skuInfoEntity.getStoreId());
		// 3.调用供应商接口，获取供应商的信息
		List<SkuStockResponse> skuStockResponseList = stockRemoteProService.getSkuInfoBySkuCode(skuInfoEntity.getSkuCode());
		List<SupplierInfoDTO> supplierInfoDTOList = BeanUtil.copyToList(skuStockResponseList, SupplierInfoDTO.class);
		// 4.获取标签信息
		List<LabelDTO> labelDTOList = labelInfoService.queryByIds(skuInfoEntity.getLabelInfo()).stream()
				.map(labelInfoEntity -> new LabelDTO(labelInfoEntity.getId(), labelInfoEntity.getLabelName(), null, null))
				.collect(toList());
		// 5.获取分类信息
		List<CategoryDTO> categoryDTOList = categoryService.listByIds(skuInfoEntity.getCategoryInfo()).stream()
				.map(categoryEntity -> new CategoryDTO(categoryEntity.getId(), categoryEntity.getName()))
				.collect(toList());
		// 6.获取区域信息
		List<AreaDTO> areaDTOList = cmsRemoteProService.querySkuProhibitAreaByIds(skuInfoEntity.getProhibitAreaInfo()).stream()
				.map(skuProhibitAreaVO -> new AreaDTO(skuProhibitAreaVO.getId(), skuProhibitAreaVO.getName())).collect(toList());
		// 组装参数
		StoreSkuDetailDTO storeSkuDetailDTO = StoreSkuDetailDTO.builder()
				.medicineCode(skuInfoEntity.getMedicineCode())
				.storeName(storeEntity.getStoreName())
				.skuName(skuInfoEntity.getSkuName())
				.salePrice(BigDecimalUtil.F2Y(skuInfoEntity.getPrice()))
				.supplierInfoList(supplierInfoDTOList)
				.labelList(labelDTOList)
				.categoryList(categoryDTOList)
				.prohibitAreaList(areaDTOList)
				.build();
		storeSkuDetailDTO.setProductType(skuInfoEntity.getSkuType());
		storeSkuDetailDTO.setSkuCode(skuInfoEntity.getSkuCode());
		storeSkuDetailDTO.setStoreId(skuInfoEntity.getStoreId());
		return storeSkuDetailDTO;
	}

	/**
	 * 协定方|西药 管理页面,列表展示
	 *
	 * @param productManageQueryRequest
	 * @author liuqiuyi
	 * @date 2023/6/13 10:23
	 */
	@Override
	public PageVO<AgreementSkuInfoVO> querySkuManageInfo(ProductManageQueryRequest productManageQueryRequest) {
		log.info("invoke querySkuManageInfo(),param:{}", JSONUtil.toJsonStr(productManageQueryRequest));
		// 1.根据入参中的条件,分页查询店铺 sku 表
		Page<StoreSkuInfoEntity> storeSkuInfoEntityPageList = storeSkuInfoService.pageQueryByParam(productManageQueryRequest);
		List<StoreSkuInfoEntity> pageListRecords = storeSkuInfoEntityPageList.getRecords();
		if (CollectionUtil.isEmpty(pageListRecords)) {
			log.info("未查询到任何sku数据,参数为:{}", JSONUtil.toJsonStr(productManageQueryRequest));
			return PageVO.newBuilder().result(Lists.newArrayList()).totalCount(0).pageNo(productManageQueryRequest.getPageNo()).pageSize(productManageQueryRequest.getPageSize()).build();
		}
		// 2.组装返回值
		List<AgreementSkuInfoVO> agreementSkuInfoVoList = buildAgreementSkuInfoVo(pageListRecords);
		return PageVO.newBuilder().result(agreementSkuInfoVoList).totalCount((int) storeSkuInfoEntityPageList.getTotal()).pageNo(productManageQueryRequest.getPageNo()).pageSize(productManageQueryRequest.getPageSize()).build();
	}

	/**
	 * 协定方|西药 管理页面,所有数据查询,用于导出
	 *
	 * @param productManageQueryRequest
	 * @author liuqiuyi
	 * @date 2023/6/14 11:23
	 */
	@Override
	public List<AgreementSkuInfoVO> listSkuManageInfo(ProductManageQueryRequest productManageQueryRequest) {
		log.info("invoke listSkuManageInfo(),param:{}", JSONUtil.toJsonStr(productManageQueryRequest));
		// 1.根据入参中的条件,查询店铺 sku 表
		List<StoreSkuInfoEntity> storeSkuInfoEntityList = storeSkuInfoService.listQueryByParam(productManageQueryRequest);
		if (CollectionUtil.isEmpty(storeSkuInfoEntityList)) {
			log.info("未查询到任何sku数据,参数为:{}", JSONUtil.toJsonStr(productManageQueryRequest));
			return Lists.newArrayList();
		}
		// 2.组装参数返回
		return buildAgreementSkuInfoVo(storeSkuInfoEntityList);
	}

	private List<AgreementSkuInfoVO> buildAgreementSkuInfoVo(List<StoreSkuInfoEntity> pageListRecords) {
		// 获取 id,一次循环全部取出
		Set<Long> storeIds = Sets.newHashSet();
		Set<Long> supplierIds = Sets.newHashSet();
		Set<Long> labelIds = Sets.newHashSet();
		pageListRecords.forEach(storeSkuInfoEntity -> {
			storeIds.add(storeSkuInfoEntity.getStoreId());
			supplierIds.addAll(storeSkuInfoEntity.getSupplierInfo());
			labelIds.addAll(storeSkuInfoEntity.getLabelInfo());
		});
		// 2.查询店铺名称
		Map<Long, String> storeIdNameMap = storeService.listByIds(storeIds).stream().collect(Collectors.toMap(StoreEntity::getId, StoreEntity::getStoreName, (v1, v2) -> v1));
		// 3.查询供应商名称
		Map<Long, String> supplierIdNameMap = supplierRemoteProService.getSupplierNameToMap(Lists.newArrayList(supplierIds));
		// 4.查询标签名称
		Map<Long, String> labelIdNameMap = labelInfoService.queryByIds(Lists.newArrayList(labelIds)).stream().collect(Collectors.toMap(LabelInfoEntity::getId, LabelInfoEntity::getLabelName, (v1, v2) -> v1));
		// 5.组装参数
		List<AgreementSkuInfoVO> agreementSkuInfoVoList = Lists.newArrayListWithCapacity(pageListRecords.size());
		pageListRecords.forEach(storeSkuInfoEntity -> {
			List<SupplierInfoDTO> supplierInfoDTOList = storeSkuInfoEntity.getSupplierInfo().stream().map(supplierId -> new SupplierInfoDTO(supplierId, supplierIdNameMap.get(supplierId), null, null)).collect(toList());
			List<LabelDTO> labelDTOList = storeSkuInfoEntity.getLabelInfo().stream().map(labelId -> new LabelDTO(labelId, labelIdNameMap.get(labelId), null, null)).collect(toList());

			AgreementSkuInfoVO skuInfoVO = AgreementSkuInfoVO.builder()
					.skuCode(storeSkuInfoEntity.getSkuCode())
					.medicineCode(storeSkuInfoEntity.getMedicineCode())
					.skuName(storeSkuInfoEntity.getSkuName())
					.storeId(storeSkuInfoEntity.getStoreId())
					.storeName(storeIdNameMap.get(storeSkuInfoEntity.getStoreId()))
					.supplierInfoList(supplierInfoDTOList)
					.salePrice(BigDecimalUtil.F2Y(storeSkuInfoEntity.getPrice()))
					.labelList(labelDTOList)
					.skuStatus(storeSkuInfoEntity.getSkuStatus())
					.skuStatusName(UpOffEnum.getValueByCode(storeSkuInfoEntity.getSkuStatus()))
					.build();
			skuInfoVO.setProductType(storeSkuInfoEntity.getSkuType());
			skuInfoVO.setProductTypeName(ProductTypeEnum.getValueByCode(storeSkuInfoEntity.getSkuType()));
			agreementSkuInfoVoList.add(skuInfoVO);
		});
		return agreementSkuInfoVoList;
	}


	/**
	 * sku 批量上下架
	 *
	 * @param updateSkuStateRequest
	 * @author liuqiuyi
	 * @date 2023/6/14 15:36
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateSkuStatus(UpdateSkuStateRequest updateSkuStateRequest) {
		log.info("invoke updateSkuStatus param:{}", JSONUtil.toJsonStr(updateSkuStateRequest));
		List<StoreSkuInfoEntity> storeSkuInfoEntityList = storeSkuInfoService.querySkuCodes(updateSkuStateRequest.getSkuCodeList());
		// 1.校验 sku 是否存在,并判断状态是否支持修改
		checkSkuStatus(updateSkuStateRequest, storeSkuInfoEntityList);
		// 2.修改状态
		storeSkuInfoService.batchUpdateSkuStatusByCodes(updateSkuStateRequest.getSkuCodeList(), updateSkuStateRequest.getSkuState(), updateSkuStateRequest.getOperatorId());
		// 3.发送操作日志
		sendSkuStatusUpdateLog(storeSkuInfoEntityList, updateSkuStateRequest.getSkuCodeList(), updateSkuStateRequest.getOperatorId(), updateSkuStateRequest.getOperatorName());
		// 4.定时上下架处理
		skuScheduledConfigFacade.batchUpdateScheduledStatusByCodes(updateSkuStateRequest);
	}

	/**
	 * 校验 sku 状态,是否能进行更新
	 */
	private void checkSkuStatus(UpdateSkuStateRequest updateSkuStateRequest, List<StoreSkuInfoEntity> storeSkuInfoEntityList) {
		if (CollectionUtil.isEmpty(storeSkuInfoEntityList) || ObjectUtil.notEqual(storeSkuInfoEntityList.size(), updateSkuStateRequest.getSkuCodeList().size())) {
			log.error("根据skuCode未找到数据,可能传入的参数为空,或者存在非法的skuCode,参数为:{}", JSONUtil.toJsonStr(updateSkuStateRequest));
			throw new BusinessException(ErrorEnums.SKU_IS_NULL);
		}
		// 校验状态
		boolean allMatch = storeSkuInfoEntityList.stream().allMatch(storeSkuInfoEntity -> {
			if (Objects.equals(UpOffEnum.UP.getCode(), updateSkuStateRequest.getSkuState())) {
				// 如果传参是上架,则当前 sku 必须是 已下架或预约上架 状态
				return Objects.equals(UpOffEnum.DOWN.getCode(), storeSkuInfoEntity.getSkuStatus()) || Objects.equals(UpOffEnum.SCHEDULED_UP.getCode(), storeSkuInfoEntity.getSkuStatus());
			} else {
				// 如果传参是下架,则当前必须是 已上架或预约下架 状态
				return Objects.equals(UpOffEnum.UP.getCode(), storeSkuInfoEntity.getSkuStatus()) || Objects.equals(UpOffEnum.SCHEDULED_DOWN.getCode(), storeSkuInfoEntity.getSkuStatus());
			}
		});
		if (!allMatch) {
			throw new BusinessException(ErrorEnums.SKU_STATUS_ERROR);
		}
	}

	/**
	 * sku 预约上下架
	 *
	 * @param scheduledSkuUpDownRequest
	 * @author liuqiuyi
	 * @date 2023/6/15 09:32
	 */
	@Override
	public void scheduledSkuUpDown(ScheduledSkuUpDownRequest scheduledSkuUpDownRequest) {
		// 1.校验 sku 是否存在
		StoreSkuInfoEntity skuInfoEntity = storeSkuInfoService.checkSkuExistByCode(scheduledSkuUpDownRequest.getSkuCode(), null);
		// 2.对当前状态进行校验,如果不符合要求直接抛异常
		if (Objects.equals(UpOffEnum.UP.getCode(), skuInfoEntity.getSkuStatus()) && Objects.equals(ScheduledSkuUpDownRequest.SCHEDULED_UP, scheduledSkuUpDownRequest.getScheduledType())) {
			throw new BusinessException(ErrorEnums.SKU_IS_UP_ERROR);
		}
		if (Objects.equals(UpOffEnum.DOWN.getCode(), skuInfoEntity.getSkuStatus()) && Objects.equals(ScheduledSkuUpDownRequest.SCHEDULED_DOWN, scheduledSkuUpDownRequest.getScheduledType())) {
			throw new BusinessException(ErrorEnums.SKU_IS_DOWN_ERROR);
		}
		// 3.保存或者更新 sku 定时上下架配置
		skuScheduledConfigFacade.saveOrUpdateSkuConfig(scheduledSkuUpDownRequest);
		// 4.发送操作日志
		sendSkuStatusUpdateLog(Lists.newArrayList(skuInfoEntity), Sets.newHashSet(scheduledSkuUpDownRequest.getSkuCode()), scheduledSkuUpDownRequest.getOperatorId(), scheduledSkuUpDownRequest.getOperatorName());
	}

	private void sendSkuStatusUpdateLog(List<StoreSkuInfoEntity> beforeDataList, Set<String> skuCodeList, Long operatorId, String operatorName) {
		Map<String, StoreSkuInfoEntity> skuCodeInfoMap = storeSkuInfoService.querySkuCodes(skuCodeList)
				.stream().collect(toMap(StoreSkuInfoEntity::getSkuCode, dto -> dto, (v1, v2) -> v1));
		// 循环发送操作日志
		beforeDataList.forEach(skuInfoEntity -> {
			OperationLog operationLog = OperationLog.buildOperationLog(skuInfoEntity.getSkuCode(), OperationLogConstant.MALL_PRODUCT_SKU_CHANGE,
					OperationLogConstant.SKU_STATUS_CHANGE, operatorId, operatorName,
					OperateTypeEnum.CMS.getCode(), JSONUtil.toJsonStr(skuInfoEntity));
			operationLog.setChangeAfterData(JSONUtil.toJsonStr(skuCodeInfoMap.get(skuInfoEntity.getSkuCode())));
			operationLogSendUtil.sendOperationLog(operationLog);
		});
	}
}
