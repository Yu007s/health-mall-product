package com.drstrong.health.product.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.drstrong.health.product.model.entity.product.ProductBasicsInfoEntity;
import com.drstrong.health.product.model.entity.product.ProductSkuEntity;
import com.drstrong.health.product.model.request.product.QuerySpuRequest;
import com.drstrong.health.product.model.request.product.SaveProductRequest;
import com.drstrong.health.product.model.response.PageVO;
import com.drstrong.health.product.model.response.product.ProductManageVO;
import com.drstrong.health.product.model.response.product.ProductSpuVO;

import java.util.List;
import java.util.Map;

/**
 * 商品信息 service
 *
 * @author liuqiuyi
 * @date 2021/12/13 15:38
 */
public interface ProductBasicsInfoService {
	/**
	 * 根据条件,分页查询商品基础信息
	 *
	 * @param querySpuRequest 查询入参
	 * @return 商品基础信息
	 * @author liuqiuyi
	 * @date 2021/12/15 21:20
	 */
	Page<ProductBasicsInfoEntity> pageQueryProductByParam(QuerySpuRequest querySpuRequest);

	/**
	 * 根据条件,查询商品基础信息
	 *
	 * @param querySpuRequest 查询条件
	 * @return 商品基础信息集合
	 * @author liuqiuyi
	 * @date 2021/12/16 00:10
	 */
	List<ProductBasicsInfoEntity> queryProductByParam(QuerySpuRequest querySpuRequest);

	/**
	 * 根据商品信息,组装 sku map
	 *
	 * @param basicsInfoEntityList 商品基本信息
	 * @return map.key = 商品 id,map.value = sku 集合
	 * @author liuqiuyi
	 * @date 2021/12/16 00:30
	 */
	Map<Long, List<ProductSkuEntity>> buildSkuMap(List<ProductBasicsInfoEntity> basicsInfoEntityList);

	/**
	 * 根据商品 id 查询商品基础信息
	 *
	 * @param productId 商品 id
	 * @return 商品基础信息
	 * @author liuqiuyi
	 * @date 2021/12/13 20:30
	 */
	ProductBasicsInfoEntity queryBasicsInfoByProductId(Long productId);

	/**
	 * 保存或者更新商品信息
	 *
	 * @param saveProductRequest 保存商品
	 * @author liuqiuyi
	 * @date 2021/12/13 15:48
	 */
	void saveOrUpdateProduct(SaveProductRequest saveProductRequest);

	/**
	 * 根据商品 id,查询管理端商品信息
	 *
	 * @param productId 商品 id
	 * @return 商品管理端信息
	 * @author liuqiuyi
	 * @date 2021/12/13 20:26
	 */
	ProductManageVO queryManageProductInfo(Long productId);

	/**
	 * 管理端页面分页查询 spu 信息
	 *
	 * @param querySpuRequest 查询参数
	 * @return 分页返回值
	 * @author liuqiuyi
	 * @date 2021/12/14 10:25
	 */
	PageVO<ProductSpuVO> pageQuerySpuByParam(QuerySpuRequest querySpuRequest);

	/**
	 * 通过SkuCode 查询 已上架条数
	 *
	 * @param spuCode spu_code
	 * @return 上架条目条数
	 */
	Integer getCountBySPUCode(String spuCode);
}
