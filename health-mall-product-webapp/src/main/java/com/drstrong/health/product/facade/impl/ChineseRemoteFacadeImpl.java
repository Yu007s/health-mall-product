package com.drstrong.health.product.facade.impl;

import com.drstrong.health.product.facade.ChineseRemoteFacade;
import com.drstrong.health.product.model.entity.chinese.ChineseMedicineEntity;
import com.drstrong.health.product.model.entity.chinese.ChineseSkuInfoEntity;
import com.drstrong.health.product.model.entity.store.StoreEntity;
import com.drstrong.health.product.model.enums.ErrorEnums;
import com.drstrong.health.product.model.enums.ProductStateEnum;
import com.drstrong.health.product.model.enums.ProductTypeEnum;
import com.drstrong.health.product.model.response.chinese.ChineseSkuInfoVO;
import com.drstrong.health.product.model.response.result.BusinessException;
import com.drstrong.health.product.service.chinese.ChineseMedicineService;
import com.drstrong.health.product.service.chinese.ChineseSkuInfoService;
import com.drstrong.health.product.service.store.StoreService;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

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
		StoreEntity storeEntity;
		if (Objects.isNull(storeId)) {
			storeEntity = storeService.getStoreByAgencyId(agencyId);
		} else {
			storeEntity = storeService.getById(storeId);
		}
		if (Objects.isNull(storeEntity)) {
			throw new BusinessException(ErrorEnums.STORE_NOT_EXIST);
		}
		// 2.调用店铺的搜索接口，模糊查询获取所有的药材 code
		List<ChineseMedicineEntity> chineseMedicineEntityList = chineseMedicineService.likeQueryByKeyword(keyword);
		if (CollectionUtils.isEmpty(chineseMedicineEntityList)) {
			return Lists.newArrayList();
		}
		Set<String> medicineCodes = chineseMedicineEntityList.stream().map(ChineseMedicineEntity::getMedicineCode).collect(Collectors.toSet());
		// 3.根据药材 code 和 店铺 id，获取 sku 信息
		List<ChineseSkuInfoEntity> skuInfoEntityList = chineseSkuInfoService.listByMedicineCodeAndStoreId(medicineCodes, storeEntity.getId());
		if (CollectionUtils.isEmpty(skuInfoEntityList)) {
			return Lists.newArrayList();
		}
		// 4.组装数据返回
		return buildChineseSkuInfoVOList(storeEntity.getStoreName(), skuInfoEntityList);
	}

	private List<ChineseSkuInfoVO> buildChineseSkuInfoVOList(String storeName, List<ChineseSkuInfoEntity> skuInfoEntityList) {
		List<ChineseSkuInfoVO> chineseSkuInfoVOList = Lists.newArrayListWithCapacity(skuInfoEntityList.size());
		skuInfoEntityList.forEach(chineseSkuInfoEntity -> {
			ChineseSkuInfoVO chineseSkuInfoVO = ChineseSkuInfoVO.builder()
					.productType(ProductTypeEnum.CHINESE.getCode())
					.skuCode(chineseSkuInfoEntity.getSkuCode())
					.skuName(chineseSkuInfoEntity.getSkuName())
					.medicineId(chineseSkuInfoEntity.getOldMedicineId())
					.medicineCode(chineseSkuInfoEntity.getMedicineCode())
					.storeId(chineseSkuInfoEntity.getStoreId())
					.storeName(storeName)
					.price(chineseSkuInfoEntity.getPrice())
					.skuState(chineseSkuInfoEntity.getSkuStatus())
					.skuStateName(ProductStateEnum.getValueByCode(chineseSkuInfoEntity.getSkuStatus()))
					.build();
			chineseSkuInfoVOList.add(chineseSkuInfoVO);
		});
		return chineseSkuInfoVOList;
	}
}
