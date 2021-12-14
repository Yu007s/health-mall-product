package com.drstrong.health.product.banner.controller;


import com.drstrong.health.product.model.request.banner.BannerRequest;
import com.drstrong.health.product.model.response.banner.BannerResponse;
import com.drstrong.health.product.banner.service.BannerService;
import com.drstrong.health.product.model.response.result.ResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 轮播图 前端控制器
 * </p>
 *
 * @author mybatis plus generator
 * @since 2021-12-14
 */
@Slf4j
@Api(tags = "轮播图")
@RestController
@RequestMapping("/banner")
public class BannerController {

    @Resource
    BannerService bannerService;

    @ApiOperation("获取轮播图")
    @GetMapping("/get")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "location",defaultValue = "1",value = "轮播图显示位置，1:首页 (默认1)", dataType = "Integer", paramType = "query", required = true),
            @ApiImplicitParam(name = "pageSize",defaultValue = "5",value = "展示条目数 (默认5)", dataType = "Integer", paramType = "query", required = true)
    })
    public ResultVO<List<BannerResponse>> get(@RequestParam(defaultValue = "1") Integer location, @RequestParam(defaultValue = "5") Integer pageSize){
        return ResultVO.success(bannerService.get(location,pageSize));
    }

    @ApiOperation("添加轮播图")
    @PostMapping("/addOrUpdate")
    public ResultVO addOrUpdate(@RequestBody @Validated BannerRequest request){
        if (request.getLinkType() ==  1 && StringUtils.isEmpty(request.getLinkAddress())){
           return ResultVO.failed("跳转链接不得为空");
        }
        if (request.getLinkType() ==  2 && StringUtils.isEmpty(request.getProductSpuSn())){
            return ResultVO.failed("SKU 编码不得为空");
        }
        if (request.getEndTime().compareTo(request.getStartTime()) == -1){
            return ResultVO.failed("开始时间不得大于结束时间");
        }
        //TODO 远程调用商品模块校验 SKU_SN是否存在

        return bannerService.addOrUpdate(request) ? ResultVO.success() : ResultVO.failed("");
    }
}

