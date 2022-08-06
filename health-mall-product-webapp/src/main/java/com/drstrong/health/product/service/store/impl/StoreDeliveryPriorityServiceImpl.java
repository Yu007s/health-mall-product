package com.drstrong.health.product.service.store.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.drstrong.health.product.model.entity.productstore.AreaEntity;
import com.drstrong.health.product.model.entity.store.DeliveryPriorityEntity;
import com.drstrong.health.product.dao.store.StoreDeliveryPriorityMapper;
import com.drstrong.health.product.model.entity.store.StoreLinkSupplierEntity;
import com.drstrong.health.product.model.enums.DelFlagEnum;
import com.drstrong.health.product.model.request.store.DeliveryPriRequest;
import com.drstrong.health.product.model.request.store.SaveDeliveryRequest;
import com.drstrong.health.product.model.response.store.SupplierResponse;
import com.drstrong.health.product.model.response.store.delievy.DeliveryPriResponse;
import com.drstrong.health.product.model.response.store.delievy.DeliveryPriorityVO;
import com.drstrong.health.product.service.area.AreaService;
import com.drstrong.health.product.service.store.StoreDeliveryPriorityService;
import cn.strong.mybatis.plus.extend.CustomServiceImpl;
import com.drstrong.health.product.service.store.StoreLinkSupplierService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author mybatis plus generator
 * @since 2022-08-05
 */
@Service
public class StoreDeliveryPriorityServiceImpl extends CustomServiceImpl<StoreDeliveryPriorityMapper, DeliveryPriorityEntity> implements StoreDeliveryPriorityService {

    @Resource
    StoreLinkSupplierService storeLinkSupplierService;
    @Resource
    AreaService areaService;

