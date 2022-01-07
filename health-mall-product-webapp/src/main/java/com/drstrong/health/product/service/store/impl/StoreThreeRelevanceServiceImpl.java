package com.drstrong.health.product.service.store.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.drstrong.health.product.dao.store.StoreThreeRelevanceMapper;
import com.drstrong.health.product.model.entity.product.ProductSkuEntity;
import com.drstrong.health.product.model.entity.store.StoreEntity;
import com.drstrong.health.product.model.entity.store.StoreSkuEntity;
import com.drstrong.health.product.model.entity.store.StoreThreeRelevanceEntity;
import com.drstrong.health.product.model.enums.DelFlagEnum;
import com.drstrong.health.product.model.enums.ErrorEnums;
import com.drstrong.health.product.model.enums.ProductStateEnum;
import com.drstrong.health.product.model.request.store.RelevanceThreeRequest;
import com.drstrong.health.product.model.request.store.StoreSkuRequest;
import com.drstrong.health.product.model.request.store.UpdateSkuRequest;
import com.drstrong.health.product.model.request.store.UpdateThreeRequest;
import com.drstrong.health.product.model.response.PageVO;
import com.drstrong.health.product.model.response.result.BusinessException;
import com.drstrong.health.product.model.response.store.StoreSkuResponse;
import com.drstrong.health.product.model.response.store.ThreeSkuInfoResponse;
import com.drstrong.health.product.mq.model.SkuStateStockMqEvent;
import com.drstrong.health.product.service.product.ProductBasicsInfoService;
import com.drstrong.health.product.service.product.ProductSkuService;
import com.drstrong.health.product.service.store.StoreService;
import com.drstrong.health.product.service.store.StoreThreeRelevanceService;
import com.drstrong.health.product.util.BigDecimalUtil;
import com.drstrong.health.product.utils.MqMessageUtil;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author lsx
 * @projectName health-mall-product
 * @desc 三方药店关联商品服务
 * @createTime 2021/12/14 21:10
 * @since TODO
 */
@Service
@Slf4j
public class StoreThreeRelevanceServiceImpl implements StoreThreeRelevanceService {

    @Resource
    private StoreThreeRelevanceMapper storeThreeRelevanceMapper;
    @Resource
    private ProductSkuService productSkuService;
    @Resource
    private ProductBasicsInfoService productBasicsInfoService;
    @Resource
    private StoreService storeService;
    @Resource
    private MqMessageUtil mqMessageUtil;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePurchasePrice(UpdateThreeRequest updateThreeRequest, String userId) {
        checkRelevanceSingle(updateThreeRequest.getSkuId());
        StoreThreeRelevanceEntity storeThreeRelevanceEntity = new StoreThreeRelevanceEntity();
        storeThreeRelevanceEntity.setThreePurchasePrice(BigDecimalUtil.Y2F(updateThreeRequest.getPurchasePrice()).intValue());
        storeThreeRelevanceEntity.setChangedBy(userId);
        storeThreeRelevanceEntity.setChangedAt(LocalDateTime.now());
        LambdaUpdateWrapper<StoreThreeRelevanceEntity> updateWrapper = new LambdaUpdateWrapper();
        updateWrapper.eq(StoreThreeRelevanceEntity::getThreeSkuId,updateThreeRequest.getThreeSkuId())
                .eq(StoreThreeRelevanceEntity::getSkuId,updateThreeRequest.getSkuId());
        storeThreeRelevanceMapper.update(storeThreeRelevanceEntity,updateWrapper);
    }

    @Override
    public PageVO<StoreSkuResponse> pageSkuList(StoreSkuRequest storeSkuRequest) {
        Long storeId = storeSkuRequest.getStoreId();
        if(Objects.isNull(storeId)){
          throw new BusinessException(ErrorEnums.PARAM_IS_NOT_NULL);
        }
        Page<StoreSkuEntity> page = new Page<>(storeSkuRequest.getPageNo(),storeSkuRequest.getPageSize());
        QueryWrapper<StoreSkuEntity> queryWrapper = buildSkuQuery(storeSkuRequest, storeId);
        List<StoreSkuEntity> storeSkuEntities =  storeThreeRelevanceMapper.pageSkuList(page,queryWrapper);
        List<StoreSkuResponse> storeSkuResponses = convetResponse(storeSkuEntities);
        if(CollectionUtils.isEmpty(storeSkuResponses)){
            return PageVO.newBuilder().pageNo(storeSkuRequest.getPageNo()).pageSize(storeSkuRequest.getPageSize()).totalCount(0).result(Lists.newArrayList()).build();
        }
        return PageVO.newBuilder().pageNo(storeSkuRequest.getPageNo()).pageSize(storeSkuRequest.getPageSize()).totalCount((int) page.getTotal()).result(storeSkuResponses).build();
    }

