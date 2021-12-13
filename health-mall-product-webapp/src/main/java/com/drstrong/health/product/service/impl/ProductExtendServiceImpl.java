package com.drstrong.health.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.drstrong.health.product.dao.ProductExtendMapper;
import com.drstrong.health.product.model.entity.product.ProductExtendEntity;
import com.drstrong.health.product.model.enums.DelFlagEnum;
import com.drstrong.health.product.service.ProductExtendService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * 商品扩展信息 service
 *
 * @author liuqiuyi
 * @date 2021/12/13 15:59
 */
@Slf4j
@Service
public class ProductExtendServiceImpl extends ServiceImpl<ProductExtendMapper, ProductExtendEntity> implements ProductExtendService {
	@Resource
	ProductExtendMapper productExtendMapper;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean saveOrUpdate(ProductExtendEntity entity) {
		return super.saveOrUpdate(entity);
	}

	/**
	 * 根据 商品 id 查询商品的扩展信息
	 *
	 * @param productId 商品id
	 * @return 商品信息
	 * @author liuqiuyi
	 * @date 2021/12/13 17:43
	 */
	@Override
	public ProductExtendEntity queryByProductId(Long productId) {
		if (Objects.isNull(productId)) {
			return new ProductExtendEntity();
		}
		LambdaQueryWrapper<ProductExtendEntity> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.eq(ProductExtendEntity::getProductId, productId).eq(ProductExtendEntity::getDelFlag, DelFlagEnum.UN_DELETED.getCode());
		return productExtendMapper.selectOne(queryWrapper);
	}
}
