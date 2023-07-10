package com.drstrong.health.product.facade.sku.recommend;

import com.drstrong.health.product.model.dto.product.SkuBaseDTO;
import com.drstrong.health.product.model.request.sku.recommend.PageSkuRecommendRequest;
import com.drstrong.health.product.model.request.sku.recommend.SaveRecommendRequest;
import com.drstrong.health.product.model.response.PageVO;
import com.drstrong.health.product.model.response.product.v3.recommend.SkuRecommendManageResponse;

import java.util.List;

/**
 * @author liuqiuyi
 * @date 2023/7/10 16:48
 */
public interface SkuRecommendManageFacade {
    /**
     * 新增/编辑推荐药品
     *
     * @author liuqiuyi
     * @date 2023/7/10 16:49
     */
    void saveOrUpdateRecommend(SaveRecommendRequest saveRecommendRequest);

    /**
     * 删除推荐药品
     *
     * @author liuqiuyi
     * @date 2023/7/10 16:50
     */
    void deleteBySkuCode(SkuBaseDTO skuBaseDTO);

    /**
     * 分页查询推荐药品信息
     *
     * @author liuqiuyi
     * @date 2023/7/10 16:50
     */
    PageVO<SkuRecommendManageResponse> pageQuery(PageSkuRecommendRequest pageSkuRecommendRequest);

    /**
     * 查询推荐药品信息
     *
     * @author liuqiuyi
     * @date 2023/7/10 16:50
     */
    List<SkuRecommendManageResponse> listQuery(PageSkuRecommendRequest pageSkuRecommendRequest);
}
