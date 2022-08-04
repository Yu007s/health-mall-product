package com.drstrong.health.product.service.store.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.drstrong.health.product.dao.store.StoreInvoiceMapper;
import com.drstrong.health.product.dao.store.StoreMapper;
import com.drstrong.health.product.model.entity.store.StoreEntity;
import com.drstrong.health.product.model.entity.store.StoreInvoiceEntity;
import com.drstrong.health.product.model.entity.store.StoreLinkSupplierEntity;
import com.drstrong.health.product.model.enums.DelFlagEnum;
import com.drstrong.health.product.model.enums.StoreTypeEnum;
import com.drstrong.health.product.model.request.store.StoreInfoDetailSaveRequest;
import com.drstrong.health.product.model.request.store.StoreSearchRequest;
import com.drstrong.health.product.model.response.store.StoreAddResponse;
import com.drstrong.health.product.model.response.store.StoreInfoEditResponse;
import com.drstrong.health.product.model.response.store.StoreInfoResponse;
import com.drstrong.health.product.model.response.store.SupplierResponse;
import com.drstrong.health.product.service.store.AgencyService;
import com.drstrong.health.product.service.store.StoreInvoiceService;
import com.drstrong.health.product.service.store.StoreLinkSupplierService;
import com.drstrong.health.product.service.store.StoreService;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Author xieYueFeng
 * @Date 2022/07/30/9:24
 */
@Service
public class StoreServiceImpl extends ServiceImpl<StoreMapper, StoreEntity> implements StoreService {
    @Resource
    AgencyService agencyService;
    @Resource
    StoreInvoiceService storeInvoiceService;

    @Resource
    StoreInvoiceMapper storeInvoiceMapper;
    @Resource
    StoreLinkSupplierService storeLinkSupplierService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(StoreInfoDetailSaveRequest storeRequest, Long userId) throws Exception {
        String storeName = storeRequest.getStoreName();
        LambdaQueryWrapper<StoreEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StoreEntity::getStoreName,storeName).eq(StoreEntity::getDelFlag,DelFlagEnum.UN_DELETED.getCode()).last("limit 1");
        StoreEntity one = super.getOne(queryWrapper);
        if(one != null){
            throw new Exception("已经存在同名店铺");
        }
        StoreInvoiceEntity invoice = BeanUtil.copyProperties(storeRequest, StoreInvoiceEntity.class);
        List<StoreLinkSupplierEntity> storeLinkSuppliers = new ArrayList<>(storeRequest.getSupplierIds().size());
        StoreEntity storeEntity = new StoreEntity();
        buildEntityForSaveOrUpdate(storeRequest,storeEntity,storeLinkSuppliers,invoice,userId);
        storeEntity.setCreatedBy(userId);
        super.save(storeEntity);
        Long storeId = storeEntity.getId();
        invoice.setStoreId(storeId);
        invoice.setCreatedBy(userId);
        storeInvoiceMapper.insert(invoice);
        storeLinkSuppliers.forEach(
                storeLinkSupplierEntity -> {
                    storeLinkSupplierEntity.setCreatedBy(userId);
                    storeLinkSupplierEntity.setStoreId(storeId);
                }
        );
        storeLinkSupplierService.saveBatch(storeLinkSuppliers);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(StoreInfoDetailSaveRequest storeRequest, Long userId) throws Exception {
        StoreInvoiceEntity invoice = BeanUtil.copyProperties(storeRequest, StoreInvoiceEntity.class);
        List<StoreLinkSupplierEntity> storeLinkSuppliers = new ArrayList<>(storeRequest.getSupplierIds().size());
        StoreEntity storeEntity = new StoreEntity();
        buildEntityForSaveOrUpdate(storeRequest,storeEntity,storeLinkSuppliers,invoice,userId);
        Long storeId = storeEntity.getId();
        //禁止更新字段设置为null
        storeEntity.setStoreType(null);
        //互联网医院不支持更新
        storeEntity.setAgencyId(null);
        //更新店铺
        super.updateById(storeEntity);
        //更新店铺发票  关联供应商，
        LambdaQueryWrapper<StoreInvoiceEntity> lambdaQueryWrapper =  new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(StoreInvoiceEntity::getStoreId, storeId).eq(StoreInvoiceEntity::getDelFlag,DelFlagEnum.UN_DELETED.getCode());
        storeInvoiceMapper.update(invoice,lambdaQueryWrapper);
    }


