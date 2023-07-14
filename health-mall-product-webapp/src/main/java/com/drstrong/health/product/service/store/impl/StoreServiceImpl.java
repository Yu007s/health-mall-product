package com.drstrong.health.product.service.store.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.drstrong.health.product.dao.store.StoreInvoiceMapper;
import com.drstrong.health.product.dao.store.StoreMapper;
import com.drstrong.health.product.facade.postage.StorePostageFacade;
import com.drstrong.health.product.facade.postage.impl.StorePostageFacadeImpl;
import com.drstrong.health.product.model.entity.store.StoreEntity;
import com.drstrong.health.product.model.entity.store.StoreInvoiceEntity;
import com.drstrong.health.product.model.entity.store.StoreLinkSupplierEntity;
import com.drstrong.health.product.model.enums.DelFlagEnum;
import com.drstrong.health.product.model.enums.ErrorEnums;
import com.drstrong.health.product.model.enums.StoreTypeEnum;
import com.drstrong.health.product.model.request.store.QueryStoreRequest;
import com.drstrong.health.product.model.request.store.SaveDeliveryRequest;
import com.drstrong.health.product.model.request.store.SaveStoreSupplierPostageRequest;
import com.drstrong.health.product.model.request.store.StoreInfoDetailSaveRequest;
import com.drstrong.health.product.model.response.result.BusinessException;
import com.drstrong.health.product.model.response.result.ResultStatus;
import com.drstrong.health.product.model.response.store.*;
import com.drstrong.health.product.service.area.AreaService;
import com.drstrong.health.product.service.store.*;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author xieYueFeng
 * @Date 2022/07/30/9:24
 */
@Service
@Slf4j
public class StoreServiceImpl extends ServiceImpl<StoreMapper, StoreEntity> implements StoreService {
    @Resource
    AgencyService agencyService;
    @Resource
    StoreInvoiceService storeInvoiceService;
    @Resource
    StoreInvoiceMapper storeInvoiceMapper;
    @Resource
    StoreLinkSupplierService storeLinkSupplierService;

    @Resource
    StoreDeliveryPriorityService storeDeliveryPriorityService;

    @Resource
    StorePostageFacade storePostageFacade;

	@Resource
	AreaService areaService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long save(StoreInfoDetailSaveRequest storeRequest) {
        // 入参校验 ObjectUtil.isNull(storeRequest.getHasInvoiceInfo()) 是为了兼容老页面
        checkStoreInvoiceParam(storeRequest);

        Long userId = storeRequest.getUserId();
        // 供应链三期修改,发票信息为非必填项
        StoreInvoiceEntity invoice = null;
        if (Objects.equals(1, storeRequest.getHasInvoiceInfo()) || ObjectUtil.isNull(storeRequest.getHasInvoiceInfo())) {
             invoice = BeanUtil.copyProperties(storeRequest, StoreInvoiceEntity.class);
        }
        List<StoreLinkSupplierEntity> storeLinkSuppliers = new ArrayList<>(storeRequest.getSupplierIds().size());
        StoreEntity storeEntity = new StoreEntity();
        buildEntityForSaveOrUpdate(storeRequest, storeEntity, storeLinkSuppliers, invoice, userId);
        checkStore(storeEntity);
        if (storeEntity.getAgencyId() != null) {
            //校验将要关联的互联网医院是否已经关联了店铺
            LambdaQueryWrapper<StoreEntity> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(StoreEntity::getAgencyId, storeEntity.getAgencyId()).eq(StoreEntity::getDelFlag, DelFlagEnum.UN_DELETED.getCode()).last("limit 1");
            StoreEntity one = super.getOne(queryWrapper);
            if (one != null) {
                throw new BusinessException("该互联网医院已经关联了店铺");
            }
        }
        storeEntity.setCreatedBy(userId);
        super.save(storeEntity);
        Long storeId = storeEntity.getId();
        if (Objects.nonNull(invoice)) {
            invoice.setStoreId(storeId);
            invoice.setCreatedBy(userId);
            storeInvoiceMapper.insert(invoice);
        }
        storeLinkSuppliers.forEach(
                storeLinkSupplierEntity -> {
                    storeLinkSupplierEntity.setCreatedBy(userId);
                    storeLinkSupplierEntity.setStoreId(storeId);
                }
        );
        storeLinkSupplierService.saveBatch(storeLinkSuppliers);
        //这里为店铺配送设置一个默认优先级
        SaveDeliveryRequest saveDeliveryRequest = new SaveDeliveryRequest();
        saveDeliveryRequest.setStoreId(storeId);
        List<Long> supplierIds = storeRequest.getSupplierIds();
        if (supplierIds.size() == 0) {
            throw new BusinessException(ResultStatus.PARAM_ERROR.getCode(), "店铺供应商设置错误");
        }
        saveDeliveryRequest.setDefaultDelPriority(storeRequest.getSupplierIds());
        saveDeliveryRequest.setDefaultDelPriority(saveDeliveryRequest.getDefaultDelPriority());
        storeDeliveryPriorityService.save(saveDeliveryRequest, userId);
        return storeId;
    }

