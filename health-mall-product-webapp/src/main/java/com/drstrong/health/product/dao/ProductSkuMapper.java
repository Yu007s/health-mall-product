package com.drstrong.health.product.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.drstrong.health.product.model.entity.product.ProductSkuEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商品 sku mapper
 *
 * @author liuqiuyi
 * @date 2021/12/13 16:44
 */
@Mapper
public interface ProductSkuMapper extends BaseMapper<ProductSkuEntity> {

	/**
	 * 批量插入
	 *
	 * @param skuEntityList 入参信息
	 * @return 插入的行数
	 * @author liuqiuyi
	 * @date 2021/12/13 17:09
	 */
	int batchInsert(List<ProductSkuEntity> skuEntityList);

	/**
	 * 模糊查询 skuName
	 *
	 * @author liuqiuyi
	 * @date 2021/12/23 21:09
	 */
	List<String> searchSkuNameByName(@Param("content") String content, @Param("count") Integer count);
}
