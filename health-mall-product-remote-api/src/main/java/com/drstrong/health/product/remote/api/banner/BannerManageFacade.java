package com.drstrong.health.product.remote.api.banner;

import com.drstrong.health.product.model.request.banner.BannerListRequest;
import com.drstrong.health.product.model.request.banner.BannerRequest;
import com.drstrong.health.product.model.request.category.CategoryQueryRequest;
import com.drstrong.health.product.model.response.PageVO;
import com.drstrong.health.product.model.response.banner.BannerListResponse;
import com.drstrong.health.product.model.response.category.FrontCategoryVO;
import com.drstrong.health.product.model.response.result.ResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @description: 轮播图管理的 api 接口,目前主要提供给 cms 调用
 * @Author: JiaoYuSheng
 * @Date: 2021-12-22 15:45
 * @program health-mall-product
 */
@Api("健康商城-轮播图管理远程接口")
@FeignClient(value = "health-mall-product", path = "/product/banner")
public interface BannerManageFacade {

    @ApiOperation("添加轮播图")
    @PostMapping("/addOrUpdate")
    public ResultVO addOrUpdate(@RequestBody @Validated BannerRequest request);

    @ApiOperation("查询轮播图列表")
    @PostMapping("/queryList")
    public ResultVO<PageVO<BannerListResponse>> queryList(@Validated BannerListRequest request);
}
