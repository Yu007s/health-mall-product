package com.drstrong.health.product.facade.sku.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.drstrong.health.common.enums.OperateTypeEnum;
import com.drstrong.health.product.constants.OperationLogConstant;
import com.drstrong.health.product.facade.sku.SkuManageFacade;
import com.drstrong.health.product.model.OperationLog;
import com.drstrong.health.product.model.dto.area.AreaDTO;
import com.drstrong.health.product.model.dto.category.CategoryDTO;
import com.drstrong.health.product.model.dto.label.LabelDTO;
import com.drstrong.health.product.model.dto.product.StoreSkuDetailDTO;
import com.drstrong.health.product.model.dto.product.SupplierInfoDTO;
import com.drstrong.health.product.model.entity.sku.StoreSkuInfoEntity;
import com.drstrong.health.product.model.entity.store.StoreEntity;
import com.drstrong.health.product.model.enums.ErrorEnums;
import com.drstrong.health.product.model.enums.ProductTypeEnum;
import com.drstrong.health.product.model.enums.UpOffEnum;
import com.drstrong.health.product.model.request.product.v3.SaveOrUpdateStoreSkuRequest;
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
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.toList;

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

	/**
	 * 保存或者更新 sku 信息(目前不包括中药)
	 *
	 * @param saveOrUpdateStoreProductRequest
	 * @author liuqiuyi
	 * @date 2023/6/10 09:32
	 */
	@Override
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
		storeSkuInfoService.saveOrUpdate(storeSkuInfoEntity);
		// 3.发送操作日志
		operationLog.setBusinessId(storeSkuInfoEntity.getSkuCode());
		StoreSkuInfoEntity afterEntity = storeSkuInfoService.checkSkuExistByCode(storeSkuInfoEntity.getSkuCode(), null);
		operationLog.setChangeAfterData(JSONUtil.toJsonStr(afterEntity));
		operationLogSendUtil.sendOperationLog(operationLog);
	}

	private StoreSkuInfoEntity checkSaveOrUpdateStoreSkuParam(SaveOrUpdateStoreSkuRequest saveOrUpdateStoreProductRequest, Boolean updateFlag) {
		// 1.根据药材code校验编码是否存在
		// TODO 等振武的接口
//		ChineseMedicineEntity chineseMedicineEntity = chineseMedicineService.getByMedicineCode(saveOrUpdateSkuVO.getMedicineCode());
//		if (Objects.isNull(chineseMedicineEntity)) {
//			throw new BusinessException(ErrorEnums.CHINESE_MEDICINE_IS_NULL);
//		}
		// 2.校验店铺是否存在
		List<StoreEntity> storeEntityList = storeService.listByIds(Sets.newHashSet(saveOrUpdateStoreProductRequest.getStoreId()));
		if (CollectionUtils.isEmpty(storeEntityList)) {
			throw new BusinessException(ErrorEnums.STORE_NOT_EXIST);
		}
		// 3.校验药材是否关联了供应商
		// TODO 等振武的接口
//		List<com.drstrong.health.ware.model.response.SupplierInfoDTO> supplierInfoDTOList = supplierRemoteProService.searchSupplierByCode(saveOrUpdateStoreProductRequest.getMedicineCode());
//		if (CollectionUtils.isEmpty(supplierInfoDTOList)) {
//			throw new BusinessException(ErrorEnums.MEDICINE_CODE_NOT_ASSOCIATED);
//		}
//		List<Long> supplierIds = supplierInfoDTOList.stream().map(com.drstrong.health.ware.model.response.SupplierInfoDTO::getSupplierId).collect(toList());
//		boolean supplierFlag = saveOrUpdateStoreProductRequest.getSupplierInfoList().stream().anyMatch(supplierInfo -> !supplierIds.contains(supplierInfo.getSupplierId()));
//		if (supplierFlag) {
//			throw new BusinessException(ErrorEnums.SUPPLIER_IS_NULL);
//		}
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
}
