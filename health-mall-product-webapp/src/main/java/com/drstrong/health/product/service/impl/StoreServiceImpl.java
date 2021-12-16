package com.drstrong.health.product.service.impl;

import cn.strong.common.utils.CollectionUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.drstrong.health.product.dao.StoreMapper;
import com.drstrong.health.product.dao.StorePostageAreaMapper;
import com.drstrong.health.product.model.entity.store.StoreEntity;
import com.drstrong.health.product.model.entity.store.StorePostageAreaEntity;
import com.drstrong.health.product.model.enums.DelFlagEnum;
import com.drstrong.health.product.model.enums.ErrorEnums;
import com.drstrong.health.product.model.enums.StorePostageEnum;
import com.drstrong.health.product.model.enums.StoreStatusEnum;
import com.drstrong.health.product.model.request.store.AreaPostage;
import com.drstrong.health.product.model.request.store.StoreAddOrUpdateRequest;
import com.drstrong.health.product.model.request.store.StoreIdRequest;
import com.drstrong.health.product.model.request.store.StorePostage;
import com.drstrong.health.product.model.response.result.BusinessException;
import com.drstrong.health.product.model.response.store.StoreInfoResponse;
import com.drstrong.health.product.service.StorePostageAreaService;
import com.drstrong.health.product.service.StoreService;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author lsx
 * @projectName health-mall-product
 * @desc 店铺service
 * @createTime 2021/12/13 11:25
 */
@Service
@Slf4j
public class StoreServiceImpl implements StoreService {

    private static final String STORE_ADD_ACTION = "add";
    private static final String STORE_UPDATE_ACTION = "update";
    private static final Integer STORE_NAME_MIN_LENGTH = 0;
    private static final Integer STORE_NAME_MAX_LENGTH = 20;

    @Resource
    private StoreMapper storeMapper;
    @Resource
    private StorePostageAreaService storePostageAreaService;
    @Resource
    private StorePostageAreaMapper storePostageAreaMapper;

