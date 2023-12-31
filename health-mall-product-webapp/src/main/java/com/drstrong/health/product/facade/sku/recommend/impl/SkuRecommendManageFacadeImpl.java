package com.drstrong.health.product.facade.sku.recommend.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.drstrong.health.product.facade.sku.SkuBusinessFacadeHolder;
import com.drstrong.health.product.facade.sku.recommend.SkuRecommendManageFacade;
import com.drstrong.health.product.model.dto.medicine.MedicineUsageDTO;
import com.drstrong.health.product.model.dto.product.SkuBaseDTO;
import com.drstrong.health.product.model.entity.sku.StoreSkuInfoEntity;
import com.drstrong.health.product.model.entity.sku.StoreSkuRecommendEntity;
import com.drstrong.health.product.model.entity.store.StoreEntity;
import com.drstrong.health.product.model.enums.DelFlagEnum;
import com.drstrong.health.product.model.enums.ErrorEnums;
import com.drstrong.health.product.model.enums.ProductTypeEnum;
import com.drstrong.health.product.model.enums.UpOffEnum;
import com.drstrong.health.product.model.request.sku.recommend.PageSkuRecommendRequest;
import com.drstrong.health.product.model.request.sku.recommend.SaveRecommendRequest;
import com.drstrong.health.product.model.response.PageVO;
import com.drstrong.health.product.model.response.product.v3.recommend.SkuRecommendManageResponse;
import com.drstrong.health.product.model.response.result.BusinessException;
import com.drstrong.health.product.service.sku.StoreSkuInfoService;
import com.drstrong.health.product.service.sku.StoreSkuRecommendService;
import com.drstrong.health.product.service.store.StoreService;
import com.drstrong.health.product.utils.HanZiToPinYinUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author liuqiuyi
 * @date 2023/7/10 16:49
 */
@Slf4j
@Service
public class SkuRecommendManageFacadeImpl implements SkuRecommendManageFacade {
    /**
     * 匹配任何不是汉字的字符
     */
    private final String NOT_CHINESE_CHARACTERS = "[^\\u4e00-\\u9fa5]";

    @Resource
    StoreSkuInfoService storeSkuInfoService;

    @Resource
    StoreSkuRecommendService storeSkuRecommendService;

    @Resource
    SkuBusinessFacadeHolder skuBusinessFacadeHolder;

    @Resource
    StoreService storeService;

    @Override
    public void saveOrUpdateRecommend(SaveRecommendRequest saveRecommendRequest) {
        boolean isSave = ObjectUtil.isNull(saveRecommendRequest.getSkuRecommendId());
        // 1.入参数据判断
        checkSaveOrUpdateRecommendParam(saveRecommendRequest, isSave);
        // 2.组装推荐信息列表（拼音+关键字）,参照之前b2c的处理代码
        List<StoreSkuRecommendEntity.RecommendDetailInfoEntity> recommendDetailInfoEntityList = saveRecommendRequest.getKeywordList().stream().map(keyword -> {
            String replaceName = keyword.replaceAll(NOT_CHINESE_CHARACTERS, "");
            String pinyin = HanZiToPinYinUtil.toFirstChar(replaceName);
            return StoreSkuRecommendEntity.RecommendDetailInfoEntity.builder().keyword(keyword).pinyin(pinyin).build();
        }).collect(Collectors.toList());
        // 3.新增或者保存
        StoreSkuRecommendEntity storeSkuRecommendEntity;
        if (isSave) {
            storeSkuRecommendEntity = StoreSkuRecommendEntity.buildDefault(saveRecommendRequest.getOperatorId());
        } else {
            storeSkuRecommendEntity = storeSkuRecommendService.queryBySkuRecommendId(saveRecommendRequest.getSkuRecommendId());
            storeSkuRecommendEntity.setChangedBy(saveRecommendRequest.getOperatorId());
            storeSkuRecommendEntity.setChangedAt(LocalDateTime.now());
        }
        storeSkuRecommendEntity.setStoreId(saveRecommendRequest.getStoreId());
        storeSkuRecommendEntity.setSkuCode(saveRecommendRequest.getSkuCode());
        storeSkuRecommendEntity.setRecommendDetailInfoList(recommendDetailInfoEntityList);
        storeSkuRecommendService.saveOrUpdate(storeSkuRecommendEntity);
    }

    private void checkSaveOrUpdateRecommendParam(SaveRecommendRequest saveRecommendRequest, boolean isSave) {
        // 校验sku是否存在
        storeSkuInfoService.checkSkuExistByCode(saveRecommendRequest.getSkuCode(), null);
        // 校验入参
        StoreSkuRecommendEntity storeSkuRecommendEntity = storeSkuRecommendService.queryBySkuCode(saveRecommendRequest.getSkuCode());
        if (isSave) {
            // 如果是新增，且sku推荐记录存在，报错
            if (ObjectUtil.isNotNull(storeSkuRecommendEntity)) {
                throw new BusinessException(ErrorEnums.RECOMMEND_IS_REPEAT);
            }
        } else {
            // 如果是修改，根据id未找到数据，或者找到的数据id不一致，均报错
            StoreSkuRecommendEntity storeSkuRecommendEntityById = storeSkuRecommendService.queryBySkuRecommendId(saveRecommendRequest.getSkuRecommendId());
            if (ObjectUtil.isNull(storeSkuRecommendEntityById)) {
                throw new BusinessException(ErrorEnums.RECOMMEND_IS_NULL);
            }
            if (ObjectUtil.notEqual(saveRecommendRequest.getSkuRecommendId(), storeSkuRecommendEntityById.getId())) {
                throw new BusinessException(ErrorEnums.RECOMMEND_IS_REPEAT);
            }
        }
    }

