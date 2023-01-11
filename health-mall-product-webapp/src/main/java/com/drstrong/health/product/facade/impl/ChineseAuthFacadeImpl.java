package com.drstrong.health.product.facade.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.Opt;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.drstrong.health.product.facade.ChineseAuthFacade;
import com.drstrong.health.product.model.entity.chinese.ChineseMedicineEntity;
import com.drstrong.health.product.model.entity.chinese.ChineseSkuInfoEntity;
import com.drstrong.health.product.model.entity.store.StoreEntity;
import com.drstrong.health.product.model.enums.DelFlagEnum;
import com.drstrong.health.product.model.enums.DosageTypeEnum;
import com.drstrong.health.product.model.enums.ErrorEnums;
import com.drstrong.health.product.model.enums.ProductStateEnum;
import com.drstrong.health.product.model.enums.ProductTypeEnum;
import com.drstrong.health.product.model.request.chinese.ChineseQueryDosageRequest;
import com.drstrong.health.product.model.response.chinese.ChineseDosageInfoVO;
import com.drstrong.health.product.model.response.chinese.ChineseSkuInfoVO;
import com.drstrong.health.product.model.response.result.BusinessException;
import com.drstrong.health.product.remote.pro.UserRemoteProService;
import com.drstrong.health.product.service.chinese.ChineseMedicineService;
import com.drstrong.health.product.service.chinese.ChineseSkuInfoService;
import com.drstrong.health.product.service.store.StoreService;
import com.drstrong.health.product.util.BigDecimalUtil;
import com.drstrong.health.ware.enums.SkuStatusEnum;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toMap;

/**
 * @author liuqiuyi
 * @date 2022/12/8 16:52
 */
@Slf4j
@Service
public class ChineseAuthFacadeImpl implements ChineseAuthFacade {

	@Resource
	StoreService storeService;

	@Resource
	UserRemoteProService userRemoteProService;

	@Resource
	ChineseSkuInfoService chineseSkuInfoService;

	@Resource
	ChineseMedicineService chineseMedicineService;

	@Value("${posts.min.gram:5}")
	private Integer postsMinGram;

	/**
	 * 查询店铺所有存在倍数限制的中药
	 *
	 * @param chineseQueryDosageRequest 参数
	 * @author liuqiuyi
	 * @date 2022/12/9 09:51
	 */
	@Override
	public ChineseDosageInfoVO queryAllDosage(ChineseQueryDosageRequest chineseQueryDosageRequest) {
		log.info("invoke queryAllDosage() param:{}", JSONUtil.toJsonStr(chineseQueryDosageRequest));
		ChineseDosageInfoVO chineseDosageInfoVO = ChineseDosageInfoVO.builder().dosageChineseSkuInfoList(Lists.newArrayList()).postsMinGram(postsMinGram).build();
		// 1.查询店铺信息
		Long storeId = queryStoreId(chineseQueryDosageRequest.getStoreId(), chineseQueryDosageRequest.getAgencyId(), chineseQueryDosageRequest.getUcDoctorId());
		Assert.notNull(storeId, () -> new BusinessException(ErrorEnums.STORE_NOT_EXIST));
		// 2.查询店铺下所有设置了剂量倍数的中药材信息
		LambdaQueryWrapper<ChineseSkuInfoEntity> queryWrapper = new LambdaQueryWrapper<ChineseSkuInfoEntity>()
				.eq(ChineseSkuInfoEntity::getDelFlag, DelFlagEnum.UN_DELETED.getCode())
				.eq(ChineseSkuInfoEntity::getStoreId, storeId)
				.eq(ChineseSkuInfoEntity::getDosageType, DosageTypeEnum.MULTIPLE.getCode())
				.eq(ChineseSkuInfoEntity::getSkuStatus, SkuStatusEnum.SKU_UP.getCode());
		List<ChineseSkuInfoEntity> chineseSkuInfoEntityList = chineseSkuInfoService.getBaseMapper().selectList(queryWrapper);
		if (CollectionUtil.isEmpty(chineseSkuInfoEntityList)) {
			return chineseDosageInfoVO;
		}
		// 3.查询药材信息
		Set<String> medicineCodeList = chineseSkuInfoEntityList.stream().map(ChineseSkuInfoEntity::getMedicineCode).collect(Collectors.toSet());
		Map<String, ChineseMedicineEntity> codeAndMedicineEntityMap = chineseMedicineService.getByMedicineCode(medicineCodeList).stream().collect(toMap(ChineseMedicineEntity::getMedicineCode, dto -> dto, (v1, v2) -> v1));
		// 4.组装返回值
		List<ChineseSkuInfoVO> resultSkuInfoVoList = Lists.newArrayListWithCapacity(chineseSkuInfoEntityList.size());
		chineseSkuInfoEntityList.forEach(chineseSkuInfoEntity -> {
			ChineseMedicineEntity chineseMedicineEntity = codeAndMedicineEntityMap.getOrDefault(chineseSkuInfoEntity.getMedicineCode(), new ChineseMedicineEntity());
			resultSkuInfoVoList.add(ChineseSkuInfoVO.builder()
					.productType(ProductTypeEnum.CHINESE.getCode())
					.productTypeName(ProductTypeEnum.CHINESE.getValue())
					.skuCode(chineseSkuInfoEntity.getSkuCode())
					.skuName(chineseSkuInfoEntity.getSkuName())
					.medicineId(chineseSkuInfoEntity.getOldMedicineId())
					.medicineCode(chineseSkuInfoEntity.getMedicineCode())
					.medicineName(chineseMedicineEntity.getMedicineName())
					.maxDosage(chineseMedicineEntity.getMaxDosage())
					.storeId(chineseSkuInfoEntity.getStoreId())
					.price(BigDecimalUtil.F2Y(chineseSkuInfoEntity.getPrice()))
					.skuState(chineseSkuInfoEntity.getSkuStatus())
					.skuStateName(ProductStateEnum.getValueByCode(chineseSkuInfoEntity.getSkuStatus()))
					.dosageType(chineseSkuInfoEntity.getDosageType())
					.dosageValue(chineseSkuInfoEntity.getDosageValue())
					.build());
		});
		chineseDosageInfoVO.setDosageChineseSkuInfoList(resultSkuInfoVoList);
		return chineseDosageInfoVO;
	}

	/**
	 * 根据参数获取店铺id
	 *
	 * @param storeId    店铺 id
	 * @param agencyId   互联网医院 id
	 * @param ucDoctorId 用户中心的医生 id
	 * @author liuqiuyi
	 * @date 2022/12/8 17:36
	 */
	@Override
	public Long queryStoreId(Long storeId, Long agencyId, Long ucDoctorId) {
		Long queryStoreId = Opt.ofNullable(storeService.getStoreByAgencyIdOrStoreId(agencyId, storeId)).map(StoreEntity::getId).orElse(null);
		if (Objects.nonNull(queryStoreId)) {
			return queryStoreId;
		}
		return Opt.ofNullable(userRemoteProService.getDoctorAgencyId(ucDoctorId)).map(paramAgencyId -> {
			StoreEntity storeEntity = storeService.getStoreByAgencyIdOrStoreId(paramAgencyId, null);
			return Objects.isNull(storeEntity) ? null : storeEntity.getId();
		}).orElse(null);
	}
}
