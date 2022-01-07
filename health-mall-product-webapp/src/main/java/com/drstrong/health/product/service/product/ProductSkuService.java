package com.drstrong.health.product.service.product;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.drstrong.health.product.model.entity.product.ProductSkuEntity;
import com.drstrong.health.product.model.enums.UpOffEnum;
import com.drstrong.health.product.model.request.product.QuerySkuRequest;
import com.drstrong.health.product.model.response.product.SkuBaseInfoVO;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 商品 sku 信息
 *
 * @author liuqiuyi
 * @date 2021/12/13 17:10
 */
public interface ProductSkuService {

	/**
	 * 批量保存 sku 信息
	 *
	 * @param skuEntityList 入参信息
	 * @author liuqiuyi
	 * @date 2021/12/13 17:11
	 */
	void batchSave(List<ProductSkuEntity> skuEntityList);

	/**
	 * 批量保存或者更新
	 *
	 * @author liuqiuyi
	 * @date 2021/12/16 15:55
	 */
	boolean saveOrUpdateBatch(Collection<ProductSkuEntity> entityList, int batchSize);

	/**
	 * 根据 商品id 查询 sku 集合
	 *
	 * @param productId 商品 id
	 * @return 商品 sku 集合
	 * @author liuqiuyi
	 * @date 2021/12/13 21:13
	 */
	List<ProductSkuEntity> queryByProductId(Long productId, UpOffEnum upOffEnum);

	/**
	 * 根据 商品id 查询 sku 集合,转成 skuMap
	 *
	 * @param productId 商品 id
	 * @return 商品 sku 集合,key=skuId,value=sku 信息
	 * @author liuqiuyi
	 * @date 2021/12/13 21:13
	 */
	Map<Long, ProductSkuEntity> queryByProductIdToMap(Long productId, UpOffEnum upOffEnum);

	/**
	 * 根据 商品id 集合,查询 sku 集合
	 *
	 * @param productIdList 商品 id 集合
	 * @return sku 信息
	 * @author liuqiuyi
	 * @date 2021/12/14 10:51
	 */
	List<ProductSkuEntity> queryByProductIdList(Set<Long> productIdList);

	/**
	 * 根据 商品id 集合,查询 sku 集合,组装为 map
	 *
	 * @param productIdList 商品 id 集合
	 * @return Map.key = productId,map.value = sku 集合
	 * @author liuqiuyi
	 * @date 2021/12/14 10:54
	 */
	Map<Long, List<ProductSkuEntity>> queryByProductIdListToMap(Set<Long> productIdList);

	/**
	 * 根据条件查询 sku 信息
	 *
	 * @param querySkuRequest 查询条件
	 * @return sku 信息
	 * @author liuqiuyi
	 * @date 2021/12/23 21:17
	 */
	List<ProductSkuEntity> querySkuByParam(QuerySkuRequest querySkuRequest);

	/**
	 * 根据 skuId 或者 skuCode 查询 sku 信息
	 *
	 * @param skuCode   sku 编码
	 * @param skuId     sku 的 id
	 * @param upOffEnum 上架状态(0-未上架,1-已上架)
	 * @return sku 信息
	 * @author liuqiuyi
	 * @date 2021/12/14 15:05
	 */
	ProductSkuEntity queryBySkuIdOrCode(Long skuId, String skuCode, UpOffEnum upOffEnum);

	/**
	 * 根据 skuId 集合或者 skuCode 集合查询 sku 信息
	 *
	 * @param skuIdList   skuId 集合
	 * @param skuCodeList sku编码集合
	 * @param upOffEnum   上架状态(0-未上架,1-已上架)
	 * @return sku 信息
	 * @author liuqiuyi
	 * @date 2021/12/16 09:59
	 */
	List<ProductSkuEntity> queryBySkuIdOrCode(Set<Long> skuIdList, Set<String> skuCodeList, UpOffEnum upOffEnum);

	void updateState(List<Long> skuIdList, Integer state, String userId);


	/**
	 * 小程序 - 根据 spuCode 查询 sku 集合
	 *
	 * @param spuCode spu编码
	 * @return sku 集合
	 * @author liuqiuyi
	 * @date 2021/12/16 21:00
	 */
	List<SkuBaseInfoVO> listSkuBySpuCode(String spuCode);

	/**
	 * 获取 sku 的最低价格和最高价格
	 *
	 * @author liuqiuyi
	 * @date 2021/12/18 14:50
	 */
	Map<String, BigDecimal> getPriceSectionMap(List<ProductSkuEntity> productSkuEntities);

	/**
	 * 批量获取店铺对应的sku数量
	 *
	 * @param storeIds
	 * @return
	 */
	Map<Long, Integer> searchSkuCountMap(List<Long> storeIds);

	/**
	 * 根据 skuId 集合删除 sku
	 *
	 * @param skuIdList skuId
	 * @param userId    用户 id
	 * @author liuqiuyi
	 * @date 2021/12/29 20:13
	 */
	void deleteBySkuIdList(Set<Long> skuIdList, String userId);

	LambdaQueryWrapper<ProductSkuEntity> buildQuerySkuParam(QuerySkuRequest querySkuRequest);
}