    @Override
    @Transactional(readOnly = true)
    public DeliveryPriorityVO queryByStoreId(Long storeId) {
        LambdaQueryWrapper<DeliveryPriorityEntity> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.select(DeliveryPriorityEntity::getPriorities,DeliveryPriorityEntity::getAreaId,DeliveryPriorityEntity::getAreaType)
                .eq(DeliveryPriorityEntity::getStoreId,storeId).eq(DeliveryPriorityEntity::getDelFlag, DelFlagEnum.UN_DELETED.getCode());
        List<DeliveryPriorityEntity> list = super.list(lambdaQueryWrapper);
        List<DeliveryPriResponse> deliveries = new ArrayList<>(list.size());
        DeliveryPriorityVO deliveryPriorityVO = new DeliveryPriorityVO();
        for (DeliveryPriorityEntity deliveryPriorityEntity : list) {
            DeliveryPriResponse deliveryPriResponse = buildDeliveryResponse(deliveryPriorityEntity);
            if (DeliveryPriorityEntity.CHINA.equals(deliveryPriorityEntity.getAreaType())) {
                deliveryPriorityVO.setDefaultDeliveries(deliveryPriResponse.getSupplierIds());
            }
            else {
                deliveries.add(deliveryPriResponse);
            }
        }
        deliveryPriorityVO.setStoreId(storeId);
        List<StoreLinkSupplierEntity> linkSupplierEntities = storeLinkSupplierService.queryByStoreId(storeId);
        List<SupplierResponse> collect = linkSupplierEntities.stream().map(storeLinkSupplierEntity -> {
            SupplierResponse supplierResponse = new SupplierResponse();
            supplierResponse.setSupplierId(storeLinkSupplierEntity.getSupplierId());
            //缺少一个调用接口
            supplierResponse.setSupplierName("测试中，我是供应商名字");
            return supplierResponse;
        }).collect(Collectors.toList());
        deliveryPriorityVO.setSupplierResponses(collect);
        deliveryPriorityVO.setDeliveryPriorities(deliveries);
        return deliveryPriorityVO;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Long> queryByStoreIdAndArea(Long storeId, Long areaId) {
        List<AreaEntity> areaInfoResponses = areaService.queryFatherAreaById(areaId);
        List<Long> collect = areaInfoResponses.stream().map(AreaEntity::getId).collect(Collectors.toList());
        collect.forEach(System.out::println);
        LambdaQueryWrapper<DeliveryPriorityEntity> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.select(DeliveryPriorityEntity::getAreaId,DeliveryPriorityEntity::getPriorities,DeliveryPriorityEntity::getAreaType)
                 .in(DeliveryPriorityEntity::getAreaId, collect)
                .eq(DeliveryPriorityEntity::getStoreId,storeId)
                .eq(DeliveryPriorityEntity::getDelFlag, DelFlagEnum.UN_DELETED.getCode());
        List<DeliveryPriorityEntity> list = list(lambdaQueryWrapper);
        //按照市级省级国家级排好序
        list.sort((a,b) -> b.getAreaType() - a.getAreaType());
        DeliveryPriorityEntity deliveryPriorityEntity = list.get(0);
        String priorities = deliveryPriorityEntity.getPriorities();
        if (priorities == null) {
            return new ArrayList<>(0);
        }
        String[] split = priorities.split(",");
        return Arrays.stream(split).map(Long::valueOf).collect(Collectors.toList());
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(SaveDeliveryRequest saveDeliveryRequest,Long userId) {
        Long storeId = saveDeliveryRequest.getStoreId();
        LambdaQueryWrapper<DeliveryPriorityEntity> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(DeliveryPriorityEntity::getStoreId,storeId).
                eq(DeliveryPriorityEntity::getDelFlag,DelFlagEnum.UN_DELETED.getCode()).last("limit 1");
        DeliveryPriorityEntity one = getOne(lambdaQueryWrapper);
        DeliveryPriRequest defaultDelPriority = saveDeliveryRequest.getDefaultDelPriority();
        List<DeliveryPriRequest> deliveryPriorities = saveDeliveryRequest.getDeliveryPriorities() == null ? new ArrayList<>() : saveDeliveryRequest.getDeliveryPriorities();
        deliveryPriorities.add(defaultDelPriority);
        List<DeliveryPriorityEntity> collect = deliveryPriorities.stream().map(deliveryPriRequest -> {
            DeliveryPriorityEntity deliveryPriorityEntity = new DeliveryPriorityEntity();
            deliveryPriorityEntity.setStoreId(storeId);
            deliveryPriorityEntity.setCreatedBy(userId);
            deliveryPriorityEntity.setAreaId(deliveryPriRequest.getAreaId());
            List<Long> supplierIds = deliveryPriRequest.getSupplierIds();
            StringBuilder stringBuilder = new StringBuilder();
            supplierIds.forEach(id -> stringBuilder.append(id).append(","));
            deliveryPriorityEntity.setPriorities(stringBuilder.toString());
            if (one == null) {
                deliveryPriorityEntity.setCreatedBy(userId);
            }
            deliveryPriorityEntity.setCreatedBy(userId);
            //这里暂时没有考虑省级区域的设置
            deliveryPriorityEntity.setAreaType(DeliveryPriorityEntity.CITY);
            return deliveryPriorityEntity;
        }).collect(Collectors.toList());
        //给第一个设置为全国  默认优先级
        collect.get(0).setAreaType(DeliveryPriorityEntity.CHINA);
        if (one == null) {
            //新增配送优先级
            saveBatch(collect);
        }
        else{
            //编辑配送优先级
            collect.forEach( deliveryPriorityEntity -> {
                LambdaUpdateWrapper<DeliveryPriorityEntity> updateWrapper = new LambdaUpdateWrapper<>();
                updateWrapper.eq(DeliveryPriorityEntity::getStoreId,storeId).
                        eq(DeliveryPriorityEntity::getDelFlag,DelFlagEnum.UN_DELETED.getCode()).
                        eq(DeliveryPriorityEntity::getAreaId,deliveryPriorityEntity.getAreaId());
                update(deliveryPriorityEntity,updateWrapper);
            });
        }
    }

    private DeliveryPriResponse buildDeliveryResponse(DeliveryPriorityEntity deliveryPriorityEntity){
        DeliveryPriResponse deliveryPriResponse = new DeliveryPriResponse();
        deliveryPriResponse.setAreaId(deliveryPriorityEntity.getAreaId());
        String priorities = deliveryPriorityEntity.getPriorities();
        String[] split = priorities.split(",");
        List<Long> suppliers = Arrays.stream(split).map(Long::valueOf).collect(Collectors.toList());
        deliveryPriResponse.setSupplierIds(suppliers);
        deliveryPriResponse.setAreaId(deliveryPriorityEntity.getAreaId());
        return deliveryPriResponse;
    }
}
