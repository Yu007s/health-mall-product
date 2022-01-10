package com.drstrong.health.product.dao.product;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.drstrong.health.product.model.entity.product.ProductSkuEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

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

	/**
	 * 根据 skuId 集合删除 sku
	 *
	 * @param skuIdList skuId
	 * @param userId    用户 id
	 * @author liuqiuyi
	 * @date 2021/12/29 20:13
	 */
	int deleteBySkuIdList(@Param("skuIdList") Set<Long> skuIdList, @Param("userId") String userId);

	/**
	 * 根据 skuId 集合或者 skuCode 集合查询 sku 信息(包含已删除的数据)
	 * <p> 包含 delFlag 为 1 的数据 </>
	 *
	 * @param skuIdList   skuId 集合
	 * @param skuCodeList sku编码集合
	 * @param upOffCode   上架状态(0-未上架,1-已上架)
	 * @return sku 信息
	 * @author liuqiuyi
	 * @date 2022/1/10 16:54
	 */
	List<ProductSkuEntity> selectListContainDel(@Param("skuIdList") Set<Long> skuIdList, @Param("skuCodeList") Set<String> skuCodeList, @Param("upOffCode") Integer upOffCode);
}
