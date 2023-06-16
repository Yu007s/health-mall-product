package com.drstrong.health.product.dao.category.v3;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.drstrong.health.product.model.entity.category.v3.CategoryEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;

/**
 * @author liuqiuyi
 * @date 2023/6/12 15:45
 */
@Mapper
public interface CategoryMapper extends BaseMapper<CategoryEntity> {
	int updateLeafCountByIds(@Param("categoryIds") Collection<Long> categoryIds, @Param("offset") Integer offset);

	int updateChildrenNamePath(@Param("idPath") String idPath,
							   @Param("oldNamePath") String oldNamePath, @Param("newNamePath") String newNamePath);
}
