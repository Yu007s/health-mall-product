package com.drstrong.health.product.service.store.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.drstrong.health.product.model.entity.store.DeliveryPriorityEntity;
import com.drstrong.health.product.dao.store.StoreDeliveryPriorityMapper;
import com.drstrong.health.product.model.entity.store.StoreLinkSupplierEntity;
import com.drstrong.health.product.model.enums.DelFlagEnum;
import com.drstrong.health.product.model.request.store.DeliveryPriRequest;
import com.drstrong.health.product.model.request.store.SaveDeliveryRequest;
import com.drstrong.health.product.model.response.area.AreaInfoResponse;
import com.drstrong.health.product.model.response.result.BusinessException;
import com.drstrong.health.product.model.response.result.ResultStatus;
import com.drstrong.health.product.model.response.store.SupplierResponse;
import com.drstrong.health.product.model.response.store.delievy.DeliveryPriResponse;
import com.drstrong.health.product.model.response.store.delievy.DeliveryPriorityVO;
import com.drstrong.health.product.service.area.AreaService;
import com.drstrong.health.product.service.store.StoreDeliveryPriorityService;
import cn.strong.mybatis.plus.extend.CustomServiceImpl;
import com.drstrong.health.product.service.store.StoreLinkSupplierService;
import com.drstrong.health.ware.model.response.SupplierInfoDTO;
import com.drstrong.health.ware.model.result.ResultVO;
import com.drstrong.health.ware.remote.api.SupplierManageRemoteApi;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
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

    @Resource
    SupplierManageRemoteApi supplierManageRemoteApi;

    @Override
    @Transactional(readOnly = true)
    public DeliveryPriorityVO queryByStoreId(Long storeId) {
        LambdaQueryWrapper<DeliveryPriorityEntity> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.select(DeliveryPriorityEntity::getPriorities, DeliveryPriorityEntity::getAreaId, DeliveryPriorityEntity::getParentAreaId)
                .eq(DeliveryPriorityEntity::getStoreId, storeId).eq(DeliveryPriorityEntity::getDelFlag, DelFlagEnum.UN_DELETED.getCode());
        List<DeliveryPriorityEntity> list = super.list(lambdaQueryWrapper);
        List<DeliveryPriResponse> deliveries = new ArrayList<>(list.size());
        DeliveryPriorityVO deliveryPriorityVO = new DeliveryPriorityVO();
        //查询国家区域id
        AreaInfoResponse areaInfoResponse = areaService.queryCountryId();
        Long countryId = areaInfoResponse.getAreaId();
        for (DeliveryPriorityEntity deliveryPriorityEntity : list) {
            DeliveryPriResponse deliveryPriResponse = buildDeliveryResponse(deliveryPriorityEntity);
            if (countryId.equals(deliveryPriorityEntity.getAreaId())) {
                List<Long> supplierIds = deliveryPriResponse.getSupplierIds();
                deliveryPriorityVO.setDefaultDeliveries(supplierIds);
            } else {
                deliveries.add(deliveryPriResponse);
            }
        }
        deliveryPriorityVO.setStoreId(storeId.toString());
        List<StoreLinkSupplierEntity> linkSupplierEntities = storeLinkSupplierService.queryByStoreId(storeId);
        List<Long> supplierIds = linkSupplierEntities.stream().map(StoreLinkSupplierEntity::getSupplierId).collect(Collectors.toList());
        ResultVO<List<SupplierInfoDTO>> listResultVO = supplierManageRemoteApi.queryBySupplierIds(supplierIds);
        List<SupplierInfoDTO> data = listResultVO.getData();
        List<SupplierResponse> supplierResponses = data.stream().map(supplierInfoDTO -> {
            SupplierResponse supplierResponse = new SupplierResponse();
            supplierResponse.setSupplierName(supplierInfoDTO.getSupplierName());
            supplierResponse.setSupplierId(supplierInfoDTO.getSupplierId());
            return supplierResponse;
        }).collect(Collectors.toList());
        deliveryPriorityVO.setSupplierResponses(supplierResponses);
        deliveryPriorityVO.setDeliveryPriorities(deliveries);
        return deliveryPriorityVO;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Long> queryByStoreIdAndArea(Long storeId, Long areaId) {
        List<Long> collect = areaService.queryFatherAreaById(areaId);
        LambdaQueryWrapper<DeliveryPriorityEntity> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.select(DeliveryPriorityEntity::getAreaId, DeliveryPriorityEntity::getPriorities)
                .in(DeliveryPriorityEntity::getAreaId, collect)
                .eq(DeliveryPriorityEntity::getStoreId, storeId)
                .eq(DeliveryPriorityEntity::getDelFlag, DelFlagEnum.UN_DELETED.getCode());
        List<DeliveryPriorityEntity> deliveryPriorityEntities = list(lambdaQueryWrapper);
        //查询不到配送优先级 该地区没有设置
        if (deliveryPriorityEntities == null || deliveryPriorityEntities.size() == 0) {
            return new ArrayList<>(0);
        }
        List<Long> areaIdList = deliveryPriorityEntities.stream().map(DeliveryPriorityEntity::getAreaId).collect(Collectors.toList());
        //从最小的区域开始检索 如果检索到即返回相应的下标
        int index = 0;
        for (Long id : collect) {
            int i = areaIdList.indexOf(id);
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
        //这里-1是不配送的标志  写死在前端  即供应商id为-1  即不配送
        List<String> priStrings = Arrays.stream(split).filter(StringUtils::isNumeric).filter(s -> !"-1".equals(s)).collect(Collectors.toList());
        return priStrings.stream().map(Long::valueOf).collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(SaveDeliveryRequest saveDeliveryRequest, Long userId) {
        Long storeId = saveDeliveryRequest.getStoreId();
        LambdaQueryWrapper<DeliveryPriorityEntity> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(DeliveryPriorityEntity::getStoreId, storeId).
                eq(DeliveryPriorityEntity::getDelFlag, DelFlagEnum.UN_DELETED.getCode()).last("limit 1");
        DeliveryPriorityEntity one = getOne(lambdaQueryWrapper);
        List<Long> defaultIds = saveDeliveryRequest.getDefaultDelPriority();
        if (defaultIds.stream().anyMatch(Objects::isNull)) {
            throw new BusinessException(ResultStatus.PARAM_ERROR.getCode(), "错误的供应商设置");
        }
        DeliveryPriRequest defaultDelPriority = new DeliveryPriRequest();
        //默认配送优先级设置
        List<Long> defaultAreaId = new ArrayList<>(1);
        AreaInfoResponse areaInfoResponse = areaService.queryCountryId();
        defaultAreaId.add(areaInfoResponse.getAreaId());
        defaultDelPriority.setAreaId(defaultAreaId);
        defaultDelPriority.setSupplierIds(defaultIds);
        List<DeliveryPriRequest> deliveryPriorities = saveDeliveryRequest.getDeliveryPriorities() == null ?
                new ArrayList<>() : saveDeliveryRequest.getDeliveryPriorities();
        deliveryPriorities.add(defaultDelPriority);
        //用于用户设置地区的判重  如果多个设置项设置了同一个地区id  抛出异常
        HashSet<Long> hashSet = new HashSet<>();
        List<DeliveryPriorityEntity> collect = deliveryPriorities.stream().map(deliveryPriRequest -> {
            DeliveryPriorityEntity deliveryPriorityEntity = new DeliveryPriorityEntity();
            deliveryPriorityEntity.setStoreId(storeId);
            deliveryPriorityEntity.setCreatedBy(userId);
            deliveryPriorityEntity.setChangedBy(userId);
            //前端返回值带省级id  最后一个才是市级id
            List<Long> areaIds = deliveryPriRequest.getAreaId();
            areaIds = areaIds.stream().filter(Objects::nonNull).collect(Collectors.toList());
            if (areaIds.size() == 0) {
                throw new BusinessException(ResultStatus.PARAM_ERROR.getCode(), "设置地区不能为空");
            }
            if (areaIds.size() > 1) {
                deliveryPriorityEntity.setParentAreaId(areaIds.get(0));
            }
            Long aLong = areaIds.get(areaIds.size() - 1);
            if (!hashSet.add(aLong)) {
                throw new BusinessException(ResultStatus.PARAM_ERROR.getCode(), "不能重复设置同一个地区");
            }
            deliveryPriorityEntity.setAreaId(aLong);
            List<Long> supplierIds = deliveryPriRequest.getSupplierIds();
            if (supplierIds != null) {
                if (supplierIds.stream().anyMatch(Objects::isNull)) {
                    throw new BusinessException(ResultStatus.PARAM_ERROR.getCode(), "错误的供应商设置");
                }
                String priorities = supplierIds.stream().map(String::valueOf).collect(Collectors.joining(","));
                deliveryPriorityEntity.setPriorities(priorities);
            }
            return deliveryPriorityEntity;
        }).collect(Collectors.toList());
        if (one == null) {
            //新增配送优先级
            saveBatch(collect);
        } else {
            //先删除先前的优先级设置
            LambdaUpdateWrapper<DeliveryPriorityEntity> deleteWrapper = new LambdaUpdateWrapper<>();
            deleteWrapper.eq(DeliveryPriorityEntity::getStoreId, storeId).
                    eq(DeliveryPriorityEntity::getDelFlag, DelFlagEnum.UN_DELETED.getCode()).
                    set(DeliveryPriorityEntity::getDelFlag, DelFlagEnum.IS_DELETED.getCode());
            update(deleteWrapper);
            //编辑配送优先级
            saveBatch(collect);
        }
    }

    private DeliveryPriResponse buildDeliveryResponse(DeliveryPriorityEntity deliveryPriorityEntity) {
        DeliveryPriResponse deliveryPriResponse = new DeliveryPriResponse();
        List<String> areaIds = new ArrayList<>();
        if (deliveryPriorityEntity.getParentAreaId() != -1) {
            areaIds.add(deliveryPriorityEntity.getParentAreaId().toString());
        }
        areaIds.add(deliveryPriorityEntity.getAreaId().toString());
        deliveryPriResponse.setAreaId(areaIds);
        String priorities = deliveryPriorityEntity.getPriorities();
        String[] split = priorities.split(",");
        List<Long> suppliers = Arrays.stream(split).map(Long::valueOf).collect(Collectors.toList());
        deliveryPriResponse.setSupplierIds(suppliers);
        return deliveryPriResponse;
    }
}
