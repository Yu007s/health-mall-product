package com.drstrong.health.product.dao.activty;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.drstrong.health.product.model.entity.activty.ActivityPackageInfoEntity;
import com.drstrong.health.product.model.entity.sku.StoreSkuInfoEntity;
import com.drstrong.health.product.model.request.product.ActivityPackageManageQueryRequest;
import com.drstrong.health.product.model.request.product.v3.ProductManageQueryRequest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * huangpeng
 * 2023/7/7 12:00
 */
@Mapper
public interface ActivityPackageInfoMapper extends BaseMapper<ActivityPackageInfoEntity> {

    Page<ActivityPackageInfoEntity> pageQueryByParam(Page<ActivityPackageInfoEntity> entityPage, @Param("queryParam") ActivityPackageManageQueryRequest queryParam);

    List<ActivityPackageInfoEntity> listQueryByParam(@Param("queryParam") ActivityPackageManageQueryRequest activityPackageManageQueryRequest);

    int batchUpdateActivityStatusByCodes(@Param("activityPackageCodeList") Set<String> activityPackageCodeList, @Param("activityStatus") Integer skuState, @Param("operatorId") Long operatorId);

    Page<ActivityPackageInfoEntity> pageQueryByStoreIds(Page<ActivityPackageInfoEntity> entityPage, @Param("activityPackageName") String activityPackageName, @Param("storeIds") List<Long> storeIds, @Param("activityStatus") Integer activityStatus);
}
