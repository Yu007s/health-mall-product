package com.drstrong.health.product.service.sku;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.drstrong.health.product.model.entity.sku.StoreSkuRecommendEntity;
import com.drstrong.health.product.model.request.sku.recommend.PageSkuRecommendRequest;

import java.util.List;

/**
 * @author liuqiuyi
 * @date 2023/6/8 14:47
 */
public interface StoreSkuRecommendService extends IService<StoreSkuRecommendEntity> {

    /**
     * 根据 主键id 查询推荐信息
     *
     * @author liuqiuyi
     * @date 2023/7/10 17:08
     */
    StoreSkuRecommendEntity queryBySkuRecommendId(Long skuRecommendId);

    /**
     * 根据 skuCode 查询推荐信息
     *
     * @author liuqiuyi
     * @date 2023/7/10 17:08
     */
    StoreSkuRecommendEntity queryBySkuCode(String skuCode);

    /**
     * 根据条件分页查询
     *
     * @author liuqiuyi
     * @date 2023/7/10 17:44
     */
    Page<StoreSkuRecommendEntity> pageQueryByParam(PageSkuRecommendRequest pageSkuRecommendRequest);

    /**
     * 根据条件查询
     *
     * @author liuqiuyi
     * @date 2023/7/13 17:07
     */
    List<StoreSkuRecommendEntity> listQueryByParam(PageSkuRecommendRequest pageSkuRecommendRequest);
}
