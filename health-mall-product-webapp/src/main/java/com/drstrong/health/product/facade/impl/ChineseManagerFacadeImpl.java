package com.drstrong.health.product.facade.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.drstrong.health.product.dao.chinese.OldChineseMedicineMapper;
import com.drstrong.health.product.facade.ChineseManagerFacade;
import com.drstrong.health.product.model.dto.SupplierChineseSkuDTO;
import com.drstrong.health.product.model.entity.chinese.ChineseMedicineEntity;
import com.drstrong.health.product.model.entity.chinese.ChineseSkuInfoEntity;
import com.drstrong.health.product.model.entity.chinese.ChineseSkuSupplierRelevanceEntity;
import com.drstrong.health.product.model.entity.chinese.OldChineseMedicine;
import com.drstrong.health.product.model.entity.store.StoreEntity;
import com.drstrong.health.product.model.enums.DelFlagEnum;
import com.drstrong.health.product.model.enums.ErrorEnums;
import com.drstrong.health.product.model.enums.ProductStateEnum;
import com.drstrong.health.product.model.request.chinese.ChineseManagerSkuRequest;
import com.drstrong.health.product.model.request.chinese.StoreDataInitializeRequest;
import com.drstrong.health.product.model.request.chinese.UpdateSkuStateRequest;
import com.drstrong.health.product.model.response.PageVO;
import com.drstrong.health.product.model.response.chinese.ChineseManagerSkuVO;
import com.drstrong.health.product.model.response.chinese.SaveOrUpdateSkuVO;
import com.drstrong.health.product.model.response.chinese.SupplierChineseManagerSkuVO;
import com.drstrong.health.product.model.response.result.BusinessException;
import com.drstrong.health.product.remote.pro.StockRemoteProService;
import com.drstrong.health.product.remote.pro.SupplierRemoteProService;
import com.drstrong.health.product.service.chinese.ChineseMedicineService;
import com.drstrong.health.product.service.chinese.ChineseSkuInfoService;
import com.drstrong.health.product.service.chinese.ChineseSkuSupplierRelevanceService;
import com.drstrong.health.product.service.store.StoreService;
import com.drstrong.health.ware.model.response.SkuStockResponse;
import com.drstrong.health.ware.model.response.SupplierInfoDTO;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;

import static java.util.stream.Collectors.*;

/**
 * @author liuqiuyi
 * @date 2022/8/1 11:14
 */
@Slf4j
@Service
public class ChineseManagerFacadeImpl implements ChineseManagerFacade {
    @Resource
    ChineseSkuInfoService chineseSkuInfoService;

    @Resource
    ChineseSkuSupplierRelevanceService chineseSkuSupplierRelevanceService;

    @Resource
    StoreService storeService;

    @Resource
    ChineseMedicineService chineseMedicineService;

    @Resource
    SupplierRemoteProService supplierRemoteProService;

    @Resource
    OldChineseMedicineMapper oldChineseMedicineMapper;

	@Resource
	StockRemoteProService stockRemoteProService;

    /**
     * 中药管理页面，列表查询
     *
     * @param skuRequest 分页查询的入参
     * @return 中药管理列表
     * @author liuqiuyi
     * @date 2022/8/1 11:16
     */
    @Override
    public PageVO<ChineseManagerSkuVO> pageChineseManagerSku(ChineseManagerSkuRequest skuRequest) {
        log.info("invoke pageChineseManagerSku() param:{}", JSON.toJSONString(skuRequest));
        // 1。根据条件查询 sku 信息
        Page<ChineseSkuInfoEntity> infoEntityPage = chineseSkuInfoService.pageQuerySkuByRequest(skuRequest);
        List<ChineseSkuInfoEntity> skuInfoEntityList = infoEntityPage.getRecords();
        if (CollectionUtils.isEmpty(skuInfoEntityList)) {
            return PageVO.buildPageVO();
        }
        // 2.组装返回值
        List<ChineseManagerSkuVO> managerSkuVOList = buildChineseManagerSku(skuInfoEntityList);
        return PageVO.buildPageVO(skuRequest.getPageNo(), skuRequest.getPageSize(), infoEntityPage.getTotal(), managerSkuVOList);
    }

