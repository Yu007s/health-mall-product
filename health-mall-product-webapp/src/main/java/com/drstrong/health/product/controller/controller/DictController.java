package com.drstrong.health.product.controller.controller;

import com.drstrong.health.product.model.entity.dict.DictEntity;
import com.drstrong.health.product.model.response.result.ResultVO;
import com.drstrong.health.product.service.dict.DictService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * huangpeng
 * 2023/6/9 14:21
 */
@Api(tags = "商城字典数据")
@RestController
@RequestMapping("/dict")
public class DictController {

    @Autowired
    private DictService dictService;

    @ApiOperation("根据字典类型获取字典数据")
    @GetMapping("/getDictByType")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dictType", value = "字典类型", dataType = "string", required = true)
    })
    public ResultVO<List<DictEntity>> getDictByType(String dictType) {
        return ResultVO.success(dictService.getDictByType(dictType));
    }
}
