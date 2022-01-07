package com.drstrong.health.product.dao.product;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.drstrong.health.product.model.entity.product.ProductAttributeEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商品关联的属性值
 *
 * @author liuqiuyi
 * @date 2021/12/13 16:19
 */
@Mapper
public interface ProductAttributeMapper extends BaseMapper<ProductAttributeEntity> {

	/**
	 * 批量保存
	 *
	 * @param entityList 保存的参数
	 * @return 影响的行数
	 * @author liuqiuyi
	 * @date 2021/12/13 16:30
	 */
	int batchInsert(@Param("saveParamList") List<ProductAttributeEntity> entityList);
}