    /**
     * 中药管理页面，列表查询，导出数据使用
     *
     * @param skuRequest 分页查询的入参
     * @return 中药管理列表
     * @author liuqiuyi
     * @date 2022/8/1 11:16
     */
    @Override
    public List<ChineseManagerSkuVO> listChineseManagerSkuExport(ChineseManagerSkuRequest skuRequest) {
        log.info("invoke listChineseManagerSkuExport() param:{}", JSON.toJSONString(skuRequest));
        // 1。根据条件查询 sku 信息
        List<ChineseSkuInfoEntity> infoEntityList = chineseSkuInfoService.listQuerySkuByRequest(skuRequest);
        if (CollectionUtils.isEmpty(infoEntityList)) {
            return Lists.newArrayList();
        }
        // 2.组装返回值
        return buildChineseManagerSku(infoEntityList);
    }

    private List<ChineseManagerSkuVO> buildChineseManagerSku(List<ChineseSkuInfoEntity> skuInfoEntityList) {
        Set<String> skuCodes = Sets.newHashSetWithExpectedSize(skuInfoEntityList.size());
        Set<Long> storeIds = Sets.newHashSetWithExpectedSize(skuInfoEntityList.size());
        skuInfoEntityList.forEach(chineseSkuInfoEntity -> {
            skuCodes.add(chineseSkuInfoEntity.getSkuCode());
            storeIds.add(chineseSkuInfoEntity.getStoreId());
        });
        // 2.由于一个店铺可能关联多个供应商，但是入参中只传入了一个供应商，这里需要在回查一遍店铺的供应商
        List<ChineseSkuSupplierRelevanceEntity> supplierRelevanceEntityList = chineseSkuSupplierRelevanceService.listQueryBySkuCodeList(skuCodes);
        Map<String, Set<Long>> skuCodeSupplierIdsMap = supplierRelevanceEntityList.stream()
                .collect(groupingBy(ChineseSkuSupplierRelevanceEntity::getSkuCode, mapping(ChineseSkuSupplierRelevanceEntity::getSupplierId, toSet())));
        // 3.获取店铺名称
        List<StoreEntity> storeEntityList = storeService.listByIds(storeIds);
        Map<Long, String> storeIdNameMap = storeEntityList.stream().collect(toMap(StoreEntity::getId, StoreEntity::getStoreName, (v1, v2) -> v1));
        // 4.获取供应商名称
        List<Long> supplierIds = skuCodeSupplierIdsMap.values().stream().flatMap(Collection::stream).distinct().collect(toList());
        Map<Long, String> supplierIdNameMap = supplierRemoteProService.getSupplierNameToMap(supplierIds);
        // 5.组装返回值
        return buildChineseManagerSkuResponse(skuInfoEntityList, skuCodeSupplierIdsMap, storeIdNameMap, supplierIdNameMap);
    }

    /**
     * 保存sku信息
     *
     * @param saveOrUpdateSkuVO 接口入参
     * @author liuqiuyi
     * @date 2022/8/1 15:44
     */
    @Override
    public void saveOrUpdateSku(SaveOrUpdateSkuVO saveOrUpdateSkuVO) {
        log.info("invoke saveOrUpdateSku() param：{}", JSON.toJSONString(saveOrUpdateSkuVO));
        boolean updateFlag = StringUtils.isNotBlank(saveOrUpdateSkuVO.getSkuCode());
        // 校验请求入参
        checkSaveOrUpdateSkuParam(saveOrUpdateSkuVO, updateFlag);
        // 保存或者更新
        if (updateFlag) {
            chineseSkuInfoService.updateSku(saveOrUpdateSkuVO);
        } else {
            chineseSkuInfoService.saveSku(saveOrUpdateSkuVO);
        }
    }

