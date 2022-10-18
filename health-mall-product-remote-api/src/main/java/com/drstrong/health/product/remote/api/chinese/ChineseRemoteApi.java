package com.drstrong.health.product.remote.api.chinese;

import com.drstrong.health.product.model.entity.chinese.ChineseMedicineEntity;
import com.drstrong.health.product.model.request.chinese.QueryChineseSkuRequest;
import com.drstrong.health.product.model.request.store.AgencyStoreVO;
import com.drstrong.health.product.model.response.chinese.ChineseMedicineConflictVO;
import com.drstrong.health.product.model.response.chinese.ChineseMedicineInfoResponse;
import com.drstrong.health.product.model.response.chinese.ChineseSkuInfoVO;
import com.drstrong.health.product.model.response.product.ProductInfoVO;
import com.drstrong.health.product.model.response.result.ResultVO;
import com.drstrong.health.product.remote.model.ChineseMedicineDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * 中药的远程接口
 *
 * @author liuqiuyi
 * @date 2022/8/3 16:41
 */
@Api("健康商城-商品服务-中药的远程接口")
@FeignClient(value = "health-mall-product", path = "/inner/product/chinese")
public interface ChineseRemoteApi {

	@ApiOperation("中药查询接口，用于医生搜索中药材")
	@GetMapping("/keyword/search")
	ResultVO<List<ChineseSkuInfoVO>> keywordSearch(@RequestParam("keyword") String keyword, @RequestParam("agencyId") Long agencyId);

	@ApiOperation("中药 sku,查询店铺的 sku 信息")
	@PostMapping("/sku/query")
	ResultVO<ProductInfoVO> queryStoreSku(@RequestBody @Valid QueryChineseSkuRequest chineseSkuRequest);

	@ApiOperation("获取中药材相反信息,出参和之前保持一致,用于 app 端查询")
	@GetMapping("/all/conflict")
	ResultVO<List<ChineseMedicineConflictVO>> listAllConflict();

	@ApiOperation("根据互联网医院 id 获取店铺 id")
	@PostMapping("/store/agencyId")
	ResultVO<List<AgencyStoreVO>> listStoreByAgencyIds(@RequestBody Set<Long> agencyIds);

	@ApiOperation("根据店铺 id 获取互联网医院 id")
	@PostMapping("/agency/storeId")
	ResultVO<List<AgencyStoreVO>> listAgencyByStoreIds(@RequestBody Set<Long> storeIds);

	@ApiOperation("根据药材编码校验是否有上架的sku,用于供应商那边删除中药材时进行校验,如果删除的中药材关联了上架的 sku,则不允许删除")
	@PostMapping("/check/up/chinese")
	ResultVO<List<ChineseMedicineInfoResponse>> checkHasUpChineseByMedicineCodes(@RequestBody @NotNull(message = "参数不能为空") @Size(max = 200, message = "入参不能超过200") Set<String> medicineCodes);
	@ApiOperation("通过IDs获取中药材详情")
	@PostMapping("/get/chineseMedicineDTO")
	ResultVO<List<ChineseMedicineDTO>> getChineseMedicineDTOListByIds(Set<Long> ids);
}
