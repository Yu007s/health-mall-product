package com.drstrong.health.product.service.productstore.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.drstrong.health.product.config.MqTopicConfig;
import com.drstrong.health.product.dao.productstore.ProductStoreMapper;
import com.drstrong.health.product.dao.productstore.StorePostageAreaMapper;
import com.drstrong.health.product.model.entity.productstore.StoreEntity;
import com.drstrong.health.product.model.entity.productstore.StorePostageAreaEntity;
import com.drstrong.health.product.model.enums.*;
import com.drstrong.health.product.model.request.productstore.AreaPostage;
import com.drstrong.health.product.model.request.productstore.StoreAddOrUpdateRequest;
import com.drstrong.health.product.model.request.productstore.StoreIdRequest;
import com.drstrong.health.product.model.request.productstore.StorePostage;
import com.drstrong.health.product.model.response.result.BusinessException;
import com.drstrong.health.product.model.response.productstore.StoreInfoResponse;
import com.drstrong.health.product.mq.model.product.StoreChangeEvent;
import com.drstrong.health.product.mq.model.product.StoreChangeTypeEnum;
import com.drstrong.health.product.remote.model.StorePostageDTO;
import com.drstrong.health.product.service.product.ProductSkuService;
import com.drstrong.health.product.service.productstore.StorePostageAreaService;
import com.drstrong.health.product.service.productstore.ProductStoreService;
import com.drstrong.health.product.util.BigDecimalUtil;
import com.drstrong.health.product.utils.MqMessageUtil;
import com.drstrong.health.redis.utils.RedisUtils;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author lsx
 * @projectName health-mall-product
 * @desc 店铺service
 * @createTime 2021/12/13 11:25
 */
@Service
@Slf4j
public class ProductStoreServiceImpl implements ProductStoreService {

    private static final String STORE_ADD_ACTION = "add";
    private static final String STORE_UPDATE_ACTION = "update";
    private static final Integer STORE_NAME_MIN_LENGTH = 0;
    private static final Integer STORE_NAME_MAX_LENGTH = 20;
    private static final String STORE_NAME_PREFIX = "mall:product:store:";
    private static final String STORE_CODE_KEY = "mall:product:store:code";

    @Resource
    private ProductStoreMapper productStoreMapper;
    @Resource
    private StorePostageAreaService storePostageAreaService;
    @Resource
    private StorePostageAreaMapper storePostageAreaMapper;
    @Resource
    private RedisUtils redisUtils;
    @Resource
    private ProductSkuService productSkuService;
    @Resource
    private MqMessageUtil mqMessageUtil;
    @Resource
    private MqTopicConfig mqTopicConfig;