    /**
     * 供应链三期需求,新店铺保存时,需要添加默认的邮费信息
     * <p> 新店铺满 300 包邮,店铺的供应商满 88 包邮,供应商的区域满 22 包邮 </>
     *
     * @author liuqiuyi
     * @date 2023/6/13 14:51
     */
    @Override
    public void saveStoreDefaultPostage(List<Long> supplierIds, Long storeId) {
        log.info("保存店铺的默认配送费,店铺id为:{},供应商id为:{}", storeId, supplierIds);
        if (CollectionUtils.isEmpty(supplierIds) || ObjectUtil.isNull(storeId)) {
            return;
        }
        // 1.获取所有的区域信息,组装默认值
        List<SaveStoreSupplierPostageRequest.StoreSupplierAreaPostageVO> storeSupplierAreaPostageVOList = areaService.queryAll().stream()
                .map(provinceAreaInfo -> new SaveStoreSupplierPostageRequest.StoreSupplierAreaPostageVO(Long.valueOf(provinceAreaInfo.getValue()), new BigDecimal(StorePostageFacadeImpl.STORE_SUPPLIER_AREA_FREE_POSTAGE)))
                .collect(Collectors.toList());
        // 2.保存供应商
        supplierIds.forEach(supplierId -> {
            SaveStoreSupplierPostageRequest saveStoreSupplierPostageRequest = SaveStoreSupplierPostageRequest.builder()
                    .storeId(storeId)
                    .supplierId(supplierId)
                    .freePostage(new BigDecimal(StorePostageFacadeImpl.STORE_SUPPLIER_FREE_POSTAGE))
                    .storeSupplierAreaPostageList(storeSupplierAreaPostageVOList)
                    .build();
            saveStoreSupplierPostageRequest.setOperatorId(9999L);
            saveStoreSupplierPostageRequest.setOperatorName("system");
            storePostageFacade.saveStoreSupplierPostage(saveStoreSupplierPostageRequest);
        });
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(StoreInfoDetailSaveRequest storeRequest) {
        // 入参校验
        checkStoreInvoiceParam(storeRequest);

        List<StoreLinkSupplierEntity> storeLinkSuppliers = new ArrayList<>(storeRequest.getSupplierIds().size());
        StoreEntity storeEntity = new StoreEntity();
        Long userId = storeRequest.getUserId();
        buildEntityForSaveOrUpdate(storeRequest, storeEntity, storeLinkSuppliers, null, userId);
        Long storeId = storeEntity.getId();
        //禁止更新字段设置为null
        storeEntity.setStoreType(null);
        //互联网医院不支持更新
        storeEntity.setAgencyId(null);
        checkStore(storeEntity);
        //更新店铺
        super.updateById(storeEntity);
        // 供应链三期修改,发票信息为非必填项
        updateStoreInvoiceInfo(storeId, storeRequest);
    }

	private void checkStoreInvoiceParam(StoreInfoDetailSaveRequest storeRequest) {
		if (ObjectUtil.equals(1, storeRequest.getHasInvoiceInfo()) || ObjectUtil.isNull(storeRequest.getHasInvoiceInfo())) {
            if (StringUtils.isAnyBlank(storeRequest.getEnterpriseTaxNumber(),
                    storeRequest.getEnterpriseBankAccount(),
                    storeRequest.getEnterpriseTelNumber(),
                    storeRequest.getEnterpriseAddress(),
                    storeRequest.getPayee(),
                    storeRequest.getChecker(),
                    storeRequest.getAppSecret(),
                    storeRequest.getAppKey())) {
                log.error("保存店铺发票信息,有参数不符合要求,入参为:{}", JSONUtil.toJsonStr(storeRequest));
                throw new BusinessException(ErrorEnums.PARAM_TYPE_IS_ERROR);
            }
        }
	}

	/**
	 * 供应链三期修改,发票信息为非必填项,如果进行修改,存在三种情况:
	 * <p>
	 * 之前有发票 -> 现在也有  -- 更新
	 * 之前有发票 -> 现在没有 -- 删除老发票信息
	 * 之前没有发票 -> 现在有  -- 新增
     * 之前没有发票 -> 现在也没有 -- 不处理
	 * </>
	 */
	private void updateStoreInvoiceInfo(Long storeId, StoreInfoDetailSaveRequest storeRequest) {
        boolean hasInvoiceInfo = Objects.equals(1, storeRequest.getHasInvoiceInfo()) || Objects.isNull(storeRequest.getHasInvoiceInfo());
        StoreInvoiceEntity storeInvoiceEntity = storeInvoiceService.getByStoreId(storeId);
        if (ObjectUtil.isNotNull(storeInvoiceEntity)) {
            if (hasInvoiceInfo) {
                // 更新
                StoreInvoiceEntity invoice = BeanUtil.copyProperties(storeRequest, StoreInvoiceEntity.class);
                invoice.setChangedBy(storeRequest.getUserId());
                storeInvoiceService.updateByStoreId(storeId, invoice);
            } else {
                // 删除老发票信息
                storeInvoiceService.removeByStoreId(storeId);
            }
        } else if (hasInvoiceInfo) {
            // 新增
            StoreInvoiceEntity invoice = BeanUtil.copyProperties(storeRequest, StoreInvoiceEntity.class);
            invoice.setCreatedBy(storeRequest.getUserId());
            invoice.setStoreId(storeId);
            storeInvoiceMapper.insert(invoice);
        }
	}


    @Override
    public List<StoreInfoResponse> query(Long storeId, String storeName, Long agencyId, String storeTypeName) {
        LambdaQueryWrapper<StoreEntity> storeEntityQueryWrapper = new LambdaQueryWrapper<>();
        Integer storeType = StoreTypeEnum.nameToCode(storeTypeName);
        storeEntityQueryWrapper.eq(storeId != null, StoreEntity::getId, storeId)
                .like(StringUtils.isNotBlank(storeName), StoreEntity::getStoreName, storeName)
                .eq(storeType != null, StoreEntity::getStoreType, storeType)
                .eq(agencyId != null, StoreEntity::getAgencyId, agencyId)
                .eq(StoreEntity::getDelFlag, DelFlagEnum.UN_DELETED.getCode()).orderByDesc(StoreEntity::getId);
        List<StoreEntity> storeEntities = list(storeEntityQueryWrapper);
        return storeEntities.stream().map(storeEntity -> {
            StoreInfoResponse storeInfoResponse = BeanUtil.copyProperties(storeEntity, StoreInfoResponse.class);
            Integer storeTypeCode = storeEntity.getStoreType();
            storeInfoResponse.setStoreTypeId(storeTypeCode);
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
    public StoreEntity getStoreByAgencyIdOrStoreId(Long agencyId, Long storeId) {
        if (Objects.isNull(agencyId) && Objects.isNull(storeId)) {
            return null;
        }
        StoreEntity storeEntity;
        if (Objects.nonNull(storeId)) {
            storeEntity = getById(storeId);
        } else {
            LambdaQueryWrapper<StoreEntity> queryWrapper = Wrappers.<StoreEntity>lambdaQuery()
                    .eq(StoreEntity::getDelFlag, DelFlagEnum.UN_DELETED.getCode())
                    .eq(StoreEntity::getStoreType, 0)
                    .eq(StoreEntity::getAgencyId, agencyId)
                    .last("limit 1");
            storeEntity = getOne(queryWrapper);
        }
        return storeEntity;
    }


    @Override
    public StoreQueryResponse queryStoreConInfo() {
        //所有店铺类型
        List<StoreQueryResponse.AgencyIdAndName> agencyIdAndNames = new ArrayList<>();
        List<String> storeTypeNames = Arrays.stream(StoreTypeEnum.values()).map(StoreTypeEnum::getValue).collect(Collectors.toList());
        //所有互联网医院
        List<String> allName = agencyService.getAllName();
        for (int i = 0; i < allName.size(); i++) {
            StoreQueryResponse.AgencyIdAndName agencyIdAndName = new StoreQueryResponse.AgencyIdAndName(Long.valueOf(i + 1).toString(), allName.get(i));
            agencyIdAndNames.add(agencyIdAndName);
        }
        StoreQueryResponse storeQueryResponse = new StoreQueryResponse();
        storeQueryResponse.setStoreTypeNames(storeTypeNames);
        storeQueryResponse.setAgencyIdAndNames(agencyIdAndNames);
        return storeQueryResponse;
    }

    /**
     * 根据互联网医院 id 获取店铺 id
     *
     * @param agencyIds 互联网医院 id
     * @return 店铺信息
     * @author liuqiuyi
     * @date 2022/8/8 19:57
     */
    @Override
    public List<StoreEntity> getStoreByAgencyIds(Set<Long> agencyIds) {
        if (CollectionUtils.isEmpty(agencyIds)) {
            return Lists.newArrayList();
        }
        LambdaQueryWrapper<StoreEntity> queryWrapper = Wrappers.<StoreEntity>lambdaQuery()
                .eq(StoreEntity::getDelFlag, DelFlagEnum.UN_DELETED.getCode())
                .eq(StoreEntity::getStoreType, StoreTypeEnum.INT_HOSPITAL.getCode())
                .in(StoreEntity::getAgencyId, agencyIds);
        return list(queryWrapper);
    }


    @Override
    @Transactional(readOnly = true)
    public StoreInfoEditResponse queryById(Long storeId) {
        StoreEntity storeEntity = super.getById(storeId);
        if (storeEntity == null) {
            throw new BusinessException(ResultStatus.PARAM_ERROR.getCode(), "不存在的店铺");
        }
        Long storeEntityId = storeEntity.getId();
        //根据店铺id 查询发票信息  组合后返回
        StoreInvoiceEntity invoice = storeInvoiceService.getByStoreId(storeEntityId);
        //查询关联供应商信息
        List<StoreLinkSupplierEntity> linkSupplierEntities = storeLinkSupplierService.queryByStoreId(storeId);
        List<Long> supplierIds = linkSupplierEntities.stream().map(StoreLinkSupplierEntity::getSupplierId).collect(Collectors.toList());
        return buildStoreInfoResponse(invoice, storeEntity, supplierIds);
    }

    private StoreInfoEditResponse buildStoreInfoResponse(StoreInvoiceEntity storeInvoiceEntity, StoreEntity storeEntity, List<Long> suppliers) {
        StoreInfoEditResponse storeInfoEditResponse = BeanUtil.copyProperties(storeInvoiceEntity, StoreInfoEditResponse.class);
        BeanUtil.copyProperties(storeEntity, storeInfoEditResponse);
        Integer storeType = storeEntity.getStoreType();
        storeInfoEditResponse.setStoreTypeName(StoreTypeEnum.values()[storeType].getValue());
        Long agencyId = storeEntity.getAgencyId();
        if (agencyId != null) {
            storeInfoEditResponse.setAgencyId(agencyId.toString());
        }
        storeInfoEditResponse.setStoreName(storeEntity.getStoreName());
        storeInfoEditResponse.setSupplierIds(suppliers);
        return storeInfoEditResponse;
    }

    private void buildEntityForSaveOrUpdate(StoreInfoDetailSaveRequest storeRequest, StoreEntity storeEntity,
                                            List<StoreLinkSupplierEntity> linkSupplierEntities, StoreInvoiceEntity storeInvoiceEntity, Long userId) {
        //组装店铺实体
        storeEntity.setChangedBy(userId);
        storeEntity.setStoreName(storeRequest.getStoreName());
        String storeTypeName = storeRequest.getStoreTypeName();
        Integer integer = StoreTypeEnum.nameToCode(storeTypeName);
        if (integer == null) {
            throw new BusinessException(ResultStatus.PARAM_ERROR.getCode(), "错误的店铺类型名称");
        }
        storeEntity.setStoreType(integer);
        storeEntity.setId(storeRequest.getStoreId());
        if (integer.equals(StoreTypeEnum.INT_HOSPITAL.getCode())) {
            Long agencyId = storeRequest.getAgencyId();
            if (agencyId == null || agencyService.id2name(agencyId) == null) {
                throw new BusinessException(ResultStatus.PARAM_ERROR.getCode(), "店铺类型为互联网医院，但未关联互联网医院或关联了错误的互联网医院");
            }
            storeEntity.setAgencyId(agencyId);
        }
        //组装发票实例
        if (Objects.nonNull(storeInvoiceEntity)) {
            storeInvoiceEntity.setChangedBy(userId);
        }
        //组装供应商实例列表
        List<Long> supplierIds = storeRequest.getSupplierIds();
        supplierIds.forEach(supplierId -> {
            StoreLinkSupplierEntity storeLinkSupplierEntity = new StoreLinkSupplierEntity();
            storeLinkSupplierEntity.setSupplierId(supplierId);
            storeLinkSupplierEntity.setChangedBy(userId);
            linkSupplierEntities.add(storeLinkSupplierEntity);
        });
    }

    /**
     * 检验店铺相关信息是否合法
     *
     * @param store 店铺
     */
    private void checkStore(StoreEntity store) {
        LambdaQueryWrapper<StoreEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StoreEntity::getStoreName, store.getStoreName()).eq(StoreEntity::getDelFlag, DelFlagEnum.UN_DELETED.getCode()).last("limit 1");
        StoreEntity one = super.getOne(queryWrapper);
        if (store.getId() != null) {
            //说明是编辑店铺  此时根据名字检索  如果检索到有店铺但是id不是传入id  那么报错
            if (one != null && !one.getId().equals(store.getId())) {
                throw new BusinessException(ResultStatus.PARAM_ERROR.getCode(), "已经存在同名店铺");
            }
            return;
        }
        if (one != null) {
            throw new BusinessException(ResultStatus.PARAM_ERROR.getCode(), "已经存在同名店铺");
        }
    }

    /**
     * 根据条件查询店铺信息，该方法可公用
     *
     * @param queryStoreRequest
     * @author liuqiuyi
     * @date 2023/7/14 10:09
     */
    @Override
    public List<StoreEntity> queryStoreByParam(QueryStoreRequest queryStoreRequest) {
        LambdaQueryWrapper<StoreEntity> queryWrapper = new LambdaQueryWrapper<StoreEntity>()
                .eq(StoreEntity::getDelFlag, DelFlagEnum.UN_DELETED.getCode())
                .like(StrUtil.isNotBlank(queryStoreRequest.getStoreName()), StoreEntity::getStoreName, queryStoreRequest.getStoreName())
                .eq(Objects.nonNull(queryStoreRequest.getStoreType()), StoreEntity::getStoreType, queryStoreRequest.getStoreType())
                .in(CollectionUtil.isNotEmpty(queryStoreRequest.getAgencyIds()), StoreEntity::getAgencyId, queryStoreRequest.getAgencyIds())
                .in(CollectionUtil.isNotEmpty(queryStoreRequest.getStoreIds()), StoreEntity::getId, queryStoreRequest.getStoreIds());

        return baseMapper.selectList(queryWrapper);
    }
}
