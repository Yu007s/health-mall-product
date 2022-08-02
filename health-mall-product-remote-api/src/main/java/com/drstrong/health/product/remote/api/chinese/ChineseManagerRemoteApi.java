package com.drstrong.health.product.remote.api.chinese;

import com.drstrong.health.product.model.request.chinese.ChineseManagerSkuRequest;
import com.drstrong.health.product.model.request.chinese.UpdateSkuStateRequest;
import com.drstrong.health.product.model.response.PageVO;
import com.drstrong.health.product.model.response.chinese.ChineseManagerSkuVO;
import com.drstrong.health.product.model.response.chinese.SaveOrUpdateSkuVO;
import com.drstrong.health.product.model.response.result.ResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

/**
 * 健康商城-商品服务-中药管理页面远程接口
 *
 * @author liuqiuyi
 * @date 2022/8/1 10:46
 */
@Api("健康商城-商品服务-中药管理页面远程接口")
@FeignClient(value = "health-mall-product", path = "/inner/chinese/manager")
public interface ChineseManagerRemoteApi {

    @ApiOperation("中药管理页面，列表查询")
    @PostMapping("/page/sku/info")
    ResultVO<PageVO<ChineseManagerSkuVO>> pageChineseManagerSku(@RequestBody ChineseManagerSkuRequest skuRequest);

    @ApiOperation("中药管理页面，列表查询并导出")
    @PostMapping("/sku/info/export")
    ResultVO<List<ChineseManagerSkuVO>> listChineseManagerSkuExport(@RequestBody ChineseManagerSkuRequest skuRequest);

    @ApiOperation("中药管理页面，保存sku")
    @PostMapping("/sku/save")
    ResultVO<Object> saveOrUpdateSku(@RequestBody @Valid SaveOrUpdateSkuVO saveOrUpdateSkuVO);

    @ApiOperation("中药管理页面，根据skuCode获取sku详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "skuCode", value = "sku编码", dataType = "String", paramType = "query", required = true)
    })
    @GetMapping("/get/sku")
    ResultVO<SaveOrUpdateSkuVO> getSkuByCode(@RequestParam("skuCode") String skuCode);

    @ApiOperation("中药管理页面，sku批量上下架")
    @PostMapping("/update/sku/state")
    ResultVO<Object> listUpdateSkuState(@RequestBody UpdateSkuStateRequest updateSkuStateRequest);
}
