package com.drstrong.health.product.service.postage.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.drstrong.health.product.dao.postage.StorePostageMapper;
import com.drstrong.health.product.model.entity.postage.StorePostageEntity;
import com.drstrong.health.product.model.enums.DelFlagEnum;
import com.drstrong.health.product.model.enums.ErrorEnums;
import com.drstrong.health.product.model.request.store.SaveStoreSupplierPostageRequest;
import com.drstrong.health.product.model.response.result.BusinessException;
import com.drstrong.health.product.service.postage.StorePostageService;
import com.drstrong.health.product.util.BigDecimalUtil;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * @author liuqiuyi
 * @date 2023/6/6 13:53
 */
@Slf4j
@Service
public class StorePostageServiceImpl extends ServiceImpl<StorePostageMapper, StorePostageEntity> implements StorePostageService {

	/**
	 * 根据店铺 id 查询配送费信息
	 *
	 * @param storeId
	 * @author liuqiuyi
	 * @date 2023/6/6 14:08
	 */
	@Override
	public StorePostageEntity queryByStoreId(Long storeId) {
		if (Objects.isNull(storeId)) {
			return null;
		}
		LambdaQueryWrapper<StorePostageEntity> queryWrapper = new LambdaQueryWrapper<StorePostageEntity>()
				.eq(StorePostageEntity::getDelFlag, DelFlagEnum.UN_DELETED.getCode())
				.eq(StorePostageEntity::getStoreId, storeId);
		return baseMapper.selectOne(queryWrapper);
	}

	/**
	 * 保存或者更新店铺供应商的全局配送费信息
	 *
	 * @param saveStoreSupplierPostageRequest 入参
	 * @author liuqiuyi
	 * @date 2023/6/6 15:02
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveOrUpdateSupplierPostageInfo(SaveStoreSupplierPostageRequest saveStoreSupplierPostageRequest) {
		StorePostageEntity storePostageEntity = queryByStoreId(saveStoreSupplierPostageRequest.getStoreId());
		if (Objects.isNull(storePostageEntity)) {
			throw new BusinessException(ErrorEnums.STORE_POSTAGE_IS_NULL);
		}
		// 1.先保存供应商包邮金额
		doSetSupplierFreePostageInfo(saveStoreSupplierPostageRequest, storePostageEntity);
		// 2.在处理供应商区域邮费
		doSetSupplierAreaPostageInfo(saveStoreSupplierPostageRequest, storePostageEntity);
		// 3.更新数据库
		storePostageEntity.setChangedBy(saveStoreSupplierPostageRequest.getOperatorId());
		storePostageEntity.setChangedAt(LocalDateTime.now());
		baseMapper.updateById(storePostageEntity);
	}

	private void doSetSupplierAreaPostageInfo(SaveStoreSupplierPostageRequest saveStoreSupplierPostageRequest, StorePostageEntity storePostageEntity) {
		List<SaveStoreSupplierPostageRequest.StoreSupplierAreaPostageVO> storeSupplierAreaPostageList = saveStoreSupplierPostageRequest.getStoreSupplierAreaPostageList();
		if (CollectionUtil.isEmpty(storeSupplierAreaPostageList)) {
			log.error("供应商区域邮费信息为空,不保存数据!,参数为:{}", JSONUtil.toJsonStr(saveStoreSupplierPostageRequest));
			return;
		}
		List<StorePostageEntity.SupplierAreaPostageInfo> supplierAreaPostageInfoList = Lists.newArrayListWithCapacity(storeSupplierAreaPostageList.size());
		storeSupplierAreaPostageList.forEach(storeSupplierAreaPostageVO -> {
			StorePostageEntity.SupplierAreaPostageInfo saveParam = StorePostageEntity.SupplierAreaPostageInfo.builder()
					.supplierId(saveStoreSupplierPostageRequest.getSupplierId())
					.areaId(storeSupplierAreaPostageVO.getAreaId())
					.postage(BigDecimalUtil.Y2F(storeSupplierAreaPostageVO.getPostage()))
					.build();
			supplierAreaPostageInfoList.add(saveParam);
		});

		// 未设置任何供应商的区域邮费
		if (CollectionUtil.isEmpty(storePostageEntity.getSupplierAreaPostageInfo())) {
			storePostageEntity.setSupplierAreaPostageInfo(supplierAreaPostageInfoList);
		} else {
			// 已经存在了供应商区域的邮费,更新
			storePostageEntity.getSupplierAreaPostageInfo().removeIf(infoEntity -> Objects.equals(infoEntity.getSupplierId(), saveStoreSupplierPostageRequest.getSupplierId()));
			storePostageEntity.getSupplierAreaPostageInfo().addAll(supplierAreaPostageInfoList);
		}
	}

	private void doSetSupplierFreePostageInfo(SaveStoreSupplierPostageRequest saveStoreSupplierPostageRequest, StorePostageEntity storePostageEntity) {
		StorePostageEntity.SupplierFreePostageInfo supplierFreePostageInfo = StorePostageEntity.SupplierFreePostageInfo.builder()
				.supplierId(saveStoreSupplierPostageRequest.getSupplierId())
				.freePostage(BigDecimalUtil.Y2F(saveStoreSupplierPostageRequest.getFreePostage()))
				.build();
		// 未设置任何供应商的邮费
		if (CollectionUtil.isEmpty(storePostageEntity.getSupplierFreePostageInfo())) {
			storePostageEntity.setSupplierFreePostageInfo(Lists.newArrayList(supplierFreePostageInfo));
		} else {
			// 已经存在了供应商的邮费,更新
			storePostageEntity.getSupplierFreePostageInfo().removeIf(infoEntity -> Objects.equals(infoEntity.getSupplierId(), saveStoreSupplierPostageRequest.getSupplierId()));
			storePostageEntity.getSupplierFreePostageInfo().add(supplierFreePostageInfo);
		}
	}
}
