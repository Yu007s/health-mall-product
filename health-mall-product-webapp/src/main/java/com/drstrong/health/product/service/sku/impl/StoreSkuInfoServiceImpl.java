package com.drstrong.health.product.service.sku.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.drstrong.health.product.dao.sku.StoreSkuInfoMapper;
import com.drstrong.health.product.model.entity.sku.StoreSkuInfoEntity;
import com.drstrong.health.product.model.enums.DelFlagEnum;
import com.drstrong.health.product.model.enums.ErrorEnums;
import com.drstrong.health.product.model.enums.UpOffEnum;
import com.drstrong.health.product.model.request.product.v3.ProductManageQueryRequest;
import com.drstrong.health.product.model.response.result.BusinessException;
import com.drstrong.health.product.service.sku.StoreSkuInfoService;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Set;

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
	 * @return
	 * @author liuqiuyi
	 * @date 2023/6/13 10:28
	 */
	@Override
	public Page<StoreSkuInfoEntity> pageQueryByParam(ProductManageQueryRequest productManageQueryRequest) {
		Page<StoreSkuInfoEntity> entityPage = new Page<>(productManageQueryRequest.getPageNo(), productManageQueryRequest.getPageSize());
		return baseMapper.pageQueryByParam(entityPage, productManageQueryRequest);
	}

	/**
	 * 根据条件查询所有
	 *
	 * @param productManageQueryRequest
	 * @return
	 * @author liuqiuyi
	 * @date 2023/6/13 10:28
	 */
	@Override
	public List<StoreSkuInfoEntity> listQueryByParam(ProductManageQueryRequest productManageQueryRequest) {
		return baseMapper.listQueryByParam(productManageQueryRequest);
	}

	/**
	 * 根据类型查询所有的 sku 信息
	 *
	 * @param productType
	 * @author liuqiuyi
	 * @date 2023/6/13 16:57
	 */
	@Override
	public List<StoreSkuInfoEntity> queryAllByProductType(Long storeId, Integer productType) {
		LambdaQueryWrapper<StoreSkuInfoEntity> queryWrapper = new LambdaQueryWrapper<StoreSkuInfoEntity>()
				.eq(StoreSkuInfoEntity::getDelFlag, DelFlagEnum.UN_DELETED.getCode())
				.eq(Objects.nonNull(storeId), StoreSkuInfoEntity::getStoreId, storeId)
				.eq(Objects.nonNull(productType), StoreSkuInfoEntity::getSkuType, productType);
		return baseMapper.selectList(queryWrapper);
	}

	/**
	 * 根据 skuCode 查询
	 *
	 * @param skuCodeList
	 * @author liuqiuyi
	 * @date 2023/6/14 15:38
	 */
	@Override
	public List<StoreSkuInfoEntity> querySkuCodes(Set<String> skuCodeList) {
		if (CollectionUtil.isEmpty(skuCodeList)) {
			return Lists.newArrayList();
		}
		LambdaQueryWrapper<StoreSkuInfoEntity> queryWrapper = new LambdaQueryWrapper<StoreSkuInfoEntity>()
				.eq(StoreSkuInfoEntity::getDelFlag, DelFlagEnum.UN_DELETED.getCode())
				.in(StoreSkuInfoEntity::getSkuCode, skuCodeList);
		return baseMapper.selectList(queryWrapper);
	}

	/**
	 * 批量更新 sku 状态
	 *
	 * @param skuCodeList
	 * @param skuState
	 * @param operatorId
	 * @author liuqiuyi
	 * @date 2023/6/14 15:50
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void batchUpdateSkuStatusByCodes(Set<String> skuCodeList, Integer skuState, Long operatorId) {
		int size = baseMapper.batchUpdateSkuStatusByCodes(skuCodeList, skuState, operatorId);
		if (size > 0) {
			log.info("店铺sku信息表,已将skuCode列表:{} 的状态更新为:{},操作人是:{}", skuCodeList, skuState, operatorId);
		}
	}
}
