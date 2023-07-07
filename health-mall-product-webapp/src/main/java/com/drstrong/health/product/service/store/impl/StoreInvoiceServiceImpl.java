package com.drstrong.health.product.service.store.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.drstrong.health.product.dao.store.StoreInvoiceMapper;

import com.drstrong.health.product.model.entity.store.StoreInvoiceEntity;
import com.drstrong.health.product.model.enums.DelFlagEnum;
import com.drstrong.health.product.service.store.StoreInvoiceService;
import org.springframework.stereotype.Service;

/**
 * @Author xieYueFeng
 * @Date 2022/08/02/16:44
 */
@Service
public class StoreInvoiceServiceImpl extends ServiceImpl<StoreInvoiceMapper, StoreInvoiceEntity> implements StoreInvoiceService {

    @Override
    public StoreInvoiceEntity getByStoreId(Long storeId) {
        if (storeId == null) {
            return null;
        }
        LambdaQueryWrapper<StoreInvoiceEntity> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(StoreInvoiceEntity::getStoreId, storeId)
                .eq(StoreInvoiceEntity::getDelFlag, DelFlagEnum.UN_DELETED.getCode());
        return getOne(lambdaQueryWrapper);
    }

    @Override
    public void removeByStoreId(Long storeId) {
        if (storeId ==  null) {
            return;
        }
        LambdaUpdateWrapper<StoreInvoiceEntity> lambdaQueryWrapper = new LambdaUpdateWrapper<>();
        lambdaQueryWrapper.eq(StoreInvoiceEntity::getStoreId,storeId)
                .set(StoreInvoiceEntity::getDelFlag, DelFlagEnum.IS_DELETED.getCode());
        update(lambdaQueryWrapper);
    }

    @Override
    public void updateByStoreId(Long storeId, StoreInvoiceEntity storeInvoiceEntity) {
        LambdaQueryWrapper<StoreInvoiceEntity> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(StoreInvoiceEntity::getStoreId, storeId).eq(StoreInvoiceEntity::getDelFlag, DelFlagEnum.UN_DELETED.getCode());
        update(storeInvoiceEntity, lambdaQueryWrapper);
    }
}
