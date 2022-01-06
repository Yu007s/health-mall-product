package com.drstrong.health.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.drstrong.health.product.dao.StorePostageAreaMapper;
import com.drstrong.health.product.model.entity.store.StorePostageAreaEntity;
import com.drstrong.health.product.model.enums.DelFlagEnum;
import com.drstrong.health.product.service.StorePostageAreaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * @author lsx
 * @projectName health-mall-product
 * @desc 店铺service
 * @createTime 2021/12/13 11:25
 */
@Service
@Slf4j
public class StorePostageAreaServiceImpl implements StorePostageAreaService {



    @Resource
    private StorePostageAreaMapper storePostageAreaMapper;


    @Override
    public List<StorePostageAreaEntity> queryByStoreId(Long storeId) {
        LambdaQueryWrapper<StorePostageAreaEntity> storePostageAreaEntityWrapper = new LambdaQueryWrapper<>();
        storePostageAreaEntityWrapper.eq(StorePostageAreaEntity::getStoreId,storeId)
                .eq(StorePostageAreaEntity::getDelFlag, DelFlagEnum.UN_DELETED.getCode());
        return storePostageAreaMapper.selectList(storePostageAreaEntityWrapper);
    }

    @Override
    public List<StorePostageAreaEntity> queryByStoreIdsAndAreaName(Set<Long> storeIds, String areaName) {
        LambdaQueryWrapper<StorePostageAreaEntity> storePostageAreaEntityWrapper = new LambdaQueryWrapper<>();
        storePostageAreaEntityWrapper.in(StorePostageAreaEntity::getStoreId,storeIds)
                .eq(StorePostageAreaEntity::getDelFlag, DelFlagEnum.UN_DELETED.getCode())
                .eq(StorePostageAreaEntity::getAreaName,areaName);
        return storePostageAreaMapper.selectList(storePostageAreaEntityWrapper);
    }

    @Override
    public void deleteByStoreId(Long storeId,String userId) {
        StorePostageAreaEntity deleteEntity = new StorePostageAreaEntity();
        deleteEntity.setChangedAt(LocalDateTime.now());
        deleteEntity.setChangedBy(userId);
        deleteEntity.setDelFlag(DelFlagEnum.IS_DELETED.getCode());
        LambdaUpdateWrapper<StorePostageAreaEntity> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(StorePostageAreaEntity::getStoreId,storeId)
                .eq(StorePostageAreaEntity::getDelFlag,DelFlagEnum.UN_DELETED.getCode());
        storePostageAreaMapper.update(deleteEntity,updateWrapper);
    }
}