    @Override
    public List<StoreInfoResponse> queryAll() {
        LambdaQueryWrapper<StoreEntity> storeWrapper = new LambdaQueryWrapper<>();
        storeWrapper.eq(StoreEntity::getDelFlag, DelFlagEnum.UN_DELETED.getCode());
        List<StoreEntity> storeEntities = productStoreMapper.selectList(storeWrapper);
        return buildResponse(storeEntities);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(StoreAddOrUpdateRequest storeAddOrUpdateRequest,String userId) {
        String storeName = storeAddOrUpdateRequest.getName();
        checkStoreName(storeName);
        checkStoreNameRepeat(storeAddOrUpdateRequest,STORE_ADD_ACTION);
        StoreEntity storeSaveEntity = new StoreEntity();
        storeSaveEntity.setName(storeName);
        storeSaveEntity.setStoreStatus(storeAddOrUpdateRequest.getStatus());
        storeSaveEntity.setCode("" + redisUtils.incr(STORE_CODE_KEY,1));
        storeSaveEntity.setCreatedBy(userId);
        storeSaveEntity.setChangedBy(userId);
        productStoreMapper.insert(storeSaveEntity);
        redisUtils.set(STORE_NAME_PREFIX + storeSaveEntity.getId(),storeSaveEntity.getName());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(StoreAddOrUpdateRequest storeAddOrUpdateRequest,String userId) {
        String storeName = storeAddOrUpdateRequest.getName();
        checkStoreName(storeName);
        checkStoreNameRepeat(storeAddOrUpdateRequest,STORE_UPDATE_ACTION);
        StoreEntity storeUpdateEntity = new StoreEntity();
        storeUpdateEntity.setId(storeAddOrUpdateRequest.getStoreId());
        storeUpdateEntity.setStoreStatus(storeAddOrUpdateRequest.getStatus());
        storeUpdateEntity.setName(storeName);
        storeUpdateEntity.setChangedAt(LocalDateTime.now());
        storeUpdateEntity.setChangedBy(userId);
        productStoreMapper.updateById(storeUpdateEntity);
        redisUtils.set(STORE_NAME_PREFIX + storeAddOrUpdateRequest.getStoreId(),storeName);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateState(StoreIdRequest storeIdRequest, String userId) {
        StoreEntity disEntity = new StoreEntity();
        disEntity.setId(storeIdRequest.getStoreId());
        disEntity.setChangedBy(userId);
        disEntity.setChangedAt(LocalDateTime.now());
        disEntity.setStoreStatus(storeIdRequest.getStoreStatus());
        productStoreMapper.updateById(disEntity);
    }

    @Override
    public StorePostage getPostage(Long storeId) {
        StorePostage storePostage = new StorePostage();
        StoreEntity storeEntity = checkStoreIdExist(storeId);
        storePostage.setFreePostage(BigDecimalUtil.F2Y(storeEntity.getFreePostage().longValue()));
        storePostage.setStoreId(storeEntity.getId());
        List<StorePostageAreaEntity> storePostageAreaEntities = storePostageAreaService.queryByStoreId(storeId);
        if(CollectionUtils.isEmpty(storePostageAreaEntities)){
           storePostage.setAreaPostageList(Collections.emptyList());
        }else {
            storePostage.setAreaPostageList(storePostageAreaEntities.stream().map(s -> {
                AreaPostage areaPostage = new AreaPostage();
                BeanUtils.copyProperties(s, areaPostage);
                areaPostage.setPostage(BigDecimalUtil.F2Y(s.getPostage().longValue()));
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
    public void updatePostage(StorePostage storePostage,String userId) {
        Long storeId = storePostage.getStoreId();
        StoreEntity storeEntity = checkStoreIdExist(storeId);
        storeEntity.setFreePostage(BigDecimalUtil.Y2F(storePostage.getFreePostage()).intValue());
        storeEntity.setChangedAt(LocalDateTime.now());
        storeEntity.setChangedBy(userId);
        storeEntity.setSetPostage(StorePostageEnum.HAS_SET.getCode());
        productStoreMapper.updateById(storeEntity);
        storePostageAreaService.deleteByStoreId(storeId,userId);
        List<AreaPostage> areaPostageList = storePostage.getAreaPostageList();
        List<StorePostageAreaEntity> collect = areaPostageList.stream().map(areaPostage -> {
            StorePostageAreaEntity storePostageAreaEntity = new StorePostageAreaEntity();
            BeanUtils.copyProperties(areaPostage, storePostageAreaEntity);
            storePostageAreaEntity.setPostage(BigDecimalUtil.Y2F(areaPostage.getPostage()).intValue());
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
                .eq(StoreEntity::getSetPostage,StorePostageEnum.HAS_SET.getCode())
                .in(StoreEntity::getId,storeIds);
        return productStoreMapper.selectList(queryWrapper);
    }

    @Override
    public List<StorePostageDTO> getStorePostageByIds(Set<Long> storeIds, String areaName) {
        if(CollectionUtils.isEmpty(storeIds)){
           return Collections.emptyList();
        }
        List<StoreEntity> storeEntities = selectByStoreIds(storeIds);
        List<StorePostageAreaEntity> storePostageAreaEntities = storePostageAreaService.queryByStoreIdsAndAreaName(storeIds, areaName);
        Map<Long, Integer> map = storePostageAreaEntities.stream().collect(Collectors.toMap(StorePostageAreaEntity::getStoreId, StorePostageAreaEntity::getPostage, (v1, v2) -> v1));
        return storeEntities.stream().map(s -> {
            StorePostageDTO storePostageDTO = new StorePostageDTO();
            storePostageDTO.setStoreId(s.getId());
            storePostageDTO.setStoreName(s.getName());
            storePostageDTO.setFreePostage(BigDecimalUtil.F2Y(s.getFreePostage().longValue()));
            storePostageDTO.setPostage(BigDecimalUtil.F2Y(map.getOrDefault(s.getId(),0).longValue()));
            return storePostageDTO;
        }).collect(Collectors.toList());
    }

    @Override
    public List<StoreInfoResponse> queryByStoreIds(Set<Long> storeIds) {
        if(CollectionUtils.isEmpty(storeIds)){
           return Collections.emptyList();
        }
        return buildResponse(selectByStoreIds(storeIds));
    }

    /**
     * 增加或者减少 店铺的商品数量
     *
     * @param storeId     店铺id
     * @param count       要累加的商品数量
     * @param operateEnum 操作类型
     * @author liuqiuyi
     * @date 2021/12/28 11:08
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addOrReduceProductNumById(Long storeId, Integer count, CategoryProductNumOperateEnum operateEnum) {
        if (Objects.isNull(storeId) || Objects.isNull(operateEnum)) {
            log.error("StoreServiceImpl.addOrReduceProductNumById param is null");
            return;
        }
        if (Objects.isNull(count)) {
            count = 1;
        }
        // 查询店铺
        StoreEntity storeEntity = productStoreMapper.selectById(storeId);
        if (Objects.isNull(storeEntity)) {
            throw new BusinessException(ErrorEnums.STORE_NOT_EXIST);
        }
        // 如果是增加操作
        if (Objects.equals(CategoryProductNumOperateEnum.ADD, operateEnum)) {
            storeEntity.setProductCount(storeEntity.getProductCount() + count);
        } else {
            int num = storeEntity.getProductCount() - count;
            if (num < 0) {
                num = 0;
            }
            storeEntity.setProductCount(num);
        }
        storeEntity.setChangedAt(LocalDateTime.now());
        productStoreMapper.updateById(storeEntity);
    }

    /**
     * 发送店铺变更的事件
     *
     * @param storeId 店铺 id
     * @param userId  操作人 id
     * @author liuqiuyi
     * @date 2021/12/30 11:03
     */
    @Override
    public void sendStoreChangeEvent(Long storeId, String userId, StoreChangeTypeEnum storeChangeTypeEnum) {
        if (Objects.isNull(storeId) || StringUtils.isBlank(userId)) {
            log.error("invoke StoreServiceImpl.sendStoreChangeEvent error,param is null. param:{},{}", storeId, userId);
            return;
        }
        StoreChangeEvent changeEvent = new StoreChangeEvent();
        changeEvent.setOperatorId(userId);
        changeEvent.setStoreId(storeId);
        changeEvent.setStoreChangeTypeEnum(storeChangeTypeEnum);
        mqMessageUtil.sendMsg(mqTopicConfig.getStoreChangeTopic(), mqTopicConfig.getStoreChangeTag(), changeEvent);
    }

    private StoreEntity selectByStoreId(Long storeId) {
        LambdaQueryWrapper<StoreEntity> storeWrapper = new LambdaQueryWrapper<>();
        storeWrapper.eq(StoreEntity::getId, storeId)
                .eq(StoreEntity::getDelFlag, DelFlagEnum.UN_DELETED.getCode())
                .eq(StoreEntity::getStoreStatus, StoreStatusEnum.ENABLE.getCode());
        return productStoreMapper.selectOne(storeWrapper);
    }

    private List<StoreEntity> selectByStoreIds(Set<Long> storeIds) {
        LambdaQueryWrapper<StoreEntity> storeWrapper = new LambdaQueryWrapper<>();
        storeWrapper.in(StoreEntity::getId, storeIds)
                .eq(StoreEntity::getDelFlag, DelFlagEnum.UN_DELETED.getCode())
                .eq(StoreEntity::getStoreStatus, StoreStatusEnum.ENABLE.getCode());
        return productStoreMapper.selectList(storeWrapper);
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
        StoreEntity storeEntity = productStoreMapper.selectOne(storeWrapper);
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
        List<Long> storeIds = storeEntities.stream().map(StoreEntity::getId).collect(Collectors.toList());
        if(CollectionUtils.isEmpty(storeIds)){
           return Collections.emptyList();
        }
        Map<Long, Integer> storeCountMap = productSkuService.searchSkuCountMap(storeIds);
        if(CollectionUtils.isEmpty(storeEntities)){
          return Collections.emptyList();
       }
       List<StoreInfoResponse> responses = Lists.newArrayListWithCapacity(storeEntities.size());
       storeEntities.forEach(s -> {
         StoreInfoResponse storeInfoResponse = new StoreInfoResponse();
         storeInfoResponse.setStoreId(s.getId());
         storeInfoResponse.setStoreCode(s.getCode());
         storeInfoResponse.setFreePostage(BigDecimalUtil.F2Y(s.getFreePostage().longValue()));
         storeInfoResponse.setSkuCount(storeCountMap.get(s.getId()));
         storeInfoResponse.setStoreName(s.getName());
         storeInfoResponse.setStoreStatus(s.getStoreStatus());
         responses.add(storeInfoResponse);
       });
       return responses;
    }
}
