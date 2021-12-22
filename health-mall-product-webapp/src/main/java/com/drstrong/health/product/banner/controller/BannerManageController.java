package com.drstrong.health.product.banner.controller;

import com.drstrong.health.product.banner.service.BannerService;
import com.drstrong.health.product.model.request.banner.BannerListRequest;
import com.drstrong.health.product.model.request.banner.BannerRequest;
import com.drstrong.health.product.model.response.PageVO;
import com.drstrong.health.product.model.response.banner.BannerListResponse;
import com.drstrong.health.product.model.response.result.ResultVO;
import com.drstrong.health.product.remote.api.banner.BannerManageFacade;
import com.drstrong.health.product.service.ProductBasicsInfoService;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Resource;

/**
 * @description: 轮播图管理的 api 接口,目前主要提供给 cms 调用
 * @Author: JiaoYuSheng
 * @Date: 2021-12-22 15:48
 * @program health-mall-product
 */
public class BannerManageController implements BannerManageFacade {

    @Resource
    BannerService bannerService;

    @Resource
    ProductBasicsInfoService productBasicsInfoService;

    @Override
    public ResultVO addOrUpdate(@RequestBody @Validated BannerRequest request){
        if (request.getLinkType() ==  1 && StringUtils.isEmpty(request.getLinkAddress())){
            return ResultVO.failed("跳转链接不得为空");
        }
        if (request.getLinkType() ==  2 && StringUtils.isEmpty(request.getProductSpuSn())){
            return ResultVO.failed("SPU 编码不得为空");
        }
        if (request.getEndTime().compareTo(request.getStartTime()) == -1){
            return ResultVO.failed("开始时间不得大于结束时间");
        }
        if (request.getLinkType()== 2 && StringUtils.isNotBlank(request.getProductSpuSn()) && productBasicsInfoService.getCountBySPUCode(request.getProductSpuSn()) <1  ){
            return ResultVO.failed("SPU不存在，或已下架");
        }
        return bannerService.addOrUpdate(request) ? ResultVO.success() : ResultVO.failed("");
    }

    @Override
    public ResultVO<PageVO<BannerListResponse>> queryList(@Validated BannerListRequest request){
        return ResultVO.success(bannerService.queryList(request));
    }

}