    @Override
    public void deleteBySkuCode(SkuBaseDTO skuBaseDTO) {
        // 1.根据skuCode 查询
        StoreSkuRecommendEntity storeSkuRecommendEntity = storeSkuRecommendService.queryBySkuCode(skuBaseDTO.getSkuCode());
        if (Objects.isNull(storeSkuRecommendEntity)) {
            throw new BusinessException(ErrorEnums.SKU_IS_NULL);
        }
        // 2.更新状态
        storeSkuRecommendEntity.setDelFlag(DelFlagEnum.IS_DELETED.getCode());
        storeSkuRecommendEntity.setChangedAt(LocalDateTime.now());
        storeSkuRecommendEntity.setChangedBy(skuBaseDTO.getOperatorId());
        storeSkuRecommendService.removeById(storeSkuRecommendEntity);
    }

    @Override
    public PageVO<SkuRecommendManageResponse> pageQuery(PageSkuRecommendRequest pageSkuRecommendRequest) {
        log.info("invoke pageQuery() param:{}", JSONUtil.toJsonStr(pageSkuRecommendRequest));
        // 1.根据入参分页查询
        Page<StoreSkuRecommendEntity> storeSkuRecommendEntityPage = storeSkuRecommendService.pageQueryByParam(pageSkuRecommendRequest);
        if (Objects.isNull(storeSkuRecommendEntityPage) || CollectionUtil.isEmpty(storeSkuRecommendEntityPage.getRecords())) {
            log.info("未查询到任何sku推荐数据，参数为：{}", JSONUtil.toJsonStr(pageSkuRecommendRequest));
            return PageVO.newBuilder().result(Lists.newArrayList()).totalCount(0).pageNo(pageSkuRecommendRequest.getPageNo()).pageSize(pageSkuRecommendRequest.getPageSize()).build();
        }
        List<SkuRecommendManageResponse> recommendManageResponseList = buildRecommendManageResponseList(storeSkuRecommendEntityPage.getRecords());
        return PageVO.newBuilder().result(recommendManageResponseList).totalCount((int) storeSkuRecommendEntityPage.getTotal()).pageNo(pageSkuRecommendRequest.getPageNo()).pageSize(pageSkuRecommendRequest.getPageSize()).build();
    }

    @Override
    public List<SkuRecommendManageResponse> listQuery(PageSkuRecommendRequest pageSkuRecommendRequest) {
        log.info("invoke listQuery() param:{}", JSONUtil.toJsonStr(pageSkuRecommendRequest));
        List<StoreSkuRecommendEntity> storeSkuRecommendEntityList = storeSkuRecommendService.listQueryByParam(pageSkuRecommendRequest);
        return buildRecommendManageResponseList(storeSkuRecommendEntityList);
    }

    private List<SkuRecommendManageResponse> buildRecommendManageResponseList(List<StoreSkuRecommendEntity> storeSkuRecommendEntityList) {
        Set<String> skuCodes = Sets.newHashSetWithExpectedSize(storeSkuRecommendEntityList.size());
        Set<Long> storeIds = Sets.newHashSetWithExpectedSize(storeSkuRecommendEntityList.size());
        storeSkuRecommendEntityList.forEach(storeSkuRecommendEntity -> {
            skuCodes.add(storeSkuRecommendEntity.getSkuCode());
            storeIds.add(storeSkuRecommendEntity.getStoreId());
        });
        // 2.获取sku的用法用量
        Map<String, MedicineUsageDTO> skuCodeMedicineUsageDtoMap = skuBusinessFacadeHolder.queryMedicineUsageBySkuCode(skuCodes).stream()
                .collect(Collectors.toMap(MedicineUsageDTO::getSkuCode, dto -> dto, (v1, v2) -> v1));
        // 3.获取店铺名称
        Map<Long, String> storeIdNameMap = storeService.listByIds(storeIds).stream().collect(Collectors.toMap(StoreEntity::getId, StoreEntity::getStoreName, (v1, v2) -> v1));
        // 5.组装数据
        List<SkuRecommendManageResponse> recommendManageResponseList = Lists.newArrayListWithCapacity(storeSkuRecommendEntityList.size());
        storeSkuRecommendEntityList.forEach(storeSkuRecommendEntity -> {
            MedicineUsageDTO medicineUsageDTO = skuCodeMedicineUsageDtoMap.getOrDefault(storeSkuRecommendEntity.getSkuCode(), new MedicineUsageDTO());

            SkuRecommendManageResponse skuRecommendManageResponse = SkuRecommendManageResponse.builder()
                    .skuRecommendId(storeSkuRecommendEntity.getId())
                    .skuCode(storeSkuRecommendEntity.getSkuCode())
                    .skuName(medicineUsageDTO.getSkuName())
                    .keywordList(storeSkuRecommendEntity.getRecommendDetailInfoKeywordArray())
                    .storeId(storeSkuRecommendEntity.getStoreId())
                    .storeName(storeIdNameMap.get(storeSkuRecommendEntity.getStoreId()))
                    .createTime(storeSkuRecommendEntity.getCreatedAt())
                    .build();
            skuRecommendManageResponse.setProductType(medicineUsageDTO.getProductType());
            skuRecommendManageResponse.setProductTypeName(ProductTypeEnum.getValueByCode(medicineUsageDTO.getProductType()));
            recommendManageResponseList.add(skuRecommendManageResponse);
        });
        return recommendManageResponseList;
    }
}