    @Override
    public List<StoreInfoResponse> query(StoreSearchRequest storeSearchRequest) {
        LambdaQueryWrapper<StoreEntity> storeEntityQueryWrapper = new LambdaQueryWrapper<>();
        String storeTypeName = storeSearchRequest.getStoreTypeName();
        Integer storeType = null;
        if (storeTypeName != null) {
            storeType = StoreTypeEnum.nameToCode(storeTypeName);
        }
        storeEntityQueryWrapper.select(StoreEntity::getStoreName,StoreEntity::getId,StoreEntity::getStoreType,StoreEntity::getAgencyId)
                .eq(storeSearchRequest.getStoreId() != null, StoreEntity::getId, storeSearchRequest.getStoreId())
                .like(storeSearchRequest.getStoreName() != null, StoreEntity::getStoreName, storeSearchRequest.getStoreName())
                .eq(storeType != null, StoreEntity::getStoreType, storeType)
                .eq(storeSearchRequest.getAgencyId() != null, StoreEntity::getAgencyId, storeSearchRequest.getAgencyId())
                .eq(StoreEntity::getDelFlag, DelFlagEnum.UN_DELETED.getCode());
        List<StoreEntity> storeEntities = list(storeEntityQueryWrapper);
        return storeEntities.stream().map(storeEntity -> {
            StoreInfoResponse storeInfoResponse = BeanUtil.copyProperties(storeEntity, StoreInfoResponse.class);
            Integer storeTypeCode = storeEntity.getStoreType();
            storeInfoResponse.setStoreTypeName(StoreTypeEnum.values()[storeTypeCode].getValue());
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
            return Lists.newArrayList();
        }
        LambdaQueryWrapper<StoreEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StoreEntity::getDelFlag, DelFlagEnum.UN_DELETED.getCode())
                .in(StoreEntity::getId, storeIds);
        return list(queryWrapper);
    }

    /**
     * 根据店铺id集合查询店铺信息
     *
     * @param storeId 店铺id
     * @return 店铺信息集合
     * @author liuqiuyi
     * @date 2022/8/1 15:26
     */
    @Override
    public StoreEntity getById(Long storeId) {
        List<StoreEntity> storeEntityList = listByIds(Sets.newHashSet(storeId));
        if (!CollectionUtils.isEmpty(storeEntityList)) {
            return storeEntityList.get(0);
        }
        return null;
    }

    /**
     * 根据互联网医院 id，获取店铺信息
     *
     * @param agencyId 互联网医院 id
     * @return 店铺信息
     * @author liuqiuyi
     * @date 2022/8/3 19:47
     */
    @Override
    public StoreEntity getStoreByAgencyId(Long agencyId) {
        if (Objects.isNull(agencyId)) {
            return null;
        }
        LambdaQueryWrapper<StoreEntity> queryWrapper = Wrappers.<StoreEntity>lambdaQuery()
                .eq(StoreEntity::getDelFlag, DelFlagEnum.UN_DELETED.getCode())
                .eq(StoreEntity::getStoreType, StoreTypeEnum.INT_HOSPITAL.getCode())
                .eq(StoreEntity::getAgencyId, agencyId)
                .last("limit 1");
        return getOne(queryWrapper);
    }

    @Override
    public StoreAddResponse queryStoreCloseInfo() {
        //查询所有店铺类型
        List<String> storeTypeNames = new ArrayList<>(StoreTypeEnum.values().length);
        for (StoreTypeEnum value : StoreTypeEnum.values()) {
             storeTypeNames.add(value.getValue());
        }
        //查询所有供应商
        List<SupplierResponse> supplierResponses = new ArrayList<>();
        SupplierResponse supplierResponse = new SupplierResponse();
        supplierResponse.setSupplierId(1024L);
        supplierResponse.setSupplierName("我是测试供应商名字");
        supplierResponses.add(supplierResponse);
        //查询所有互联网医院
        List<String> allName = agencyService.getAllName();
        StoreAddResponse storeAddResponse = new StoreAddResponse();
        storeAddResponse.setStoreTypeNames(storeTypeNames);
        storeAddResponse.setSuppliers(supplierResponses);
        List<StoreAddResponse.AgencyIdAndName> agencyIdAndNames = new ArrayList<>();
        for (int i = 0; i < allName.size(); i++) {
            StoreAddResponse.AgencyIdAndName agencyIdAndName = new StoreAddResponse.AgencyIdAndName((long) i + 1, allName.get(i));
            agencyIdAndNames.add(agencyIdAndName);
        }
        storeAddResponse.setAgencyIdAndNames(agencyIdAndNames);
        return storeAddResponse;
    }



