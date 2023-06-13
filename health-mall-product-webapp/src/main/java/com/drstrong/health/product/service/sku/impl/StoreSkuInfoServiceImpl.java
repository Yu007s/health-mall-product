package com.drstrong.health.product.service.sku.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.drstrong.health.product.dao.sku.StoreSkuInfoMapper;
import com.drstrong.health.product.model.entity.sku.StoreSkuInfoEntity;
import com.drstrong.health.product.model.enums.DelFlagEnum;
import com.drstrong.health.product.model.enums.ErrorEnums;
import com.drstrong.health.product.model.enums.UpOffEnum;
import com.drstrong.health.product.model.request.product.v3.ProductManageQueryRequest;
import com.drstrong.health.product.model.response.result.BusinessException;
import com.drstrong.health.product.service.sku.StoreSkuInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @author liuqiuyi
 * @date 2023/6/8 15:20
 */
@Slf4j
@Service
public class StoreSkuInfoServiceImpl extends ServiceImpl<StoreSkuInfoMapper, StoreSkuInfoEntity> implements StoreSkuInfoService {


	/**
	 * 根据编码查询 sku 信息
	 *
	 * @param skuCode
	 * @author liuqiuyi
	 * @date 2023/6/8 15:22
	 */
	@Override
	public StoreSkuInfoEntity queryBySkuCode(String skuCode, Integer skuStatus) {
		if (StrUtil.isBlank(skuCode)) {
			return null;
		}
		LambdaQueryWrapper<StoreSkuInfoEntity> queryWrapper = new LambdaQueryWrapper<StoreSkuInfoEntity>()
				.eq(StoreSkuInfoEntity::getDelFlag, DelFlagEnum.UN_DELETED.getCode())
				.eq(StoreSkuInfoEntity::getSkuCode, skuCode)
				.eq(Objects.nonNull(skuStatus), StoreSkuInfoEntity::getSkuStatus, skuStatus);
		return baseMapper.selectOne(queryWrapper);
	}

	/**
	 * 校验 sku 是否存在
	 * <p>
	 * {@link UpOffEnum}
	 *
	 * @param skuCode
	 * @param skuStatus
	 * @author liuqiuyi
	 * @date 2023/6/9 09:32
	 */
	@Override
	public StoreSkuInfoEntity checkSkuExistByCode(String skuCode, Integer skuStatus) {
		StoreSkuInfoEntity storeSkuInfoEntity = queryBySkuCode(skuCode, skuStatus);
		if (Objects.isNull(storeSkuInfoEntity)) {
			throw new BusinessException(ErrorEnums.SKU_IS_NULL);
		}
		return storeSkuInfoEntity;
	}

	/**
	 * 校验店铺下 skuName 是否重复
	 *
	 * @param skuName
	 * @param storeId
	 * @author liuqiuyi
	 * @date 2023/6/10 14:07
	 */
	@Override
	public void checkSkuNameIsRepeat(String skuName, Long storeId) {
		if (StrUtil.isBlank(skuName) || Objects.isNull(storeId)) {
			return;
		}
		LambdaQueryWrapper<StoreSkuInfoEntity> queryWrapper = new LambdaQueryWrapper<StoreSkuInfoEntity>()
				.eq(StoreSkuInfoEntity::getDelFlag, DelFlagEnum.UN_DELETED.getCode())
				.eq(StoreSkuInfoEntity::getSkuName, skuName)
				.eq(StoreSkuInfoEntity::getStoreId, storeId);
		StoreSkuInfoEntity storeSkuInfoEntity = baseMapper.selectOne(queryWrapper);
		if (Objects.nonNull(storeSkuInfoEntity)) {
			throw new BusinessException(ErrorEnums.SKU_NAME_IS_REPEAT);
		}
	}

	/**
	 * 根据药材编码和店铺 id 校验是否存在
	 *
	 * @param medicineCode
	 * @param storeId
	 * @author liuqiuyi
	 * @date 2023/6/10 14:11
	 */
	@Override
	public void checkMedicineCodeAndStoreId(String medicineCode, Long storeId) {
		if (StrUtil.isBlank(medicineCode) || Objects.isNull(storeId)) {
			return;
		}
		LambdaQueryWrapper<StoreSkuInfoEntity> queryWrapper = new LambdaQueryWrapper<StoreSkuInfoEntity>()
				.eq(StoreSkuInfoEntity::getDelFlag, DelFlagEnum.UN_DELETED.getCode())
				.eq(StoreSkuInfoEntity::getMedicineCode, medicineCode)
				.eq(StoreSkuInfoEntity::getStoreId, storeId);
		StoreSkuInfoEntity storeSkuInfoEntity = baseMapper.selectOne(queryWrapper);
		if (Objects.nonNull(storeSkuInfoEntity)) {
			throw new BusinessException(ErrorEnums.CHINESE_IS_REPEAT);
		}
	}

	/**
	 * 根据条件分页查询
	 *
	 * @param productManageQueryRequest
	 * @author liuqiuyi
	 * @date 2023/6/13 10:28
	 */
	@Override
	public void pageQueryByParam(ProductManageQueryRequest productManageQueryRequest) {

	}
}
