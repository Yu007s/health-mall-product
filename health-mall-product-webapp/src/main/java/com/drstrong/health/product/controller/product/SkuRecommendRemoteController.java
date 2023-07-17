package com.drstrong.health.product.controller.product;

import com.drstrong.health.product.facade.sku.recommend.SkuRecommendManageFacade;
import com.drstrong.health.product.model.dto.product.SkuBaseDTO;
import com.drstrong.health.product.model.request.sku.recommend.PageSkuRecommendRequest;
import com.drstrong.health.product.model.request.sku.recommend.SaveRecommendRequest;
import com.drstrong.health.product.model.response.PageVO;
import com.drstrong.health.product.model.response.product.v3.recommend.SkuRecommendManageResponse;
import com.drstrong.health.product.model.response.result.ResultVO;
import com.drstrong.health.product.remote.api.product.SkuRecommendManageRemoteApi;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * sku 推荐管理
 *
 * @author liuqiuyi
 * @date 2023/7/10 16:11
 */
@RestController
@RequestMapping("/inner/sku/recommend")
public class SkuRecommendRemoteController implements SkuRecommendManageRemoteApi {
    @Resource
    SkuRecommendManageFacade skuRecommendManageFacade;

    @Override
    public ResultVO<Void> saveOrUpdateRecommend(@Valid SaveRecommendRequest saveRecommendRequest) {
        skuRecommendManageFacade.saveOrUpdateRecommend(saveRecommendRequest);
        return ResultVO.success();
    }

    @Override
    public ResultVO<Void> deleteBySkuCode(SkuBaseDTO skuBaseDTO) {
        skuRecommendManageFacade.deleteBySkuCode(skuBaseDTO);
        return ResultVO.success();
    }

    @Override
    public ResultVO<PageVO<SkuRecommendManageResponse>> pageQuery(PageSkuRecommendRequest pageSkuRecommendRequest) {
        return ResultVO.success(skuRecommendManageFacade.pageQuery(pageSkuRecommendRequest));
    }

    @Override
    public ResultVO<List<SkuRecommendManageResponse>> listQuery(PageSkuRecommendRequest pageSkuRecommendRequest) {
        return ResultVO.success(skuRecommendManageFacade.listQuery(pageSkuRecommendRequest));
    }
}
