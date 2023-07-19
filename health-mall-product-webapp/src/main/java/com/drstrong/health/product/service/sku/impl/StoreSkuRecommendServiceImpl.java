package com.drstrong.health.product.service.sku.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.drstrong.health.product.dao.sku.StoreSkuRecommendMapper;
import com.drstrong.health.product.model.entity.sku.StoreSkuRecommendEntity;
import com.drstrong.health.product.model.enums.DelFlagEnum;
import com.drstrong.health.product.model.request.sku.recommend.PageSkuRecommendRequest;
import com.drstrong.health.product.service.sku.StoreSkuRecommendService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author liuqiuyi
 * @date 2023/6/8 15:20
 */
@Slf4j
@Service
public class StoreSkuRecommendServiceImpl extends ServiceImpl<StoreSkuRecommendMapper, StoreSkuRecommendEntity> implements StoreSkuRecommendService {

    /**
     * 根据 主键id 查询推荐信息
     *
     * @param skuRecommendId
     * @author liuqiuyi
     * @date 2023/7/10 17:08
     */
    @Override
    public StoreSkuRecommendEntity queryBySkuRecommendId(Long skuRecommendId) {
        if (ObjectUtil.isNull(skuRecommendId)) {
            return null;
        }
        LambdaQueryWrapper<StoreSkuRecommendEntity> queryWrapper = new LambdaQueryWrapper<StoreSkuRecommendEntity>()
                .eq(StoreSkuRecommendEntity::getDelFlag, DelFlagEnum.UN_DELETED.getCode())
                .eq(StoreSkuRecommendEntity::getId, skuRecommendId);
        return baseMapper.selectOne(queryWrapper);
    }

    @Override
    public StoreSkuRecommendEntity queryBySkuCode(String skuCode) {
        if (StrUtil.isBlank(skuCode)) {
            return null;
        }
        LambdaQueryWrapper<StoreSkuRecommendEntity> queryWrapper = new LambdaQueryWrapper<StoreSkuRecommendEntity>()
                .eq(StoreSkuRecommendEntity::getDelFlag, DelFlagEnum.UN_DELETED.getCode())
                .eq(StoreSkuRecommendEntity::getSkuCode, skuCode);
        return baseMapper.selectOne(queryWrapper);
    }

    @Override
    public Page<StoreSkuRecommendEntity> pageQueryByParam(PageSkuRecommendRequest pageSkuRecommendRequest) {
        Page<StoreSkuRecommendEntity> entityPage = new Page<>(pageSkuRecommendRequest.getPageNo(), pageSkuRecommendRequest.getPageSize());
        return baseMapper.pageQueryByParam(entityPage, pageSkuRecommendRequest);
    }

    /**
     * 根据条件查询
     *
     * @param pageSkuRecommendRequest
     * @author liuqiuyi
     * @date 2023/7/13 17:07
     */
    @Override
    public List<StoreSkuRecommendEntity> listQueryByParam(PageSkuRecommendRequest pageSkuRecommendRequest) {
        return baseMapper.listQueryByParam(pageSkuRecommendRequest);
    }
}
