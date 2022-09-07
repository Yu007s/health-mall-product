package com.drstrong.health.product.service.store.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.drstrong.health.product.dao.store.StoreLinkSupplierMapper;
import com.drstrong.health.product.model.entity.store.StoreLinkSupplierEntity;
import com.drstrong.health.product.model.enums.DelFlagEnum;
import com.drstrong.health.product.model.response.store.StoreInfoResponse;
import com.drstrong.health.product.service.store.StoreLinkSupplierService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author xieYueFeng
 * @Date 2022/08/04/14:42
 */
@Service
public class StoreLinkSupplierServiceImpl extends ServiceImpl<StoreLinkSupplierMapper, StoreLinkSupplierEntity> implements StoreLinkSupplierService {
    @Resource
    StoreLinkSupplierMapper storeLinkSupplierMapper;

    @Override
    public void saveBatch(List<StoreLinkSupplierEntity> linkSupplierEntities) {
        super.saveBatch(linkSupplierEntities);
    }

    @Override
    public List<StoreLinkSupplierEntity> queryByStoreId(Long storeId) {
        LambdaQueryWrapper<StoreLinkSupplierEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(StoreLinkSupplierEntity::getStoreId, StoreLinkSupplierEntity::getSupplierId)
                .eq(StoreLinkSupplierEntity::getStoreId, storeId).eq(StoreLinkSupplierEntity::getDelFlag, DelFlagEnum.UN_DELETED.getCode());
        return super.list(queryWrapper);
    }

    @Override
    public List<StoreInfoResponse> queryBySupplierId(Long supplierId) {
        return storeLinkSupplierMapper.findStoreBySupplierId(supplierId);
    }

}
