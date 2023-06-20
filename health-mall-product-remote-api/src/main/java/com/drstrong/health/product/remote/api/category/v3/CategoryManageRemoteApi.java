package com.drstrong.health.product.remote.api.category.v3;

import com.drstrong.health.product.model.request.category.v3.SaveCategoryRequest;
import com.drstrong.health.product.model.response.category.v3.CategoryVO;
import com.drstrong.health.product.model.response.result.ResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.List;

/**
 * 分类远程接口
 *
 * @author liuqiuyi
 * @date 2023/6/12 16:40
 */
@Api("健康商城-商品服务-分类远程接口")
@FeignClient(value = "health-mall-product", path = "/inner/category/manage")
public interface CategoryManageRemoteApi {

    @ApiOperation("根据商品类型查询分类信息")
    @GetMapping("/query/by/product-type")
    ResultVO<List<CategoryVO>> queryAllCategoryByProductType(@RequestParam(value = "productType") Integer productType, @RequestParam(value = "needFilter", required = false) Boolean needFilter);

    @ApiOperation("保存或者更新分类信息")
    @PostMapping("/save-or-update")
    ResultVO<Void> saveCategoryEntity(@RequestBody @Valid SaveCategoryRequest saveCategoryRequest);

    @ApiOperation("根据id删除分类信息")
    @PostMapping("/delete/id")
    ResultVO<Void> deleteCategoryEntity(@RequestParam(value = "categoryId") Long categoryId, @RequestParam(value = "changedName") String changedName);
}