    /**
     * 根据 skuCode 获取详情
     *
     * @param skuCode sku 编码
     * @return sku 详细信息，包含供应商等信息
     * @author liuqiuyi
     * @date 2022/8/2 11:50
     */
    @Override
    public SaveOrUpdateSkuVO getSkuByCode(String skuCode) {
        ChineseSkuInfoEntity skuInfoEntity = chineseSkuInfoService.getBySkuCode(skuCode);
        if (Objects.isNull(skuInfoEntity)) {
            throw new BusinessException(ErrorEnums.SKU_IS_NULL);
        }
        SaveOrUpdateSkuVO response = new SaveOrUpdateSkuVO();
        response.setSkuCode(skuInfoEntity.getSkuCode());
        response.setMedicineId(skuInfoEntity.getOldMedicineId());
        response.setMedicineCode(skuInfoEntity.getMedicineCode());
        response.setSkuName(skuInfoEntity.getSkuName());
        response.setPrice(skuInfoEntity.getPrice());
        response.setStoreId(skuInfoEntity.getStoreId());
        // 1、根据店铺id获取店铺名称
        List<StoreEntity> storeEntityList = storeService.listByIds(Sets.newHashSet(skuInfoEntity.getStoreId()));
        if (!CollectionUtils.isEmpty(storeEntityList)) {
            String storeName = Optional.ofNullable(storeEntityList.get(0)).map(StoreEntity::getStoreName).orElse("");
            response.setStoreName(storeName);
        }
        // 2.根据药材code获取药材名称
        ChineseMedicineEntity chineseMedicineEntity = chineseMedicineService.getByMedicineCode(skuInfoEntity.getMedicineCode());
        if (Objects.nonNull(chineseMedicineEntity)) {
            response.setMedicineName(chineseMedicineEntity.getMedicineName());
        }
        // 3.调用供应商接口，获取供应商的信息
		List<SkuStockResponse> skuStockResponseList = stockRemoteProService.getSkuInfoBySkuCode(skuInfoEntity.getSkuCode());
		if (!CollectionUtils.isEmpty(skuStockResponseList)) {
			List<SaveOrUpdateSkuVO.SupplierInfo> supplierInfoList = Lists.newArrayListWithCapacity(skuStockResponseList.size());
			skuStockResponseList.forEach(skuStockResponse -> {
				SaveOrUpdateSkuVO.SupplierInfo supplierInfo = new SaveOrUpdateSkuVO.SupplierInfo();
				BeanUtils.copyProperties(skuStockResponse, supplierInfo);
				supplierInfoList.add(supplierInfo);
			});
			response.setSupplierInfoList(supplierInfoList);
		}
        return response;
    }

    /**
     * 批量更新sku上下架状态
     *
     * @param updateSkuStateRequest 入参
     * @author liuqiuyi
     * @date 2022/8/2 14:21
     */
    @Override
    public void listUpdateSkuState(UpdateSkuStateRequest updateSkuStateRequest) {
        Set<String> skuCodeList = updateSkuStateRequest.getSkuCodeList();
        // 1.校验sku是否存在
        List<ChineseSkuInfoEntity> chineseSkuInfoEntityList = chineseSkuInfoService.listBySkuCode(skuCodeList);
        if (CollectionUtils.isEmpty(chineseSkuInfoEntityList) || !Objects.equals(chineseSkuInfoEntityList.size(), skuCodeList.size())) {
            throw new BusinessException(ErrorEnums.SKU_IS_NULL);
        }
        // 2.校验所属的店铺是否存在
        Set<Long> storeIds = chineseSkuInfoEntityList.stream().map(ChineseSkuInfoEntity::getStoreId).collect(toSet());
        List<StoreEntity> storeEntities = storeService.listByIds(storeIds);
        if (CollectionUtils.isEmpty(storeEntities) || !Objects.equals(storeEntities.size(), storeIds.size())) {
            throw new BusinessException(ErrorEnums.STORE_NOT_EXIST);
        }
        // 3.更新
        chineseSkuInfoService.updateSkuStatue(updateSkuStateRequest);
    }

