package com.drstrong.health.product.controller.banner;

import com.drstrong.health.product.service.banner.BannerService;
import com.drstrong.health.product.model.request.banner.BannerListRequest;
import com.drstrong.health.product.model.request.banner.BannerRequest;
import com.drstrong.health.product.model.response.PageVO;
import com.drstrong.health.product.model.response.banner.BannerListResponse;
import com.drstrong.health.product.model.response.result.ResultVO;
import com.drstrong.health.product.remote.api.banner.BannerManageFacade;
import com.drstrong.health.product.service.product.ProductManageService;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 轮播图管理的 api 接口,目前主要提供给 cms 调用
 * @author JiaoYuSheng
 * @date 2021/12/22 17:33
 */
@RestController
@RequestMapping("/inner/product/banner")
@Slf4j
@Api(tags = {"轮播图"})
@Deprecated
public class BannerManageController implements BannerManageFacade {

    @Resource
    BannerService bannerService;

    @Resource
    ProductManageService productManageService;

    @Override
    public ResultVO addOrUpdate(@RequestBody @Validated BannerRequest request){
/*        log.info("添加/修改轮播题------BannerRequest:{}",request);
        if (request.getLinkType() ==  1 && StringUtils.isEmpty(request.getLinkAddress())){
            return ResultVO.failed("跳转链接不得为空");
        }
        if (request.getLinkType() ==  2 && StringUtils.isEmpty(request.getProductSpuSn())){
            return ResultVO.failed("SPU 编码不得为空");
        }
        if (request.getEndTime().compareTo(request.getStartTime()) == -1){
            return ResultVO.failed("开始时间不得大于结束时间");
        }
        if (request.getLinkType()== 2 && StringUtils.isNotBlank(request.getProductSpuSn()) && productManageService.getCountBySPUCode(request.getProductSpuSn()) <1  ){
            return ResultVO.failed("SPU不存在，或已下架");
        }
        return bannerService.addOrUpdate(request) ? ResultVO.success() : ResultVO.failed("");*/
        return ResultVO.success();
    }

    @Override
    public ResultVO<PageVO<BannerListResponse>> queryList(@RequestBody @Validated BannerListRequest request){
//        return ResultVO.success(bannerService.queryList(request));
        PageVO<BannerListResponse> objectPageVO = PageVO.newBuilder().pageNo(request.getPageNo()).pageSize(request.getPageSize()).totalCount(0).result(Lists.newArrayList()).build();
        return ResultVO.success(objectPageVO);
    }

}
