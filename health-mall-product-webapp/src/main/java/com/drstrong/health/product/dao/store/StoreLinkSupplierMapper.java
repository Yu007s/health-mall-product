package com.drstrong.health.product.dao.store;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.drstrong.health.product.model.entity.store.StoreLinkSupplierEntity;
import com.drstrong.health.product.model.response.store.StoreInfoResponse;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author xieYueFeng
 * @Date 2022/07/30/9:51
 */
@Mapper
public interface StoreLinkSupplierMapper extends BaseMapper<StoreLinkSupplierEntity> {
    List<StoreInfoResponse> findStoreBySupplierId(Long supplierId);
}