    @Override
    public List<StoreInfoResponse> queryAll() {
        LambdaQueryWrapper<StoreEntity> storeWrapper = new LambdaQueryWrapper<>();
        storeWrapper.eq(StoreEntity::getDelFlag, DelFlagEnum.UN_DELETED.getCode()).eq(StoreEntity::getStoreStatus, StoreStatusEnum.ENABLE.getCode());
        List<StoreEntity> storeEntities = storeMapper.selectList(storeWrapper);
        return buildResponse(storeEntities);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(StoreAddOrUpdateRequest storeAddOrUpdateRequest,Long userId) {
        String storeName = storeAddOrUpdateRequest.getName();
        checkStoreName(storeName);
        checkStoreNameRepeat(storeAddOrUpdateRequest,STORE_ADD_ACTION);
        StoreEntity storeSaveEntity = new StoreEntity();
        storeSaveEntity.setName(storeName);
        storeSaveEntity.setStoreStatus(storeAddOrUpdateRequest.getStatus());
        //TODO 店铺编码生成规则暂定
        storeSaveEntity.setCode("110");
        storeSaveEntity.setCreatedBy(userId);
        storeSaveEntity.setChangedBy(userId);
        storeMapper.insert(storeSaveEntity);
    }

    @Override
    public void update(StoreAddOrUpdateRequest storeAddOrUpdateRequest,Long userId) {
        String storeName = storeAddOrUpdateRequest.getName();
        checkStoreName(storeName);
        checkStoreNameRepeat(storeAddOrUpdateRequest,STORE_UPDATE_ACTION);
        StoreEntity storeUpdateEntity = new StoreEntity();
        storeUpdateEntity.setId(storeAddOrUpdateRequest.getStoreId());
        storeUpdateEntity.setStoreStatus(storeAddOrUpdateRequest.getStatus());
        storeUpdateEntity.setName(storeName);
        storeUpdateEntity.setChangedAt(LocalDateTime.now());
        storeUpdateEntity.setChangedBy(userId);
        storeMapper.updateById(storeUpdateEntity);
    }

    @Override
    public void disable(StoreIdRequest storeIdRequest, Long userId) {
        StoreEntity disEntity = new StoreEntity();
        disEntity.setId(storeIdRequest.getStoreId());
        disEntity.setChangedBy(userId);
        disEntity.setChangedAt(LocalDateTime.now());
        disEntity.setStoreStatus(StoreStatusEnum.DISABLE.getCode());
        storeMapper.updateById(disEntity);
    }

    @Override
    public StorePostage getPostage(Long storeId) {
        StorePostage storePostage = new StorePostage();
        StoreEntity storeEntity = checkStoreIdExist(storeId);
        storePostage.setFreePostage(storeEntity.getFreePostage());
        storePostage.setStoreId(storeEntity.getId());
        List<StorePostageAreaEntity> storePostageAreaEntities = storePostageAreaService.queryByStoreId(storeId);
        if(CollectionUtils.isEmpty(storePostageAreaEntities)){
           storePostage.setAreaPostageList(Collections.emptyList());
        }else {
            storePostage.setAreaPostageList(storePostageAreaEntities.stream().map(s -> {
                AreaPostage areaPostage = new AreaPostage();
                BeanUtils.copyProperties(s, areaPostage);
                return areaPostage;
            }).collect(Collectors.toList()));
        }
        return storePostage;
    }

    @Override
    public StoreEntity getByStoreId(Long storeId) {
        return selectByStoreId(storeId);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePostage(StorePostage storePostage,Long userId) {
        Long storeId = storePostage.getStoreId();
        StoreEntity storeEntity = checkStoreIdExist(storeId);
        storeEntity.setFreePostage(storePostage.getFreePostage());
        storeEntity.setChangedAt(LocalDateTime.now());
        storeEntity.setChangedBy(userId);
        storeEntity.setSetPostage(StorePostageEnum.HAS_SET.getCode());
        storeMapper.updateById(storeEntity);
        storePostageAreaService.deleteByStoreId(storeId,userId);
        List<AreaPostage> areaPostageList = storePostage.getAreaPostageList();
        List<StorePostageAreaEntity> collect = areaPostageList.stream().map(areaPostage -> {
            StorePostageAreaEntity storePostageAreaEntity = new StorePostageAreaEntity();
            BeanUtils.copyProperties(areaPostage, storePostageAreaEntity);
            storePostageAreaEntity.setStoreId(storeId);
            storePostageAreaEntity.setCreatedBy(userId);
            storePostageAreaEntity.setChangedBy(userId);
            return storePostageAreaEntity;
        }).collect(Collectors.toList());
        storePostageAreaMapper.batchInsert(collect);
    }

    /**
     * 根据店铺集合 获取已设置邮费的店铺
     * @param storeIds
     * @return
     */
    @Override
    public List<StoreEntity> querySetPostageByStoreIds(List<Long> storeIds) {
        LambdaQueryWrapper<StoreEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StoreEntity::getDelFlag, DelFlagEnum.UN_DELETED.getCode())
                .eq(StoreEntity::getStoreStatus, StoreStatusEnum.ENABLE.getCode())
                .eq(StoreEntity::getSetPostage,StorePostageEnum.HAS_SET)
                .in(StoreEntity::getId,storeIds);
        return storeMapper.selectList(queryWrapper);
    }

    private StoreEntity selectByStoreId(Long storeId) {
        LambdaQueryWrapper<StoreEntity> storeWrapper = new LambdaQueryWrapper<>();
        storeWrapper.eq(StoreEntity::getId, storeId)
                .eq(StoreEntity::getDelFlag, DelFlagEnum.UN_DELETED.getCode())
                .eq(StoreEntity::getStoreStatus, StoreStatusEnum.ENABLE.getCode());
        return storeMapper.selectOne(storeWrapper);
    }

    private StoreEntity checkStoreIdExist(Long storeId) {
        if (Objects.isNull(storeId)) {
            throw new BusinessException(ErrorEnums.PARAM_IS_NOT_NULL);
        }
        StoreEntity storeEntity = selectByStoreId(storeId);
        if (Objects.isNull(storeEntity)) {
            throw new BusinessException(ErrorEnums.STORE_NOT_EXIST);
        }
        return storeEntity;
    }

    /**
     * 店铺添加、编辑操作检验名称重复
     * @param storeAddOrUpdateRequest
     * @param action
     */
    private void checkStoreNameRepeat(StoreAddOrUpdateRequest storeAddOrUpdateRequest,String action) {
        String storeName = storeAddOrUpdateRequest.getName();
        LambdaQueryWrapper<StoreEntity> storeWrapper = new LambdaQueryWrapper<>();
        if(STORE_ADD_ACTION.equals(action)){
            storeWrapper.eq(StoreEntity::getName, storeName).eq(StoreEntity::getDelFlag, DelFlagEnum.UN_DELETED.getCode());
        }
        if(STORE_UPDATE_ACTION.equals(action)){
            storeWrapper.eq(StoreEntity::getName, storeName).eq(StoreEntity::getDelFlag, DelFlagEnum.UN_DELETED.getCode()).ne(StoreEntity::getId,storeAddOrUpdateRequest.getStoreId());
        }
        StoreEntity storeEntity = storeMapper.selectOne(storeWrapper);
        if(Objects.nonNull(storeEntity)){
           throw new BusinessException(ErrorEnums.STORE_NAME_REPEAT);
        }
    }

    /**
     * 校验店铺名称长度
     * @param name
     */
    private void checkStoreName(String name) {
        if(name.length() <= STORE_NAME_MIN_LENGTH || name.length() > STORE_NAME_MAX_LENGTH){
           throw new BusinessException(ErrorEnums.STORE_NAME_LENGTH);
        }
    }

    /**
     * 转换店铺返回实体
     * @param storeEntities
     * @return
     */
    private List<StoreInfoResponse> buildResponse(List<StoreEntity> storeEntities) {
       if(CollectionUtils.isEmpty(storeEntities)){
          return Collections.emptyList();
       }
       List<StoreInfoResponse> responses = Lists.newArrayListWithCapacity(storeEntities.size());
       storeEntities.forEach(s -> {
         StoreInfoResponse storeInfoResponse = new StoreInfoResponse();
         storeInfoResponse.setStoreId(s.getId());
         storeInfoResponse.setStoreCode(s.getCode());
         storeInfoResponse.setFreePostage(s.getFreePostage());
         storeInfoResponse.setSkuCount(s.getProductCount());
         storeInfoResponse.setStoreName(s.getName());
         storeInfoResponse.setStoreStatus(s.getStoreStatus());
         responses.add(storeInfoResponse);
       });
       return responses;
    }
}
