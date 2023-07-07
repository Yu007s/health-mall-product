package com.drstrong.health.product.dao.sku;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.drstrong.health.product.model.entity.sku.StoreSkuInfoEntity;
import com.drstrong.health.product.model.request.product.QueryStoreSkuInfoRequest;
import com.drstrong.health.product.model.request.product.v3.ProductManageQueryRequest;
import com.drstrong.health.product.model.response.PageVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * @author liuqiuyi
 * @date 2023/6/8 14:42
 */
@Mapper
public interface StoreSkuInfoMapper extends BaseMapper<StoreSkuInfoEntity> {

	Page<StoreSkuInfoEntity> pageQueryByParam(Page<StoreSkuInfoEntity> page, @Param("queryParam") ProductManageQueryRequest queryParam);

	List<StoreSkuInfoEntity> listQueryByParam(@Param("queryParam") ProductManageQueryRequest queryParam);

	int batchUpdateSkuStatusByCodes(@Param("skuCodeList") Set<String> skuCodeList, @Param("skuState") Integer skuState, @Param("operatorId") Long operatorId);

	StoreSkuInfoEntity selectOneByCategoryId(@Param("categoryId") Long categoryId);

    PageVO<StoreSkuInfoEntity> pageQueryStoreSkuInfo(Page<StoreSkuInfoEntity> entityPage, @Param("queryParam")QueryStoreSkuInfoRequest queryStoreSkuInfoRequest);
}
