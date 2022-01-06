package com.drstrong.health.product.dao.category;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.drstrong.health.product.model.entity.category.CategoryAttributeEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品分类属性字典表 mapper
 *
 * @author liuqiuyi
 * @date 2021/12/27 11:45
 */
@Mapper
public interface CategoryAttributeMapper extends BaseMapper<CategoryAttributeEntity> {
	/**
	 * 插入数据
	 * <p> 仅供数据同步使用 </>
	 *
	 * @author liuqiuyi
	 * @date 2021/12/27 17:24
	 */
	@Deprecated
	void syncDateInsertOne(CategoryAttributeEntity categoryAttributeEntity);
}
