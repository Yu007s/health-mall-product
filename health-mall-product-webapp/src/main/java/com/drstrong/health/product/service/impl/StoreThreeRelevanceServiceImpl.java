package com.drstrong.health.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.drstrong.health.product.dao.StoreThreeRelevanceMapper;
import com.drstrong.health.product.model.entity.product.ProductSkuEntity;
import com.drstrong.health.product.model.entity.store.StoreEntity;
import com.drstrong.health.product.model.entity.store.StoreThreeRelevanceEntity;
import com.drstrong.health.product.model.enums.DelFlagEnum;
import com.drstrong.health.product.model.enums.ErrorEnums;
import com.drstrong.health.product.model.enums.ExcelMappingEnum;
import com.drstrong.health.product.model.enums.ProductStateEnum;
import com.drstrong.health.product.model.request.store.RelevanceThreeRequest;
import com.drstrong.health.product.model.request.store.StoreSkuRequest;
import com.drstrong.health.product.model.request.store.UpdateSkuRequest;
import com.drstrong.health.product.model.request.store.UpdateThreeRequest;
import com.drstrong.health.product.model.response.PageVO;
import com.drstrong.health.product.model.response.result.BusinessException;
import com.drstrong.health.product.model.response.store.StoreSkuResponse;
import com.drstrong.health.product.service.ProductSkuService;
import com.drstrong.health.product.service.StoreService;
import com.drstrong.health.product.service.StoreThreeRelevanceService;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;
import org.easy.excel.ExcelContext;
import org.easy.excel.util.ExcelDownLoadUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
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
    private StoreService storeService;
    @Autowired
    private ExcelContext excelContext;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePurchasePrice(UpdateThreeRequest updateThreeRequest, Long userId) {
        StoreThreeRelevanceEntity storeThreeRelevanceEntity = new StoreThreeRelevanceEntity();
        storeThreeRelevanceEntity.setThreePurchasePrice(updateThreeRequest.getPurchasePrice());
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
        Page<StoreSkuResponse> page = new Page<>(storeSkuRequest.getPageNo(),storeSkuRequest.getPageSize());
        QueryWrapper<StoreSkuResponse> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("p.del_flag", DelFlagEnum.UN_DELETED.getCode());
        queryWrapper.eq("p.source_id", storeId);
        if(StringUtils.hasLength(storeSkuRequest.getSkuCode())){
           queryWrapper.eq("p.sku_code",storeSkuRequest.getSkuCode());
        }
        if(StringUtils.hasLength(storeSkuRequest.getSkuName())){
            queryWrapper.like(" p.sku_name",storeSkuRequest.getSkuName());
        }
        if(Objects.nonNull(storeSkuRequest.getSkuState())){
            queryWrapper.eq("p.state",storeSkuRequest.getSkuState());
        }
        if(Objects.nonNull(storeSkuRequest.getThreeSkuId())){
            queryWrapper.eq("t.three_sku_id",storeSkuRequest.getThreeSkuId());
        }
        List<StoreSkuResponse> storeSkuResponses =  storeThreeRelevanceMapper.pageSkuList(page,queryWrapper);
        if(CollectionUtils.isEmpty(storeSkuResponses)){
            return PageVO.newBuilder().pageNo(storeSkuRequest.getPageNo()).pageSize(storeSkuRequest.getPageSize()).totalCount(0).result(Lists.newArrayList()).build();
        }
        return PageVO.newBuilder().pageNo(storeSkuRequest.getPageNo()).pageSize(storeSkuRequest.getPageSize()).totalCount((int) page.getTotal()).result(storeSkuResponses).build();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void relevanceAdd(RelevanceThreeRequest relevanceThreeRequest, Long userId) {
        StoreThreeRelevanceEntity storeThreeRelevanceEntity = queryBySkuId(relevanceThreeRequest.getSkuId());
        if(Objects.isNull(storeThreeRelevanceEntity)){
           buildAndSave(relevanceThreeRequest,userId);
        }else {
           buildAndUpdate(storeThreeRelevanceEntity.getId(),relevanceThreeRequest,userId);
        }
    }

    @Override
    public void updateSkuState(UpdateSkuRequest updateSkuRequest, Long userId) {
        List<Long> skuIdList = updateSkuRequest.getSkuIdList();
        Integer state = updateSkuRequest.getState();
        if(ProductStateEnum.HAS_PUT.getCode().equals(state)){
            checkRelevance(skuIdList);
            checkSetPostage(skuIdList);
        }
        productSkuService.updateState(skuIdList,state,userId);
        //TODO 发送mq消息通知库存
    }

    @Override
    public void exportStoreSku(StoreSkuRequest storeSkuRequest, HttpServletRequest request, HttpServletResponse response) {
        PageVO<StoreSkuResponse> pageVO = this.pageSkuList(storeSkuRequest);
        List<StoreSkuResponse> storeSkuResponses = pageVO.getResult();
        Workbook excel = excelContext.createExcel(ExcelMappingEnum.STORE_SKU_EXPORT.getMappingId(), storeSkuResponses);
        try {
            ExcelDownLoadUtil.downLoadExcel(excel,ExcelMappingEnum.STORE_SKU_EXPORT.getExcelName(),ExcelMappingEnum.STORE_SKU_EXPORT.getEmptyMessage(),request,response);
        } catch (IOException e) {
            throw new BusinessException(ErrorEnums.EXCEL_EXPORT_ERROR);
        }
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
        lambdaQueryWrapper.eq(StoreThreeRelevanceEntity::getDelFlag,DelFlagEnum.UN_DELETED)
                .in(StoreThreeRelevanceEntity::getSkuId,skuIdList);
        Integer count = storeThreeRelevanceMapper.selectCount(lambdaQueryWrapper);
        if(skuIdList.size() != count){
           throw new BusinessException(ErrorEnums.STORE_NOT_RELEVANCE);
        }
    }

    private void buildAndUpdate(Long id, RelevanceThreeRequest relevanceThreeRequest,Long userId) {
        StoreThreeRelevanceEntity storeThreeRelevanceEntity = new StoreThreeRelevanceEntity();
        BeanUtils.copyProperties(relevanceThreeRequest,storeThreeRelevanceEntity);
        storeThreeRelevanceEntity.setId(id);
        storeThreeRelevanceEntity.setChangedBy(userId);
        storeThreeRelevanceEntity.setChangedAt(LocalDateTime.now());
        storeThreeRelevanceMapper.updateById(storeThreeRelevanceEntity);
    }

    private void buildAndSave(RelevanceThreeRequest relevanceThreeRequest,Long userId) {
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
