package com.drstrong.health.product.service.store.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.drstrong.health.product.dao.store.StoreMapper;
import com.drstrong.health.product.model.entity.store.StoreEntity;
import com.drstrong.health.product.model.entity.store.StoreInvoiceEntity;
import com.drstrong.health.product.model.enums.DelFlagEnum;
import com.drstrong.health.product.model.request.store.StoreInfoDetailSaveRequest;
import com.drstrong.health.product.model.request.store.StoreSearchRequest;
import com.drstrong.health.product.model.response.store.StoreInfoEditResponse;
import com.drstrong.health.product.model.response.store.StoreInfoResponse;
import com.drstrong.health.product.service.store.AgencyService;
import com.drstrong.health.product.service.store.StoreInvoiceService;
import com.drstrong.health.product.service.store.StoreService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Author xieYueFeng
 * @Date 2022/07/30/9:24
 */
@Service
public class StoreServiceImpl extends ServiceImpl<StoreMapper, StoreEntity> implements StoreService {
    @Resource
    StoreMapper storeMapper;
    @Resource
    AgencyService agencyService;
    @Resource
    StoreInvoiceService storeInvoiceService;
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(StoreInfoDetailSaveRequest store, String userId) {
        StoreEntity storeEntity = new StoreEntity();
        storeEntity.setStoreName(store.getName());
        StoreInvoiceEntity invoice = BeanUtil.copyProperties(store, StoreInvoiceEntity.class);
        if (store.getId() == null) {
            storeEntity.setStoreType(store.getStoreTypeName().ordinal());
            storeEntity.setCreatedBy(userId);
            storeEntity.setChangedBy(userId);
            storeMapper.insert(storeEntity);
            storeInvoiceService.save(invoice);
        }else{
            invoice.setStoreId(store.getId());
            storeEntity.setChangedBy(userId);
            storeMapper.updateById(storeEntity);
        }
    }
    @Override
    public List<StoreInfoResponse> query(StoreSearchRequest storeSearchRequest) {
        LambdaQueryWrapper<StoreEntity> storeEntityQueryWrapper  = new LambdaQueryWrapper<>();
        storeEntityQueryWrapper.select()
                .eq(storeSearchRequest.getStoreId()!= null,StoreEntity::getId, storeSearchRequest.getStoreId())
                .like(storeSearchRequest.getStoreName()!= null,StoreEntity::getStoreName, storeSearchRequest.getStoreName())
                .eq(storeSearchRequest.getStoreType()!=null,StoreEntity::getStoreType, storeSearchRequest.getStoreType().ordinal())
                .eq(storeSearchRequest.getAgencyId()!= null,StoreEntity::getAgencyId, storeSearchRequest.getAgencyId())
                .eq(StoreEntity::getDelFlag, DelFlagEnum.UN_DELETED.getCode());
        List<StoreEntity> storeEntities = storeMapper.selectList(storeEntityQueryWrapper);
        return storeEntities.stream().map(storeEntity -> {
            StoreInfoResponse storeInfoResponse = BeanUtil.copyProperties(storeEntity, StoreInfoResponse.class);
            String name = agencyService.id2name(storeEntity.getAgencyId());
            storeInfoResponse.setAgencyName(name);
            return storeInfoResponse;
        }).collect(Collectors.toList());
    }


    /**
     * 根据店铺id集合查询店铺信息
     *
     * @param storeIds 店铺id集合
     * @return 店铺信息集合
     * @author liuqiuyi
     * @date 2022/8/1 15:26
     */
    @Override
    public List<StoreEntity> listByIds(Set<Long> storeIds) {
        if (CollectionUtils.isEmpty(storeIds)) {
            return null;
        }
        LambdaQueryWrapper<StoreEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StoreEntity::getDelFlag, DelFlagEnum.UN_DELETED.getCode())
                .in(StoreEntity::getId, storeIds);
        return list(queryWrapper);
    }

    @Override
    @Transactional(readOnly = true)
    public StoreInfoEditResponse queryById(Long storeId) {
        StoreEntity storeEntity = storeMapper.selectById(storeId);
        Long storeEntityId = storeEntity.getId();
        //根据店铺id 查询发票信息  组合后返回
        StoreInvoiceEntity invoice = storeInvoiceService.getByStoreId(storeEntityId);
        return buildStoreInfoResponse(invoice, storeEntity);
    }

    private StoreInfoEditResponse buildStoreInfoResponse(StoreInvoiceEntity storeInvoiceEntity,StoreEntity storeEntity){
        StoreInfoEditResponse storeInfoEditResponse = BeanUtil.copyProperties(storeInvoiceEntity, StoreInfoEditResponse.class);
        BeanUtil.copyProperties(storeEntity,storeInfoEditResponse);
        return storeInfoEditResponse;
    }
}
