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
import com.drstrong.health.product.model.response.area.AreaInfoResponse;
import com.drstrong.health.product.model.response.result.BusinessException;
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
        lambdaQueryWrapper.select(DeliveryPriorityEntity::getPriorities,DeliveryPriorityEntity::getAreaId,DeliveryPriorityEntity::getParentAreaId)
                .eq(DeliveryPriorityEntity::getStoreId,storeId).eq(DeliveryPriorityEntity::getDelFlag, DelFlagEnum.UN_DELETED.getCode());
        List<DeliveryPriorityEntity> list = super.list(lambdaQueryWrapper);
        List<DeliveryPriResponse> deliveries = new ArrayList<>(list.size());
        DeliveryPriorityVO deliveryPriorityVO = new DeliveryPriorityVO();
        List<AreaInfoResponse> areaInfoResponses = areaService.queryProvinceAndCountry();
        Long countryId = areaInfoResponses.get(areaInfoResponses.size()-1).getAreaId();
        for (DeliveryPriorityEntity deliveryPriorityEntity : list) {
            DeliveryPriResponse deliveryPriResponse = buildDeliveryResponse(deliveryPriorityEntity);
            if (countryId.equals(deliveryPriorityEntity.getAreaId())) {
                List<String> collect = deliveryPriResponse.getSupplierIds().stream().map(Object::toString).collect(Collectors.toList());
                deliveryPriorityVO.setDefaultDeliveries(collect);
            }
            else {
                deliveries.add(deliveryPriResponse);
            }
        }
        if (deliveryPriorityVO.getDefaultDeliveries() == null) {
            throw new BusinessException("没有默认优先级");
        }
        deliveryPriorityVO.setStoreId(storeId.toString());
        List<StoreLinkSupplierEntity> linkSupplierEntities = storeLinkSupplierService.queryByStoreId(storeId);
        List<SupplierResponse> collect = linkSupplierEntities.stream().map(storeLinkSupplierEntity -> {
            SupplierResponse supplierResponse = new SupplierResponse();
            supplierResponse.setSupplierId(storeLinkSupplierEntity.getSupplierId().toString());
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
        //默认配送优先级设置
        List<Long> defaultArea = new ArrayList<>(1);
        defaultArea.add(1L);
        defaultDelPriority.setAreaId(defaultArea);
        defaultDelPriority.setSupplierIds(defaultIds);
        List<DeliveryPriRequest> deliveryPriorities = saveDeliveryRequest.getDeliveryPriorities() == null ? new ArrayList<>() : saveDeliveryRequest.getDeliveryPriorities();
        deliveryPriorities.add(defaultDelPriority);
        List<DeliveryPriorityEntity> collect = deliveryPriorities.stream().map(deliveryPriRequest -> {
            DeliveryPriorityEntity deliveryPriorityEntity = new DeliveryPriorityEntity();
            deliveryPriorityEntity.setStoreId(storeId);
            deliveryPriorityEntity.setCreatedBy(userId);
            deliveryPriorityEntity.setChangedBy(userId);
            //前端返回值带省级id  最后一个才是市级id
            List<Long> areaIds = deliveryPriRequest.getAreaId();
            if (areaIds.size() > 1) {
                deliveryPriorityEntity.setParentAreaId(areaIds.get(0));
            }
            deliveryPriorityEntity.setAreaId(areaIds.get(areaIds.size()-1));
            List<Long> supplierIds = deliveryPriRequest.getSupplierIds();
            String priorities = supplierIds.stream().map(String::valueOf).collect(Collectors.joining(","));
            deliveryPriorityEntity.setPriorities(priorities);
            return deliveryPriorityEntity;
        }).collect(Collectors.toList());
        if (one == null) {
            //新增配送优先级
            saveBatch(collect);
        }
        else{
            //先删除先前的优先级设置
            LambdaUpdateWrapper<DeliveryPriorityEntity> deleteWrapper = new LambdaUpdateWrapper<>();
            deleteWrapper.eq(DeliveryPriorityEntity::getStoreId,storeId).
                    eq(DeliveryPriorityEntity::getDelFlag,DelFlagEnum.UN_DELETED.getCode()).
                    set(DeliveryPriorityEntity::getDelFlag,DelFlagEnum.IS_DELETED.getCode());
            update(deleteWrapper);
            //编辑配送优先级
            saveBatch(collect);
        }
    }

    private DeliveryPriResponse buildDeliveryResponse(DeliveryPriorityEntity deliveryPriorityEntity){
        DeliveryPriResponse deliveryPriResponse = new DeliveryPriResponse();
        List<String> areaIds = new ArrayList<>();
        if (deliveryPriorityEntity.getParentAreaId() != -1) {
            areaIds.add(deliveryPriorityEntity.getParentAreaId().toString());
        }
        areaIds.add(deliveryPriorityEntity.getAreaId().toString());
        deliveryPriResponse.setAreaId(areaIds);
        String priorities = deliveryPriorityEntity.getPriorities();
        String[] split = priorities.split(",");
        List<String> suppliers = Arrays.asList(split);
        deliveryPriResponse.setSupplierIds(suppliers);
        return deliveryPriResponse;
    }
}
