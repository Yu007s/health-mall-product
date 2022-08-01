package com.drstrong.health.product.service.zStore.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.drstrong.health.product.dao.zStore.ZstoreMapper;
import com.drstrong.health.product.model.entity.zStore.StoreEntity;
import com.drstrong.health.product.model.enums.DelFlagEnum;
import com.drstrong.health.product.model.request.zStore.StoreRequest;
import com.drstrong.health.product.model.response.zStore.StoreInfoDetailVO;
import com.drstrong.health.product.model.response.zStore.StoreInfoResponse;
import com.drstrong.health.product.service.zStore.AgencyService;
import com.drstrong.health.product.service.zStore.StoreService;
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
@Service("zStoreService")
public class StoreServiceImpl extends ServiceImpl<ZstoreMapper, StoreEntity> implements StoreService {
    @Resource
    ZstoreMapper zstoreMapper;
    @Resource
    AgencyService agencyService;

    @Override
    @Transactional(readOnly = true)
    public void save(StoreInfoDetailVO store, String userId) {
//        StoreEntity storeEntity = BeanUtil.copyProperties(store, StoreEntity.class);
        StoreEntity storeEntity = new StoreEntity();
        storeEntity.setName(store.getName());
        storeEntity.setStoreType(store.getStoreType().ordinal());
        if (store.getId() == null) {
            storeEntity.setCreatedBy(userId);
            storeEntity.setChangedBy(userId);
            zstoreMapper.insert(storeEntity);
        }else{
            storeEntity.setChangedBy(userId);
            zstoreMapper.updateById(storeEntity);
        }
    }

    @Override
    public List<StoreInfoResponse> query(StoreRequest storeRequest) {
        LambdaQueryWrapper<StoreEntity> storeEntityQueryWrapper  = new LambdaQueryWrapper<>();
        storeEntityQueryWrapper.select()
                .eq(storeRequest.getStoreId()!= null,StoreEntity::getId,storeRequest.getStoreId())
                .like(storeRequest.getStoreName()!= null,StoreEntity::getName,storeRequest.getStoreName())
                .eq(storeRequest.getStoreType()!=null,StoreEntity::getStoreType,storeRequest.getStoreType().ordinal())
                .eq(storeRequest.getAgencyId()!= null,StoreEntity::getAgencyId,storeRequest.getAgencyId())
                .eq(StoreEntity::getDelFlag, DelFlagEnum.UN_DELETED.getCode());
        List<StoreEntity> storeEntities = zstoreMapper.selectList(storeEntityQueryWrapper);
        return storeEntities.stream().map(storeEntity -> {
            StoreInfoResponse storeInfoResponse = BeanUtil.copyProperties(storeEntity, StoreInfoResponse.class);
            String name = agencyService.id2name(storeEntity.getAgencyId());
            storeInfoResponse.setAgencyName(name);
            return storeInfoResponse;
        }).collect(Collectors.toList());
    }

    @Override
    public StoreInfoDetailVO queryById(Long storeId) {
        return null;
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
}