    @Override
    @Transactional(readOnly = true)
    public StoreInfoEditResponse queryById(Long storeId) {
        StoreEntity storeEntity = super.getById(storeId);
        Long storeEntityId = storeEntity.getId();
        //根据店铺id 查询发票信息  组合后返回
        StoreInvoiceEntity invoice = storeInvoiceService.getByStoreId(storeEntityId);
        //查询供应商信息  这里要根据供应商id调用供应商名字
        List<StoreLinkSupplierEntity> linkSupplierEntities = storeLinkSupplierService.queryByStoreId(storeId);
        List<SupplierResponse> collect = linkSupplierEntities.stream().map(supplier -> {
            SupplierResponse supplierResponse = new SupplierResponse();
            supplierResponse.setSupplierId(supplier.getSupplierId());
            supplierResponse.setSupplierName("测试中，未实现");
            return supplierResponse;
        }).collect(Collectors.toList());
        return buildStoreInfoResponse(invoice, storeEntity,collect);
    }

    private StoreInfoEditResponse buildStoreInfoResponse(StoreInvoiceEntity storeInvoiceEntity, StoreEntity storeEntity, List<SupplierResponse> suppliers) {
        StoreInfoEditResponse storeInfoEditResponse = BeanUtil.copyProperties(storeInvoiceEntity, StoreInfoEditResponse.class);
        BeanUtil.copyProperties(storeEntity, storeInfoEditResponse);
        Integer storeType = storeEntity.getStoreType();
        storeInfoEditResponse.setStoreTypeName(StoreTypeEnum.values()[storeType].getValue());
        Long agencyId = storeEntity.getAgencyId();
        if (agencyId != null) {
            String agencyName = agencyService.id2name(agencyId);
            storeInfoEditResponse.setAgencyName(agencyName);
        }
        storeInfoEditResponse.setStoreName(storeEntity.getStoreName());
        storeInfoEditResponse.setSuppliers(suppliers);
        return storeInfoEditResponse;
    }

    private void buildEntityForSaveOrUpdate(StoreInfoDetailSaveRequest storeRequest,StoreEntity storeEntity,
                                            List<StoreLinkSupplierEntity> linkSupplierEntities,StoreInvoiceEntity storeInvoiceEntity,Long userId) throws Exception {
        //组装店铺实体
        storeEntity.setChangedBy(userId);
        storeEntity.setStoreName(storeRequest.getStoreName());
        String storeTypeName = storeRequest.getStoreTypeName();
        Integer integer = StoreTypeEnum.nameToCode(storeTypeName);
        if (integer == null) {
            throw new Exception("错误的店铺名称");
        }
        storeEntity.setStoreType(integer);
        storeEntity.setId(storeRequest.getStoreId());
        if (integer.equals(StoreTypeEnum.INT_HOSPITAL.getCode())) {
            Long agencyId = storeRequest.getAgencyId();
            storeEntity.setAgencyId(agencyId);
        }
        //组装发票实例
        storeInvoiceEntity.setChangedBy(userId);
        //组装供应商实例列表
        List<Long> supplierIds = storeRequest.getSupplierIds();
        supplierIds.forEach(supplierId -> {
            StoreLinkSupplierEntity storeLinkSupplierEntity = new StoreLinkSupplierEntity();
            storeLinkSupplierEntity.setSupplierId(supplierId);
            storeLinkSupplierEntity.setChangedBy(userId);
            linkSupplierEntities.add(storeLinkSupplierEntity);
        });
    }
}
