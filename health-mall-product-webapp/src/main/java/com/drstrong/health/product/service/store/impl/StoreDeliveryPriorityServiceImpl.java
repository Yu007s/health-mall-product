package com.drstrong.health.product.service.store.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.drstrong.health.product.model.entity.productstore.AreaEntity;
import com.drstrong.health.product.model.entity.store.DeliveryPriorityEntity;
import com.drstrong.health.product.dao.store.StoreDeliveryPriorityMapper;
import com.drstrong.health.product.model.entity.store.StoreLinkSupplierEntity;
import com.drstrong.health.product.model.enums.AreaTypeEnum;
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
import org.apache.commons.lang.StringUtils;
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
        lambdaQueryWrapper.select(DeliveryPriorityEntity::getPriorities,DeliveryPriorityEntity::getAreaId)
                .eq(DeliveryPriorityEntity::getStoreId,storeId).eq(DeliveryPriorityEntity::getDelFlag, DelFlagEnum.UN_DELETED.getCode());
        List<DeliveryPriorityEntity> list = super.list(lambdaQueryWrapper);
        List<DeliveryPriResponse> deliveries = new ArrayList<>(list.size());
        DeliveryPriorityVO deliveryPriorityVO = new DeliveryPriorityVO();
        for (DeliveryPriorityEntity deliveryPriorityEntity : list) {
            DeliveryPriResponse deliveryPriResponse = buildDeliveryResponse(deliveryPriorityEntity);
            if (AreaTypeEnum.COUNTRY.ordinal() == deliveryPriorityEntity.getAreaId()) {
                List<String> collect = deliveryPriResponse.getSupplierIds().stream().map(Object::toString).collect(Collectors.toList());
                deliveryPriorityVO.setDefaultDeliveries(collect);
            }
            else {
                deliveries.add(deliveryPriResponse);
            }
        }
        deliveryPriorityVO.setStoreId(storeId.toString());
        List<StoreLinkSupplierEntity> linkSupplierEntities = storeLinkSupplierService.queryByStoreId(storeId);
        List<SupplierResponse> collect = linkSupplierEntities.stream().map(storeLinkSupplierEntity -> {
            SupplierResponse supplierResponse = new SupplierResponse();
            supplierResponse.setSupplierId(storeLinkSupplierEntity.getSupplierId());
            //测试
            supplierResponse.setSupplierName("供应商"+storeLinkSupplierEntity.getSupplierId());
            return supplierResponse;
        }).collect(Collectors.toList());
        deliveryPriorityVO.setSupplierResponses(collect);
        deliveryPriorityVO.setDeliveryPriorities(deliveries);
        return deliveryPriorityVO;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Long> queryByStoreIdAndArea(Long storeId, Long areaId) {
        //根据区级查询市级id
        List<AreaEntity> areaInfoResponses = areaService.queryFatherAreaById(areaId);
        List<Long> collect = areaInfoResponses.stream().map(AreaEntity::getId).collect(Collectors.toList());
        LambdaQueryWrapper<DeliveryPriorityEntity> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.select(DeliveryPriorityEntity::getAreaId,DeliveryPriorityEntity::getPriorities)
                 .in(DeliveryPriorityEntity::getAreaId, collect)
                .eq(DeliveryPriorityEntity::getStoreId,storeId)
                .eq(DeliveryPriorityEntity::getDelFlag, DelFlagEnum.UN_DELETED.getCode());
        List<DeliveryPriorityEntity> deliveryPriorityEntities = list(lambdaQueryWrapper);
        //查询不到配送优先级 该地区没有设置
        if ( deliveryPriorityEntities==null || deliveryPriorityEntities.size() == 0) {
            return new ArrayList<>(0);
        }
        List<Long> areaIdList = deliveryPriorityEntities.stream().map(DeliveryPriorityEntity::getAreaId).collect(Collectors.toList());
        //从最小的区域开始检索 如果检索到即返回相应的下标
        int index = 0;
        for (AreaEntity areaInfoRe : areaInfoResponses) {
            int i = areaIdList.indexOf(areaInfoRe.getId());
            if (i != -1) {
                index = i;
                break;
            }
        }
        DeliveryPriorityEntity deliveryPriorityEntity = deliveryPriorityEntities.get(index);
        String priorities = deliveryPriorityEntity.getPriorities();
        if (StringUtils.isBlank(priorities)) {
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
        List<Long> defaultIds = saveDeliveryRequest.getDefaultDelPriority();
        DeliveryPriRequest defaultDelPriority = new DeliveryPriRequest();
        //
        defaultDelPriority.setAreaId((long)AreaTypeEnum.COUNTRY.ordinal());
        defaultDelPriority.setSupplierIds(defaultIds);
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
            return deliveryPriorityEntity;
        }).collect(Collectors.toList());
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
        deliveryPriResponse.setAreaId(deliveryPriorityEntity.getAreaId().toString());
        String priorities = deliveryPriorityEntity.getPriorities();
        String[] split = priorities.split(",");
        List<String> suppliers = Arrays.asList(split);
        deliveryPriResponse.setSupplierIds(suppliers);
        deliveryPriResponse.setAreaId(deliveryPriorityEntity.getAreaId().toString());
        return deliveryPriResponse;
    }
}
