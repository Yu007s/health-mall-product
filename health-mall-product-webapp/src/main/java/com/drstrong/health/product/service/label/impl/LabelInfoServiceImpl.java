package com.drstrong.health.product.service.label.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.drstrong.health.product.dao.label.LabelInfoMapper;
import com.drstrong.health.product.model.entity.label.LabelInfoEntity;
import com.drstrong.health.product.model.enums.DelFlagEnum;
import com.drstrong.health.product.service.label.LabelInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * @author liuqiuyi
 * @date 2023/6/7 11:27
 */
@Slf4j
@Service
public class LabelInfoServiceImpl extends ServiceImpl<LabelInfoMapper, LabelInfoEntity> implements LabelInfoService {
	/**
	 * 根据 Id 查询
	 *
	 * @param id
	 * @author liuqiuyi
	 * @date 2023/6/7 12:01
	 */
	@Override
	public LabelInfoEntity queryById(Long id) {
		if (Objects.isNull(id)) {
			return null;
		}
		LambdaQueryWrapper<LabelInfoEntity> queryWrapper = new LambdaQueryWrapper<LabelInfoEntity>()
				.eq(LabelInfoEntity::getDelFlag, DelFlagEnum.UN_DELETED.getCode())
				.eq(LabelInfoEntity::getId, id);
		return baseMapper.selectOne(queryWrapper);
	}

	/**
	 * 根据店铺 id 和标签名称查询
	 *
	 * @param storeId
	 * @param labelName
	 * @author liuqiuyi
	 * @date 2023/6/7 11:29
	 */
	@Override
	public LabelInfoEntity queryByStoreIdAndName(Long storeId, String labelName) {
		if (Objects.isNull(storeId) || StrUtil.isBlank(labelName)) {
			return null;
		}
		LambdaQueryWrapper<LabelInfoEntity> queryWrapper = new LambdaQueryWrapper<LabelInfoEntity>()
				.eq(LabelInfoEntity::getDelFlag, DelFlagEnum.UN_DELETED.getCode())
				.eq(LabelInfoEntity::getStoreId, storeId)
				.eq(LabelInfoEntity::getLabelName, labelName);
		return baseMapper.selectOne(queryWrapper);
	}

	/**
	 * 根据店铺 id 和标签类型查询
	 *
	 * @param storeId
	 * @param labelType
	 * @author liuqiuyi
	 * @date 2023/6/7 11:29
	 */
	@Override
	public List<LabelInfoEntity> listByStoreIdAndType(Long storeId, Integer labelType) {
		LambdaQueryWrapper<LabelInfoEntity> queryWrapper = new LambdaQueryWrapper<LabelInfoEntity>()
				.eq(LabelInfoEntity::getDelFlag, DelFlagEnum.UN_DELETED.getCode())
				.eq(Objects.nonNull(storeId), LabelInfoEntity::getStoreId, storeId)
				.eq(Objects.nonNull(labelType), LabelInfoEntity::getLabelType, labelType);
		return baseMapper.selectList(queryWrapper);
	}
}
