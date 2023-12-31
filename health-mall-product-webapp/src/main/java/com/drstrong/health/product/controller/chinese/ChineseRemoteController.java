package com.drstrong.health.product.controller.chinese;

import com.alibaba.fastjson.JSONObject;
import com.drstrong.health.product.facade.ChineseRemoteFacade;
import com.drstrong.health.product.model.request.chinese.ChineseQueryDosageRequest;
import com.drstrong.health.product.model.request.chinese.QueryChineseSkuRequest;
import com.drstrong.health.product.model.request.store.AgencyStoreVO;
import com.drstrong.health.product.model.response.chinese.ChineseMedicineConflictVO;
import com.drstrong.health.product.model.response.chinese.ChineseMedicineInfoResponse;
import com.drstrong.health.product.model.response.chinese.ChineseSkuInfoVO;
import com.drstrong.health.product.model.response.product.ProductInfoVO;
import com.drstrong.health.product.model.response.result.ResultVO;
import com.drstrong.health.product.remote.api.chinese.ChineseRemoteApi;
import com.drstrong.health.product.remote.model.ChineseMedicineDTO;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * 中药对外业务的远程接口
 *
 * @author liuqiuyi
 * @date 2022/8/3 19:28
 */
@Validated
@RestController
@RequestMapping("/inner/product/chinese")
@Slf4j
@Api(tags = {"中药对外业务的远程接口"})
public class ChineseRemoteController implements ChineseRemoteApi {
	@Resource
	ChineseRemoteFacade chineseRemoteFacade;

	@Override
	public ResultVO<List<ChineseSkuInfoVO>> keywordSearch(String keyword, Long agencyId) {
		return ResultVO.success(chineseRemoteFacade.keywordSearch(keyword, agencyId, null));
	}

	@Override
	public ResultVO<ProductInfoVO> queryStoreSku(QueryChineseSkuRequest chineseSkuRequest) {
		return ResultVO.success(chineseRemoteFacade.queryStoreSku(chineseSkuRequest));
	}

	@Override
	public ResultVO<List<ChineseMedicineConflictVO>> listAllConflict() {
		return ResultVO.success(chineseRemoteFacade.listAllConflict());
	}

	@Override
	public ResultVO<List<AgencyStoreVO>> listStoreByAgencyIds(Set<Long> agencyIds) {
		return ResultVO.success(chineseRemoteFacade.listStoreByAgencyIds(agencyIds));
	}

	@Override
	public ResultVO<List<AgencyStoreVO>> listAgencyByStoreIds(Set<Long> storeIds) {
		return ResultVO.success(chineseRemoteFacade.listAgencyByStoreIds(storeIds));
	}

	@Override
	public ResultVO<List<ChineseMedicineInfoResponse>> checkHasUpChineseByMedicineCodes(Set<String> medicineCodes, Long supplierId, Long operatorId) {
		return ResultVO.success(chineseRemoteFacade.checkHasUpChineseByMedicineCodes(medicineCodes, supplierId, operatorId));
	}

	@Override
	public ResultVO<List<ChineseMedicineDTO>> getChineseMedicineDTOListByIds(Set<Long> ids) {
		log.info("invoke getChineseMedicineDTOListByIds param:{}", JSONObject.toJSONString(ids));
		return ResultVO.success(chineseRemoteFacade.getChineseMedicineDTOListByIds(ids));
	}

	@Override
	public ResultVO<List<ChineseSkuInfoVO>> queryAllDosage(ChineseQueryDosageRequest chineseQueryDosageRequest) {
		return ResultVO.success(chineseRemoteFacade.queryAllDosage(chineseQueryDosageRequest));
	}
}