    /**
     * 供应商中药库存页面，列表查询接口,提供给供应商远程调用
     *
     * @param skuRequest
     * @author liuqiuyi
     * @date 2022/8/5 10:17
     */
    @Override
    public PageVO<SupplierChineseManagerSkuVO> pageSupplierChineseManagerSku(ChineseManagerSkuRequest skuRequest) {
        log.info("invoke pageSupplierChineseManagerSku() param:{}", JSON.toJSONString(skuRequest));
        // 1。根据条件查询 sku 信息,不进行去重
        Page<SupplierChineseSkuDTO> infoEntityPage = chineseSkuInfoService.pageSupplierChineseManagerSku(skuRequest);
        List<SupplierChineseSkuDTO> skuInfoEntityList = infoEntityPage.getRecords();
        if (CollectionUtils.isEmpty(skuInfoEntityList)) {
            return PageVO.buildPageVO();
        }
        // 2.组装返回值
        List<SupplierChineseManagerSkuVO> managerSkuVOList = buildSupplierChineseManagerSku(skuInfoEntityList);
        return PageVO.buildPageVO(skuRequest.getPageNo(), skuRequest.getPageSize(), infoEntityPage.getTotal(), managerSkuVOList);
    }

    /**
     * 供应商中药库存页面，列表查询导出,提供给供应商远程调用
     *
     * @param skuRequest
     * @author liuqiuyi
     * @date 2022/8/5 10:17
     */
    @Override
    public List<SupplierChineseManagerSkuVO> listSupplierChineseManagerSkuExport(ChineseManagerSkuRequest skuRequest) {
        log.info("invoke listSupplierChineseManagerSkuExport() param:{}", JSON.toJSONString(skuRequest));
        // 1。根据条件查询 sku 信息
        List<SupplierChineseSkuDTO> infoEntityList = chineseSkuInfoService.listSupplierChineseManagerSkuExport(skuRequest);
        if (CollectionUtils.isEmpty(infoEntityList)) {
            return Lists.newArrayList();
        }
        // 2.组装返回值
        return buildSupplierChineseManagerSku(infoEntityList);
    }

    /**
     * 店铺数据初始化,将中药材默认上架到所有店铺,关联天江供应商
	 * <p> 仅用于一期上线时数据初始化,不要用于其它用途 </>
     *
     * @param initializeRequest   初始化入参信息
     * @author liuqiuyi
     * @date 2022/8/5 14:23
     */
    @Override
	@Deprecated
    public List<StoreDataInitializeRequest.CompensateInfo> storeDataInitialize(StoreDataInitializeRequest initializeRequest) {
		if (!CollectionUtils.isEmpty(initializeRequest.getStoreIds()) && CollectionUtils.isEmpty(initializeRequest.getCompensateInfoList())) {
			return initSaveSku(initializeRequest);
		} else if (!CollectionUtils.isEmpty(initializeRequest.getCompensateInfoList())) {
			return compensateSaveSku(initializeRequest);
		} else {
			throw new BusinessException("未执行任何初始化操作,请检查传参!");
		}
	}

