package com.drstrong.health.product.remote.api.product;

import com.drstrong.health.product.model.dto.product.SkuBaseDTO;
import com.drstrong.health.product.model.request.sku.recommend.PageSkuRecommendRequest;
import com.drstrong.health.product.model.request.sku.recommend.SaveRecommendRequest;
import com.drstrong.health.product.model.response.PageVO;
import com.drstrong.health.product.model.response.product.v3.recommend.SkuRecommendManageResponse;
import com.drstrong.health.product.model.response.result.ResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * sku 推荐 - 管理端接口
 *
 * @author liuqiuyi
 * @date 2023/7/10 16:13
 */
@Api("健康商城-商品服务-sku推荐信息管理接口")
@FeignClient(value = "health-mall-product", path = "/inner/sku/recommend")
public interface SkuRecommendManageRemoteApi {

    @ApiOperation("新增/编辑推荐药品")
    @PostMapping("/save-or-update")
    ResultVO<Void> saveOrUpdateRecommend(@RequestBody SaveRecommendRequest saveRecommendRequest);

    @ApiOperation("删除推荐药品")
    @PostMapping("/delete-by-skuCode")
    ResultVO<Void> deleteBySkuCode(@RequestBody SkuBaseDTO skuBaseDTO);


    @ApiOperation("分页查询推荐药品")
    @PostMapping("/page/query")
    ResultVO<PageVO<SkuRecommendManageResponse>> pageQuery(@RequestBody PageSkuRecommendRequest pageSkuRecommendRequest);

    @ApiOperation("按查询推荐药品")
    @PostMapping("/list/query")
    ResultVO<List<SkuRecommendManageResponse>> listQuery(@RequestBody PageSkuRecommendRequest pageSkuRecommendRequest);
}
