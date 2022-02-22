package com.drstrong.health.product.service.product;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.drstrong.health.product.model.entity.product.ProductBasicsInfoEntity;
import com.drstrong.health.product.model.entity.product.ProductSkuEntity;
import com.drstrong.health.product.model.enums.UpOffEnum;
import com.drstrong.health.product.model.request.PageRequest;
import com.drstrong.health.product.model.request.product.ProductSearchRequest;
import com.drstrong.health.product.model.request.product.QuerySpuRequest;
import com.drstrong.health.product.model.response.PageVO;
import com.drstrong.health.product.model.response.product.ProductDetailVO;
import com.drstrong.health.product.model.response.product.ProductRecommendVO;
import com.drstrong.health.product.model.response.product.ProductSearchDetailVO;

import java.util.Collection;
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
	 * 根据条件,查询商品基础信息,转成 map 结构
	 *
	 * @param querySpuRequest 查询条件
	 * @return 商品基础信息集合 map.key = 商品 id,value = 商品基础信息
	 * @author liuqiuyi
	 * @date 2021/12/16 00:10
	 */
	Map<Long, ProductBasicsInfoEntity> queryProductByParamToMap(QuerySpuRequest querySpuRequest);

	/**
	 * 根据 productId 或者 spuCode 查询商品基本信息
	 *
	 * @param productId 商品 id
	 * @param spuCode   spu编码
	 * @param upOffEnum 上下架状态
	 * @author liuqiuyi
	 * @date 2021/12/16 20:15
	 */
	ProductBasicsInfoEntity getByProductIdOrSpuCode(Long productId, String spuCode, UpOffEnum upOffEnum);

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
	 * 小程序 - 根据 spuCode 获取商品详细信息
	 *
	 * @param spuCode spu编码
	 * @return spu信息
	 * @author liuqiuyi
	 * @date 2021/12/16 19:55
	 */
	ProductDetailVO getSpuInfo(String spuCode, Long userId);

	/**
	 * 分页查询搜索的内容,只返回商品名称
	 *
	 * @param content 搜索条件
	 * @param count   返回的个数
	 * @return 搜索结果
	 * @author liuqiuyi
	 * @date 2021/12/17 15:49
	 */
	List<String> searchByName(String content, Integer count);

	/**
	 * 模糊查询商品标题和品牌名
	 *
	 * @param content 查询内容
	 * @param count   个数
	 * @return 满足条件的标题集合
	 * @author liuqiuyi
	 * @date 2021/12/17 16:12
	 */
	List<ProductBasicsInfoEntity> searchTitleAndBrandName(String content, Integer count);

	/**
	 * 分页查询搜索结果
	 *
	 * @param productSearchRequest 搜索条件
	 * @return 搜索结果
	 * @author liuqiuyi
	 * @date 2021/12/18 14:05
	 */
	PageVO<ProductSearchDetailVO> pageSearchDetail(ProductSearchRequest productSearchRequest);

	/**
	 * 分页查询热门推荐商品
	 *
	 * @param pageRequest
	 * @return
	 */
	PageVO<ProductRecommendVO> pageSearchRecommend(PageRequest pageRequest);

	/**
	 * TableId 注解存在更新记录，否插入一条记录
	 *
	 * @param entity 实体对象
	 * @return boolean
	 */
	boolean saveOrUpdate(ProductBasicsInfoEntity entity);

	/**
	 * 根据 id 集合批量更新
	 *
	 * @author liuqiuyi
	 * @date 2021/12/30 15:17
	 */
	boolean updateBatchById(Collection<ProductBasicsInfoEntity> entityList);

	LambdaQueryWrapper<ProductBasicsInfoEntity> buildQuerySpuParam(QuerySpuRequest querySpuRequest);
}