	@Deprecated
	private List<StoreDataInitializeRequest.CompensateInfo> compensateSaveSku(StoreDataInitializeRequest initializeRequest) {
		List<StoreDataInitializeRequest.CompensateInfo> resultList = Lists.newArrayList();

		List<StoreDataInitializeRequest.CompensateInfo> compensateInfoList = initializeRequest.getCompensateInfoList();
		Set<Long> medicineIds = compensateInfoList.stream().map(StoreDataInitializeRequest.CompensateInfo::getMedicineId).collect(toSet());

		// 1.获取需要补偿的老药材
		LambdaQueryWrapper<OldChineseMedicine> queryWrapper = Wrappers.<OldChineseMedicine>lambdaQuery()
				.eq(OldChineseMedicine::getInvalid, DelFlagEnum.UN_DELETED.getCode())
				.in(OldChineseMedicine::getId, medicineIds);
		List<OldChineseMedicine> oldChineseMedicineList = oldChineseMedicineMapper.selectList(queryWrapper);
		if (CollectionUtils.isEmpty(oldChineseMedicineList)) {
			return resultList;
		}
		Map<Long, OldChineseMedicine> idAndOldChineseMedicineMap = oldChineseMedicineList.stream().collect(toMap(OldChineseMedicine::getId, dto -> dto, (v1, v2) -> v1));

		// 2.获取本期生成的药材 code
		Map<Long, String> medicineIdAndMedicineCodeMap = chineseMedicineService.getMedicineIdAndMedicineCodeMap(medicineIds);
		// 3.生成数据
		Long supplierId = initializeRequest.getSupplierId();
		for (StoreDataInitializeRequest.CompensateInfo compensateInfo : compensateInfoList) {
			Long medicineId = compensateInfo.getMedicineId();
			Long storeId = compensateInfo.getStoreId();
			try {
				OldChineseMedicine oldChineseMedicine = idAndOldChineseMedicineMap.get(medicineId);

				SaveOrUpdateSkuVO saveOrUpdateSkuVO = new SaveOrUpdateSkuVO();
				saveOrUpdateSkuVO.setMedicineId(medicineId);
				saveOrUpdateSkuVO.setMedicineCode(medicineIdAndMedicineCodeMap.get(medicineId));
				saveOrUpdateSkuVO.setMedicineName(oldChineseMedicine.getName());
				saveOrUpdateSkuVO.setSkuName(oldChineseMedicine.getName());
				saveOrUpdateSkuVO.setPrice(oldChineseMedicine.getGramPrice());
				saveOrUpdateSkuVO.setStoreId(storeId);
				// 库存信息,初始化时默认为天江供应商,且无限库存
				SaveOrUpdateSkuVO.SupplierInfo supplierInfo = new SaveOrUpdateSkuVO.SupplierInfo();
				supplierInfo.setSupplierId(supplierId);
				supplierInfo.setStockType(1);

				saveOrUpdateSkuVO.setSupplierInfoList(Lists.newArrayList(supplierInfo));
				saveOrUpdateSkuVO.setOperatorId(0L);


				chineseSkuInfoService.saveSku(saveOrUpdateSkuVO);
			} catch (Throwable e) {
				log.error("初始化数据失败,异常信息为:", e);
				StoreDataInitializeRequest.CompensateInfo info = new StoreDataInitializeRequest.CompensateInfo();
				info.setMedicineId(medicineId);
				info.setStoreId(storeId);
				resultList.add(info);
			}
		}
		return resultList;
	}