    private QueryWrapper<StoreSkuEntity> buildSkuQuery(StoreSkuRequest storeSkuRequest, Long storeId) {
        QueryWrapper<StoreSkuEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("p.del_flag", DelFlagEnum.UN_DELETED.getCode());
        queryWrapper.eq("p.source_id", storeId);
        if (StringUtils.hasLength(storeSkuRequest.getSkuCode())) {
            queryWrapper.eq("p.sku_code", storeSkuRequest.getSkuCode());
        }
        if (StringUtils.hasLength(storeSkuRequest.getSkuName())) {
            queryWrapper.like(" p.sku_name", storeSkuRequest.getSkuName());
        }
        if (Objects.nonNull(storeSkuRequest.getSkuState())) {
            queryWrapper.eq("p.state", storeSkuRequest.getSkuState());
        }
        if (Objects.nonNull(storeSkuRequest.getThreeSkuId())) {
            queryWrapper.eq("t.three_sku_id", storeSkuRequest.getThreeSkuId());
        }
        return queryWrapper;
    }

    private List<StoreSkuResponse> convetResponse(List<StoreSkuEntity> storeSkuEntities) {
        return storeSkuEntities.stream()
        .map(e -> {
            StoreSkuResponse storeSkuResponse = new StoreSkuResponse();
            BeanUtils.copyProperties(e,storeSkuResponse);
            if(Objects.isNull(e.getIntoPrice())){
                storeSkuResponse.setIntoPrice(BigDecimal.ZERO);
            }else {
                storeSkuResponse.setIntoPrice(BigDecimalUtil.F2Y(e.getIntoPrice().longValue()));
            }
            storeSkuResponse.setPrice(BigDecimalUtil.F2Y(e.getPrice().longValue()));
            return storeSkuResponse;
        }).collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void relevanceAdd(RelevanceThreeRequest relevanceThreeRequest, String userId) {
        StoreThreeRelevanceEntity storeThreeRelevanceEntity = queryBySkuId(relevanceThreeRequest.getSkuId());
        if(Objects.isNull(storeThreeRelevanceEntity)){
           buildAndSave(relevanceThreeRequest,userId);
        }else {
           buildAndUpdate(storeThreeRelevanceEntity.getId(),relevanceThreeRequest,userId);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateSkuState(UpdateSkuRequest updateSkuRequest, String userId) {
        List<Long> skuIdList = updateSkuRequest.getSkuIdList();
        Integer state = updateSkuRequest.getState();
        if(ProductStateEnum.HAS_PUT.getCode().equals(state)){
            checkRelevance(skuIdList);
            checkPrice(skuIdList);
            checkSetPostage(skuIdList);
        }
        productSkuService.updateState(skuIdList,state,userId);
        checkAndUpdateSpu(skuIdList,state,userId);
        //TODO 发送mq消息通知库存
        sendWareMessage(updateSkuRequest,userId);
    }

    private void sendWareMessage(UpdateSkuRequest updateSkuRequest, String userId) {
        SkuStateStockMqEvent stateStockMqEvent = new SkuStateStockMqEvent();
        BeanUtils.copyProperties(updateSkuRequest,stateStockMqEvent);
        stateStockMqEvent.setUserId(userId);
        mqMessageUtil.sendMsg(MqMessageUtil.SKU_STATE_STOCK_TOPIC,MqMessageUtil.SKU_STATE_STOCK_TAG,stateStockMqEvent);
    }

    private void checkAndUpdateSpu(List<Long> skuIdList, Integer state, String userId) {
        List<ProductSkuEntity> allSpu = productSkuService.queryBySkuIdOrCode(skuIdList.stream().collect(Collectors.toSet()), Collections.EMPTY_SET, null);
        Set<Long> spuIds = allSpu.stream().map(ProductSkuEntity::getProductId).collect(Collectors.toSet());
        List<ProductSkuEntity> allSku = productSkuService.queryByProductIdList(spuIds);
        Map<Long, List<ProductSkuEntity>> spuMap = allSku.stream().collect(Collectors.groupingBy(ProductSkuEntity::getProductId));
        if(state.equals(ProductStateEnum.HAS_PUT.getCode())){
           //上架，更新这些spu为上架状态
            productBasicsInfoService.updateState(spuMap.keySet(),state,userId);
        }
        if(state.equals(ProductStateEnum.UN_PUT.getCode())){
           //下架，更新商品全被下架的spu为下架状态
           Set<Long> downSpuIds = new HashSet<>();
           spuMap.forEach((k,v) -> {
               List<ProductSkuEntity> collect = v.stream().filter(p -> p.getState().equals(ProductStateEnum.HAS_PUT.getCode())).collect(Collectors.toList());
               if(CollectionUtils.isEmpty(collect)){
                  downSpuIds.add(k);
               }
           });
           if(CollectionUtils.isNotEmpty(downSpuIds)){
               productBasicsInfoService.updateState(downSpuIds,state,userId);
           }
        }
    }

    @Override
    public List<StoreSkuResponse> searchSkuList(StoreSkuRequest storeSkuRequest) {
        Long storeId = storeSkuRequest.getStoreId();
        if(Objects.isNull(storeId)){
            throw new BusinessException(ErrorEnums.PARAM_IS_NOT_NULL);
        }
        QueryWrapper<StoreSkuEntity> queryWrapper = buildSkuQuery(storeSkuRequest, storeId);
        List<StoreSkuEntity> storeSkuEntities = storeThreeRelevanceMapper.searchSkuList(queryWrapper);
        return convetResponse(storeSkuEntities);
    }

    @Override
    public List<ThreeSkuInfoResponse> queryBySkuIds(List<Long> skuIds) {
        if(CollectionUtils.isEmpty(skuIds)){
           return Collections.emptyList();
        }
        LambdaQueryWrapper<StoreThreeRelevanceEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(StoreThreeRelevanceEntity::getSkuId,skuIds)
                .eq(StoreThreeRelevanceEntity::getDelFlag,DelFlagEnum.UN_DELETED);
        List<StoreThreeRelevanceEntity> storeThreeRelevanceEntities = storeThreeRelevanceMapper.selectList(queryWrapper);
        return storeThreeRelevanceEntities.stream().map(e -> {
            ThreeSkuInfoResponse threeSkuInfoResponse = new ThreeSkuInfoResponse();
            BeanUtils.copyProperties(e,threeSkuInfoResponse);
            return threeSkuInfoResponse;
        }).collect(Collectors.toList());
    }

    /**
     * 上架检验商品是否设置邮费
     * @param skuIdList
     */
    private void checkSetPostage(List<Long> skuIdList) {
        List<ProductSkuEntity> productSkuEntities = productSkuService.queryBySkuIdOrCode(skuIdList.stream().collect(Collectors.toSet()), Collections.EMPTY_SET, null);
        List<Long> storeIds = productSkuEntities.stream().map(ProductSkuEntity::getSourceId).distinct().collect(Collectors.toList());
        List<StoreEntity> storeEntities = storeService.querySetPostageByStoreIds(storeIds);
        if(storeIds.size() != storeEntities.size()){
           throw new BusinessException(ErrorEnums.STORE_NOT_SETPOSTAGE);
        }
    }

    /**
     * 上架检验商品是否关联三方
     * @param skuIdList
     */
    private void checkRelevance(List<Long> skuIdList) {
        LambdaQueryWrapper<StoreThreeRelevanceEntity> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(StoreThreeRelevanceEntity::getSkuId,skuIdList);
        Integer count = storeThreeRelevanceMapper.selectCount(lambdaQueryWrapper);
        if(skuIdList.size() != count){
            throw new BusinessException(ErrorEnums.STORE_NOT_RELEVANCE);
        }
    }

    /**
     * 上架检验商品是否设置进货价
     * @param skuIdList
     */
    private void checkPrice(List<Long> skuIdList) {
        LambdaQueryWrapper<StoreThreeRelevanceEntity> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(StoreThreeRelevanceEntity::getSkuId,skuIdList)
                .gt(StoreThreeRelevanceEntity::getThreePurchasePrice,0);
        Integer count = storeThreeRelevanceMapper.selectCount(lambdaQueryWrapper);
        if(skuIdList.size() != count){
            throw new BusinessException(ErrorEnums.STORE_NOT_SETPRICE);
        }
    }

    /**
     * 上架检验商品是否关联三方(单个)
     * @param skuId
     */
    private void checkRelevanceSingle(Long skuId) {
        LambdaQueryWrapper<StoreThreeRelevanceEntity> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(StoreThreeRelevanceEntity::getSkuId,skuId);
        StoreThreeRelevanceEntity storeThreeRelevanceEntity = storeThreeRelevanceMapper.selectOne(lambdaQueryWrapper);
        if(Objects.isNull(storeThreeRelevanceEntity)){
            throw new BusinessException(ErrorEnums.STORE_NOT_RELEVANCE);
        }
    }

    private void buildAndUpdate(Long id, RelevanceThreeRequest relevanceThreeRequest,String userId) {
        StoreThreeRelevanceEntity storeThreeRelevanceEntity = new StoreThreeRelevanceEntity();
        BeanUtils.copyProperties(relevanceThreeRequest,storeThreeRelevanceEntity);
        storeThreeRelevanceEntity.setId(id);
        storeThreeRelevanceEntity.setChangedBy(userId);
        storeThreeRelevanceEntity.setChangedAt(LocalDateTime.now());
        storeThreeRelevanceMapper.updateById(storeThreeRelevanceEntity);
    }

    private void buildAndSave(RelevanceThreeRequest relevanceThreeRequest,String userId) {
        StoreThreeRelevanceEntity storeThreeRelevanceEntity = new StoreThreeRelevanceEntity();
        BeanUtils.copyProperties(relevanceThreeRequest,storeThreeRelevanceEntity);
        storeThreeRelevanceEntity.setCreatedBy(userId);
        storeThreeRelevanceEntity.setChangedBy(userId);
        storeThreeRelevanceMapper.insert(storeThreeRelevanceEntity);

    }

    private StoreThreeRelevanceEntity queryBySkuId(Long skuId) {
        LambdaQueryWrapper<StoreThreeRelevanceEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StoreThreeRelevanceEntity::getSkuId,skuId)
                .eq(StoreThreeRelevanceEntity::getDelFlag,DelFlagEnum.UN_DELETED);
        return storeThreeRelevanceMapper.selectOne(queryWrapper);
    }
}
