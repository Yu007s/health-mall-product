package com.drstrong.health.product.service.chinese;

import com.baomidou.mybatisplus.extension.service.IService;
import com.drstrong.health.product.model.entity.chinese.ChineseSkuSupplierRelevanceEntity;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * 中药sku和供应商关联表 服务类
 * </p>
 *
 * @author mybatis plus generator
 * @since 2022-08-01
 */
public interface ChineseSkuSupplierRelevanceService extends IService<ChineseSkuSupplierRelevanceEntity> {
    /**
     * 根据 skuCode 集合查询关联关系
     *
     * @param skuCodes skuCode 集合
     * @return 店铺和供应商的关联关系
     * @author liuqiuyi
     * @date 2022/8/1 14:39
     */
    List<ChineseSkuSupplierRelevanceEntity> listQueryBySkuCodeList(Set<String> skuCodes);

	/**
	 * 根据 skuCode 集合查询关联关系,并组装成 map
	 *
	 * @param skuCodes skuCode 集合
	 * @return skuCode 和供应商的关联关系
	 * @author liuqiuyi
	 * @date 2022/10/31 11:53
	 */
	Map<String, Set<Long>> getSkuCodeAndSupplierIdsMap(Set<String> skuCodes);

    /**
     * 根据 skuCode 删除关联关系
     *
     * @param skuCode    sku 编码
     * @param operatorId 操作人id
     * @author liuqiuyi
     * @date 2022/8/2 11:06
     */
    void deleteBySkuCode(String skuCode, Long operatorId);

	/**
	 * 根据 sku 编号和供应商 id,逻辑删除
	 *
	 * @author liuqiuyi
	 * @date 2022/10/31 14:42
	 */
	void deleteRelevanceBySkuCodesAndSupplierId(Set<String> skuCodes, Long supplierId, Long operatorId);
}