	@Deprecated
	private List<StoreDataInitializeRequest.CompensateInfo> initSaveSku(StoreDataInitializeRequest initializeRequest) {
		List<StoreDataInitializeRequest.CompensateInfo> resultList = Lists.newArrayList();
		// 1.获取所有的老药材
		LambdaQueryWrapper<OldChineseMedicine> queryWrapper = Wrappers.<OldChineseMedicine>lambdaQuery().eq(OldChineseMedicine::getInvalid, DelFlagEnum.UN_DELETED.getCode());
		List<OldChineseMedicine> oldChineseMedicineList = oldChineseMedicineMapper.selectList(queryWrapper);
		if (CollectionUtils.isEmpty(oldChineseMedicineList)) {
			return resultList;
		}
		// 2.获取本期生成的药材 code
		Set<Long> medicineIds = oldChineseMedicineList.stream().map(OldChineseMedicine::getId).collect(toSet());
		Map<Long, String> medicineIdAndMedicineCodeMap = chineseMedicineService.getMedicineIdAndMedicineCodeMap(medicineIds);

		// 3.循环生成店铺的药材信息
		List<Long> storeIds = initializeRequest.getStoreIds();
		Long supplierId = initializeRequest.getSupplierId();
		for (Long storeId : storeIds) {
            oldChineseMedicineList.forEach(oldChineseMedicine -> {
				try {
					SaveOrUpdateSkuVO saveOrUpdateSkuVO = new SaveOrUpdateSkuVO();
					saveOrUpdateSkuVO.setMedicineId(oldChineseMedicine.getId());
					saveOrUpdateSkuVO.setMedicineCode(medicineIdAndMedicineCodeMap.get(oldChineseMedicine.getId()));
					saveOrUpdateSkuVO.setMedicineName(oldChineseMedicine.getName());
					saveOrUpdateSkuVO.setSkuName(oldChineseMedicine.getName());
					saveOrUpdateSkuVO.setPrice(oldChineseMedicine.getGramPrice());
					saveOrUpdateSkuVO.setStoreId(storeId);
					// 库存信息,初始化时默认为天江供应商,且无限库存
					SaveOrUpdateSkuVO.SupplierInfo supplierInfo = new SaveOrUpdateSkuVO.SupplierInfo();
					supplierInfo.setSupplierId(supplierId);
					supplierInfo.setStockType(1);

					saveOrUpdateSkuVO.setSupplierInfoList(Lists.newArrayList(supplierInfo));
					saveOrUpdateSkuVO.setOperatorId(0L);

					chineseSkuInfoService.saveSku(saveOrUpdateSkuVO);
				} catch (Throwable e) {
					log.error("初始化数据失败,异常信息为:", e);
					StoreDataInitializeRequest.CompensateInfo info = new StoreDataInitializeRequest.CompensateInfo();
					info.setMedicineId(oldChineseMedicine.getId());
					info.setStoreId(storeId);
					resultList.add(info);
				}
            });
        }
		return resultList;
	}

	private void checkSaveOrUpdateSkuParam(SaveOrUpdateSkuVO saveOrUpdateSkuVO, boolean updateFlag) {
        // 1.根据药材code校验编码是否存在
        ChineseMedicineEntity chineseMedicineEntity = chineseMedicineService.getByMedicineCode(saveOrUpdateSkuVO.getMedicineCode());
        if (Objects.isNull(chineseMedicineEntity)) {
            throw new BusinessException(ErrorEnums.CHINESE_MEDICINE_IS_NULL);
        }
        saveOrUpdateSkuVO.setMedicineId(chineseMedicineEntity.getId());
        saveOrUpdateSkuVO.setMedicineName(chineseMedicineEntity.getMedicineName());
        // 2.校验店铺是否存在
        List<StoreEntity> storeEntityList = storeService.listByIds(Sets.newHashSet(saveOrUpdateSkuVO.getStoreId()));
        if (CollectionUtils.isEmpty(storeEntityList)) {
            throw new BusinessException(ErrorEnums.STORE_NOT_EXIST);
        }
        // 3.校验供应商是否存在
        List<Long> supplierIds = saveOrUpdateSkuVO.getSupplierInfoList().stream().map(SaveOrUpdateSkuVO.SupplierInfo::getSupplierId).collect(toList());
        List<SupplierInfoDTO> supplierInfoDTOList = supplierRemoteProService.getSupplierNameByIds(supplierIds);
        if (CollectionUtils.isEmpty(storeEntityList) || !Objects.equals(supplierInfoDTOList.size(), supplierIds.size())) {
            throw new BusinessException(ErrorEnums.SUPPLIER_IS_NULL);
        }
        // 4.如果是更新sku，校验skuCode是否存在，否则校验重复添加
        if (updateFlag) {
            ChineseSkuInfoEntity skuInfoEntity = chineseSkuInfoService.getBySkuCode(saveOrUpdateSkuVO.getSkuCode());
            if (Objects.isNull(skuInfoEntity)) {
                throw new BusinessException(ErrorEnums.SKU_IS_NULL);
            }
        } else {
            // 校验是否重复添加
            ChineseSkuInfoEntity chineseSkuInfoEntity = chineseSkuInfoService.getByMedicineCodeAndStoreId(saveOrUpdateSkuVO.getMedicineCode(), saveOrUpdateSkuVO.getStoreId());
            if (Objects.nonNull(chineseSkuInfoEntity)) {
                throw new BusinessException(ErrorEnums.CHINESE_IS_REPEAT);
            }
        }
    }

