package com.drstrong.health.product.service.sku;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.drstrong.health.product.model.entity.sku.StoreSkuInfoEntity;
import com.drstrong.health.product.model.enums.UpOffEnum;
import com.drstrong.health.product.model.request.product.v3.ProductManageQueryRequest;

import java.util.List;
import java.util.Set;

/**
 * @author liuqiuyi
 * @date 2023/6/8 14:47
 */
public interface StoreSkuInfoService extends IService<StoreSkuInfoEntity> {

	/**
	 * 根据编码查询 sku 信息
	 * <p>
	 * {@link UpOffEnum}
	 *
	 * @author liuqiuyi
	 * @date 2023/6/8 15:22
	 */
	StoreSkuInfoEntity queryBySkuCode(String skuCode, Integer skuStatus);

	/**
	 * 校验 sku 是否存在
	 * <p>
	 * {@link UpOffEnum}
	 *
	 * @author liuqiuyi
	 * @date 2023/6/9 09:32
	 */
	StoreSkuInfoEntity checkSkuExistByCode(String skuCode, Integer skuStatus);

	/**
	 * 根据分类校验sku是否存在
	 *
	 * @author liuqiuyi
	 * @date 2023/6/26 9:58
	 */
	StoreSkuInfoEntity checkSkuExistByCategoryId(Long categoryId);

	/**
	 * 校验店铺下 skuName 是否重复
	 *
	 * @author liuqiuyi
	 * @date 2023/6/10 14:07
	 */
	void checkSkuNameIsRepeat(String skuName, Long storeId);

	/**
	 * 根据药材编码和店铺 id 校验是否存在
	 *
	 * @author liuqiuyi
	 * @date 2023/6/10 14:11
	 */
	void checkMedicineCodeAndStoreId(String medicineCode, Long storeId);

	/**
	 * 根据条件分页查询
	 *
	 * @return
	 * @author liuqiuyi
	 * @date 2023/6/13 10:28
	 */
	Page<StoreSkuInfoEntity> pageQueryByParam(ProductManageQueryRequest productManageQueryRequest);

	/**
	 * 根据条件查询所有
	 *
	 * @return
	 * @author liuqiuyi
	 * @date 2023/6/13 10:28
	 */
	List<StoreSkuInfoEntity> listQueryByParam(ProductManageQueryRequest productManageQueryRequest);

	/**
	 * 根据类型查询所有的 sku 信息
	 *
	 * @author liuqiuyi
	 * @date 2023/6/13 16:57
	 */
	List<StoreSkuInfoEntity> queryAllByProductType(Long storeId, Integer productType);

	/**
	 * 根据 skuCode 查询
	 *
	 * @author liuqiuyi
	 * @date 2023/6/14 15:38
	 */
	List<StoreSkuInfoEntity> querySkuCodes(Set<String> skuCodeList);

	/**
	 * 批量更新 sku 状态
	 *
	 * @author liuqiuyi
	 * @date 2023/6/14 15:50
	 */
	void batchUpdateSkuStatusByCodes(Set<String> skuCodeList, Integer skuState, Long operatorId);

	List<StoreSkuInfoEntity> queryStoreSkuInfoByCategory(Long categoryId);

	List<StoreSkuInfoEntity> queryStoreSkuInfoByCategoryAndCityId(String key, Long categoryId, Integer cityId, List<Long> storeIds);
}
