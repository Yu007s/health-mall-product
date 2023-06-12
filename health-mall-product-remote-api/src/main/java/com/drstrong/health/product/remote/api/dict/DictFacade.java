package com.drstrong.health.product.remote.api.dict;

import com.drstrong.health.product.model.dto.DictDTO;
import com.drstrong.health.product.model.response.result.ResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * huangpeng
 * 2023/6/12 16:13
 */
@Api(tags = "商城字典数据")
@FeignClient(value = "health-mall-product", path = "/dict")
public interface DictFacade {

    @ApiOperation("根据字典类型获取字典数据")
    @GetMapping("/getDictByType")
    List<DictDTO> getDictByType(String dictType);

}