    private List<ChineseManagerSkuVO> buildChineseManagerSkuResponse(List<ChineseSkuInfoEntity> skuInfoEntityList,
                                                                     Map<String, Set<Long>> skuCodeSupplierIdsMap,
                                                                     Map<Long, String> storeIdNameMap,
                                                                     Map<Long, String> supplierIdNameMap) {
        List<ChineseManagerSkuVO> managerSkuVOList = Lists.newArrayListWithCapacity(skuInfoEntityList.size());
        for (ChineseSkuInfoEntity chineseSkuInfoEntity : skuInfoEntityList) {
            Set<Long> supplierIds = skuCodeSupplierIdsMap.get(chineseSkuInfoEntity.getSkuCode());

            ChineseManagerSkuVO chineseManagerSkuVO = ChineseManagerSkuVO.builder()
                    .medicineCode(chineseSkuInfoEntity.getMedicineCode())
                    .skuCode(chineseSkuInfoEntity.getSkuCode())
                    .skuName(chineseSkuInfoEntity.getSkuName())
                    .storeId(chineseSkuInfoEntity.getStoreId())
                    .storeName(storeIdNameMap.get(chineseSkuInfoEntity.getStoreId()))
                    .supplierIdList(supplierIds)
                    .price(chineseSkuInfoEntity.getPrice())
                    .skuState(chineseSkuInfoEntity.getSkuStatus())
                    .skuStateName(ProductStateEnum.getValueByCode(chineseSkuInfoEntity.getSkuStatus()))
                    .build();
            // 设值供应商名称
            List<String> supplierNames = Lists.newArrayListWithCapacity(supplierIds.size());
            chineseManagerSkuVO.getSupplierIdList().forEach(id -> supplierNames.add(supplierIdNameMap.getOrDefault(id, "")));
            chineseManagerSkuVO.setSupplierName(supplierNames);

            managerSkuVOList.add(chineseManagerSkuVO);
        }
        return managerSkuVOList;
    }


    private List<SupplierChineseManagerSkuVO> buildSupplierChineseManagerSku(List<SupplierChineseSkuDTO> skuInfoEntityList) {
        // 1.获取店铺名称
        Set<Long> storeIds = skuInfoEntityList.stream().map(SupplierChineseSkuDTO::getStoreId).collect(toSet());
        List<StoreEntity> storeEntityList = storeService.listByIds(storeIds);
        Map<Long, String> storeIdNameMap = storeEntityList.stream().collect(toMap(StoreEntity::getId, StoreEntity::getStoreName, (v1, v2) -> v1));
        // 2.组装返回值
        List<SupplierChineseManagerSkuVO> chineseManagerSkuVOList = Lists.newArrayListWithCapacity(skuInfoEntityList.size());
        skuInfoEntityList.forEach(supplierChineseSkuDTO -> {
            SupplierChineseManagerSkuVO chineseManagerSkuVO = new SupplierChineseManagerSkuVO();
            BeanUtils.copyProperties(supplierChineseSkuDTO, chineseManagerSkuVO);
            chineseManagerSkuVO.setSkuState(supplierChineseSkuDTO.getSkuStatus());
            chineseManagerSkuVO.setSkuStateName(ProductStateEnum.getValueByCode(supplierChineseSkuDTO.getSkuStatus()));
            chineseManagerSkuVO.setStoreName(storeIdNameMap.get(supplierChineseSkuDTO.getStoreId()));
            chineseManagerSkuVOList.add(chineseManagerSkuVO);
        });
        return chineseManagerSkuVOList;
    }
}
