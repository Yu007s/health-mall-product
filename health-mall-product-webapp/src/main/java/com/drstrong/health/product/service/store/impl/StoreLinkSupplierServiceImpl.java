package com.drstrong.health.product.service.store.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.drstrong.health.product.dao.store.StoreLinkSupplierMapper;
import com.drstrong.health.product.model.entity.store.StoreLinkSupplierEntity;
import com.drstrong.health.product.model.enums.DelFlagEnum;
import com.drstrong.health.product.model.response.store.StoreInfoResponse;
import com.drstrong.health.product.service.store.StoreLinkSupplierService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author xieYueFeng
 * @Date 2022/08/04/14:42
 */
@Service
public class StoreLinkSupplierServiceImpl extends ServiceImpl<StoreLinkSupplierMapper, StoreLinkSupplierEntity> implements StoreLinkSupplierService {

    @Override
    public void saveBatch(List<StoreLinkSupplierEntity> linkSupplierEntities) {
        super.saveBatch(linkSupplierEntities);
    }

    @Override
    public List<StoreLinkSupplierEntity> queryByStoreId(Long storeId) {
        LambdaQueryWrapper<StoreLinkSupplierEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(StoreLinkSupplierEntity::getStoreId,StoreLinkSupplierEntity::getSupplierId)
                .eq(StoreLinkSupplierEntity::getStoreId,storeId).eq(StoreLinkSupplierEntity::getDelFlag, DelFlagEnum.UN_DELETED);
        return super.list(queryWrapper);
    }

    @Override
    public List<StoreInfoResponse> queryBySupplierId(Long supplierId) {
        LambdaQueryWrapper<StoreLinkSupplierEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(StoreLinkSupplierEntity::getStoreId,StoreLinkSupplierEntity::getSupplierId)
                .eq(StoreLinkSupplierEntity::getSupplierId,supplierId)
                .eq(StoreLinkSupplierEntity::getDelFlag, DelFlagEnum.UN_DELETED).groupBy(StoreLinkSupplierEntity::getStoreId);
        List<StoreLinkSupplierEntity> list = super.list(queryWrapper);
//        list.stream().map( storeLinkSupplierEntity -> {
//
//        })
        return null;
    }

}
