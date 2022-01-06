package com.drstrong.health.product.dao.product;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.drstrong.health.product.model.entity.product.ProductBasicsInfoEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商品基础信息 mapper
 *
 * @author liuqiuyi
 * @date 2021/12/13 15:38
 */
@Mapper
public interface ProductBasicsInfoMapper extends BaseMapper<ProductBasicsInfoEntity> {

	/**
	 * 模糊查询商品标题
	 *
	 * @param content 查询内容
	 * @param count   个数
	 * @return 满足条件的标题集合
	 * @author liuqiuyi
	 * @date 2021/12/17 16:12
	 */
	List<String> likeProductTitle(@Param("content") String content, @Param("count") Integer count);

	/**
	 * 模糊查询商品标题和品牌名
	 *
	 * @param content 查询内容
	 * @param count   个数
	 * @return 满足条件的标题集合
	 * @author liuqiuyi
	 * @date 2021/12/17 16:12
	 */
	List<ProductBasicsInfoEntity> likeTitleAndBrandName(@Param("content") String content, @Param("count") Integer count);
}
